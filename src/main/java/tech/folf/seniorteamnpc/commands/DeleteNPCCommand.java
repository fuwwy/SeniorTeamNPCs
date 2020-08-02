package tech.folf.seniorteamnpc.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tech.folf.seniorteamnpc.SeniorTeamNPC;
import tech.folf.seniorteamnpc.data.ConfigManager;
import tech.folf.seniorteamnpc.npc.NPC;

public class DeleteNPCCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        NPC npc;
        if (!commandSender.hasPermission("seniorteamnpc.deletenpc"))
            commandSender.sendMessage(ConfigManager.noPermission);
        else if (strings.length < 1)
            commandSender.sendMessage(ConfigManager.missingArgs);
        else if ((npc = SeniorTeamNPC.getNpcManager().get(strings[0])) == null)
            commandSender.sendMessage(String.format(ConfigManager.notFoundName, strings[0]));
        else {
            SeniorTeamNPC.getNpcManager().deleteNpc(npc);
            commandSender.sendMessage(String.format(ConfigManager.npcDeleted, npc.getName()));
        }
        return true;
    }

}
