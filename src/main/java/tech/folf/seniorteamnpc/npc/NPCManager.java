package tech.folf.seniorteamnpc.npc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import tech.folf.seniorteamnpc.PacketUtils;
import tech.folf.seniorteamnpc.SeniorTeamNPC;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class NPCManager {
    private final List<NPC> npcList = new ArrayList<>();

    public NPCManager() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (NPC npc : npcList) {
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        if (!npc.getLocation().getWorld().equals(onlinePlayer.getLocation().getWorld())) {
                            if (npc.getInRadius().contains(onlinePlayer)) npc.destroyToPlayer(onlinePlayer);
                            continue;
                        }

                        if (npc.getLocation().distanceSquared(onlinePlayer.getLocation()) < 3600) {
                            if (!npc.getInRadius().contains(onlinePlayer)) {
                                npc.spawnToPlayer(onlinePlayer);
                            }
                        } else {
                            if (npc.getInRadius().contains(onlinePlayer)) {
                                npc.destroyToPlayer(onlinePlayer);
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(SeniorTeamNPC.getInstance(), 0L, 20L);
    }

    public void registerNpc(NPC npc) {
        npcList.add(npc);
    }

    public void createNpc(Location location, String name) {
        NPC npc = new NPC(name, location);
        npcList.add(npc);
        SeniorTeamNPC.getInstance().getDataManager().save();
    }

    public void destroyNpc(NPC npc) {
        for (Player player : npc.getInRadius()) {
            PacketUtils.sendEntityDestroy(player, npc);
        }
    }

    public void deleteNpc(NPC npc) {
        npcList.remove(npc);
        destroyNpc(npc);
        SeniorTeamNPC.getInstance().getDataManager().save();
    }

    public NPC get(String name) {
        for (NPC npc : npcList) {
            if (npc.getName().equals(name)) return npc;
        }
        return null;
    }

    public List<NPC> getNpcList() {
        return npcList;
    }

    public NPC getNearestNpc(Player player) {
        NPC nearestNpc = null;
        double nearestDistance = Double.MAX_VALUE;
        for (NPC npc : npcList) {
            if (npc.getLocation().getWorld() != player.getWorld()) continue;
            double distance = npc.getLocation().distanceSquared(player.getLocation());
            if (distance < nearestDistance) {
                nearestNpc = npc;
                nearestDistance = distance;
            }
        }
        return nearestNpc;
    }
}
