package net.rigner.limbo.world;

/**
 * Created by Rigner on 30/08/16 for project Limbo.
 * All rights reserved.
 */
public class World
{
    private Chunk[][] chunks;

    World(int width, int length)
    {
        this.chunks = new Chunk[width][length];
    }

    void setBlock(int x, int y, int z, int id, byte data)
    {
        Chunk chunk = this.chunks[(x >> 4)][(z >> 4)];
        if (chunk == null)
        {
            chunk = new Chunk(x >> 4, z >> 4);
            this.chunks[(x >> 4)][(z >> 4)] = chunk;
        }
        chunk.setBlock(x & 0xF, y, z & 0xF, id, data);
    }

    public Chunk[][] getChunks()
    {
        return this.chunks;
    }

    Chunk getChunkAtWorldPos(int x, int z)
    {
        return this.chunks[(x >> 4)][(z >> 4)];
    }
}

