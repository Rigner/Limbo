package net.rigner.limbo.world.nbt;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Rigner on 30/08/16 for project Limbo.
 * All rights reserved.
 */
@SuppressWarnings("WeakerAccess")
public class ByteTag extends NamedTag
{
    public static final byte ID = 1;
    private byte value;

    public void read(InputStream inputStream, boolean readName) throws IOException
    {
        super.read(inputStream, readName);
        this.value = this.readByte(inputStream);
    }

    public byte getValue()
    {
        return this.value;
    }

    public String toString()
    {
        return Byte.toString(this.value);
    }

    public byte getId()
    {
        return 1;
    }
}
