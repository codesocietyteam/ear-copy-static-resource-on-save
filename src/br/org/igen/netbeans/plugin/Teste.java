package br.org.igen.netbeans.plugin;

import br.org.igen.MagicaPanel;
import br.org.igen.netbeans.plugin.maven.MavenModuleProject;
import br.org.igen.netbeans.plugin.maven.MavenProjects;
import br.org.igen.netbeans.plugin.maven.MultiModuleMavenProject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.api.project.ProjectUtils;
import org.openide.filesystems.FileAttributeEvent;
import org.openide.filesystems.FileChangeListener;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileRenameEvent;
import org.openide.filesystems.FileUtil;
import org.openide.modules.OnStart;
import org.openide.util.Exceptions;
import org.openide.util.NbPreferences;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author gabrielhof
 */
@OnStart
public class Teste implements Runnable {

    private Logger logger = Logger.getLogger(getClass().getName());
    
    @Override
    public void run() {
        MavenProjects.getDefault().addChangeListener(mavenProject -> {
            if (mavenProject.isMultiModule()) {
                verifyWebProjectAndAddListener((MultiModuleMavenProject) mavenProject);
            }
        });
    }
    
    private void verifyWebProjectAndAddListener(MultiModuleMavenProject project) {
        ProjectInformation projectInfo = ProjectUtils.getInformation(project);
        
        logger.log(Level.INFO, "Projeto Maven: {0}", projectInfo.getName());
        
        List<FileObject> warProjects = new ArrayList<>();
        List<FileObject> earProjects = new ArrayList<>();
        
        for (MavenModuleProject module : project.getModules()) {
            FileObject projectDirectory = module.getProjectDirectory();
            FileObject submodulePom = projectDirectory.getFileObject("pom.xml");
            
            try (InputStream pomInputStream = submodulePom.getInputStream()) {
                Document pomDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(pomInputStream);
                
                Element root = (Element) pomDocument.getFirstChild();
                NodeList packagingElements = root.getElementsByTagName("packaging");
                
                if (packagingElements.getLength() <= 0) {
                    continue;
                }
                
                String pomPackaging = packagingElements.item(0).getTextContent();
                
                if (pomPackaging == null) {
                    continue;
                }
                
                pomPackaging = pomPackaging.trim().toLowerCase();
                
                if (pomPackaging.equals("war")) {
                    warProjects.add(projectDirectory);
                } else if (pomPackaging.equals("ear")) {
                    earProjects.add(projectDirectory);
                }
                
            } catch (Exception ex) {
                logger.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        
        for (FileObject warProject : warProjects) {
            warProject.addRecursiveListener(new TesteFileChangeListener(warProject, earProjects));
        }
    }
    
    private static class TesteFileChangeListener implements FileChangeListener {

        private Logger logger = Logger.getLogger(getClass().getName());
        
        private String projectFullPath;
        private final List<FileObject> earProjects;
        
        public TesteFileChangeListener(FileObject projectFile, List<FileObject> earProjects) {
            this.projectFullPath = FileUtil.toFile(projectFile).getPath();
            logger.info(projectFullPath);
            
            this.earProjects = earProjects;
        }
        
        @Override
        public void fileFolderCreated(FileEvent fe) {
            FileObject file = fe.getFile();
            logger.log(Level.INFO, "Nova Pasta: {0}", file.getPath());
            
            Path serverFile = getServerFile(file);
            
            if (serverFile == null) {
                return;
            }
            
            if (!serverFile.toFile().exists()) {
                try {
                    Files.createDirectory(serverFile);
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, ex.getMessage(), ex);
                    return;
                }
            }
            
            FileObject[] children = file.getChildren();
            
            if (children == null || children.length == 0) {
                return;
            }
            
            logger.log(Level.INFO, "Pasta possui {0} arquivos. Sincronizando com servidor...", children.length);
            
            for (FileObject fileObject : children) {
                copyToServer(fileObject);
            }
        }

        @Override
        public void fileDataCreated(FileEvent fe) {
            logger.log(Level.INFO, "Novo arquivo: {0}", fe.getFile().getPath());
            copyToServer(fe.getFile());
        }

        @Override
        public void fileChanged(FileEvent fe) {
            logger.log(Level.INFO, "Arquivo modificado: {0}", fe.getFile().getPath());
            copyToServer(fe.getFile());
        }
        
        private void copyToServer(FileObject fileObject) {
            Path serverFile = getServerFile(fileObject);
            
            if (serverFile == null) {
                return;
            }
            
            logger.log(Level.INFO, "Copiando arquivo para o servidor: {0}", serverFile);
            
            try (InputStream inputStream = fileObject.getInputStream()) {
                Files.copy(inputStream, serverFile, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        @Override
        public void fileDeleted(FileEvent fe) {
            logger.log(Level.INFO, "Arquivo Removido: {0}", fe.getFile());
            
            Path serverFile = getServerFile(fe.getFile());
            
            if (serverFile == null) {
                return;
            }
            
            if (!serverFile.toFile().exists()) {
                return;
            }
            
            logger.log(Level.INFO, "Removendo arquivo do servidor: {0}", serverFile);
            
            try {
                Files.delete(serverFile);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        @Override
        public void fileRenamed(FileRenameEvent fre) { }

        @Override
        public void fileAttributeChanged(FileAttributeEvent fae) { }
        
        private Path getServerFile(FileObject fileObject) {
            File file = FileUtil.toFile(fileObject);
            
            String name = file.getPath().replaceFirst(projectFullPath, "");
            
            if (!name.startsWith("/target/")) {
                logger.info("Arquivo nao compilado!");
                return null;
            }
            
            name = name.replaceFirst("^/target/", "");
            logger.log(Level.INFO, "Arquivo compilado: {0}", name);
            
            if (!name.startsWith("igen-web")) {
                return null;
            }
            
            String warName = name.split("/")[0];
            name = name.replaceFirst("^" + warName + "/", "");
                 
            for (FileObject earProject : earProjects) {
                String earName = null;
                
                FileObject[] children = earProject.getFileObject("target").getChildren();

                for (FileObject targetEarFile : children) {
                    if (targetEarFile.getExt().equalsIgnoreCase("ear")) {
                        earName = targetEarFile.getName();
                        break;
                    }
                }

                String config = NbPreferences.forModule(MagicaPanel.class).get("blablabla", "");
                
                if (config.trim().isEmpty()) {
                    config = "/opt/wildfly-11.0.0.Final/standalone/deployments/";
                }
                
                Path wildflyWarPath = Paths.get(config, earName + ".ear", warName + ".war");
                
                if (Files.exists(wildflyWarPath)) {
                    logger.log(Level.WARNING, "Pasta do servidor de aplicação não encontrada: {0}", config);
                    return null;
                }
                
                Path wildflyFilePath = wildflyWarPath.resolve(Paths.get(name));

                logger.log(Level.INFO, "Arquivo Wildfly: {0}", wildflyFilePath);
                return wildflyFilePath;
                
            }
            
            return null;
        }
        
    }
    
}
