package net.rigner.limbo.world;

import net.rigner.limbo.world.nbt.CompoundTag;

/**
 * Created by Rigner on 30/08/16 for project Limbo.
 * All rights reserved.
 */
public abstract class TileEntity
{
    private BlockPosition blockPosition;
    private CompoundTag data;

    TileEntity(CompoundTag data)
    {
        this.data = data;
        this.blockPosition = new BlockPosition(data.get("x").toIntTag().getValue(), data.get("y").toIntTag().getValue(), data.get("z").toIntTag().getValue());
    }

    public BlockPosition getBlockPosition()
    {
        return this.blockPosition;
    }

    public abstract String getId();

    public CompoundTag getData()
    {
        return this.data;
    }
}
