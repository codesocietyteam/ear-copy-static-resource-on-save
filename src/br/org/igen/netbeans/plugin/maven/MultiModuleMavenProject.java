package br.org.igen.netbeans.plugin.maven;

import java.util.List;

/**
 *
 * @author gabrielhof
 */
public interface MultiModuleMavenProject extends MavenProject {

    public List<MavenModuleProject> getModules();
    
}
