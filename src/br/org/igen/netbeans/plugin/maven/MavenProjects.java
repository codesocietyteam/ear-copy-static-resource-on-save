package br.org.igen.netbeans.plugin.maven;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ui.OpenProjects;

/**
 *
 * @author gabrielhof
 */
public final class MavenProjects {

    private static MavenProjects INSTANCE;
    
    private final OpenProjects openedProjects;
    
    private MavenProjects() {
        this.openedProjects = OpenProjects.getDefault();
    }
    
    public void addChangeListener(MavenProjectsChangeListener listener) {
        openedProjects.addPropertyChangeListener(new MavenProjectModifiedChangeListener(listener));
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
            
            for (Project openedProject : openedProjects) {
                MavenProject mavenProject = MavenProjectFactory.getDefault().from(openedProject);

                if (mavenProject == null) {
                    return;
                }

                listener.projectOpened(mavenProject);
            }
        }
    
    }
}
