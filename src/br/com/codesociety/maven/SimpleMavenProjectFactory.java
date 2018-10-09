package br.com.codesociety.maven;

import java.util.Enumeration;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;

/**
 *
 * @author gabrielhof
 */
public class SimpleMavenProjectFactory implements MavenProjectFactory {

    public MavenProject from(Project project) {
        FileObject pomFile = project.getProjectDirectory().getFileObject("pom.xml");
        
        if (pomFile == null) {
            return null;
        }
        
        Enumeration<FileObject> folders = (Enumeration<FileObject>) project.getProjectDirectory().getFolders(false);
        
         while (folders.hasMoreElements()) {
            FileObject folder = folders.nextElement();
            
            FileObject submodulePom = folder.getFileObject("pom.xml");
            
            if (submodulePom == null) {
                continue;
            }
            
            return new SimpleMultiModuleMavenProject(project);
         }
         
         return new SimpleMavenModuleProject(project);
    }
    
}
