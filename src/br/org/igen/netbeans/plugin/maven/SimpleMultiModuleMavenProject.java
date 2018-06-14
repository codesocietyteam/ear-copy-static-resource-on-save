package br.org.igen.netbeans.plugin.maven;

import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.util.Lookup;

/**
 *
 * @author gabrielhof
 */
public class SimpleMultiModuleMavenProject implements MultiModuleMavenProject {

    private final Project project;
    
    public SimpleMultiModuleMavenProject(Project project) {
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
