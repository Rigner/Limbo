package net.rigner.limbo.packets;

import net.rigner.limbo.NetworkManager;
import net.rigner.limbo.PlayerConnection;
import net.rigner.limbo.packets.in.PacketIn;
import net.rigner.limbo.packets.out.PacketOut;
import net.rigner.limbo.world.Chunk;
import net.rigner.limbo.world.SignTileEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rigner on 29/08/16 for project Limbo.
 * All rights reserved.
 */
public abstract class AbstractProtocol
{
    protected final NetworkManager networkManager;
    private final Map<Status, Map<Integer, Class<? extends PacketIn>>> registryIn;
    private final Map<Status, Map<Integer, Class<? extends PacketOut>>> registryOut;

    protected AbstractProtocol(NetworkManager networkManager)
    {
        this.networkManager = networkManager;

        this.registryIn = new HashMap<>();
        this.registryOut = new HashMap<>();
        for (Status status : Status.values())
            this.registryIn.put(status, new HashMap<>());
        for (Status status : Status.values())
            this.registryOut.put(status, new HashMap<>());
    }

    protected void registerPacketIn(Status status, int id, Class<? extends PacketIn> packetClass)
    {
        if (packetClass == null)
            this.registryIn.get(status).remove(id);
        else
            this.registryIn.get(status).put(id, packetClass);
    }

    protected void registerPacketOut(Status status, int id, Class<? extends PacketOut> packetClass)
    {
        if (packetClass == null)
            this.registryOut.get(status).remove(id);
        else
            this.registryOut.get(status).put(id, packetClass);
    }

    public abstract void sendKeepAlive(PlayerConnection playerConnection, int id);

    public abstract void sendChunk(PlayerConnection playerConnection, Chunk chunk);

    public abstract void sendSign(PlayerConnection playerConnection, SignTileEntity signTileEntity);

    public abstract void disconnect(PlayerConnection playerConnection, String message);

    public abstract void sendJoinGame(PlayerConnection playerConnection, int entityId, byte gameMode, int dimension, byte difficulty, byte maxPlayers, String levelType, boolean debugInfo);

    public abstract void sendPosition(PlayerConnection playerConnection, double x, double y, double z, float yaw, float pitch);

    public abstract int[] getVersions();

    public final Class<? extends PacketIn> getPacketInById(int id, Status status)
    {
        return this.registryIn.get(status).get(id);
    }

    public final int getPacketOutByClass(Class<? extends PacketOut> packetClass, Status status)
    {
        for (Map.Entry<Integer, Class<? extends PacketOut>> entry : this.registryOut.get(status).entrySet())
            if (entry.getValue().equals(packetClass))
                return entry.getKey();
        return -1;
    }
}
