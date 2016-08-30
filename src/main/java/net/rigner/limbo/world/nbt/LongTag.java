package net.rigner.limbo.world.nbt;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Rigner on 30/08/16 for project Limbo.
 * All rights reserved.
 */
@SuppressWarnings("WeakerAccess")
public class LongTag extends NamedTag
{
    public static final byte ID = 4;
    private long value;

    public void read(InputStream inputStream, boolean readName) throws IOException
    {
        super.read(inputStream, readName);
        this.value = this.readLong(inputStream);
    }

    public long getValue()
    {
        return this.value;
    }

    public String toString()
    {
        return Long.toString(this.value);
    }

    public byte getId()
    {
        return 4;
    }
}
