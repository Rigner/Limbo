package net.rigner.limbo.world.nbt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Created by Rigner on 30/08/16 for project Limbo.
 * All rights reserved.
 */
public class ListTag extends NamedTag
{
    public static final byte ID = 9;
    private NamedTag[] tags;
    private byte id;

    public void read(InputStream inputStream, boolean readName) throws IOException
    {
        super.read(inputStream, readName);
        this.id = this.readByte(inputStream);
        this.tags = new NamedTag[this.readInt(inputStream)];
        for (int i = 0; i < this.tags.length; i++)
        {
            NBTTag tag = NBTTag.readTag(inputStream, this.id, false);
            if (!(tag instanceof NamedTag))
                throw new IOException("END_TAG not allowed here");
            ((NamedTag) tag).read(inputStream, false);
            this.tags[i] = ((NamedTag) tag);
        }
    }

    public NamedTag[] getTags()
    {
        return this.tags;
    }

    public byte getId()
    {
        return 9;
    }

    public String toString()
    {
        return Arrays.toString(this.tags);
    }

    @Override
    public void write(OutputStream outputStream, boolean writeName) throws IOException
    {
        super.write(outputStream, writeName);
        this.writeByte(outputStream, this.id);
        this.writeInt(outputStream, this.tags.length);
        for (NamedTag namedTag : this.tags)
        {
            NBTTag.writeTag(outputStream, namedTag, false);
            namedTag.write(outputStream, false);
        }
    }
}
