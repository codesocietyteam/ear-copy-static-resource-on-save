package br.org.igen.netbeans.plugin;

import br.org.igen.netbeans.plugin.maven.MavenModuleProject;
import br.org.igen.netbeans.plugin.maven.MavenProject;
import br.org.igen.netbeans.plugin.maven.MavenProjects;
import br.org.igen.netbeans.plugin.maven.MultiModuleMavenProject;
import br.org.igen.netbeans.plugin.maven.PomPackagingType;
import br.org.igen.netbeans.plugin.server.ApplicationServer;
import br.org.igen.netbeans.plugin.server.FileContext;
import br.org.igen.netbeans.plugin.settings.PropertiesFileSettingsStore;
import br.org.igen.netbeans.plugin.settings.Settings;
import br.org.igen.netbeans.plugin.settings.SettingsStore;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        
        for (MavenModuleProject module : project.getModules()) {
            if (module.getPomInfo().isPackaging(PomPackagingType.EAR)) {
                continue;
            }
            
            module.getProjectDirectory().addRecursiveListener(new TesteFileChangeListener(module));
        }
    }
    
    private static class TesteFileChangeListener implements FileChangeListener {

        private Logger logger = Logger.getLogger(getClass().getName());
        
        private final SettingsStore settingsStore = PropertiesFileSettingsStore.getInstance();
        
        private final MavenModuleProject project;
        
        public TesteFileChangeListener(MavenModuleProject project) {
            this.project = project;
        }
        
        @Override
        public void fileFolderCreated(FileEvent fe) {
            ifAllowed(fe.getFile(), (applicationServer, fileContext) -> {
                applicationServer.createDirectory(fileContext);
            });
        }

        @Override
        public void fileDataCreated(FileEvent fe) {
            ifAllowed(fe.getFile(), (applicationServer, fileContext) -> {
                applicationServer.createFile(fileContext);
            });
        }

        @Override
        public void fileChanged(FileEvent fe) {
            ifAllowed(fe.getFile(), (applicationServer, fileContext) -> {
                applicationServer.updateFile(fileContext);
            });
        }

        @Override
        public void fileDeleted(FileEvent fe) {
            ifAllowed(fe.getFile(), (applicationServer, fileContext) -> {
                applicationServer.deleteFile(fileContext);
            });
        }

        @Override
        public void fileRenamed(FileRenameEvent fre) { }

        @Override
        public void fileAttributeChanged(FileAttributeEvent fae) { }
        
        private void ifAllowed(FileObject file, BiConsumer<ApplicationServer, FileContext> consumer) {
            if (!isCompiled(file)) {
                return;
            }
            
            Settings sourceSettings = getSourceProjectSettings();
            
            if (!sourceSettings.isActive()) {
                return;
            }
            
            getApplicationServer().ifPresent(applicationServer -> {
                consumer.accept(applicationServer, createContext(file));
            });
        }
        
        private boolean isCompiled(FileObject file) {
            return FileUtil.toFile(file).toString().contains("/target/");
        }
        
        private FileContext createContext(FileObject changedFile) {
            return new FileContext(changedFile, project, getSourceProjectSettings(), getEarProject(), getEarProjectSettings());
        }
        
        private Settings getSourceProjectSettings() {
            return settingsStore.getSettingsFor(project);
        }
        
        private MavenProject getEarProject() {
            Settings settings = getSourceProjectSettings();
            return settings.getEarProject();
        }
        
        private Settings getEarProjectSettings() {
            MavenProject earProject = getEarProject();
            
            if (earProject == null) {
                return null;
            }
            
            return settingsStore.getSettingsFor(earProject);
        }
        
        private Optional<ApplicationServer> getApplicationServer() {
            Settings earSettings = getEarProjectSettings();
            
            if (earSettings == null) {
                return Optional.<ApplicationServer>empty();
            }
            
            return earSettings.getApplicationServer();
        }
    }
    
}
