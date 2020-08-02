package tech.folf.seniorteamnpc.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.folf.seniorteamnpc.SeniorTeamNPC;
import tech.folf.seniorteamnpc.npc.NPC;

public class NPCInfoCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        NPC npc;
        if (!commandSender.hasPermission("seniorteamnpc.npcinfo"))
            commandSender.sendMessage(ChatColor.RED + "No permission.");
        else if (strings.length < 1)
            commandSender.sendMessage(ChatColor.RED + "Missing arguments.");
        else if ((npc = SeniorTeamNPC.getNpcManager().get(strings[0])) == null)
            commandSender.sendMessage(ChatColor.RED + "Couldn't find an NPC named " + strings[0] + ".");
        else {
            String divider = ChatColor.GRAY + " | ";
            String message = ChatColor.YELLOW + npc.getName() + divider +
                    ChatColor.GREEN + npc.getCurrentAction();

            if (commandSender instanceof Player) {
                message += divider + ChatColor.AQUA;
                if (npc.getLocation().getWorld().equals(((Player) commandSender).getWorld())) {
                    int distance = (int) Math.round(npc.getLocation().distance(((Player) commandSender).getLocation()));
                    message += distance + "m away.";
                } else {
                    message += "In a diferent world.";
                }
            }

            commandSender.sendMessage(message);
        }
        return true;
    }
}
