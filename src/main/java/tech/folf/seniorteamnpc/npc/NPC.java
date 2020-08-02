package tech.folf.seniorteamnpc.npc;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_16_R1.EntityPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import tech.folf.seniorteamnpc.PacketUtils;

import java.util.UUID;

public class NPC {
    private String name;
    private Location location;

    private int entityId;
    private GameProfile gameProfile;
    private Action currentAction = Action.STANDING;

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
        PacketUtils.sendPlayerInfo(player, this);
        PacketUtils.sendSpawnPacket(player, this, location);
        PacketUtils.schedulePlayerListRemove(player, this);
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

}
