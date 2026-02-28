package net.stras.dimmablelamps;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.stras.dimmablelamps.block.ModBlocks;
import net.stras.dimmablelamps.item.ModCreativeModeTab;
import net.stras.dimmablelamps.item.ModItems;
import org.slf4j.Logger;

@Mod(DimmableLamps.MOD_ID)
public class DimmableLamps {

    public static final String MOD_ID = "dimmablelamps";
    private static final Logger LOGGER = LogUtils.getLogger();

    public DimmableLamps() {

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModCreativeModeTab.CREATIVE_MODE_TABS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }
}
