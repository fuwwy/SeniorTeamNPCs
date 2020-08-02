package tech.folf.seniorteamnpc;

import org.bukkit.plugin.java.JavaPlugin;
import tech.folf.seniorteamnpc.commands.ActionCommands;
import tech.folf.seniorteamnpc.commands.CreateNPCCommand;
import tech.folf.seniorteamnpc.npc.NPCManager;

public class SeniorTeamNPC extends JavaPlugin {
    private static SeniorTeamNPC instance;
    private static NPCManager npcManager;

    public static NPCManager getNpcManager() {
        return npcManager;
    }

    public static SeniorTeamNPC getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        npcManager = new NPCManager();
        getCommand("createnpc").setExecutor(new CreateNPCCommand());
        getCommand("crouch").setExecutor(new ActionCommands.CrouchCommand());
        getCommand("attack").setExecutor(new ActionCommands.AttackCommand());
    }
}
