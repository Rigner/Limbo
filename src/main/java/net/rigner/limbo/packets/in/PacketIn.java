package net.rigner.limbo.packets.in;

import net.rigner.limbo.NetworkManager;
import net.rigner.limbo.packets.PacketSerializer;
import net.rigner.limbo.PlayerConnection;

import java.io.IOException;

/**
 * Created by Rigner on 29/08/16 for project Limbo.
 * All rights reserved.
 */
public interface PacketIn
{
    void readPacket(PacketSerializer packetSerializer) throws IOException;

    void handlePacket(NetworkManager networkManager, PlayerConnection playerConnection);
}
