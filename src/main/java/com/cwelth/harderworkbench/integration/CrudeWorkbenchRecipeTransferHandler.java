package com.cwelth.harderworkbench.integration;

import com.cwelth.harderworkbench.recipes.UniversalWorkbenchRecipe;
import com.cwelth.harderworkbench.screens.CrudeWorkbenchMenu;
import com.cwelth.harderworkbench.screens.UniversalWorkbenchMenu;
import com.cwelth.harderworkbench.setup.Registries;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.CraftingRecipe;

import java.util.List;
import java.util.Optional;

public class CrudeWorkbenchRecipeTransferHandler implements IRecipeTransferInfo<CrudeWorkbenchMenu, CraftingRecipe> {
    @Override
    public Class<? extends CrudeWorkbenchMenu> getContainerClass() {
        return CrudeWorkbenchMenu.class;
    }

    @Override
    public Optional<MenuType<CrudeWorkbenchMenu>> getMenuType() {
        return Optional.of(Registries.CRUDE_WORKBENCH_MENU.get());
    }

    @Override
    public RecipeType<CraftingRecipe> getRecipeType() {
        return RecipeTypes.CRAFTING;
    }

    @Override
    public boolean canHandle(CrudeWorkbenchMenu container, CraftingRecipe recipe) {
        return true;
    }

    @Override
    public List<Slot> getRecipeSlots(CrudeWorkbenchMenu container, CraftingRecipe recipe) {
        return container.slots.subList(1, 10);
    }

    @Override
    public List<Slot> getInventorySlots(CrudeWorkbenchMenu container, CraftingRecipe recipe) {
        return container.slots.subList(11, container.slots.size() - 11);
    }
}
