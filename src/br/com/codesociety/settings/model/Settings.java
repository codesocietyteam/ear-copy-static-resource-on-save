package br.com.codesociety.settings.model;

import br.com.codesociety.maven.MavenProject;
import br.com.codesociety.maven.MavenProjects;
import br.com.codesociety.server.ApplicationServer;
import br.com.codesociety.server.SupportedApplicationServer;
import java.io.File;
import java.util.Optional;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author gabrielhof
 * @since 1.0.0
 */
public class Settings {

    private final MavenProject project;
    
    private boolean active;
    private FileObject earProjectDirectory;
    
    private SupportedApplicationServer server;
    private FileObject serverDirectory;

    public Settings(MavenProject projet) {
        this.project = projet;
    }

    public MavenProject getProject() {
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

    public SupportedApplicationServer getSupportedServer() {
        return server;
    }

    public void setSupporterServer(SupportedApplicationServer server) {
        this.server = server;
    }
    
    public Optional<ApplicationServer> getApplicationServer() {
        if (server == null) {
            return Optional.<ApplicationServer>empty();
        }
        
        return Optional.ofNullable(server.getApplicationServer());
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
