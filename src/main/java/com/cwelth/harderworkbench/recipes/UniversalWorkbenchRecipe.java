package com.cwelth.harderworkbench.recipes;

import com.cwelth.harderworkbench.ModMain;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class UniversalWorkbenchRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final NonNullList<ItemStack> output;
    private final Ingredient tool_slot;
    private final NonNullList<Ingredient> materials;
    private final boolean damageSource;

    public UniversalWorkbenchRecipe(ResourceLocation id, NonNullList<ItemStack> output, Ingredient tool_slot, NonNullList<Ingredient> materials, boolean damageSource)
    {
        this.id = id;
        this.output = output;
        this.tool_slot = tool_slot;
        this.materials = materials;
        this.damageSource = damageSource;
    }

    public Ingredient getMaterial(int slot)
    {
        if(slot >= getMaterialsCount())
            return Ingredient.EMPTY;
        return materials.get(slot);
    }
    public int getMaterialsCount()
    {
        return materials.size();
    }

    public boolean getDamageSource()
    {
        return damageSource;
    }

    public ItemStack getMainOutput()
    {
        return output.get(0);
    }

    public ItemStack getAdditionalOutput()
    {
        if(output.size() > 1)
            return output.get(1);
        else
            return ItemStack.EMPTY;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        if(pLevel.isClientSide()) return false;

        if(!tool_slot.test(pContainer.getItem(3))) return false;

        for(int i = 0; i < materials.size(); i++) {
            if (!materials.get(i).test(pContainer.getItem(i + 4))) return false;
        }
        if(materials.size() < 3)
        {
            for(int i = materials.size(); i < 3; i++)
                if (!pContainer.getItem(i + 4).isEmpty()) return false;
        }

        return true;
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer) {
        return output.get(0);
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    public Ingredient getTool()
    {
        return tool_slot;
    }

    @Override
    public ItemStack getResultItem() {
        return output.get(0);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<UniversalWorkbenchRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final String ID = "universal_workbench_recipe";
    }

    public static class Serializer implements RecipeSerializer<UniversalWorkbenchRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(ModMain.MODID, "universal_workbench_recipe");

        @Override
        public UniversalWorkbenchRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            //ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));
            JsonArray outputJson = GsonHelper.getAsJsonArray(pSerializedRecipe, "outputs");
            NonNullList<ItemStack> outputs = NonNullList.withSize(outputJson.size(), ItemStack.EMPTY);
            for(int i = 0; i < outputJson.size(); i++)
                outputs.set(i, ShapedRecipe.itemStackFromJson(outputJson.get(i).getAsJsonObject()));
            Ingredient tool_slot = Ingredient.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "tool_slot"));
            JsonArray materialsJson = GsonHelper.getAsJsonArray(pSerializedRecipe, "materials");
            NonNullList<Ingredient> materials = NonNullList.withSize(materialsJson.size(), Ingredient.EMPTY);
            for(int i = 0; i < materialsJson.size(); i++)
                materials.set(i, Ingredient.fromJson(materialsJson.get(i)));
            boolean damageSource = GsonHelper.getAsBoolean(pSerializedRecipe, "damage_source");
            return new UniversalWorkbenchRecipe(pRecipeId, outputs, tool_slot, materials, damageSource);
        }

        @Override
        public @Nullable UniversalWorkbenchRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            int outputsCount = pBuffer.readInt();
            NonNullList<ItemStack> outputs = NonNullList.withSize(outputsCount, ItemStack.EMPTY);
            for(int i = 0; i < outputsCount; i++)
                outputs.set(i, pBuffer.readItem());
            Ingredient tool_slot = Ingredient.fromNetwork(pBuffer);
            int materialsCount = pBuffer.readInt();
            NonNullList<Ingredient> materials = NonNullList.withSize(materialsCount, Ingredient.EMPTY);
            for(int i = 0; i < materialsCount; i++)
                materials.set(i, Ingredient.fromNetwork(pBuffer));
            boolean damageSource = pBuffer.readBoolean();

            return new UniversalWorkbenchRecipe(pRecipeId, outputs, tool_slot, materials, damageSource);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, UniversalWorkbenchRecipe pRecipe) {
            pBuffer.writeInt(pRecipe.output.size());
            for(int i = 0; i < pRecipe.output.size(); i++)
                pBuffer.writeItemStack(pRecipe.output.get(i), false);
            pRecipe.tool_slot.toNetwork(pBuffer);
            pBuffer.writeInt(pRecipe.materials.size());
            for(int i = 0; i < pRecipe.materials.size(); i++)
                pRecipe.materials.get(i).toNetwork(pBuffer);
            pBuffer.writeBoolean(pRecipe.damageSource);
        }
    }

}
