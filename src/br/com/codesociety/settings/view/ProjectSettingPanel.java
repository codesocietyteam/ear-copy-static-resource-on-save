/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.codesociety.settings.view;

import br.com.codesociety.maven.MavenProject;
import br.com.codesociety.maven.MavenProjects;
import br.com.codesociety.maven.PomPackagingType;
import br.com.codesociety.server.SupportedApplicationServer;
import br.com.codesociety.settings.model.Settings;
import br.com.codesociety.settings.model.PropertiesFileSettingsStore;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import org.openide.util.Lookup;

/**
 *
 * @author gabrielhof
 */
public class ProjectSettingPanel extends javax.swing.JPanel {

    private Lookup lookup;
    
    private List<SupportedApplicationServer> supportedApplicationServers;
    private List<MavenProject> earModules;

    private Settings settings;
    
    private JFileChooser fileChooser = new JFileChooser();
    
    /**
     * Creates new form ProjectSettingPanel
     */
    public ProjectSettingPanel(Lookup lookup) {
        this.lookup = lookup;
        
        loadData();
        initComponents();
        configureComponents();
    }

    private void loadData() {
        this.supportedApplicationServers = SupportedApplicationServer.all();
        
        this.earModules = MavenProjects.getDefault().getOpenedProjects()
                .stream().filter(m -> m.getPomInfo().isPackaging(PomPackagingType.EAR))
                .collect(Collectors.toList());
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelDescription = new javax.swing.JLabel();
        descriptionSeparator = new javax.swing.JSeparator();
        panelDefaultModule = new javax.swing.JPanel();
        labelEarModule = new javax.swing.JLabel();
        comboEarModules = new javax.swing.JComboBox<>();
        checkboxActivate = new javax.swing.JCheckBox();
        panelEarModule = new javax.swing.JPanel();
        labelApplicationServerDirectory = new javax.swing.JLabel();
        labelApplicationServer = new javax.swing.JLabel();
        textFieldApplicationServerDirectory = new javax.swing.JTextField();
        buttonSelectApplicationServerDirectory = new javax.swing.JButton();
        comboApplicationServers = new javax.swing.JComboBox<>();

        setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(ProjectSettingPanel.class, "ProjectSettingPanel.border.title"))); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(labelDescription, org.openide.util.NbBundle.getMessage(ProjectSettingPanel.class, "ProjectSettingPanel.labelDescription.text")); // NOI18N

        labelEarModule.setLabelFor(comboEarModules);
        org.openide.awt.Mnemonics.setLocalizedText(labelEarModule, org.openide.util.NbBundle.getMessage(ProjectSettingPanel.class, "ProjectSettingPanel.labelEarModule.text")); // NOI18N

        comboEarModules.setModel(new DefaultComboBoxModel<MavenProject>(earModules.toArray(new MavenProject[] {})));
        comboEarModules.setSelectedIndex(-1);
        comboEarModules.setSelectedItem(earModules.isEmpty() ? null : earModules.get(0));

        org.openide.awt.Mnemonics.setLocalizedText(checkboxActivate, org.openide.util.NbBundle.getMessage(ProjectSettingPanel.class, "ProjectSettingPanel.checkboxActivate.text")); // NOI18N
        checkboxActivate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkboxActivateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelDefaultModuleLayout = new javax.swing.GroupLayout(panelDefaultModule);
        panelDefaultModule.setLayout(panelDefaultModuleLayout);
        panelDefaultModuleLayout.setHorizontalGroup(
            panelDefaultModuleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDefaultModuleLayout.createSequentialGroup()
                .addGroup(panelDefaultModuleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDefaultModuleLayout.createSequentialGroup()
                        .addComponent(labelEarModule)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboEarModules, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(checkboxActivate, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelDefaultModuleLayout.setVerticalGroup(
            panelDefaultModuleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelDefaultModuleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(checkboxActivate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(panelDefaultModuleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelEarModule)
                    .addComponent(comboEarModules, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        checkboxActivate.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(ProjectSettingPanel.class, "ProjectSettingPanel.checkboxActivate.AccessibleContext.accessibleName")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(labelApplicationServerDirectory, org.openide.util.NbBundle.getMessage(ProjectSettingPanel.class, "ProjectSettingPanel.labelApplicationServerDirectory.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(labelApplicationServer, org.openide.util.NbBundle.getMessage(ProjectSettingPanel.class, "ProjectSettingPanel.labelApplicationServer.text")); // NOI18N

        textFieldApplicationServerDirectory.setText(org.openide.util.NbBundle.getMessage(ProjectSettingPanel.class, "ProjectSettingPanel.textFieldApplicationServerDirectory.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(buttonSelectApplicationServerDirectory, org.openide.util.NbBundle.getMessage(ProjectSettingPanel.class, "ProjectSettingPanel.buttonSelectApplicationServerDirectory.text")); // NOI18N
        buttonSelectApplicationServerDirectory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSelectApplicationServerDirectoryActionPerformed(evt);
            }
        });

        comboApplicationServers.setModel(new DefaultComboBoxModel<SupportedApplicationServer>(supportedApplicationServers.toArray(new SupportedApplicationServer[] {})));

        javax.swing.GroupLayout panelEarModuleLayout = new javax.swing.GroupLayout(panelEarModule);
        panelEarModule.setLayout(panelEarModuleLayout);
        panelEarModuleLayout.setHorizontalGroup(
            panelEarModuleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEarModuleLayout.createSequentialGroup()
                .addGroup(panelEarModuleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labelApplicationServer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelApplicationServerDirectory, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelEarModuleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelEarModuleLayout.createSequentialGroup()
                        .addComponent(textFieldApplicationServerDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonSelectApplicationServerDirectory))
                    .addComponent(comboApplicationServers, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(104, Short.MAX_VALUE))
        );
        panelEarModuleLayout.setVerticalGroup(
            panelEarModuleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEarModuleLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(panelEarModuleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelApplicationServer)
                    .addComponent(comboApplicationServers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelEarModuleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelApplicationServerDirectory)
                    .addComponent(textFieldApplicationServerDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonSelectApplicationServerDirectory))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labelDescription, javax.swing.GroupLayout.DEFAULT_SIZE, 611, Short.MAX_VALUE)
                    .addComponent(descriptionSeparator)
                    .addComponent(panelEarModule, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelDefaultModule, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(labelDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(descriptionSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelDefaultModule, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panelEarModule, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(52, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void configureComponents() {
        updateCurrentSettings();
    }
    
    public void updateCurrentSettings() {
        settings = PropertiesFileSettingsStore.getInstance().getSettingsFor(getProject());
        
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        
        checkboxActivate.setSelected(settings.isActive());
        comboEarModules.setSelectedItem(settings.getEarProject());
        comboApplicationServers.setSelectedItem(settings.getSupportedServer());
        
        File serverDiretoryFile = settings.getServerDirectoryAsFile();
        textFieldApplicationServerDirectory.setText(serverDiretoryFile == null ? "" : serverDiretoryFile.toString());
        
        configurePanels();
        updateComboEarModules();
    }
    
    private void configurePanels() {
        boolean isEar = getProject().getPomInfo().isPackaging(PomPackagingType.EAR);
        
        panelEarModule.setVisible(isEar);
        panelDefaultModule.setVisible(!isEar);
    }
    
    private void checkboxActivateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkboxActivateActionPerformed
        updateComboEarModules();
    }//GEN-LAST:event_checkboxActivateActionPerformed

    private void buttonSelectApplicationServerDirectoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSelectApplicationServerDirectoryActionPerformed
        fileChooser.showDialog(this, "Selecionar Pasta");
        
        File file = fileChooser.getSelectedFile();
        textFieldApplicationServerDirectory.setText(file == null ? "" : file.toString());
    }//GEN-LAST:event_buttonSelectApplicationServerDirectoryActionPerformed

    private void updateComboEarModules() {
        boolean isActive = checkboxActivate.isSelected();
        comboEarModules.setEnabled(isActive);
        
        if (isActive) {
            if (comboEarModules.getSelectedItem() == null && !earModules.isEmpty()) {
                comboEarModules.setSelectedItem(earModules.get(0));
            }
        } else {
            comboEarModules.setSelectedItem(null);
        }
    }
    
    public MavenProject getProject() {
        return MavenProjects.getDefault().getCurrent();
    }
    
    public Settings getSettings() {
        settings.setActive(checkboxActivate.isSelected());
        
        MavenProject earProject = (MavenProject) comboEarModules.getSelectedItem();
        
        if (earProject != null) {
            settings.setEarProjectDirectory(earProject.getProjectDirectory());
        } else {
            settings.setEarProjectDirectory((File) null);
        }
        
        settings.setSupporterServer(((SupportedApplicationServer) comboApplicationServers.getSelectedItem()));
        
        String serverDirectory = textFieldApplicationServerDirectory.getText();
        
        if (serverDirectory != null && !serverDirectory.isEmpty()) {
            settings.setServerDirectory(new File(serverDirectory));
        } else {
            settings.setServerDirectory((File) null);
        }
        
        return settings;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonSelectApplicationServerDirectory;
    private javax.swing.JCheckBox checkboxActivate;
    private javax.swing.JComboBox<SupportedApplicationServer> comboApplicationServers;
    private javax.swing.JComboBox<MavenProject> comboEarModules;
    private javax.swing.JSeparator descriptionSeparator;
    private javax.swing.JLabel labelApplicationServer;
    private javax.swing.JLabel labelApplicationServerDirectory;
    private javax.swing.JLabel labelDescription;
    private javax.swing.JLabel labelEarModule;
    private javax.swing.JPanel panelDefaultModule;
    private javax.swing.JPanel panelEarModule;
    private javax.swing.JTextField textFieldApplicationServerDirectory;
    // End of variables declaration//GEN-END:variables
}
