package net.rigner.limbo.world.nbt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Rigner on 30/08/16 for project Limbo.
 * All rights reserved.
 */
@SuppressWarnings("WeakerAccess")
public class IntTag extends NamedTag
{
    public static final byte ID = 3;
    private int value;

    public void read(InputStream inputStream, boolean readName) throws IOException
    {
        super.read(inputStream, readName);
        this.value = this.readInt(inputStream);
    }

    public int getValue()
    {
        return this.value;
    }

    public String toString()
    {
        return Integer.toString(this.value);
    }

    public byte getId()
    {
        return 3;
    }

    @Override
    public void write(OutputStream outputStream, boolean writeName) throws IOException
    {
        super.write(outputStream, writeName);
        this.writeInt(outputStream, this.value);
    }
}

