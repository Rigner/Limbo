package net.rigner.limbo.world;

import net.rigner.limbo.PlayerConnection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Rigner on 30/08/16 for project Limbo.
 * All rights reserved.
 */
public class Chunk
{
    private static final int CHUNK_SIZE = 196864;
    private final int x;
    private final int z;
    private final byte[] map;
    private List<TileEntity> tileEntities;

    Chunk(int x, int z)
    {
        this.x = x;
        this.z = z;
        this.map = new byte[Chunk.CHUNK_SIZE];
        this.tileEntities = new ArrayList<>();
        Arrays.fill(this.map, (byte)0);
    }

    void setBlock(int x, int y, int z, int id, int meta)
    {
        int index = x + y * 16 * 16 + z * 16;
        index *= 2;
        this.map[index] = ((byte)((byte)(id << 4) | meta));
        this.map[(index + 1)] = ((byte)(id >> 4));
    }

    public int getX()
    {
        return this.x;
    }

    public int getZ()
    {
        return this.z;
    }

    public byte[] getMap()
    {
        return this.map;
    }

    public void sendTileEntities(PlayerConnection player)
    {
        this.tileEntities.stream().filter(tileEntity -> (tileEntity instanceof SignTileEntity)).forEach(tileEntity -> player.getProtocol().sendSign(player, (SignTileEntity) tileEntity));
    }

    void addTileEntity(TileEntity tileEntity)
    {
        this.tileEntities.add(tileEntity);
    }
}
