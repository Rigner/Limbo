package net.rigner.limbo.world.nbt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Rigner on 30/08/16 for project Limbo.
 * All rights reserved.
 */
@SuppressWarnings("WeakerAccess")
public class DoubleTag extends NamedTag
{
    public static final byte ID = 6;
    private double value;

    public void read(InputStream inputStream, boolean readName) throws IOException
    {
        super.read(inputStream, readName);
        this.value = Double.longBitsToDouble(this.readLong(inputStream));
    }

    public double getValue()
    {
        return this.value;
    }

    public String toString()
    {
        return Double.toString(this.value);
    }

    public byte getId()
    {
        return 6;
    }

    @Override
    public void write(OutputStream outputStream, boolean writeName) throws IOException
    {
        super.write(outputStream, writeName);
        this.writeLong(outputStream, Double.doubleToLongBits(this.value));
    }
}
