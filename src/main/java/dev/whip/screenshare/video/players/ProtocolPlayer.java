package dev.whip.screenshare.video.players;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import dev.whip.screenshare.ScreenShare;
import dev.whip.screenshare.video.ScreenPlayer;
import net.minecraft.server.v1_16_R1.IChatBaseComponent;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProtocolPlayer extends ScreenPlayer {
    private final static WrappedDataWatcher.Serializer byteSerializer = WrappedDataWatcher.Registry.get(Byte.class);
    private final static WrappedDataWatcher.Serializer booleanSerializer = WrappedDataWatcher.Registry.get(Boolean.class);

    private final int ID_OFFSET = 10000000;
    private final List<UUID> uuids;
    private final ProtocolManager protocolManager;

    public ProtocolPlayer(ProtocolManager protocolManager){
        this.protocolManager = protocolManager;

        uuids = new ArrayList<>();
    }

    @Override
    public void render() {
        List<String> frame = manager.getCache().getFrame();
        if (frame == null){
            //ScreenShare.getInstance().getLogger().warning("Frame was not encoded before render began");
            return;
        }

        for (int x = manager.getHEIGHT() - 1; x >= 0 ; x--){
            setRow(x, frame.get(x));
        }
    }

    @Override
    public void initWatcher(Player player) {
        for (int id = manager.getHEIGHT() - 1; id >= 0; id--){
            UUID uuid = UUID.randomUUID();
            uuids.add(uuid);

            spawnEntity(player,
                    manager.getScreenStart().getX(),
                    manager.getScreenStart().getZ(),
                    manager.getScreenStart().getY() - (id * 0.225),
                    id,
                    uuid);
        }
    }

    public void setRow(int row, String json){
        PacketContainer metaDataPacket = protocolManager.createPacket(PacketType.Play.Server.ENTITY_METADATA, true);

        WrappedDataWatcher watcher = new WrappedDataWatcher();

        watcher.setObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true), Optional.of(IChatBaseComponent.ChatSerializer.jsonToComponent(json))); //display name

        metaDataPacket.getIntegers()
                .write(0, row + ID_OFFSET);
        metaDataPacket.getWatchableCollectionModifier()
                .write(0, watcher.getWatchableObjects());

        try {
            for (Player player : players) {
                protocolManager.sendServerPacket(player, metaDataPacket);
            }
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void spawnEntity(Player player, double x, double z, double y, int id, UUID uuid){
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SPAWN_ENTITY_LIVING);

        packet.getIntegers()
                .write(0, id + ID_OFFSET)
                .write(1, 1);
        packet.getUUIDs()
                .write(0, uuid);
        packet.getDoubles() //Cords
                .write(0, x)
                .write(1, y)
                .write(2, z);

        PacketContainer metaDataPacket = protocolManager.createPacket(PacketType.Play.Server.ENTITY_METADATA, true);

        WrappedDataWatcher watcher = new WrappedDataWatcher();

        watcher.setObject(0, byteSerializer, (byte) (0x20));
        watcher.setObject(14, byteSerializer, (byte) (0x01|0x10)); //small marker
        watcher.setObject(3, booleanSerializer, Boolean.TRUE); //Custom name visible
        watcher.setObject(5, booleanSerializer, Boolean.TRUE); // No gravity

        metaDataPacket.getIntegers()
                .write(0, id + ID_OFFSET);
        metaDataPacket.getWatchableCollectionModifier()
                .write(0, watcher.getWatchableObjects());

        try {
            protocolManager.sendServerPacket(player, packet);
            protocolManager.sendServerPacket(player, metaDataPacket);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
