package br.org.igen.netbeans.plugin.server;

import br.org.igen.netbeans.plugin.maven.MavenProject;
import br.org.igen.netbeans.plugin.settings.Settings;
import java.io.File;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author gabrielhof
 * @since v1.0.0
 */
public class FileContext {

    private final FileObject changedFileObject;
    
    private final MavenProject sourceProject;
    
    private final Settings sourceSettings;
    
    private final MavenProject earProject;
    
    private final Settings earSettings;

    public FileContext(FileObject changedFileObject, FileContext context) {
        this(changedFileObject, context.getSourceProject(), context.getSourceSettings(), context.getEarProject(), context.getEarSettings());
    }
    
    public FileContext(FileObject changedFileObject, MavenProject sourceProject, Settings sourceSettings, MavenProject earProject, Settings earSettings) {
        this.changedFileObject = changedFileObject;
        this.sourceProject = sourceProject;
        this.sourceSettings = sourceSettings;
        this.earProject = earProject;
        this.earSettings = earSettings;
    }
    
    public FileObject getChangedFileObject() {
        return changedFileObject;
    }
    
    public File getChangedFile() {
        return FileUtil.toFile(changedFileObject);
    }

    public MavenProject getSourceProject() {
        return sourceProject;
    }

    public Settings getSourceSettings() {
        return sourceSettings;
    }

    public MavenProject getEarProject() {
        return earProject;
    }

    public Settings getEarSettings() {
        return earSettings;
    }
    
}
