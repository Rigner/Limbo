package net.rigner.limbo.world.nbt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Rigner on 30/08/16 for project Limbo.
 * All rights reserved.
 */
@SuppressWarnings("WeakerAccess")
public class StringTag extends NamedTag
{
    public static final byte ID = 8;
    private String value;

    public void read(InputStream inputStream, boolean readName) throws IOException
    {
        super.read(inputStream, readName);
        this.value = this.readString(inputStream);
    }

    public String getValue()
    {
        return this.value;
    }

    public String toString()
    {
        return this.value;
    }

    public byte getId()
    {
        return 8;
    }

    @Override
    public void write(OutputStream outputStream, boolean writeName) throws IOException
    {
        super.write(outputStream, writeName);
        this.writeString(outputStream, this.value);
    }
}
