package net.rigner.limbo.world;

import net.rigner.limbo.world.nbt.CompoundTag;
import net.rigner.limbo.world.nbt.NamedTag;

/**
 * Created by Rigner on 30/08/16 for project Limbo.
 * All rights reserved.
 */
public class SignTileEntity extends TileEntity
{
    private String text1;
    private String text2;
    private String text3;
    private String text4;

    SignTileEntity(CompoundTag data)
    {
        super(data);

        NamedTag data1 = data.get("Text1");
        NamedTag data2 = data.get("Text2");
        NamedTag data3 = data.get("Text3");
        NamedTag data4 = data.get("Text4");

        this.text1 = data1 == null ? "{}" : data1.toStringTag().getValue();
        this.text2 = data2 == null ? "{}" : data2.toStringTag().getValue();
        this.text3 = data3 == null ? "{}" : data3.toStringTag().getValue();
        this.text4 = data4 == null ? "{}" : data4.toStringTag().getValue();
    }

    public String getText1()
    {
        return this.text1;
    }

    public String getText2()
    {
        return this.text2;
    }

    public String getText3()
    {
        return this.text3;
    }

    public String getText4()
    {
        return this.text4;
    }

    public String getId()
    {
        return "Sign";
    }
}
