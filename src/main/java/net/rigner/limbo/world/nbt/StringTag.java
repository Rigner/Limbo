package net.rigner.limbo.world.nbt;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Rigner on 30/08/16 for project Limbo.
 * All rights reserved.
 */
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
}
