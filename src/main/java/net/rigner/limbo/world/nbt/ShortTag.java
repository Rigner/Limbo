package net.rigner.limbo.world.nbt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Rigner on 30/08/16 for project Limbo.
 * All rights reserved.
 */
public class ShortTag extends NamedTag
{
    public static final byte ID = 2;
    private short value;

    public void read(InputStream inputStream, boolean readName) throws IOException
    {
        super.read(inputStream, readName);
        this.value = this.readShort(inputStream);
    }

    public short getValue()
    {
        return this.value;
    }

    public String toString()
    {
        return Short.toString(this.value);
    }

    public byte getId()
    {
        return 2;
    }

    @Override
    public void write(OutputStream outputStream, boolean writeName) throws IOException
    {
        super.write(outputStream, writeName);
        this.writeShort(outputStream, this.value);
    }
}
