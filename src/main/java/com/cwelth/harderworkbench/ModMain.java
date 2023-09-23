package com.cwelth.harderworkbench;

import com.cwelth.harderworkbench.setup.MainSetup;
import com.mojang.logging.LogUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import static net.minecraftforge.registries.ForgeRegistries.BLOCKS;

@Mod(ModMain.MODID)
public class ModMain {
    public static final String MODID = "harderworkbench";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ModMain()
    {
        MainSetup.setup();
    }
}
