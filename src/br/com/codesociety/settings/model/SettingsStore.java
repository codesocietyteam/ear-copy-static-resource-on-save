package br.com.codesociety.settings.model;

import br.com.codesociety.maven.MavenProject;

/**
 *
 * @author gabrielhof
 * @since v1.0.0
 */
public interface SettingsStore {

    public void saveSettings(Settings settings);
    
    public Settings getSettingsFor(MavenProject project);
    
}
