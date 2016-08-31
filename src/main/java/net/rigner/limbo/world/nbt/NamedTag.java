package net.rigner.limbo.world.nbt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Rigner on 30/08/16 for project Limbo.
 * All rights reserved.
 */
public abstract class NamedTag extends NBTTag
{
    private String name;

    public void read(InputStream inputStream) throws IOException
    {
        this.read(inputStream, true);
    }

    public void read(InputStream inputStream, boolean readName) throws IOException
    {
        if (readName)
            this.name = this.readString(inputStream);
    }

    public void write(OutputStream outputStream) throws IOException
    {
        this.write(outputStream, true);
    }

    public void write(OutputStream outputStream, boolean writeName) throws IOException
    {
        if (writeName)
            this.writeString(outputStream, this.name);
    }

    String getName()
    {
        return this.name;
    }
}
