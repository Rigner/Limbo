package net.rigner.limbo.world;

import net.rigner.limbo.PlayerConnection;
import net.rigner.limbo.packets.PacketSerializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    private final long[][] mapWithPalette;
    private List<TileEntity> tileEntities;

    Chunk(int x, int z)
    {
        this.x = x;
        this.z = z;
        this.map = new byte[Chunk.CHUNK_SIZE];
        this.mapWithPalette = new long[16][832];
        this.tileEntities = new ArrayList<>();
        Arrays.fill(this.map, (byte)0);
        for (long[] tab : this.mapWithPalette)
            Arrays.fill(tab, (long)0);
    }

    void setBlock(int x, int y, int z, int id, int meta)
    {
        int index = x + y * 16 * 16 + z * 16;
        index *= 2;
        this.map[index] = ((byte) ((byte) (id << 4) | meta));
        this.map[(index + 1)] = ((byte) (id >> 4));

        int section = y / 16;
        index = x + z * 16 + y % 16 * 16 * 16;
        index *= 13;
        int l = index / 64;
        index = index % 64;
        for (int i = 0; i < 13; ++i)
        {
            if (index == 64)
            {
                index = 0;
                l++;
            }
            long value;
            if (i < 4)
                value = (meta & (1L << i)) != 0 ? 1 : 0;
            else
                value = (id & (1L << (i - 4))) != 0 ? 1 : 0;
            this.mapWithPalette[section][l] = this.mapWithPalette[section][l] & ~(1L << index);
            this.mapWithPalette[section][l] = this.mapWithPalette[section][l] | (value << index);

            index++;
        }
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

    public byte[] getMapWithPalette() throws IOException
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PacketSerializer packetSerializer = new PacketSerializer(byteArrayOutputStream);
        for (int i = 0; i < 16; ++i)
        {
            packetSerializer.writeByte((byte) 13);
            packetSerializer.writeVarInt(0);
            packetSerializer.writeVarInt(this.mapWithPalette[i].length);
            for (long value : this.mapWithPalette[i])
                packetSerializer.writeLong(value);
            for (int j = 0; j < 2048; ++j)
                packetSerializer.writeByte((byte)0);
        }
        return byteArrayOutputStream.toByteArray();
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
