package br.org.igen.netbeans.plugin.maven;

import org.netbeans.api.project.Project;

/**
 *
 * @author gabrielhof
 */
public interface MavenProject extends Project {

    public PomInfo getPomInfo();
    
    public default boolean isMultiModule() {
        return this instanceof MultiModuleMavenProject;
    }

}
