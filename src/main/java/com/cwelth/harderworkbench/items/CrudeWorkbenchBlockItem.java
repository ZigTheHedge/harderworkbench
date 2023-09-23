package com.cwelth.harderworkbench.items;

import com.cwelth.harderworkbench.ModMain;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class CrudeWorkbenchBlockItem extends BlockItem {
    public CrudeWorkbenchBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        CompoundTag tag = pStack.getTag();
        if(tag != null)
        {
            pTooltip.add(Component.translatable("gui.crudeworkbench.usesleft", tag.getInt("usesLeft")).withStyle(ChatFormatting.GRAY));
        } else
            pTooltip.add(Component.translatable("gui.crudeworkbench.usesleft", 21).withStyle(ChatFormatting.GRAY));
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    }

    @Override
    public String getDescriptionId(ItemStack pStack) {
        CompoundTag tag = pStack.getTag();
        if(tag != null)
        {
            if(tag.getInt("usesLeft") == 0)
                return pStack.getItem().getDescriptionId() + ".broken";
        }
        return super.getDescriptionId(pStack);
    }

    @Override
    public Block getBlock() {
        return super.getBlock();
    }

    @Override
    public void registerBlocks(Map<Block, Item> pBlockToItemMap, Item pItem) {
        if(!pItem.toString().equals("crude_workbench_three_thirds"))return;
        /*
        ModMain.LOGGER.warn("Mapping BlockItem " + getBlock().toString() + " to " + pItem.toString());
        if(pBlockToItemMap.containsKey(this.getBlock())) return;
        ModMain.LOGGER.warn("Not found. Adding...");

         */
        super.registerBlocks(pBlockToItemMap, pItem);
    }
}
