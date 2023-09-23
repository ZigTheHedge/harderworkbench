package com.cwelth.harderworkbench.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class MyUtilities {
    public static SimpleContainer passItemsToContainer(ItemStackHandler itemStackHandler)
    {
        SimpleContainer toRet = new SimpleContainer(itemStackHandler.getSlots());
        for(int i = 0; i < itemStackHandler.getSlots(); i++)
            toRet.setItem(i, itemStackHandler.getStackInSlot(i));
        return toRet;
    }

    public static CompoundTag SerializeContainer(SimpleContainer container)
    {
        CompoundTag tag = new CompoundTag();
        tag.putInt("container_size", container.getContainerSize());
        for(int i = 0; i < container.getContainerSize(); i++)
        {
            tag.put(String.valueOf(i), container.getItem(i).save(new CompoundTag()));
        }
        return tag;
    }

    public static SimpleContainer DeserializeContainer(CompoundTag tag)
    {
        SimpleContainer container = new SimpleContainer(tag.getInt("container_size"));
        for(int i = 0; i < container.getContainerSize(); i++)
        {
            container.setItem(i, ItemStack.of(tag.getCompound(String.valueOf(i))));
        }
        return container;
    }
}
