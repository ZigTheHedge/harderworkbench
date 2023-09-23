package com.cwelth.harderworkbench.setup;

import com.cwelth.harderworkbench.Config;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.loading.FMLPaths;

import static com.cwelth.harderworkbench.ModMain.MODID;

public class MainSetup {
    public static final String TAB_NAME = "Harder Workbench";
    public static final CreativeModeTab ITEM_GROUP = new CreativeModeTab(TAB_NAME) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Registries.CRUDE_WORKBENCH_ITEM_BLOCK_THREE_THIRDS.get());
        }
    };

    public static void setup() {
        Config.loadConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(MODID + "-common.toml"));

        Registries.setup();
        IEventBus bus = MinecraftForge.EVENT_BUS;
        bus.register(new EventHandlersForge());

    }

}
