package tech.folf.seniorteamnpc.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import tech.folf.seniorteamnpc.PacketUtils;
import tech.folf.seniorteamnpc.SeniorTeamNPC;
import tech.folf.seniorteamnpc.npc.Action;
import tech.folf.seniorteamnpc.npc.NPC;

public class ActionCommands {

    public static class CrouchCommand implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
            NPC npc;
            if (!(commandSender instanceof Player))
                commandSender.sendMessage(ChatColor.RED + "This command can only be executed by in-game players.");
            else if (!commandSender.hasPermission("seniorteamnpc.crouch"))
                commandSender.sendMessage(ChatColor.RED + "No permission.");
            else if ((npc = SeniorTeamNPC.getNpcManager().getNearestNpc((Player) commandSender)) == null)
                commandSender.sendMessage(ChatColor.RED + "Couldn't find an NPC near you.");
            else if (npc.getCurrentAction() != Action.STANDING)
                commandSender.sendMessage(ChatColor.RED + "NPC " + npc.getName() + " is already executing an action.");
            else if (strings.length < 1)
                commandSender.sendMessage(ChatColor.RED + "Missing arguments.");
            else {
                long interval;
                try {
                    interval = Long.parseLong(strings[0]);
                } catch (NumberFormatException e) {
                    commandSender.sendMessage(ChatColor.RED + strings[1] + " is not a valid number.");
                    return true;
                }

                long stopAt = System.currentTimeMillis() + 5000;
                npc.setCurrentAction(Action.CROUCHING);
                final boolean[] sneaking = {false};
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        PacketUtils.sendPlayerCrouching((Player) commandSender, npc, sneaking[0]);
                        sneaking[0] = !sneaking[0];
                        if (stopAt < System.currentTimeMillis()) {
                            cancel();
                            PacketUtils.sendPlayerCrouching((Player) commandSender, npc, true);
                            npc.setCurrentAction(Action.STANDING);
                        }
                    }
                }.runTaskTimer(SeniorTeamNPC.getInstance(), 0L, interval / 1000 * 20);
            }
            return true;
        }
    }

    public static class AttackCommand implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
            NPC npc;
            if (!(commandSender instanceof Player))
                commandSender.sendMessage(ChatColor.RED + "This command can only be executed by in-game players.");
            else if (!commandSender.hasPermission("seniorteamnpc.attack"))
                commandSender.sendMessage(ChatColor.RED + "No permission.");
            else if ((npc = SeniorTeamNPC.getNpcManager().getNearestNpc((Player) commandSender)) == null)
                commandSender.sendMessage(ChatColor.RED + "Couldn't find an NPC near you.");
            else if (npc.getCurrentAction() != Action.STANDING)
                commandSender.sendMessage(ChatColor.RED + "NPC " + npc.getName() + " is already executing an action.");
            else if (strings.length < 2)
                commandSender.sendMessage(ChatColor.RED + "Missing arguments.");
            else {
                boolean offHand;
                switch (strings[0]) {
                    case "left":
                        offHand = true;
                        break;
                    case "right":
                        offHand = false;
                        break;
                    default:
                        commandSender.sendMessage(ChatColor.RED + strings[0] + " is not a valid hand.");
                        return true;
                }

                long interval;
                try {
                    interval = Long.parseLong(strings[1]);
                } catch (NumberFormatException e) {
                    commandSender.sendMessage(ChatColor.RED + strings[1] + " is not a valid number.");
                    return true;
                }

                long stopAt = System.currentTimeMillis() + 5000;
                npc.setCurrentAction(Action.ATTACKING);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        PacketUtils.sendPlayerAnimation((Player) commandSender, npc, offHand);
                        if (stopAt < System.currentTimeMillis()) {
                            cancel();
                            npc.setCurrentAction(Action.STANDING);
                        }
                    }
                }.runTaskTimer(SeniorTeamNPC.getInstance(), 0L, interval / 1000 * 20);
            }
            return true;
        }
    }

}
