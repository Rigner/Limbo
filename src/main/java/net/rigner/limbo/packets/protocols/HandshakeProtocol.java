package net.rigner.limbo.packets.protocols;

import net.rigner.limbo.NetworkManager;
import net.rigner.limbo.PlayerConnection;
import net.rigner.limbo.packets.AbstractProtocol;
import net.rigner.limbo.packets.Status;
import net.rigner.limbo.packets.in.PacketInHandshake;
import net.rigner.limbo.packets.out.PacketOutDisconnect;
import net.rigner.limbo.world.Chunk;
import net.rigner.limbo.world.SignTileEntity;

/**
 * Created by Rigner on 29/08/16 for project Limbo.
 * All rights reserved.
 */
public class HandshakeProtocol extends AbstractProtocol
{
    public HandshakeProtocol(NetworkManager networkManager)
    {
        super(networkManager);

        this.registerPacketIn(Status.HANDSHAKE, 0x00, PacketInHandshake.class);

        this.registerPacketOut(Status.HANDSHAKE, 0x00, PacketOutDisconnect.class);
    }

    @Override
    public void disconnect(PlayerConnection playerConnection, String message)
    {
        this.networkManager.sendPacket(playerConnection, new PacketOutDisconnect(message));
    }

    @Override
    public void sendJoinGame(PlayerConnection playerConnection, int entityId, byte gameMode, int dimension, byte difficulty, byte maxPlayers, String levelType, boolean debugInfo)
    {
    }

    @Override
    public void sendPosition(PlayerConnection playerConnection, double x, double y, double z, float yaw, float pitch)
    {
    }

    @Override
    public void sendKeepAlive(PlayerConnection playerConnection, int id)
    {
    }

    @Override
    public void sendChunk(PlayerConnection playerConnection, Chunk chunk)
    {
    }

    @Override
    public void sendSign(PlayerConnection playerConnection, SignTileEntity signTileEntity)
    {
    }

    @Override
    public int[] getVersions()
    {
        return new int[]{-1};
    }
}
