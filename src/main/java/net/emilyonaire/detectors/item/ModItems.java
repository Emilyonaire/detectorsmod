package net.emilyonaire.detectors.item;

import net.emilyonaire.detectors.DetectorsMod;
import net.emilyonaire.detectors.item.custom.detector_item;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, DetectorsMod.MOD_ID);

    //CAPS name is code name, used in code, whereas, the name we use when registering VVVVV is used in the mod's LANG files.
    public static final RegistryObject<Item> COPPER_COIL = ITEMS.register("copper_coil",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> HANDHELD_DETECTOR = ITEMS.register("handheld_detector",
            () -> new detector_item(new Item.Properties()
                    .durability(32)

            ));




    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }

}
