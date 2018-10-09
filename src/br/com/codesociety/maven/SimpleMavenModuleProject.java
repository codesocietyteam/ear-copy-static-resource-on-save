package br.com.codesociety.maven;

import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.util.Lookup;

/**
 *
 * @author gabrielhof
 */
public class SimpleMavenModuleProject implements MavenModuleProject {

    private final Project project;
    
    private PomInfo pomInfo;
    
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
    
    @Override
    public PomInfo getPomInfo() {
        if (pomInfo == null) {
            pomInfo = PomInfoParser.parse(getProjectDirectory().getFileObject("pom.xml"));
        }
        
        return pomInfo;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        
        if (!(other instanceof SimpleMavenModuleProject)) {
            return false;
        }
        
        SimpleMavenModuleProject otherProject = (SimpleMavenModuleProject) other;
        return otherProject.project.equals(this.project);
    }

    @Override
    public int hashCode() {
        return (project.hashCode() + 12) * 37;
    }
    
    @Override
    public String toString() {
        return getProjectDirectory().getName();
    }
}
