package net.rigner.limbo.packets.in;

import net.rigner.limbo.NetworkManager;
import net.rigner.limbo.PlayerConnection;
import net.rigner.limbo.packets.PacketSerializer;

import java.io.IOException;

/**
 * Created by Rigner on 30/08/16 for project Limbo.
 * All rights reserved.
 */
public class PacketPlayInKeepAlive339 implements PacketIn
{
    private int id;

    @Override
    public void readPacket(PacketSerializer packetSerializer) throws IOException
    {
        this.id = (int) packetSerializer.readLong();
    }

    @Override
    public void handlePacket(NetworkManager networkManager, PlayerConnection playerConnection)
    {
        if (this.id == networkManager.getCurrentKeepAliveId())
            playerConnection.keepAlive();
    }
}
