package br.com.codesociety.settings.model;

import br.com.codesociety.maven.MavenProject;
import br.com.codesociety.server.SupportedApplicationServer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;

/**
 *
 * @author gabrielhof
 * @since v1.0.0
 */
public class PropertiesFileSettingsStore implements SettingsStore {

    private static final String CONFIG_FILE = "nbproject/private/ear-copy-static-resource-on-save.properties";
    
    private static PropertiesFileSettingsStore INSTANCE;
    
    private final Map<Project, Settings> cachedSettings = new HashMap<>();
    
    @Override
    public void saveSettings(Settings settings) {
        cachedSettings.put(settings.getProject(), settings);
        
        Properties properties = loadProperties(settings.getProject());
        properties.put("active", Boolean.toString(settings.isActive()));
        properties.put("earProject.directory", settings.getEarProjectDirectory() == null ? "" : settings.getEarProjectDirectoryAsFile().toString());
        properties.put("applicationServer", settings.getSupportedServer() == null ? "" : settings.getSupportedServer().name());
        properties.put("applicationServer.diretory", settings.getServerDirectory() == null ? "" : settings.getServerDirectoryAsFile().toString());
        
        File configFile = getConfigFile(settings.getProject());
        
        if (configFile.exists()) {
            try (OutputStream out = new FileOutputStream(configFile)) {
                properties.store(out, CONFIG_FILE);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
    
    @Override
    public Settings getSettingsFor(MavenProject project) {
        if (cachedSettings.containsKey(project)) {
            return cachedSettings.get(project);
        }
        
        Settings settings = getSettingsFromPropeties(project);
        cachedSettings.put(project, settings);
        
        return settings;
    }
    
    private Settings getSettingsFromPropeties(MavenProject project) {
        Properties properties = loadProperties(project);
        
        Settings settings = new Settings(project);
        settings.setActive(Boolean.parseBoolean(properties.getProperty("active", "false")));
        
        String earProjectDir = properties.getProperty("earProject.directory");
        
        if (earProjectDir != null && !earProjectDir.isEmpty()) {
            settings.setEarProjectDirectory(new File(earProjectDir));
        }
        
        String server = properties.getProperty("applicationServer");
        
        if (server != null && !server.isEmpty()) {
            settings.setSupporterServer(SupportedApplicationServer.valueOf(server));
        }
        
        String serverDiretory = properties.getProperty("applicationServer.diretory");
        
        if (serverDiretory != null) {
            settings.setServerDirectory(new File(serverDiretory));
        }
        
        return settings;
    }
    
    private Properties loadProperties(Project project) {
        Properties properties = new Properties();
        
        File configFile = getConfigFile(project);
        
        if (!configFile.exists()) {
            return properties;
        }
        
        try (InputStream in = new FileInputStream(configFile)) {
            properties.load(in);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        return properties;
    }
    
    private File getConfigFile(Project project) {
        FileObject projectDiretory = project.getProjectDirectory();
        
        File configFile = FileUtil.toFile(projectDiretory).toPath().resolve(CONFIG_FILE).toFile();
        
        if (!configFile.getParentFile().exists()) {
            configFile.getParentFile().mkdirs();
        }
        
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        
        return configFile;
    }

    public static SettingsStore getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        
        INSTANCE = new PropertiesFileSettingsStore();
        return INSTANCE;
    }
}
