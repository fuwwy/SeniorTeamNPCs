package tech.folf.seniorteamnpc;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.*;
import net.minecraft.server.v1_16_R1.PacketPlayOutAnimation;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import tech.folf.seniorteamnpc.npc.NPC;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

public class PacketUtils {

    public static void sendPlayerInfo(Player player, NPC npc) {
        PacketContainer packetContainer = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
        WrappedGameProfile wrappedGameProfile = WrappedGameProfile.fromHandle(npc.getGameProfile());
        PlayerInfoData playerInfoData = new PlayerInfoData(wrappedGameProfile, 1,
                EnumWrappers.NativeGameMode.NOT_SET, WrappedChatComponent.fromText(npc.getName()));

        packetContainer.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);
        packetContainer.getPlayerInfoDataLists().write(0, Collections.singletonList(playerInfoData));

        sendPacket(player, packetContainer);
    }

    public static void sendSpawnPacket(Player player, NPC npc, Location location) {
        PacketContainer packetContainer = new PacketContainer(PacketType.Play.Server.NAMED_ENTITY_SPAWN);
        StructureModifier<Object> structureModifier = packetContainer.getModifier();
        int entityId = npc.getEntityId();

        structureModifier.write(0, entityId);
        structureModifier.write(1, npc.getGameProfile().getId());
        structureModifier.write(2, location.getX());
        structureModifier.write(3, location.getY());
        structureModifier.write(4, location.getZ());
        structureModifier.write(5, (byte) location.getYaw());
        structureModifier.write(6, (byte) location.getPitch());

        sendPacket(player, packetContainer);

        // Fix yaw
        packetContainer = new PacketContainer(PacketType.Play.Server.ENTITY_HEAD_ROTATION);
        structureModifier = packetContainer.getModifier();
        structureModifier.write(0, entityId);
        structureModifier.write(1, (byte) location.getYaw());
        sendPacket(player, packetContainer);
    }

    public static void schedulePlayerListRemove(Player player, NPC npc) {
        // Not really sure if delaying is necessary but there might be issues when not running locally
        new BukkitRunnable() {
            @Override
            public void run() {
                PacketContainer packetContainer = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
                WrappedGameProfile wrappedGameProfile = WrappedGameProfile.fromHandle(npc.getGameProfile());
                PlayerInfoData playerInfoData = new PlayerInfoData(wrappedGameProfile, 1,
                        EnumWrappers.NativeGameMode.NOT_SET, WrappedChatComponent.fromText(npc.getName()));

                packetContainer.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
                packetContainer.getPlayerInfoDataLists().write(0, Collections.singletonList(playerInfoData));

                sendPacket(player, packetContainer);
            }
        }.runTaskLater(SeniorTeamNPC.getInstance(), 20L);
    }

    public static void sendPlayerCrouching(Player player, NPC npc, boolean stop) {
        PacketContainer packetContainer = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        WrappedDataWatcher wrappedDataWatcher = new WrappedDataWatcher();
        wrappedDataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(6,
                WrappedDataWatcher.Registry.get(EnumWrappers.getEntityPoseClass())),
                stop ? EnumWrappers.EntityPose.STANDING.toNms() : EnumWrappers.EntityPose.CROUCHING.toNms());

        packetContainer.getIntegers().write(0, npc.getEntityId());
        packetContainer.getWatchableCollectionModifier().write(0, wrappedDataWatcher.getWatchableObjects());
        sendPacket(player, packetContainer);
    }

    public static void sendPlayerAnimation(Player player, NPC npc, boolean offHand) {
        PacketContainer packetContainer = new PacketContainer(PacketType.Play.Server.ANIMATION);
        packetContainer.getIntegers().write(0, npc.getEntityId());
        packetContainer.getIntegers().write(1, offHand ? 3 : 0);
        sendPacket(player, packetContainer);
    }

    private static void sendPacket(Player player, PacketContainer packetContainer) {
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packetContainer);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
