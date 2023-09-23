package com.cwelth.harderworkbench.setup;

import com.cwelth.harderworkbench.ModMain;
import com.cwelth.harderworkbench.screens.CrudeWorkbenchScreen;
import com.cwelth.harderworkbench.screens.UniversalWorkbenchScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ModMain.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventHandlersMod {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        MenuScreens.register(Registries.UNIVERSAL_WORKBENCH_MENU.get(), UniversalWorkbenchScreen::new);
        MenuScreens.register(Registries.CRUDE_WORKBENCH_MENU.get(), CrudeWorkbenchScreen::new);
    }
}
