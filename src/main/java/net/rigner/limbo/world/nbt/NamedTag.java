package net.rigner.limbo.world.nbt;

import java.io.IOException;
import java.io.InputStream;

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
            this.name = readString(inputStream);
    }

    public void write()
    {
    }

    String getName()
    {
        return this.name;
    }
}
