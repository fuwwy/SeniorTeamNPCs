package tech.folf.seniorteamnpc.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.folf.seniorteamnpc.SeniorTeamNPC;
import tech.folf.seniorteamnpc.data.ConfigManager;
import tech.folf.seniorteamnpc.npc.NPC;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ListNPCCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.hasPermission("seniorteamnpc.listnpcs"))
            commandSender.sendMessage(ConfigManager.noPermission);
        else {
            List<NPC> npcList = new ArrayList<>(SeniorTeamNPC.getNpcManager().getNpcList());
            if (npcList.size() < 1) {
                commandSender.sendMessage(ConfigManager.noNpcs);
                return true;
            }

            if (commandSender instanceof Player) {
                Location playerLocation = ((Player) commandSender).getLocation();
                npcList.sort(Comparator.comparingDouble(o -> o.getLocation().distanceSquared(playerLocation)));
            } else {
                npcList.sort(Comparator.comparing(npc -> npc.getName().toLowerCase()));
            }

            commandSender.sendMessage(npcList.stream().map(npc -> String.format(ConfigManager.npcListElement, npcList.indexOf(npc) + 1, npc.getName())).collect(Collectors.joining("\n")));
        }
        return true;
    }
}
