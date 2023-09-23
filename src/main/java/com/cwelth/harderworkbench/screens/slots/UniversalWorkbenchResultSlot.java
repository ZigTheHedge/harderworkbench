package com.cwelth.harderworkbench.screens.slots;

import com.cwelth.harderworkbench.ModMain;
import com.cwelth.harderworkbench.blocks.entities.UniversalWorkbenchBE;
import com.cwelth.harderworkbench.recipes.UniversalWorkbenchRecipe;
import com.cwelth.harderworkbench.utils.MyUtilities;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class UniversalWorkbenchResultSlot extends Slot {
    private final SimpleContainer inputSlots;
    private final SimpleContainer outputSlots;
    private final AbstractContainerMenu menu;

    public UniversalWorkbenchResultSlot(AbstractContainerMenu menu, SimpleContainer inputSlots, SimpleContainer outputSlots, int index, int xPosition, int yPosition) {
        super(outputSlots, index, xPosition, yPosition);
        this.inputSlots = inputSlots;
        this.outputSlots = outputSlots;
        this.menu = menu;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public void onTake(Player pPlayer, ItemStack pStack) {
        super.onTake(pPlayer, pStack);
        if(pPlayer.level.isClientSide()) return;

        Optional<UniversalWorkbenchRecipe> recipe = pPlayer.level.getRecipeManager()
                .getRecipeFor(UniversalWorkbenchRecipe.Type.INSTANCE, inputSlots, pPlayer.level);

        if(recipe.isPresent()) {
            UniversalWorkbenchRecipe matchedRecipe = recipe.get();
            for(int i = 0; i < matchedRecipe.getMaterialsCount(); i++) {
                inputSlots.removeItem(4 + i, 1);
            }
            if(matchedRecipe.getDamageSource()) {
                ItemStack tool = inputSlots.getItem(3);
                if(tool.isDamageableItem()) {
                    tool.setDamageValue(tool.getDamageValue() + 1);
                    if(tool.getDamageValue() >= tool.getMaxDamage())tool = ItemStack.EMPTY;
                }
                inputSlots.setItem(3, tool);
            }
        }

        menu.slotsChanged(inputSlots);
    }
}
