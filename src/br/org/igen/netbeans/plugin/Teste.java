package br.org.igen.netbeans.plugin;

import br.org.igen.netbeans.plugin.maven.MavenProject;
import br.org.igen.netbeans.plugin.maven.MavenProjects;
import br.org.igen.netbeans.plugin.maven.MavenProjectsChangeListener;
import br.org.igen.netbeans.plugin.maven.SimpleMultiModuleMavenProject;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.filesystems.FileAttributeEvent;
import org.openide.filesystems.FileChangeListener;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileRenameEvent;
import org.openide.filesystems.FileUtil;
import org.openide.modules.OnStart;
import org.openide.util.Exceptions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author gabrielhof
 * @since VERSION
 */
@OnStart
public class Teste implements Runnable {

    private Logger logger = Logger.getLogger(getClass().getName());
    
    @Override
    public void run() {
        MavenProjects.getDefault().addChangeListener(new MavenProjectsChangeListener() {
            @Override
            public void projectOpened(MavenProject project) {
                if (project.isMultiModule()) {
                    verifyWebProjectAndAddListener(project);
                }
            }
        });
    }

    private void verifyWebProjectAndAddListener(Project project) {
        ProjectInformation projectInfo = ProjectUtils.getInformation(project);
        
        logger.log(Level.INFO, "Projeto: {0}", project);
        logger.log(Level.INFO, "Classe Projeto: {0}", project.getClass());
        logger.log(Level.INFO, "Projeto Pai: {0}", project.getProjectDirectory().getParent());
        logger.log(Level.INFO, "Nome1: {0}", projectInfo.getName());
        logger.log(Level.INFO, "Nome2: {0}", projectInfo.getDisplayName());
        
        FileObject pomFile = project.getProjectDirectory().getFileObject("pom.xml");
        
        if (pomFile == null) {
            return;
        }
        
        logger.log(Level.INFO, "Projeto Maven encontrado!");
        
        Enumeration<FileObject> folders = (Enumeration<FileObject>) project.getProjectDirectory().getFolders(false);
        
        List<FileObject> warProjects = new ArrayList<>();
        List<FileObject> earProjects = new ArrayList<>();
        
        while (folders.hasMoreElements()) {
            FileObject folder = folders.nextElement();
            
            FileObject submodulePom = folder.getFileObject("pom.xml");
            
            if (submodulePom == null) {
                continue;
            }
            
            logger.log(Level.INFO, "Modulo encontrado: {0}", folder.getName());
            
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
                    warProjects.add(folder);
                } else if (pomPackaging.equals("ear")) {
                    earProjects.add(folder);
                }
                
            } catch (Exception ex) {
                logger.log(Level.SEVERE, ex.getMessage(), ex);
            }
            
            for (FileObject warProject : warProjects) {
                folder.addRecursiveListener(new TesteFileChangeListener(warProject, earProjects));
            }
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

                Path wildflyWarPath = Paths.get("/opt/wildfly-11.0.0.Final/standalone/deployments/" + earName + ".ear/" + warName + ".war");
                
                if (Files.exists(wildflyWarPath)) {
                    Path wildflyFilePath = wildflyWarPath.resolve(Paths.get(name));
                    
                    logger.log(Level.INFO, "Arquivo Wildfly: {0}", wildflyFilePath);
                    return wildflyFilePath;
                }
            }
            
            return null;
        }
        
    }
    
}
