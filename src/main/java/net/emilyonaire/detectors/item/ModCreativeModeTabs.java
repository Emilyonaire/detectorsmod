package net.emilyonaire.detectors.item;

import net.emilyonaire.detectors.DetectorsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DetectorsMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> DETECTORS_ITEMS_TAB = CREATIVE_MODE_TABS.register("detectors_items_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.COPPER_COIL.get()))
                    .title(Component.translatable("creativetab.detectors.detectors_items"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.COPPER_COIL.get());
                        output.accept(ModItems.HANDHELD_DETECTOR.get());
                    })
                    .build());

    //tools.
    public static final RegistryObject<CreativeModeTab> DETECTORS_TOOLS_TAB = CREATIVE_MODE_TABS.register("detectors_tools_tab",
            () -> CreativeModeTab.builder()
                    .withTabsBefore(DETECTORS_ITEMS_TAB.getId())
                    .icon(() -> new ItemStack(ModItems.HANDHELD_DETECTOR.get()))
                    .title(Component.translatable("creativetab.detectors.detectors_tools"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.HANDHELD_DETECTOR.get());
                    })
                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);


    }
}
