package net.rigner.limbo.packets.in;

import net.rigner.limbo.NetworkManager;
import net.rigner.limbo.PlayerConnection;
import net.rigner.limbo.packets.PacketSerializer;
import net.rigner.limbo.packets.out.PacketStatusOutPong;

import java.io.IOException;

/**
 * Created by Rigner on 30/08/16 for project Limbo.
 * All rights reserved.
 */
public class PacketStatusInPing implements PacketIn
{
    private long id;

    @Override
    public void readPacket(PacketSerializer packetSerializer) throws IOException
    {
        this.id = packetSerializer.readLong();
    }

    @Override
    public void handlePacket(NetworkManager networkManager, PlayerConnection playerConnection)
    {
        networkManager.sendPacket(playerConnection, new PacketStatusOutPong(this.id));
    }
}
