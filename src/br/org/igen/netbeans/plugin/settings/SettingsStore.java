package br.org.igen.netbeans.plugin.settings;

import br.org.igen.netbeans.plugin.maven.MavenProject;

/**
 *
 * @author gabrielhof
 * @since v1.0.0
 */
public interface SettingsStore {

    public void saveSettings(Settings settings);
    
    public Settings getSettingsFor(MavenProject project);
    
}
