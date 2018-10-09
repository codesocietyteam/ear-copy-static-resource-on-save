package br.com.codesociety.maven;

import java.io.File;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author gabrielhof
 */
public interface MavenProject extends Project {

    public PomInfo getPomInfo();
    
    public default File getProjectDirectoryAsFile() {
        return FileUtil.toFile(getProjectDirectory());
    }
    
    
    public default boolean isMultiModule() {
        return this instanceof MultiModuleMavenProject;
    }

}
