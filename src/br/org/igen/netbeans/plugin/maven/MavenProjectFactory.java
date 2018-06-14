package br.org.igen.netbeans.plugin.maven;

import org.netbeans.api.project.Project;

/**
 *
 * @author gabrielhof
 */
public interface MavenProjectFactory {

    public MavenProject from(Project project);
    
    public static MavenProjectFactory getDefault() {
        return new SimpleMavenProjectFactory();
    }
}
