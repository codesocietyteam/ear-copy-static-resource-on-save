package br.org.igen.netbeans.plugin.settings;

import org.netbeans.api.project.Project;

/**
 *
 * @author gabrielhof
 * @since v1.0.0
 */
public interface SettingsStore {

    public void saveSettings(Settings settings);
    
    public Settings getSettingsFor(Project project);
    
}
