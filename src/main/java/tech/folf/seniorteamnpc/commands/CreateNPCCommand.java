package tech.folf.seniorteamnpc.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.folf.seniorteamnpc.SeniorTeamNPC;
import tech.folf.seniorteamnpc.data.ConfigManager;

public class CreateNPCCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player))
            commandSender.sendMessage(ConfigManager.noConsole);
        else if (!commandSender.hasPermission("seniorteamnpc.createnpc"))
            commandSender.sendMessage(ConfigManager.noPermission);
        else if (strings.length < 1) commandSender.sendMessage(ConfigManager.missingArgs);
        else {
            if (SeniorTeamNPC.getNpcManager().get(strings[0]) != null) {
                commandSender.sendMessage(ConfigManager.alreadyExists);
                return true;
            }
            SeniorTeamNPC.getNpcManager().createNpc(((Player) commandSender).getLocation(), strings[0]);
            commandSender.sendMessage(String.format(ConfigManager.npcCreated, strings[0]));
        }
        return true;
    }
}
