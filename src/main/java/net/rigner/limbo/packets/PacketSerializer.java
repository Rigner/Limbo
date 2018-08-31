package net.rigner.limbo.packets;

import net.rigner.limbo.world.BlockPosition;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Created by Rigner on 29/08/16 for project Limbo.
 * All rights reserved.
 */
public class PacketSerializer
{
    private InputStream inputStream;
    private OutputStream outputStream;

    public PacketSerializer(InputStream inputStream)
    {
        this.inputStream = inputStream;
        this.outputStream = null;
    }

    public PacketSerializer(OutputStream outputStream)
    {
        this.inputStream = null;
        this.outputStream = outputStream;
    }

    private void ensureInputStream()
    {
        if (this.inputStream == null)
            throw new IllegalStateException("Trying to read an output PacketSerializer");
    }

    private void ensureOutputStream()
    {
        if (this.outputStream == null)
            throw new IllegalStateException("Trying to write on input PacketSerializer");
    }

    public int readVarInt() throws IOException
    {
        this.ensureInputStream();

        int out = 0;
        int bytes = 0;
        int in;
        while (true)
        {
            in = this.readByte();
            out |= (in & 0x7F) << (bytes++ * 7);
            if (bytes > 5)
                throw new IOException("VarInt too big");

            if ((in & 0x80) != 0x80)
                break;
        }

        return out;
    }

    private byte[] readByteArray() throws IOException
    {
        this.ensureInputStream();
        int size = this.readVarInt();
        byte[] bytes = new byte[size];
        int ret = this.inputStream.read(bytes);
        if (ret != size)
            throw new IOException("Incorrect byte array");
        return bytes;
    }

    public byte readByte() throws IOException
    {
        this.ensureInputStream();
        return (byte) this.inputStream.read();
    }

    public short readShort() throws IOException
    {
        this.ensureInputStream();
        short result = this.readByte();
        result *= 256;
        result += this.readByte();
        return result;
    }

    public String readString() throws IOException
    {
        this.ensureInputStream();
        return new String(this.readByteArray(), Charset.forName("UTF-8"));
    }

    public long readLong() throws IOException
    {
        this.ensureInputStream();
        long result = this.readByte();
        for (int i = 0; i < 7; i++)
        {
            result *= 256;
            result += this.readByte();
        }
        return result;
    }

    public void writeVarInt(int value) throws IOException
    {
        this.ensureOutputStream();
        int part;
        while (true)
        {
            part = value & 0x7F;

            value >>>= 7;
            if (value != 0)
                part |= 0x80;

            this.outputStream.write(part);

            if (value == 0)
                break;
        }
    }

    public void writeByteArray(byte[] bytes) throws IOException
    {
        this.ensureOutputStream();
        this.writeVarInt(bytes.length);
        this.outputStream.write(bytes);
    }

    public void writeString(String string) throws IOException
    {
        this.ensureOutputStream();
        byte[] bytes = string.getBytes("UTF-8");
        this.writeByteArray(bytes);
    }

    public void writeInt(int value) throws IOException
    {
        this.ensureOutputStream();
        this.outputStream.write(ByteBuffer.allocate(4).putInt(value).array());
    }

    public void writeShort(short value) throws IOException
    {
        this.ensureOutputStream();
        this.outputStream.write(ByteBuffer.allocate(2).putShort(value).array());
    }

    public void writeLong(long value) throws IOException
    {
        this.ensureOutputStream();
        this.outputStream.write(ByteBuffer.allocate(8).putLong(value).array());
    }

    public void writeByte(byte value) throws IOException
    {
        this.ensureOutputStream();
        this.outputStream.write(value);
    }

    public void writeBoolean(boolean value) throws IOException
    {
        this.ensureOutputStream();
        this.outputStream.write(value ? 0x01 : 0x00);
    }

    public void writeDouble(double value) throws IOException
    {
        this.ensureOutputStream();
        this.outputStream.write(ByteBuffer.allocate(8).putDouble(value).array());
    }

    public void writeFloat(float value) throws IOException
    {
        this.ensureOutputStream();
        this.outputStream.write(ByteBuffer.allocate(4).putFloat(value).array());
    }

    @SuppressWarnings("ShiftOutOfRange")
    public void writePosition(BlockPosition blockPosition) throws IOException
    {
        this.ensureOutputStream();
        this.writeLong(((blockPosition.getX() & 0x3FFFFFF) << 38) | ((blockPosition.getY() & 0xFFF) << 26) | (blockPosition.getZ() & 0x3FFFFFF));
    }

    public int available() throws IOException
    {
        this.ensureInputStream();
        return this.inputStream.available();
    }
}
