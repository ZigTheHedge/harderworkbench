package com.cwelth.harderworkbench.blocks.entities;

import com.cwelth.harderworkbench.ModMain;
import com.cwelth.harderworkbench.blocks.CrudeWorkbench;
import com.cwelth.harderworkbench.screens.CrudeWorkbenchMenu;
import com.cwelth.harderworkbench.screens.UniversalWorkbenchMenu;
import com.cwelth.harderworkbench.setup.Registries;
import com.cwelth.harderworkbench.utils.MyUtilities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.Nullable;

public class CrudeWorkbenchBE extends BlockEntity implements MenuProvider {
    private int usesLeft = 21;
    public CrudeWorkbenchBE(BlockPos pPos, BlockState pBlockState) {
        super(Registries.CRUDE_WORKBENCH_BE.get(), pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.putInt("usesLeft", usesLeft);

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        usesLeft = pTag.getInt("usesLeft");
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("usesLeft", usesLeft);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void increaseCraftCount(int qty, Container container) {
        usesLeft -= qty;
        ModMain.LOGGER.warn("increaseCraftCount called. qty: " + qty + ", usesLeft: " + usesLeft);
        if(usesLeft <= 0)
        {
            Containers.dropContents(this.level, this.worldPosition, container);
        }
        setShapeAccordingToCondition();
        setChanged();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 2);
    }

    public void setShapeAccordingToCondition()
    {
        CrudeWorkbench.DAMAGE damage = CrudeWorkbench.DAMAGE.THREE_THIRDS;
        if(usesLeft <= 14) damage = CrudeWorkbench.DAMAGE.TWO_THIRDS;
        if(usesLeft <= 7) damage = CrudeWorkbench.DAMAGE.ONE_THIRD;
        if(usesLeft <= 0) damage = CrudeWorkbench.DAMAGE.BROKEN;
        level.setBlock(this.getBlockPos(), Registries.CRUDE_WORKBENCH.get().defaultBlockState().setValue(CrudeWorkbench.CONDITION, damage), 3);
    }

    public int getCraftCount()
    {
        return usesLeft;
    }
    public void setUsesLeft(int usesLeft) {
        this.usesLeft = usesLeft;
        setChanged();
        setShapeAccordingToCondition();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.harderworkbench.crude_workbench");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        //return new CrudeWorkbenchMenu(this, pContainerId, pPlayerInventory);
        return new CrudeWorkbenchMenu(this, pContainerId, pPlayerInventory);
    }
}
