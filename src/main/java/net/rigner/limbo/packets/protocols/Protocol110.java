package net.rigner.limbo.packets.protocols;

import net.rigner.limbo.NetworkManager;
import net.rigner.limbo.PlayerConnection;
import net.rigner.limbo.packets.Status;
import net.rigner.limbo.packets.out.PacketPlayOutChunkData110;
import net.rigner.limbo.packets.out.PacketPlayOutUpdateBlockEntity110;
import net.rigner.limbo.world.Chunk;
import net.rigner.limbo.world.SignTileEntity;

/**
 * Created by Rigner on 31/08/16 for project Limbo.
 * All rights reserved.
 */
public class Protocol110 extends Protocol108
{
    public Protocol110(NetworkManager networkManager)
    {
        super(networkManager);

        this.registerPacketOut(Status.PLAY, 0x09, PacketPlayOutUpdateBlockEntity110.class);
        this.registerPacketOut(Status.PLAY, 0x20, PacketPlayOutChunkData110.class);
        this.registerPacketOut(Status.PLAY, 0x46, null);
    }

    @Override
    public int[] getVersions()
    {
        return new int[]{110};
    }

    @Override
    public void sendSign(PlayerConnection playerConnection, SignTileEntity signTileEntity)
    {
        this.networkManager.sendPacket(playerConnection, new PacketPlayOutUpdateBlockEntity110(signTileEntity, (byte)9));
    }

    @Override
    public void sendChunk(PlayerConnection playerConnection, Chunk chunk)
    {
        this.networkManager.sendPacket(playerConnection, new PacketPlayOutChunkData110(chunk));
    }
}
