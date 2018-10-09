package br.com.codesociety.maven;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.util.Lookup;
import org.openide.util.Utilities;

/**
 *
 * @author gabrielhof
 */
public final class MavenProjects {

    private static MavenProjects INSTANCE;

    private final MavenProjectFactory mavenProjectFactory = MavenProjectFactory.getDefault(); 
    
    private final OpenProjects openedProjects;
    
    private MavenProjects() {
        this.openedProjects = OpenProjects.getDefault();
    }
    
    public void addChangeListener(MavenProjectsChangeListener listener) {
        openedProjects.addPropertyChangeListener(new MavenProjectModifiedChangeListener(listener));
    }
    
    public MavenProject getCurrent() {
        Lookup lookup = Utilities.actionsGlobalContext(); 
        return getCurrent(lookup);
    }
    
    public MavenProject getCurrent(Lookup lookup) {
        Project project = lookup.lookup(Project.class);
        
        if (project != null) {
            return mavenProjectFactory.from(project);
        }
        
        DataObject dataObject = lookup.lookup(DataObject.class);
        
        if (dataObject == null) {
            return null;
        }
        
        return getProject(dataObject.getPrimaryFile());
    }
    
    public MavenProject getProject(FileObject file) {
        Project project = FileOwnerQuery.getOwner(file);
        return mavenProjectFactory.from(project);
    }
    
    public List<MavenProject> getOpenedProjects() {
        return toMavenProjects(Arrays.asList(openedProjects.getOpenProjects()));
    }
    
    private List<MavenProject> toMavenProjects(List<Project> projects) {
        return projects.stream()
                    .map(project -> mavenProjectFactory.from(project))
                    .filter(mavenProject -> mavenProject != null)
                    .collect(Collectors.toList());
                    
    }
    
    public static MavenProjects getDefault() {
        if (INSTANCE == null) {
            INSTANCE = new MavenProjects();
        }
        
        return INSTANCE;
    }
    
    private class MavenProjectModifiedChangeListener implements PropertyChangeListener {

        private MavenProjectsChangeListener listener;
        
        private MavenProjectModifiedChangeListener(MavenProjectsChangeListener listener) {
            this.listener = listener;
        }

        @Override
        public void propertyChange(PropertyChangeEvent event) {
            Project[] openedProjects = (Project[]) event.getNewValue();
            
            if (openedProjects == null) {
                return;
            }
            
            toMavenProjects(Arrays.asList(openedProjects))
                    .forEach(mavenProject -> listener.projectOpened(mavenProject));
        }
    
    }
}
