package br.org.igen.netbeans.plugin.settings;

import br.org.igen.netbeans.plugin.maven.MavenProject;
import br.org.igen.netbeans.plugin.maven.MavenProjects;
import br.org.igen.netbeans.plugin.server.ApplicationServer;
import br.org.igen.netbeans.plugin.server.SupportedApplicationServer;
import java.io.File;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author gabrielhof
 * @since 1.0.0
 */
public class Settings {

    private final Project project;
    
    private boolean active;
    private FileObject earProjectDirectory;
    
    private SupportedApplicationServer server;
    private FileObject serverDirectory;

    public Settings(Project projet) {
        this.project = projet;
    }

    public Project getProject() {
        return project;
    }
    
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public FileObject getEarProjectDirectory() {
        return earProjectDirectory;
    }
    
    public File getEarProjectDirectoryAsFile() {
        if (earProjectDirectory == null) {
            return null;
        }
        
        return FileUtil.toFile(earProjectDirectory);
    }

    public void setEarProjectDirectory(FileObject earProjectDirectory) {
        this.earProjectDirectory = earProjectDirectory;
    }
    
    public void setEarProjectDirectory(File earProjectDirectory) {
        setEarProjectDirectory(earProjectDirectory == null ? null : FileUtil.toFileObject(earProjectDirectory));
    }
    
    public MavenProject getEarProject() {
        if (earProjectDirectory == null) {
            return null;
        }
        
        return MavenProjects.getDefault().getProject(earProjectDirectory);
    }

    public SupportedApplicationServer getServer() {
        return server;
    }

    public void setServer(SupportedApplicationServer server) {
        this.server = server;
    }

    public FileObject getServerDirectory() {
        return serverDirectory;
    }
    
    public File getServerDirectoryAsFile() {
        if (serverDirectory == null) {
            return null;
        }
        
        return FileUtil.toFile(serverDirectory);
    }

    public void setServerDirectory(FileObject serverDirectory) {
        this.serverDirectory = serverDirectory;
    }
    
    public void setServerDirectory(File serverDirectory) {
        setServerDirectory(serverDirectory == null ? null : FileUtil.toFileObject(serverDirectory));
    }
    
}
