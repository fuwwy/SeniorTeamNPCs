package tech.folf.seniorteamnpc.data;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import tech.folf.seniorteamnpc.SeniorTeamNPC;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConfigManager {
    File configurationFile;
    YamlConfiguration configuration;

    public void load() {
        loadConfigurationFile();
    }

    private void loadConfigurationFile() {
        try {
            configurationFile = new File(SeniorTeamNPC.getInstance().getDataFolder(), "config.yml");
            configurationFile.getParentFile().mkdirs();
            configurationFile.createNewFile();
            configuration = new YamlConfiguration();

            InputStream defaults = ConfigManager.class.getResourceAsStream("config.yml");
            YamlConfiguration defaultConfig = new YamlConfiguration();
            defaultConfig.load(new InputStreamReader(defaults));
            configuration.setDefaults(defaultConfig);
            configuration.options().copyDefaults(true);

            configuration.load(configurationFile);
            configuration.save(configurationFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
