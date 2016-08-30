package net.rigner.limbo.packets.protocols;

import net.rigner.limbo.NetworkManager;
import net.rigner.limbo.PlayerConnection;
import net.rigner.limbo.packets.AbstractProtocol;
import net.rigner.limbo.packets.Status;
import net.rigner.limbo.packets.in.*;
import net.rigner.limbo.packets.out.*;
import net.rigner.limbo.world.Chunk;
import net.rigner.limbo.world.SignTileEntity;

/**
 * Created by Rigner on 30/08/16 for project Limbo.
 * All rights reserved.
 */
public class Protocol107 extends AbstractProtocol
{
    public Protocol107(NetworkManager networkManager)
    {
        super(networkManager);

        this.registerPacketIn(Status.STATUS, 0x00, PacketStatusInRequest.class);
        this.registerPacketIn(Status.STATUS, 0x01, PacketStatusInPing.class);
        this.registerPacketOut(Status.STATUS, 0x00, PacketStatusOutResponse.class);
        this.registerPacketOut(Status.STATUS, 0x01, PacketStatusOutPong.class);

        this.registerPacketIn(Status.LOGIN, 0x00, PacketLoginInStart.class);
        this.registerPacketOut(Status.LOGIN, 0x00, PacketOutDisconnect.class);
        this.registerPacketOut(Status.LOGIN, 0x02, PacketLoginOutSuccess.class);

        this.registerPacketOut(Status.PLAY, 0x1A, PacketOutDisconnect.class);
        this.registerPacketOut(Status.PLAY, 0x1F, PacketPlayOutKeepAlive47.class);
        this.registerPacketOut(Status.PLAY, 0x20, PacketPlayOutChunkData107.class);
        this.registerPacketOut(Status.PLAY, 0x23, PacketPlayOutJoinGame47.class);
        this.registerPacketOut(Status.PLAY, 0x2E, PacketPlayOutPlayerPositionAndLook107.class);

        this.registerPacketIn(Status.PLAY, 0x00, PacketInIgnored.class);//Teleport confirm
        this.registerPacketIn(Status.PLAY, 0x01, PacketInIgnored.class);//Tab complete
        this.registerPacketIn(Status.PLAY, 0x02, PacketInIgnored.class);//Chat message
        this.registerPacketIn(Status.PLAY, 0x03, PacketInIgnored.class);//Client status
        this.registerPacketIn(Status.PLAY, 0x04, PacketInIgnored.class);//Client Settings
        this.registerPacketIn(Status.PLAY, 0x05, PacketInIgnored.class);//Confirm transaction
        this.registerPacketIn(Status.PLAY, 0x06, PacketInIgnored.class);//Enchant item
        this.registerPacketIn(Status.PLAY, 0x07, PacketInIgnored.class);//Click window
        this.registerPacketIn(Status.PLAY, 0x08, PacketInIgnored.class);//Close window
        this.registerPacketIn(Status.PLAY, 0x09, PacketInIgnored.class);//Plugin message
        this.registerPacketIn(Status.PLAY, 0x0A, PacketInIgnored.class);//Use entity
        this.registerPacketIn(Status.PLAY, 0x0B, PacketPlayInKeepAlive47.class);
        this.registerPacketIn(Status.PLAY, 0x0C, PacketInIgnored.class);//Player position
        this.registerPacketIn(Status.PLAY, 0x0D, PacketInIgnored.class);//Player position and look
        this.registerPacketIn(Status.PLAY, 0x0E, PacketInIgnored.class);//Player look
        this.registerPacketIn(Status.PLAY, 0x0F, PacketInIgnored.class);//Player (ground)
        this.registerPacketIn(Status.PLAY, 0x10, PacketInIgnored.class);//Vehicle move
        this.registerPacketIn(Status.PLAY, 0x11, PacketInIgnored.class);//Steer boat
        this.registerPacketIn(Status.PLAY, 0x12, PacketInIgnored.class);//Player abilities
        this.registerPacketIn(Status.PLAY, 0x13, PacketInIgnored.class);//Player digging
        this.registerPacketIn(Status.PLAY, 0x14, PacketInIgnored.class);//Entity action
        this.registerPacketIn(Status.PLAY, 0x15, PacketInIgnored.class);//Steer vehicle
        this.registerPacketIn(Status.PLAY, 0x16, PacketInIgnored.class);//Resource pack status
        this.registerPacketIn(Status.PLAY, 0x17, PacketInIgnored.class);//Held item change
        this.registerPacketIn(Status.PLAY, 0x18, PacketInIgnored.class);//Creative inventory action
        this.registerPacketIn(Status.PLAY, 0x19, PacketInIgnored.class);//Update sign
        this.registerPacketIn(Status.PLAY, 0x1A, PacketInIgnored.class);//Animation
        this.registerPacketIn(Status.PLAY, 0x1B, PacketInIgnored.class);//Spectate
        this.registerPacketIn(Status.PLAY, 0x1C, PacketInIgnored.class);//Block placement
        this.registerPacketIn(Status.PLAY, 0x1D, PacketInIgnored.class);//Use item
    }

    @Override
    public int[] getVersions()
    {
        return new int[]{107};
    }

    @Override
    public void disconnect(PlayerConnection playerConnection, String message)
    {
        this.networkManager.sendPacket(playerConnection, new PacketOutDisconnect(message));
    }

    @Override
    public void sendJoinGame(PlayerConnection playerConnection, int entityId, byte gameMode, int dimension, byte difficulty, byte maxPlayers, String levelType, boolean debugInfo)
    {
        this.networkManager.sendPacket(playerConnection, new PacketPlayOutJoinGame47(entityId, gameMode, (byte)dimension, difficulty, maxPlayers, levelType, debugInfo));
    }

    @Override
    public void sendPosition(PlayerConnection playerConnection, double x, double y, double z, float yaw, float pitch)
    {
        this.networkManager.sendPacket(playerConnection, new PacketPlayOutPlayerPositionAndLook107(x, y, z, yaw, pitch, (byte)0, 0));
    }

    @Override
    public void sendKeepAlive(PlayerConnection playerConnection, int id)
    {
        this.networkManager.sendPacket(playerConnection, new PacketPlayOutKeepAlive47(id));
    }

    @Override
    public void sendChunk(PlayerConnection playerConnection, Chunk chunk)
    {
        this.networkManager.sendPacket(playerConnection, new PacketPlayOutChunkData107(chunk));
    }

    @Override
    public void sendSign(PlayerConnection playerConnection, SignTileEntity signTileEntity)
    {
        this.networkManager.sendPacket(playerConnection, new PacketPlayOutUpdateSign(signTileEntity));
    }
}

