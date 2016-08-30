package net.rigner.limbo.packets.out;

import net.rigner.limbo.packets.PacketSerializer;

import java.io.IOException;

/**
 * Created by Rigner on 30/08/16 for project Limbo.
 * All rights reserved.
 */
public class PacketPlayOutPlayerPositionAndLook107 implements PacketOut
{
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private byte flags;
    private int teleportId;

    public PacketPlayOutPlayerPositionAndLook107(double x, double y, double z, float yaw, float pitch, byte flags, int teleportId)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.flags = flags;
    }

    @Override
    public void write(PacketSerializer packetSerializer) throws IOException
    {
        packetSerializer.writeDouble(this.x);
        packetSerializer.writeDouble(this.y);
        packetSerializer.writeDouble(this.z);
        packetSerializer.writeFloat(this.yaw);
        packetSerializer.writeFloat(this.pitch);
        packetSerializer.writeByte(this.flags);
        packetSerializer.writeVarInt(this.teleportId);
    }
}
