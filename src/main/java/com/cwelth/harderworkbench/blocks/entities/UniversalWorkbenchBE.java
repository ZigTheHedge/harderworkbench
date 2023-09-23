package com.cwelth.harderworkbench.blocks.entities;

import com.cwelth.harderworkbench.recipes.UniversalWorkbenchRecipe;
import com.cwelth.harderworkbench.screens.UniversalWorkbenchMenu;
import com.cwelth.harderworkbench.setup.Registries;
import com.cwelth.harderworkbench.utils.MyUtilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class UniversalWorkbenchBE extends BlockEntity implements MenuProvider {
    public SimpleContainer inputSlots = new SimpleContainer(7);
    public SimpleContainer outputSlots = new SimpleContainer(2);
    /*
    private final ItemStackHandler itemStackHandler = new ItemStackHandler(9) {
        @Override
        protected void onContentsChanged(int slot) {
            processRecipe(slot);
            setChanged();
        }

    };

    private LazyOptional<IItemHandler> capItemHandler = LazyOptional.empty();
    */
    public UniversalWorkbenchBE(BlockPos pPos, BlockState pBlockState) {
        super(Registries.UNIVERSAL_WORKBENCH_BE.get(), pPos, pBlockState);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.harderworkbench.universal_workbench");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new UniversalWorkbenchMenu(pContainerId, pPlayerInventory, this);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER)
        {
            //return capItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        //capItemHandler = LazyOptional.of(() -> itemStackHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        //capItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        //pTag.put("inventory", itemStackHandler.serializeNBT());

        pTag.put("inputSlots", MyUtilities.SerializeContainer(inputSlots));
        pTag.put("outputSlots", MyUtilities.SerializeContainer(outputSlots));

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        //itemStackHandler.deserializeNBT(pTag.getCompound("inventory"));

        inputSlots = MyUtilities.DeserializeContainer(pTag.getCompound("inputSlots"));
        outputSlots = MyUtilities.DeserializeContainer(pTag.getCompound("outputSlots"));
    }

    public void drops()
    {
        //SimpleContainer inventory = MyUtilities.passItemsToContainer(itemStackHandler);

        Containers.dropContents(this.level, this.worldPosition, inputSlots);
    }


}
