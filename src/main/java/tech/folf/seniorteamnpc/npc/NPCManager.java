package tech.folf.seniorteamnpc.npc;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import tech.folf.seniorteamnpc.SeniorTeamNPC;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NPCManager {
    public List<NPC> npcList = new ArrayList<>();

    public NPC createNpc(Location location, String name) {
        NPC npc = new NPC(name, location);
        npcList.add(npc);
        return npc;
    }

    public boolean deleteNpc(String name) {
        return npcList.removeIf(npc -> npc.getName().equals(name));
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
