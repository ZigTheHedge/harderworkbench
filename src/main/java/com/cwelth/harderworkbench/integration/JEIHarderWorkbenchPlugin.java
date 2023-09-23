package com.cwelth.harderworkbench.integration;

import com.cwelth.harderworkbench.ModMain;
import com.cwelth.harderworkbench.recipes.UniversalWorkbenchRecipe;
import com.cwelth.harderworkbench.screens.UniversalWorkbenchMenu;
import com.cwelth.harderworkbench.setup.Registries;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.helpers.IStackHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;
import java.util.Map;
import java.util.Objects;


@JeiPlugin
public class JEIHarderWorkbenchPlugin implements IModPlugin {
    public static RecipeType<UniversalWorkbenchRecipe> UW_RECIPE_TYPE = new RecipeType<>(UniversalWorkbenchRecipeCategory.UID, UniversalWorkbenchRecipe.class);
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ModMain.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new UniversalWorkbenchRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();

        List<UniversalWorkbenchRecipe> UW_RECIPES = rm.getAllRecipesFor(UniversalWorkbenchRecipe.Type.INSTANCE);
        registration.addRecipes(UW_RECIPE_TYPE, UW_RECIPES);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(Registries.UNIVERSAL_WORKBENCH.get()), UW_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(Registries.CRUDE_WORKBENCH.get()), RecipeTypes.CRAFTING);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(new UniversalWorkstationRecipeTransferHandler());
        registration.addRecipeTransferHandler(new CrudeWorkbenchRecipeTransferHandler());
    }
}
