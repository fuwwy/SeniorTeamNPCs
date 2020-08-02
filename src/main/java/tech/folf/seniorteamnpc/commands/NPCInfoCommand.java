package tech.folf.seniorteamnpc.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.folf.seniorteamnpc.SeniorTeamNPC;
import tech.folf.seniorteamnpc.data.ConfigManager;
import tech.folf.seniorteamnpc.npc.NPC;

public class NPCInfoCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        NPC npc;
        if (!commandSender.hasPermission("seniorteamnpc.npcinfo"))
            commandSender.sendMessage(ConfigManager.noPermission);
        else if (strings.length < 1)
            commandSender.sendMessage(ConfigManager.missingArgs);
        else if ((npc = SeniorTeamNPC.getNpcManager().get(strings[0])) == null)
            commandSender.sendMessage(String.format(ConfigManager.notFoundName, strings[0]));
        else {
            String location = npc.getLocation().getBlockX() + " " + npc.getLocation().getBlockY() + " " + npc.getLocation().getBlockZ();
            if (commandSender instanceof Player && npc.getLocation().getWorld().equals(((Player) commandSender).getWorld())) {
                int distance = (int) Math.round(npc.getLocation().distance(((Player) commandSender).getLocation()));
                commandSender.sendMessage(String.format(ConfigManager.npcInfo,
                        npc.getName(), npc.getCurrentAction(), location,
                        String.format(ConfigManager.distance, distance)));
            } else {
                commandSender.sendMessage(String.format(ConfigManager.npcInfo,
                        npc.getName(), npc.getCurrentAction(), location,
                        String.format(ConfigManager.diffWorld, npc.getLocation().getWorld().getName())));
            }
        }
        return true;
    }
}
