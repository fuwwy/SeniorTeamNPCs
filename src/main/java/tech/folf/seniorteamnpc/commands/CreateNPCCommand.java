package tech.folf.seniorteamnpc.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.folf.seniorteamnpc.SeniorTeamNPC;

public class CreateNPCCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player))
            commandSender.sendMessage(ChatColor.RED + "This command can only be executed by in-game players.");
        else if (!commandSender.hasPermission("seniorteamnpc.createnpc"))
            commandSender.sendMessage(ChatColor.RED + "No permission.");
        else if (strings.length < 1) commandSender.sendMessage(ChatColor.RED + "Missing arguments");
        else {
            SeniorTeamNPC.getNpcManager().createNpc(((Player) commandSender).getLocation(), strings[0]);
            commandSender.sendMessage(ChatColor.GREEN + "Created NPC");
        }
        return true;
    }
}
