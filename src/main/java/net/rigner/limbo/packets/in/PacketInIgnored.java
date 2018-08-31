package net.rigner.limbo.packets.in;

import net.rigner.limbo.NetworkManager;
import net.rigner.limbo.PlayerConnection;
import net.rigner.limbo.packets.PacketSerializer;

import java.io.IOException;

/**
 * Created by Rigner on 30/08/16 for project Limbo.
 * All rights reserved.
 */
public class PacketInIgnored implements PacketIn
{
    @Override
    public void readPacket(PacketSerializer packetSerializer) throws IOException
    {
    }

    @Override
    public void handlePacket(NetworkManager networkManager, PlayerConnection playerConnection)
    {
    }
}
