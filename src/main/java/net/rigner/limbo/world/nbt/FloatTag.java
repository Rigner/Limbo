package net.rigner.limbo.world.nbt;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Rigner on 30/08/16 for project Limbo.
 * All rights reserved.
 */
@SuppressWarnings("WeakerAccess")
public class FloatTag extends NamedTag
{
    public static final byte ID = 5;
    private float value;

    public void read(InputStream inputStream, boolean readName) throws IOException
    {
        super.read(inputStream, readName);
        this.value = Float.intBitsToFloat(this.readInt(inputStream));
    }

    public float getValue()
    {
        return this.value;
    }

    public String toString()
    {
        return Float.toString(this.value);
    }

    public byte getId()
    {
        return 5;
    }
}
