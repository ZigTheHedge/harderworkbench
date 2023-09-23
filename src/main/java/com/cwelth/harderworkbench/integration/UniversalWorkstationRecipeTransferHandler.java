package com.cwelth.harderworkbench.integration;

import com.cwelth.harderworkbench.recipes.UniversalWorkbenchRecipe;
import com.cwelth.harderworkbench.screens.UniversalWorkbenchMenu;
import com.cwelth.harderworkbench.setup.Registries;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.List;
import java.util.Optional;

public class UniversalWorkstationRecipeTransferHandler implements IRecipeTransferInfo<UniversalWorkbenchMenu, UniversalWorkbenchRecipe> {
    @Override
    public Class<? extends UniversalWorkbenchMenu> getContainerClass() {
        return UniversalWorkbenchMenu.class;
    }

    @Override
    public Optional<MenuType<UniversalWorkbenchMenu>> getMenuType() {
        return Optional.of(Registries.UNIVERSAL_WORKBENCH_MENU.get());
    }

    @Override
    public RecipeType getRecipeType() {
        return JEIHarderWorkbenchPlugin.UW_RECIPE_TYPE;
    }

    @Override
    public boolean canHandle(UniversalWorkbenchMenu container, UniversalWorkbenchRecipe recipe) {
        return true;
    }

    @Override
    public List<Slot> getRecipeSlots(UniversalWorkbenchMenu container, UniversalWorkbenchRecipe recipe) {
        return container.slots.subList(container.slots.size() - 6, container.slots.size() - 2);
    }

    @Override
    public List<Slot> getInventorySlots(UniversalWorkbenchMenu container, UniversalWorkbenchRecipe recipe) {
        return container.slots.subList(0, container.slots.size() - 6);
    }

}
