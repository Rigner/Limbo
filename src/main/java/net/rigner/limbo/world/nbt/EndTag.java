package net.rigner.limbo.world.nbt;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Rigner on 30/08/16 for project Limbo.
 * All rights reserved.
 */
@SuppressWarnings("WeakerAccess")
public class EndTag extends NBTTag
{
    public static final byte ID = 0;

    public byte getId()
    {
        return 0;
    }

    public void read(InputStream inputStream) throws IOException
    {
    }

    public String toString()
    {
        return "END";
    }

    public void write() {}
}
