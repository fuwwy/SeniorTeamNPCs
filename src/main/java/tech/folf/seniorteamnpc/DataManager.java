package tech.folf.seniorteamnpc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import tech.folf.seniorteamnpc.npc.NPC;

import java.io.File;
import java.io.IOException;

public class DataManager {
    File configurationFile;
    YamlConfiguration configuration;

    public void load() {
        loadConfigurationFile();
        for (String key : configuration.getKeys(false)) {
            ConfigurationSection section = configuration.getConfigurationSection(key);
            deserializeNpc(section);
        }
    }

    public void save() {
        configuration = new YamlConfiguration();
        for (int i = 0; i < SeniorTeamNPC.getNpcManager().getNpcList().size(); i++) {
            NPC npc = SeniorTeamNPC.getNpcManager().getNpcList().get(i);
            configuration.set(String.valueOf(i), serializeNpc(configuration.createSection(String.valueOf(i)), npc));
        }
        try {
            configuration.save(configurationFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadConfigurationFile() {
        try {
            configurationFile = new File(SeniorTeamNPC.getInstance().getDataFolder(), "data.yml");
            if (!configurationFile.getParentFile().exists()) configurationFile.getParentFile().mkdirs();
            if (!configurationFile.exists()) configurationFile.createNewFile();
            configuration = new YamlConfiguration();
            configuration.load(configurationFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void deserializeNpc(ConfigurationSection section) {
        Location location = new Location(
                Bukkit.getWorld(section.getString("world")),
                section.getDouble("x"),
                section.getDouble("y"),
                section.getDouble("z"),
                (float) section.getDouble("yaw"),
                (float) section.getDouble("pitch")
        );
        SeniorTeamNPC.getNpcManager().registerNpc(new NPC(section.getString("name"), location));
    }

    private ConfigurationSection serializeNpc(ConfigurationSection section, NPC npc) {
        section.set("name", npc.getName());
        section.set("world", npc.getLocation().getWorld().getName());
        section.set("x", npc.getLocation().getX());
        section.set("y", npc.getLocation().getY());
        section.set("z", npc.getLocation().getZ());
        section.set("yaw", npc.getLocation().getYaw());
        section.set("pitch", npc.getLocation().getPitch());
        return section;
    }
}
