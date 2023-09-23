package com.cwelth.harderworkbench.blocks;

import com.cwelth.harderworkbench.blocks.entities.CrudeWorkbenchBE;
import com.cwelth.harderworkbench.setup.Registries;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class CrudeWorkbench extends BaseEntityBlock {
    public static final EnumProperty<DAMAGE> CONDITION = EnumProperty.create("condition", DAMAGE.class);
    public static final VoxelShape[] SHAPE = new VoxelShape[]{
            Block.box(0, 0, 0, 16, 16, 16),
            Block.box(0, 0, 0, 16, 16, 16),
            Block.box(0, 0, 0, 16, 10, 16),
            Block.box(0, 0, 0, 16, 7, 16)
    };
    public CrudeWorkbench(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any().setValue(CONDITION, DAMAGE.THREE_THIRDS));
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(CONDITION);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return switch (pState.getValue(CONDITION)){
            case THREE_THIRDS -> SHAPE[0];
            case TWO_THIRDS -> SHAPE[1];
            case ONE_THIRD -> SHAPE[2];
            case BROKEN -> SHAPE[3];
        };
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CrudeWorkbenchBE(pPos, pState);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            if(pState.getValue(CONDITION) != DAMAGE.BROKEN) {
                //pPlayer.openMenu(pState.getMenuProvider(pLevel, pPos));

                BlockEntity be = pLevel.getBlockEntity(pPos);
                if(be instanceof CrudeWorkbenchBE cwBE)
                {
                    NetworkHooks.openScreen((ServerPlayer)pPlayer, cwBE, pPos);
                    pPlayer.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
                }


            }
            return InteractionResult.CONSUME;
        }
    }

    /*
    @Override
    public MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
        return new SimpleMenuProvider((p_52229_, p_52230_, p_52231_) -> {
            CrudeWorkbenchBE be = (CrudeWorkbenchBE) pLevel.getBlockEntity(pPos);
            return new CrudeWorkbenchMenu(be, p_52229_, p_52230_, ContainerLevelAccess.create(pLevel, pPos), be.getData());
        }, Component.translatable("block.harderworkbench.crude_workbench"));
    }

     */

    @Override
    public void playerDestroy(Level pLevel, Player pPlayer, BlockPos pPos, BlockState pState, @Nullable BlockEntity pBlockEntity, ItemStack pTool) {
        CompoundTag tag = new CompoundTag();
        if(pBlockEntity instanceof CrudeWorkbenchBE crudeWorkbenchBE)
        {
            int stage = crudeWorkbenchBE.getCraftCount();
            tag.putInt("usesLeft", stage);
            ItemStack itemToDrop;
            CrudeWorkbench.DAMAGE damage = CrudeWorkbench.DAMAGE.THREE_THIRDS;
            if(stage <= 14) damage = CrudeWorkbench.DAMAGE.TWO_THIRDS;
            if(stage <= 7) damage = CrudeWorkbench.DAMAGE.ONE_THIRD;
            if(stage <= 0) damage = CrudeWorkbench.DAMAGE.BROKEN;
            switch (damage)
            {
                case TWO_THIRDS -> itemToDrop = new ItemStack(Registries.CRUDE_WORKBENCH_ITEM_BLOCK_TWO_THIRDS.get());
                case ONE_THIRD -> itemToDrop = new ItemStack(Registries.CRUDE_WORKBENCH_ITEM_BLOCK_ONE_THIRD.get());
                case BROKEN -> itemToDrop = new ItemStack(Registries.CRUDE_WORKBENCH_ITEM_BLOCK_BROKEN.get());
                default -> itemToDrop = new ItemStack(Registries.CRUDE_WORKBENCH_ITEM_BLOCK_THREE_THIRDS.get());
            }

            itemToDrop.setTag(tag);

            ItemEntity itemEntity = new ItemEntity(pLevel, (double)pPos.getX() + 0.5D, (double)pPos.getY() + 0.5D, (double)pPos.getZ() + 0.5D, itemToDrop);
            itemEntity.setDefaultPickUpDelay();
            pLevel.addFreshEntity(itemEntity);
        }
        super.playerDestroy(pLevel, pPlayer, pPos, pState, pBlockEntity, pTool);
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        BlockEntity be = pLevel.getBlockEntity(pPos);
        if(be instanceof CrudeWorkbenchBE crudeBE)
        {
            CompoundTag tag = pStack.getTag();
            if(tag != null)
            {
                int usesLeft = tag.getInt("usesLeft");
                crudeBE.setUsesLeft(usesLeft);
            }
        }
    }

    @Override
    public Item asItem() {
        return Registries.CRUDE_WORKBENCH_ITEM_BLOCK_THREE_THIRDS.get();
    }

    public enum DAMAGE implements StringRepresentable {
        THREE_THIRDS,
        TWO_THIRDS,
        ONE_THIRD,
        BROKEN;

        @Override
        public String getSerializedName() {
            return switch(this)
            {
                case THREE_THIRDS -> "three_thirds";
                case TWO_THIRDS -> "two_thirds";
                case ONE_THIRD -> "one_third";
                case BROKEN -> "broken";
            };
        }
    }
}
