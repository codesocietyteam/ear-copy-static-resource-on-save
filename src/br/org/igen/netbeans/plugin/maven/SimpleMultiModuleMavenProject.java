package br.org.igen.netbeans.plugin.maven;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.util.Lookup;

/**
 *
 * @author gabrielhof
 */
public class SimpleMultiModuleMavenProject implements MultiModuleMavenProject {

    private final Project project;
    private PomInfo pomInfo;
    
    private List<MavenModuleProject> modules;
    
    
    public SimpleMultiModuleMavenProject(Project project) {
        this.project = project;
    }

    @Override
    public List<MavenModuleProject> getModules() {
        if (modules != null) {
            return modules;
        }
        
        modules = new ArrayList<>();
        
        Enumeration<FileObject> folders = (Enumeration<FileObject>) project.getProjectDirectory().getFolders(false);
        
         while (folders.hasMoreElements()) {
            FileObject folder = folders.nextElement();
            FileObject submodulePom = folder.getFileObject("pom.xml");
            
            if (submodulePom == null) {
                continue;
            }
            
            Project moduleProject = FileOwnerQuery.getOwner(folder);
            
            if (moduleProject.equals(project)) {
                continue;
            }
            
            modules.add((MavenModuleProject) MavenProjectFactory.getDefault().from(moduleProject));
         }
         
         return modules;
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
        
        if (!(other instanceof SimpleMultiModuleMavenProject)) {
            return false;
        }
        
        SimpleMultiModuleMavenProject otherProject = (SimpleMultiModuleMavenProject) other;
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
