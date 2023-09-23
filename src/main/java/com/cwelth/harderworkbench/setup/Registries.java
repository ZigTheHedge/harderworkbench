package com.cwelth.harderworkbench.setup;

import com.cwelth.harderworkbench.blocks.CrudeWorkbench;
import com.cwelth.harderworkbench.blocks.UniversalWorkbench;
import com.cwelth.harderworkbench.blocks.entities.CrudeWorkbenchBE;
import com.cwelth.harderworkbench.blocks.entities.UniversalWorkbenchBE;
import com.cwelth.harderworkbench.items.CrudeKnife;
import com.cwelth.harderworkbench.items.CrudeWorkbenchBlockItem;
import com.cwelth.harderworkbench.items.ItemWithDescription;
import com.cwelth.harderworkbench.loot.AddLootModifier;
import com.cwelth.harderworkbench.recipes.UniversalWorkbenchRecipe;
import com.cwelth.harderworkbench.screens.CrudeWorkbenchMenu;
import com.cwelth.harderworkbench.screens.UniversalWorkbenchMenu;
import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.cwelth.harderworkbench.ModMain.MODID;

public class Registries {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MODID);
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MODID);

    // Loot mods
    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_LOOT_ITEM = LOOT_MODS.register("add_item", AddLootModifier.CODEC);

    // Universal workbench
    public static final RegistryObject<UniversalWorkbench> UNIVERSAL_WORKBENCH = BLOCKS.register("universal_workbench",
            () -> new UniversalWorkbench(BlockBehaviour.Properties.of(Material.STONE).strength(6f).requiresCorrectToolForDrops().noOcclusion()));
    public static final RegistryObject<BlockItem> UNIVERSAL_WORKBENCH_ITEM_BLOCK = ITEMS.register("universal_workbench",
            () -> new BlockItem(UNIVERSAL_WORKBENCH.get(), new Item.Properties().tab(MainSetup.ITEM_GROUP)));
    public static final RegistryObject<BlockEntityType<UniversalWorkbenchBE>> UNIVERSAL_WORKBENCH_BE = BLOCK_ENTITIES.register("universal_workbench",
            () -> BlockEntityType.Builder.of(UniversalWorkbenchBE::new, UNIVERSAL_WORKBENCH.get()).build(null));
    public static final RegistryObject<MenuType<UniversalWorkbenchMenu>> UNIVERSAL_WORKBENCH_MENU = registerMenuType("universal_workbench", UniversalWorkbenchMenu::new);

    // Workbench
    public static final RegistryObject<Item> STAND_QUARTER = ITEMS.register("stand_quarter", () -> new Item(new Item.Properties().tab(MainSetup.ITEM_GROUP)));
    public static final RegistryObject<Item> STAND_FULL = ITEMS.register("stand_full", () -> new Item(new Item.Properties().tab(MainSetup.ITEM_GROUP)));
    public static final RegistryObject<CrudeWorkbench> CRUDE_WORKBENCH = BLOCKS.register("crude_workbench",
            () -> new CrudeWorkbench(BlockBehaviour.Properties.of(Material.WOOD).strength(6f).noOcclusion()));
    public static final RegistryObject<BlockItem> CRUDE_WORKBENCH_ITEM_BLOCK_THREE_THIRDS = ITEMS.register("crude_workbench_three_thirds",
            () -> new CrudeWorkbenchBlockItem(CRUDE_WORKBENCH.get(), new Item.Properties().tab(MainSetup.ITEM_GROUP).stacksTo(1)));
    public static final RegistryObject<BlockItem> CRUDE_WORKBENCH_ITEM_BLOCK_TWO_THIRDS = ITEMS.register("crude_workbench_two_thirds",
            () -> new CrudeWorkbenchBlockItem(CRUDE_WORKBENCH.get(), new Item.Properties().stacksTo(1)));
    public static final RegistryObject<BlockItem> CRUDE_WORKBENCH_ITEM_BLOCK_ONE_THIRD = ITEMS.register("crude_workbench_one_third",
            () -> new CrudeWorkbenchBlockItem(CRUDE_WORKBENCH.get(), new Item.Properties().stacksTo(1)));
    public static final RegistryObject<BlockItem> CRUDE_WORKBENCH_ITEM_BLOCK_BROKEN = ITEMS.register("crude_workbench_broken",
            () -> new CrudeWorkbenchBlockItem(CRUDE_WORKBENCH.get(), new Item.Properties().stacksTo(1)));

    public static final RegistryObject<BlockEntityType<CrudeWorkbenchBE>> CRUDE_WORKBENCH_BE = BLOCK_ENTITIES.register("crude_workbench",
            () -> BlockEntityType.Builder.of(CrudeWorkbenchBE::new, CRUDE_WORKBENCH.get()).build(null));
    public static final RegistryObject<MenuType<CrudeWorkbenchMenu>> CRUDE_WORKBENCH_MENU = registerMenuType("crude_workbench", CrudeWorkbenchMenu::new);


    // Natural parts
    public static final RegistryObject<Item> BARK_GENERIC = ITEMS.register("bark_generic", () -> new ItemWithDescription(new Item.Properties().tab(MainSetup.ITEM_GROUP), Component.translatable("gui.description.bark_generic")));
    public static final RegistryObject<Item> BARK_WORKBENCH_TOP = ITEMS.register("bark_workbench_top", () -> new Item(new Item.Properties().tab(MainSetup.ITEM_GROUP)));
    public static final RegistryObject<Item> BOARD_WORKBENCH_TOP = ITEMS.register("board_workbench_top", () -> new Item(new Item.Properties().tab(MainSetup.ITEM_GROUP)));
    public static final RegistryObject<Item> CRUDE_WORKBENCH_TOP = ITEMS.register("crude_workbench_top", () -> new Item(new Item.Properties().tab(MainSetup.ITEM_GROUP)));
    public static final RegistryObject<Item> WORKBENCH_TOP = ITEMS.register("workbench_top", () -> new Item(new Item.Properties().tab(MainSetup.ITEM_GROUP)));
    public static final RegistryObject<Item> YELLOW_FLINT = ITEMS.register("yellow_flint", () -> new Item(new Item.Properties().tab(MainSetup.ITEM_GROUP)));
    public static final RegistryObject<Item> BROWN_FLINT = ITEMS.register("brown_flint", () -> new Item(new Item.Properties().tab(MainSetup.ITEM_GROUP)));
    public static final RegistryObject<Item> BOARD_GENERIC = ITEMS.register("board_generic", () -> new Item(new Item.Properties().tab(MainSetup.ITEM_GROUP)));
    public static final RegistryObject<Item> BOARD_CORNER = ITEMS.register("board_corner", () -> new Item(new Item.Properties().tab(MainSetup.ITEM_GROUP)));
    public static final RegistryObject<Item> BOARD_SQUARE = ITEMS.register("board_square", () -> new Item(new Item.Properties().tab(MainSetup.ITEM_GROUP)));
    public static final RegistryObject<Item> BOARD_BOX = ITEMS.register("board_box", () -> new Item(new Item.Properties().tab(MainSetup.ITEM_GROUP)));
    public static final RegistryObject<Item> BOARD_BOX_HINGE = ITEMS.register("board_box_hinge", () -> new Item(new Item.Properties().tab(MainSetup.ITEM_GROUP)));
    public static final RegistryObject<Item> IRON_PLATE = ITEMS.register("iron_plate", () -> new Item(new Item.Properties().tab(MainSetup.ITEM_GROUP)));
    public static final RegistryObject<Item> IRON_STRIPS = ITEMS.register("iron_strips", () -> new Item(new Item.Properties().tab(MainSetup.ITEM_GROUP)));
    public static final RegistryObject<Item> LOOSE_GRASS = ITEMS.register("loose_grass", () -> new ItemWithDescription(new Item.Properties().tab(MainSetup.ITEM_GROUP), Component.translatable("gui.description.loose_grass")));
    public static final RegistryObject<Item> GRASS_TWINE = ITEMS.register("grass_twine", () -> new Item(new Item.Properties().tab(MainSetup.ITEM_GROUP)));
    public static final RegistryObject<Item> PEELED_POTATO = ITEMS.register("peeled_potato", () -> new Item(new Item.Properties().tab(MainSetup.ITEM_GROUP)));
    public static final RegistryObject<Item> GRATED_POTATO = ITEMS.register("grated_potato", () -> new Item(new Item.Properties().tab(MainSetup.ITEM_GROUP)));
    public static final RegistryObject<Item> POTATO_IN_BUCKET = ITEMS.register("potato_in_bucket", () -> new Item(new Item.Properties().tab(MainSetup.ITEM_GROUP)));
    public static final RegistryObject<Item> POTATO_GLUE = ITEMS.register("potato_glue", () -> new Item(new Item.Properties().tab(MainSetup.ITEM_GROUP)));
    public static final RegistryObject<Item> DARKIRON_INGOT = ITEMS.register("darkiron_ingot", () -> new Item(new Item.Properties().tab(MainSetup.ITEM_GROUP)));
    public static final RegistryObject<Item> SMALL_ROCK = ITEMS.register("small_rock", () -> new ItemWithDescription(new Item.Properties().tab(MainSetup.ITEM_GROUP), Component.translatable("gui.description.small_rock")));
    public static final RegistryObject<Item> COBBLED_BRICK = ITEMS.register("cobbled_brick", () -> new Item(new Item.Properties().tab(MainSetup.ITEM_GROUP)));


    // Building parts and tools
    public static final RegistryObject<Item> NAIL = ITEMS.register("nail", () -> new Item(new Item.Properties().tab(MainSetup.ITEM_GROUP)));
    public static final RegistryObject<Item> SCREW = ITEMS.register("screw", () -> new Item(new Item.Properties().tab(MainSetup.ITEM_GROUP)));
    public static final RegistryObject<Item> HINGE = ITEMS.register("hinge", () -> new Item(new Item.Properties().tab(MainSetup.ITEM_GROUP)));
    public static final RegistryObject<Item> SCREWDRIVER = ITEMS.register("screwdriver", () -> new Item(new Item.Properties().tab(MainSetup.ITEM_GROUP).durability(100)));
    public static final RegistryObject<Item> HAMMER = ITEMS.register("hammer", () -> new Item(new Item.Properties().tab(MainSetup.ITEM_GROUP).durability(100)));
    public static final RegistryObject<Item> HANDSAW = ITEMS.register("handsaw", () -> new Item(new Item.Properties().tab(MainSetup.ITEM_GROUP).durability(100)));
    public static final RegistryObject<Item> HANDDRILL = ITEMS.register("handdrill", () -> new Item(new Item.Properties().tab(MainSetup.ITEM_GROUP).durability(100)));
    public static final RegistryObject<Item> SCISSORS = ITEMS.register("scissors", () -> new Item(new Item.Properties().tab(MainSetup.ITEM_GROUP).durability(100)));
    public static final RegistryObject<Item> KNIFE = ITEMS.register("crude_knife", () -> new CrudeKnife(new Item.Properties().tab(MainSetup.ITEM_GROUP).durability(100)));
    public static final RegistryObject<Item> GRATE = ITEMS.register("crude_grate", () -> new Item(new Item.Properties().tab(MainSetup.ITEM_GROUP).durability(100)));

    // Recipe serializer
    public static final RegistryObject<RecipeSerializer<UniversalWorkbenchRecipe>> UW_RECIPE_SERIALIZER = SERIALIZERS.register("universal_workbench_recipe", () -> UniversalWorkbenchRecipe.Serializer.INSTANCE);


    public static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory)
    {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void setup() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(bus);
        BLOCK_ENTITIES.register(bus);
        ITEMS.register(bus);
        SERIALIZERS.register(bus);
        MENUS.register(bus);
        LOOT_MODS.register(bus);

        /*
        BLOCK_ENTITIES.register(bus);
        CONTAINERS.register(bus);
        ENTITIES.register(bus);
        STRUCTURES.register(bus);
        BIOME_MODIFIERS.register(bus);
        PLACED_FEATURES.register(bus);
        */
    }



}
