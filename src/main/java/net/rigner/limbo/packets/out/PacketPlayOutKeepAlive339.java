package net.rigner.limbo.packets.out;

import net.rigner.limbo.packets.PacketSerializer;

import java.io.IOException;

/**
 * Created by Rigner on 30/08/16 for project Limbo.
 * All rights reserved.
 */
public class PacketPlayOutKeepAlive339 implements PacketOut
{
    private int id;

    public PacketPlayOutKeepAlive339(int id)
    {
        this.id = id;
    }

    @Override
    public void write(PacketSerializer packetSerializer) throws IOException
    {
        packetSerializer.writeLong(this.id);
    }
}
