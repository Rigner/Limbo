package net.rigner.limbo.packets.out;

import net.rigner.limbo.packets.PacketSerializer;
import net.rigner.limbo.world.TileEntity;
import net.rigner.limbo.world.nbt.NBTTag;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Rigner on 31/08/16 for project Limbo.
 * All rights reserved.
 */
public class PacketPlayOutUpdateBlockEntity110 implements PacketOut
{
    private TileEntity tileEntity;
    private byte action;

    public PacketPlayOutUpdateBlockEntity110(TileEntity tileEntity, byte action)
    {
        this.tileEntity = tileEntity;
        this.action = action;
    }

    @Override
    public void write(PacketSerializer packetSerializer) throws IOException
    {
        packetSerializer.writePosition(this.tileEntity.getBlockPosition());
        packetSerializer.writeByte(this.action);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        NBTTag.writeTag(outputStream, this.tileEntity.getData(), true);
        for (byte b : outputStream.toByteArray())
            packetSerializer.writeByte(b);
    }
}
