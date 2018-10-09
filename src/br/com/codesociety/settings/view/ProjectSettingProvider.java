package br.com.codesociety.settings.view;

import br.com.codesociety.settings.model.PropertiesFileSettingsStore;
import br.com.codesociety.settings.model.Settings;
import br.com.codesociety.settings.model.SettingsStore;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import org.netbeans.spi.project.ui.support.ProjectCustomizer;
import org.openide.util.Lookup;

/**
 *
 * @author gabrielhof
 * @since 1.0.0
 */
@ProjectCustomizer.CompositeCategoryProvider.Registration(
        projectType = "org-netbeans-modules-maven", 
        position = 901
)
public class ProjectSettingProvider implements ProjectCustomizer.CompositeCategoryProvider {

    @Override
    public ProjectCustomizer.Category createCategory(Lookup lkp) {
        return ProjectCustomizer.Category.create("web-files-synchronizer", "Sincronização Web Automática", null);
    }

    @Override
    public JComponent createComponent(ProjectCustomizer.Category category, Lookup lookup) {
        ProjectSettingPanel projectSettingPanel = new ProjectSettingPanel(lookup);
        category.setOkButtonListener(new OkButtonListener(projectSettingPanel));
        
        return projectSettingPanel;
    }

    private class OkButtonListener implements ActionListener {

        private final ProjectSettingPanel projectSettingPanel;
        
        public OkButtonListener(ProjectSettingPanel projectSettingPanel) {
            this.projectSettingPanel = projectSettingPanel;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            Settings settings = projectSettingPanel.getSettings();
            PropertiesFileSettingsStore.getInstance().saveSettings(settings);
        }
        
    }
}
