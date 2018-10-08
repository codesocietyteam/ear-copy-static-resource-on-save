package br.org.igen.netbeans.plugin.server;

import br.org.igen.netbeans.plugin.maven.MavenProject;
import br.org.igen.netbeans.plugin.settings.Settings;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.filesystems.FileObject;

/**
 *
 * @author gabrielhof
 * @since v1.0.0
 */
public class WildflyApplicationServer implements ApplicationServer {

    private static final Logger logger = Logger.getLogger(WildflyApplicationServer.class.getName());
    
    private static final Path DEPLOYMENT_PATH = Paths.get("standalone", "deployments");
    
    private static final String[] PREFIXES_TO_REPLACE = new String[] {
        "target",
        "classes"
    };
    
    @Override
    public String getName() {
        return "JBoss Wildfly";
    }

    @Override
    public void createDirectory(FileContext context) {
        try {
            Path serverEquivalentFile = getServerEquivalentFile(context);

            if (!Files.exists(serverEquivalentFile)) {
                Files.createDirectories(serverEquivalentFile);
            }

            FileObject[] children = context.getChangedFileObject().getChildren();

            for (FileObject child : children) {
                updateFile(new FileContext(child, context));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createFile(FileContext context) {
        updateFile(context);
    }
    
    @Override
    public void updateFile(FileContext context) {
        try {
            Path serverEquivalentFile = getServerEquivalentFile(context);
            
            if (!Files.exists(serverEquivalentFile.getParent())) {
                Files.createDirectories(serverEquivalentFile.getParent());
            }
            
            Files.copy(context.getChangedFile().toPath(), serverEquivalentFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteFile(FileContext context) {
        try {
            Path serverEquivalentFile = getServerEquivalentFile(context);

            if (Files.exists(serverEquivalentFile)) {
                Files.delete(serverEquivalentFile);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private Path getServerEquivalentFile(FileContext context) {
        File file = context.getChangedFile();
        MavenProject sourceProject = context.getSourceProject();
        MavenProject earProject = context.getEarProject();
        Settings earSettings = context.getEarSettings();
        
        Path fileName = Paths.get(file.getPath().replaceFirst(sourceProject.getProjectDirectoryAsFile().getPath(), ""));

        for (String prefix : PREFIXES_TO_REPLACE) {
            if (fileName.getName(0).toString().startsWith(prefix)) {
                fileName = fileName.subpath(1, fileName.getNameCount());
            }
        }
        
        logger.log(Level.INFO, "Arquivo compilado: {0}", fileName);

        if (fileName.getName(0).toString().startsWith(sourceProject.getPomInfo().getName())) {
            fileName = fileName.subpath(1, fileName.getNameCount());
        }

        Path projectPath = getProjectPath(sourceProject.getProjectDirectory(), earProject.getProjectDirectory());

        if (projectPath == null) {
            return null;
        }

        Path serverPath = earSettings
                .getServerDirectoryAsFile()
                .toPath()
                .resolve(DEPLOYMENT_PATH)
                .resolve(projectPath);

        if (Files.notExists(serverPath)) {
            logger.log(Level.WARNING, "Pasta do servidor de aplicação não encontrada: {0}", serverPath);
            return null;
        }

        Path serverFullFilePath = serverPath.resolve(fileName);

        logger.log(Level.INFO, "Arquivo Wildfly: {0}", serverFullFilePath);
        return serverFullFilePath;
    }
    
    private Path getProjectPath(FileObject sourceProjectDiretory, FileObject earProjectDiretory) {
        FileObject targetDiretory = earProjectDiretory.getFileObject("target");
        FileObject[] children = targetDiretory.getChildren();
        
        Path path = null;
        FileObject compiledEarDiretory = null;
        
        for (FileObject targetEarFile : children) {
            if (!targetEarFile.getExt().equalsIgnoreCase("ear")) {
                continue;
            }
            
            path = Paths.get(targetEarFile.getName() + "." + targetEarFile.getExt());
            compiledEarDiretory = targetDiretory.getFileObject(targetEarFile.getName());
        }
        
        if (compiledEarDiretory == null || !compiledEarDiretory.canRead()) {
            return path;
        }
        
        for (FileObject targetProject : compiledEarDiretory.getChildren()) {
            if (!targetProject.getName().startsWith(sourceProjectDiretory.getName())) {
                continue;
            }

            return path.resolve(targetProject.getName() + "." + targetProject.getExt());
        }
        
        return path;
    }

}
