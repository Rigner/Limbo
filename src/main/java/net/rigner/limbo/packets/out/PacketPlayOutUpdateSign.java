package net.rigner.limbo.packets.out;

import net.rigner.limbo.packets.PacketSerializer;
import net.rigner.limbo.world.SignTileEntity;

import java.io.IOException;

/**
 * Created by Rigner on 30/08/16 for project Limbo.
 * All rights reserved.
 */
public class PacketPlayOutUpdateSign implements PacketOut
{
    private SignTileEntity signTileEntity;

    public PacketPlayOutUpdateSign(SignTileEntity signTileEntity)
    {
        this.signTileEntity = signTileEntity;
    }

    @Override
    public void write(PacketSerializer packetSerializer) throws IOException
    {
        packetSerializer.writePosition(this.signTileEntity.getBlockPosition());
        packetSerializer.writeString(this.signTileEntity.getText1());
        packetSerializer.writeString(this.signTileEntity.getText2());
        packetSerializer.writeString(this.signTileEntity.getText3());
        packetSerializer.writeString(this.signTileEntity.getText4());
    }
}
