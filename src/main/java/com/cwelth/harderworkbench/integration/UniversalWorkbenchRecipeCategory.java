package com.cwelth.harderworkbench.integration;

import com.cwelth.harderworkbench.ModMain;
import com.cwelth.harderworkbench.recipes.UniversalWorkbenchRecipe;
import com.cwelth.harderworkbench.setup.Registries;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class UniversalWorkbenchRecipeCategory implements IRecipeCategory<UniversalWorkbenchRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(ModMain.MODID, "universal_workbench_recipe");
    public static final ResourceLocation TEXTURE = new ResourceLocation("harderworkbench:textures/gui/universal_workbench_jei.png");

    IDrawable background;
    IDrawable icon;

    public UniversalWorkbenchRecipeCategory(IGuiHelper helper)
    {
        //background = helper.createDrawable(TEXTURE, 0, 10, 176, 90);
        background = helper.createDrawable(TEXTURE, 12, 10, 152, 90);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Registries.UNIVERSAL_WORKBENCH.get()));
    }

    @Override
    public RecipeType<UniversalWorkbenchRecipe> getRecipeType() {
        return JEIHarderWorkbenchPlugin.UW_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.harderworkbench.universal_workbench");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, UniversalWorkbenchRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 5, 24).addIngredients(recipe.getTool());
        builder.addSlot(RecipeIngredientRole.INPUT, 47, 24).addIngredients(recipe.getMaterial(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 79, 24).addIngredients(recipe.getMaterial(1));
        builder.addSlot(RecipeIngredientRole.INPUT, 111, 24).addIngredients(recipe.getMaterial(2));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 132, 49).addItemStack(recipe.getMainOutput().copy());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 111, 49).addItemStack(recipe.getAdditionalOutput().copy());
    }
}
