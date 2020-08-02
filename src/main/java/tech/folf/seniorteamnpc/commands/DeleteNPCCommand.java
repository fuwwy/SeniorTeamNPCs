package tech.folf.seniorteamnpc.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tech.folf.seniorteamnpc.SeniorTeamNPC;
import tech.folf.seniorteamnpc.npc.NPC;

public class DeleteNPCCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        NPC npc;
        if (!commandSender.hasPermission("seniorteamnpc.deletenpc"))
            commandSender.sendMessage(ChatColor.RED + "No permission.");
        else if (strings.length < 1)
            commandSender.sendMessage(ChatColor.RED + "Missing arguments.");
        else if ((npc = SeniorTeamNPC.getNpcManager().get(strings[0])) == null)
            commandSender.sendMessage(ChatColor.RED + "Couldn't find an NPC named " + strings[0] + ".");
        else {
            SeniorTeamNPC.getNpcManager().deleteNpc(npc);
            commandSender.sendMessage(ChatColor.GREEN + "Deleted " + npc.getName());
        }
        return true;
    }

}
