package tech.folf.seniorteamnpc.data;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import tech.folf.seniorteamnpc.SeniorTeamNPC;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ConfigManager {
    public static String noConsole;
    public static String noPermission;
    public static String missingArgs;
    public static String npcCreated;
    public static String alreadyExists;
    public static String npcDeleted;
    public static String noNpcs;
    public static String npcListElement;
    public static String npcInfo;
    public static String distance;
    public static String diffWorld;
    public static String notFound;
    public static String notFoundName;
    public static String alreadyRunning;
    public static String invalidNumber;
    public static String invalidHand;
    public static int viewDistanceSquared;
    File configurationFile;
    YamlConfiguration configuration;

    public void load() {
        loadConfigurationFile();

        Field[] declaredFields = ConfigManager.class.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (Modifier.isStatic(declaredField.getModifiers()) && declaredField.getType().equals(String.class)) {
                String key = declaredField.getName();
                if (!configuration.contains(key)) {
                    SeniorTeamNPC.getInstance().getLogger().severe("Missing configuration key " + key);
                    continue;
                }
                try {
                    declaredField.set(null, ChatColor.translateAlternateColorCodes('&', configuration.getString(key)));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        viewDistanceSquared = (int) Math.pow(configuration.getInt("npcViewDistance"), 2);
    }

    private void loadConfigurationFile() {
        try {
            configurationFile = new File(SeniorTeamNPC.getInstance().getDataFolder(), "config.yml");
            configurationFile.getParentFile().mkdirs();
            configurationFile.createNewFile();
            configuration = new YamlConfiguration();

            InputStream defaults = ConfigManager.class.getResourceAsStream("/config.yml");
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
