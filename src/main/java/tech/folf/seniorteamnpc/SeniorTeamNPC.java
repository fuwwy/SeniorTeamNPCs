package tech.folf.seniorteamnpc;

import org.bukkit.plugin.java.JavaPlugin;
import tech.folf.seniorteamnpc.commands.*;
import tech.folf.seniorteamnpc.data.ConfigManager;
import tech.folf.seniorteamnpc.data.DataManager;
import tech.folf.seniorteamnpc.npc.NPC;
import tech.folf.seniorteamnpc.npc.NPCManager;

public class SeniorTeamNPC extends JavaPlugin {
    private static SeniorTeamNPC instance;
    private static NPCManager npcManager;
    private DataManager dataManager;
    private ConfigManager configManager;

    public static NPCManager getNpcManager() {
        return npcManager;
    }

    public static SeniorTeamNPC getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        configManager = new ConfigManager();
        configManager.load();;

        npcManager = new NPCManager();

        dataManager = new DataManager();
        dataManager.load();

        getCommand("createnpc").setExecutor(new CreateNPCCommand());
        getCommand("crouch").setExecutor(new ActionCommands.CrouchCommand());
        getCommand("attack").setExecutor(new ActionCommands.AttackCommand());
        getCommand("npcinfo").setExecutor(new NPCInfoCommand());
        getCommand("listnpcs").setExecutor(new ListNPCCommand());
        getCommand("deletenpc").setExecutor(new DeleteNPCCommand());
    }

    @Override
    public void onDisable() {
        dataManager.save();
        for (NPC npc : npcManager.getNpcList()) {
            npcManager.destroyNpc(npc);
        }
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
