package net.rigner.limbo.packets.in;

import net.rigner.limbo.NetworkManager;
import net.rigner.limbo.PlayerConnection;
import net.rigner.limbo.packets.PacketSerializer;
import net.rigner.limbo.packets.out.PacketStatusOutResponse;

import java.io.IOException;

/**
 * Created by Rigner on 30/08/16 for project Limbo.
 * All rights reserved.
 */
public class PacketStatusInRequest implements PacketIn
{
    @Override
    public void readPacket(PacketSerializer packetSerializer) throws IOException
    {
    }

    @Override
    public void handlePacket(NetworkManager networkManager, PlayerConnection playerConnection)
    {
        networkManager.sendPacket(playerConnection, new PacketStatusOutResponse("{\n" +
                "    \"version\": {\n" +
                "        \"name\": \"Limbo\",\n" +
                "        \"protocol\": " + playerConnection.getProtocolId() + "\n" +
                "    },\n" +
                "    \"players\": {\n" +
                "        \"max\": 9999,\n" +
                "        \"online\": 0,\n" +
                "        \"sample\": [\n" +
                "        ]\n" +
                "    },\t\n" +
                "    \"description\": {\n" +
                "        \"text\": \"Limbo v1\"\n" +
                "    }\n" +
                "}"));
    }
}
