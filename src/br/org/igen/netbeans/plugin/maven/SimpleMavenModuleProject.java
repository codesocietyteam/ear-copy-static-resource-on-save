package br.org.igen.netbeans.plugin.maven;

import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.util.Lookup;

/**
 *
 * @author gabrielhof
 */
public class SimpleMavenModuleProject implements MavenModuleProject {

    private final Project project;
    
    public SimpleMavenModuleProject(Project project) {
        this.project = project;
    }
    
    @Override
    public FileObject getProjectDirectory() {
        return project.getProjectDirectory();
    }

    @Override
    public Lookup getLookup() {
        return project.getLookup();
    }
    
}
