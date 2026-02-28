package net.stras.dimmablelamps.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.stras.dimmablelamps.DimmableLamps;
import net.stras.dimmablelamps.block.ModBlocks;

public class ModCreativeModeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DimmableLamps.MOD_ID);

    public static final RegistryObject<CreativeModeTab> DIMMABLE_LAMPS =
            CREATIVE_MODE_TABS.register("dimmablelamps", () ->
                    CreativeModeTab.builder()
                            .title(Component.translatable("itemGroup.dimmablelamps"))
                            .icon(() -> new ItemStack(ModBlocks.LAMP_BLOCK.get()))
                            .displayItems((parameters, output) -> {
                                output.accept(ModBlocks.LAMP_BLOCK.get());
                                output.accept(ModItems.WRENCH.get());
                            })
                            .build()
            );
}
