package net.rigner.limbo.packets.out;

import net.rigner.limbo.packets.PacketSerializer;
import net.rigner.limbo.world.Chunk;

import java.io.IOException;

/**
 * Created by Rigner on 31/08/16 for project Limbo.
 * All rights reserved.
 */
public class PacketPlayOutChunkData110 implements PacketOut
{
    private Chunk chunk;

    public PacketPlayOutChunkData110(Chunk chunk)
    {
        this.chunk = chunk;
    }

    @Override
    public void write(PacketSerializer packetSerializer) throws IOException
    {
        packetSerializer.writeInt(this.chunk.getX());
        packetSerializer.writeInt(this.chunk.getZ());
        packetSerializer.writeBoolean(true);
        packetSerializer.writeVarInt(25565);
        packetSerializer.writeByteArray(this.chunk.getMapWithPalette());
        packetSerializer.writeByteArray(new byte[0]);
    }
}
