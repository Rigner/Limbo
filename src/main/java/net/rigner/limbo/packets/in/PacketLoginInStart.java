package net.rigner.limbo.packets.in;

import net.rigner.limbo.NetworkManager;
import net.rigner.limbo.packets.PacketSerializer;
import net.rigner.limbo.PlayerConnection;
import net.rigner.limbo.packets.Status;
import net.rigner.limbo.packets.out.PacketLoginOutSuccess;
import net.rigner.limbo.world.Chunk;

import java.io.IOException;

/**
 * Created by Rigner on 29/08/16 for project Limbo.
 * All rights reserved.
 */
public class PacketLoginInStart implements PacketIn
{
    private String userName;

    @Override
    public void readPacket(PacketSerializer packetSerializer) throws IOException
    {
        this.userName = packetSerializer.readString();
    }

    @Override
    public void handlePacket(NetworkManager networkManager, PlayerConnection playerConnection)
    {
        playerConnection.setUserName(this.userName);
        networkManager.sendPacket(playerConnection, new PacketLoginOutSuccess(playerConnection.getUuid(), this.userName));
        playerConnection.setStatus(Status.PLAY);
        playerConnection.getProtocol().sendJoinGame(playerConnection, 1, networkManager.getLimboConfiguration().getGameMode(), networkManager.getLimboConfiguration().getDimension(), (byte)0, (byte)1, "default", networkManager.getLimboConfiguration().isReducedDebugInfo());
        playerConnection.getProtocol().sendPosition(playerConnection, networkManager.getLimboConfiguration().getSpawnX(), networkManager.getLimboConfiguration().getSpawnY(), networkManager.getLimboConfiguration().getSpawnZ(), networkManager.getLimboConfiguration().getSpawnYaw(), networkManager.getLimboConfiguration().getSpawnPitch());
        for (Chunk[] tab : networkManager.getWorld().getChunks())
            for (Chunk chunk : tab)
                if (chunk != null)
                {
                    playerConnection.getProtocol().sendChunk(playerConnection, chunk);
                    chunk.sendTileEntities(playerConnection);
                }
    }
}
