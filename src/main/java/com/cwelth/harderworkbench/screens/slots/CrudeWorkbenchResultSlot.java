package com.cwelth.harderworkbench.screens.slots;

import com.cwelth.harderworkbench.ModMain;
import com.cwelth.harderworkbench.blocks.entities.CrudeWorkbenchBE;
import com.cwelth.harderworkbench.screens.CrudeWorkbenchMenu;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;

public class CrudeWorkbenchResultSlot extends ResultSlot {
    private final CrudeWorkbenchMenu menu;
    private final Container dropContainer;
    private int resultCount;
    public CrudeWorkbenchResultSlot(CrudeWorkbenchMenu menu, Player pPlayer, CraftingContainer pCraftSlots, Container pContainer, int pSlot, int pXPosition, int pYPosition) {
        super(pPlayer, pCraftSlots, pContainer, pSlot, pXPosition, pYPosition);
        this.menu = menu;
        this.dropContainer = pCraftSlots;
        resultCount = 0;
    }

    @Override
    protected void onQuickCraft(ItemStack pStack, int pAmount) {
        ModMain.LOGGER.warn("onQuickCraft taken: " + pStack.toString() + ", amount: " + pAmount);
        menu.increaseCraftCount(pAmount);
        resultCount = pAmount;
        super.onQuickCraft(pStack, pAmount);
    }

    @Override
    public void onTake(Player pPlayer, ItemStack pStack) {
        int qty = pStack.getCount();
        if(qty == 0) {
            qty = resultCount;
        } else
            menu.increaseCraftCount(qty);
        ModMain.LOGGER.warn("ResultSlot taken: " + qty + ", isClientSide: " + pPlayer.level.isClientSide());
        super.onTake(pPlayer, pStack);
        if(!pPlayer.level.isClientSide())
            menu.getBE().increaseCraftCount(qty, dropContainer);
    }
}
