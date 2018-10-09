package br.com.codesociety.listener;

import br.com.codesociety.maven.MavenModuleProject;
import br.com.codesociety.maven.MavenProject;
import br.com.codesociety.maven.MavenProjects;
import br.com.codesociety.maven.MultiModuleMavenProject;
import br.com.codesociety.maven.PomPackagingType;
import br.com.codesociety.server.ApplicationServer;
import br.com.codesociety.server.FileContext;
import br.com.codesociety.settings.model.PropertiesFileSettingsStore;
import br.com.codesociety.settings.model.Settings;
import br.com.codesociety.settings.model.SettingsStore;
import java.util.Optional;
import java.util.function.BiConsumer;
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

/**
 *
 * @author gabrielhof
 */
@OnStart
public class ProjectFileChangeVerifier implements Runnable {

    private Logger logger = Logger.getLogger(getClass().getName());
    
    @Override
    public void run() {
        MavenProjects.getDefault().addChangeListener(mavenProject -> {
            if (mavenProject.isMultiModule()) {
                verifyProjectAndAddListener((MultiModuleMavenProject) mavenProject);
            }
        });
    }
    
    private void verifyProjectAndAddListener(MultiModuleMavenProject project) {
        ProjectInformation projectInfo = ProjectUtils.getInformation(project);
        
        logger.log(Level.INFO, "Projeto Maven: {0}", projectInfo.getName());
        
        for (MavenModuleProject module : project.getModules()) {
            if (module.getPomInfo().isPackaging(PomPackagingType.EAR)) {
                continue;
            }
            
            module.getProjectDirectory().addRecursiveListener(new ProjectChangeListener(module));
        }
    }
    
    private static class ProjectChangeListener implements FileChangeListener {

        private Logger logger = Logger.getLogger(getClass().getName());
        
        private final SettingsStore settingsStore = PropertiesFileSettingsStore.getInstance();
        
        private final MavenModuleProject project;
        
        public ProjectChangeListener(MavenModuleProject project) {
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
