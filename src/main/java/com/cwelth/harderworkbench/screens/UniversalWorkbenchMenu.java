package com.cwelth.harderworkbench.screens;

import com.cwelth.harderworkbench.ModMain;
import com.cwelth.harderworkbench.blocks.entities.UniversalWorkbenchBE;
import com.cwelth.harderworkbench.recipes.UniversalWorkbenchRecipe;
import com.cwelth.harderworkbench.screens.slots.UniversalWorkbenchRecipeSlot;
import com.cwelth.harderworkbench.screens.slots.UniversalWorkbenchResultSlot;
import com.cwelth.harderworkbench.setup.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Optional;

public class UniversalWorkbenchMenu extends AbstractContainerMenu {
    public final UniversalWorkbenchBE blockEntity;
    private final Level level;

    public UniversalWorkbenchMenu(int id, Inventory inv, FriendlyByteBuf buf)
    {
        this(id, inv, inv.player.level.getBlockEntity(buf.readBlockPos()));
    }
    public UniversalWorkbenchMenu(int id, Inventory inv, BlockEntity entity) {
        super(Registries.UNIVERSAL_WORKBENCH_MENU.get(), id);

        blockEntity = (UniversalWorkbenchBE) entity;
        level = entity.getLevel();

        addPlayerInventorySlots(inv);
        addPlayerHotbarSlots(inv);

        /*
        blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            // Toolrack slots
            addSlot(new SlotItemHandler(handler, 0, 17, 19));
            addSlot(new SlotItemHandler(handler, 1, 38, 19));
            addSlot(new SlotItemHandler(handler, 2, 59, 19));

            // Selected tool slot
            addSlot(new SlotItemHandler(handler, 3, 17, 44));

            // Material slots
            addSlot(new SlotItemHandler(handler, 4, 59, 44));
            addSlot(new SlotItemHandler(handler, 5, 91, 44));
            addSlot(new SlotItemHandler(handler, 6, 123, 44));

            // Output slots
            addSlot(new UniversalWorkbenchResultSlot(handler, 7, 144, 69));
            addSlot(new UniversalWorkbenchResultSlot(handler, 8, 123, 69));
        });
        */
        // Toolrack slots
        addSlot(new Slot(blockEntity.inputSlots, 0, 17, 19));
        addSlot(new Slot(blockEntity.inputSlots, 1, 38, 19));
        addSlot(new Slot(blockEntity.inputSlots, 2, 59, 19));

        // Selected tool slot
        addSlot(new UniversalWorkbenchRecipeSlot(this, blockEntity.inputSlots, blockEntity.outputSlots, level, 3, 17, 44));

        // Material slots
        addSlot(new UniversalWorkbenchRecipeSlot(this, blockEntity.inputSlots, blockEntity.outputSlots, level, 4, 59, 44));
        addSlot(new UniversalWorkbenchRecipeSlot(this, blockEntity.inputSlots, blockEntity.outputSlots, level, 5, 91, 44));
        addSlot(new UniversalWorkbenchRecipeSlot(this, blockEntity.inputSlots, blockEntity.outputSlots, level, 6, 123, 44));

        // Output slots
        addSlot(new UniversalWorkbenchResultSlot(this, blockEntity.inputSlots, blockEntity.outputSlots, 0, 144, 69));
        addSlot(new UniversalWorkbenchResultSlot(this, blockEntity.inputSlots, blockEntity.outputSlots, 1, 123, 69));

    }

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 9;  // must be the number of slots you have!

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), pPlayer, Registries.UNIVERSAL_WORKBENCH.get());
    }

    private void addPlayerInventorySlots(Inventory playerInventory)
    {
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 9; j++)
                addSlot(new Slot(playerInventory, i * 9 + j + 9, 8 + j * 18, 94 + i * 18));
    }

    private void addPlayerHotbarSlots(Inventory playerInventory)
    {
        for(int i = 0; i < 9; i++)
            addSlot(new Slot(playerInventory, i, 8 + i * 18, 152));
    }

    @Override
    public void slotsChanged(Container pContainer) {
        super.slotsChanged(pContainer);

        Optional<UniversalWorkbenchRecipe> recipe = level.getRecipeManager()
                .getRecipeFor(UniversalWorkbenchRecipe.Type.INSTANCE, blockEntity.inputSlots, level);

        if(recipe.isPresent()) {
            UniversalWorkbenchRecipe matchedRecipe = recipe.get();
            blockEntity.outputSlots.setItem(0, matchedRecipe.getMainOutput().copy());
            blockEntity.outputSlots.setItem(1, matchedRecipe.getAdditionalOutput().copy());
        } else
        {
            blockEntity.outputSlots.setItem(0, ItemStack.EMPTY);
            blockEntity.outputSlots.setItem(1, ItemStack.EMPTY);
        }

        blockEntity.setChanged();
    }
}
