package tech.folf.seniorteamnpc.npc;

import com.mojang.authlib.GameProfile;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import tech.folf.seniorteamnpc.PacketUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NPC {
    private final String name;
    private final Location location;

    private int entityId;
    private GameProfile gameProfile;
    private Action currentAction = Action.STANDING;
    private final List<Player> inRadius = new ArrayList<>();

    public NPC(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public GameProfile getGameProfile() {
        if (gameProfile == null) {
            gameProfile = new GameProfile(UUID.randomUUID(), name);
        }
        return gameProfile;
    }

    public int getEntityId() {
        if (entityId == 0) entityId = (int) (Math.random() * Integer.MAX_VALUE);
        return entityId;
    }

    public void spawnToPlayer(Player player) {
        inRadius.add(player);
        PacketUtils.sendPlayerInfo(player, this);
        PacketUtils.sendSpawnPacket(player, this, location);
        PacketUtils.schedulePlayerListRemove(player, this);
    }

    public void destroyToPlayer(Player player) {
        inRadius.remove(player);
        PacketUtils.sendEntityDestroy(player, this);
    }

    public Location getLocation() {
        return location;
    }

    public Action getCurrentAction() {
        return currentAction;
    }

    public void setCurrentAction(Action currentAction) {
        this.currentAction = currentAction;
    }

    public List<Player> getInRadius() {
        return inRadius;
    }
}
