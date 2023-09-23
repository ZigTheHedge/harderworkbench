package com.cwelth.harderworkbench.screens.slots;

import com.cwelth.harderworkbench.ModMain;
import com.cwelth.harderworkbench.recipes.UniversalWorkbenchRecipe;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class UniversalWorkbenchRecipeSlot extends Slot {
    private final SimpleContainer inputSlots;
    private final SimpleContainer outputSlots;
    private final AbstractContainerMenu menu;
    private final Level level;
    public UniversalWorkbenchRecipeSlot(AbstractContainerMenu menu, SimpleContainer inputSlots, SimpleContainer outputSlots, Level level, int index, int xPosition, int yPosition) {
        super(inputSlots, index, xPosition, yPosition);
        this.outputSlots = outputSlots;
        this.inputSlots = inputSlots;
        this.level = level;
        this.menu = menu;
    }

    @Override
    public void set(ItemStack pStack) {
        super.set(pStack);
        contentsChanged();
    }

    @Override
    public ItemStack remove(int pAmount) {
        ItemStack newStack = super.remove(pAmount);
        contentsChanged();
        return newStack;
    }

    public void contentsChanged()
    {
        if(level.isClientSide()) return;

        menu.slotsChanged(inputSlots);
    }

}
