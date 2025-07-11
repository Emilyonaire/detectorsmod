package net.emilyonaire.detectors.item;

import net.emilyonaire.detectors.DetectorsMod;
import net.emilyonaire.detectors.item.custom.detector_item;
import net.emilyonaire.detectors.sound.ModSounds;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.JukeboxPlayable;
import net.minecraft.world.item.Rarity;
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

    public static final RegistryObject<Item> GOLD_DETECTOR = ITEMS.register("gold_detector",
            () -> new detector_item(new Item.Properties()
                    .durability(64)
                    .rarity(net.minecraft.world.item.Rarity.UNCOMMON)
            ));
    public static final RegistryObject<Item> DIAMOND_DETECTOR = ITEMS.register("diamond_detector",
            () -> new detector_item(new Item.Properties()
                    .durability(128)
                    .rarity(net.minecraft.world.item.Rarity.RARE)
            ));
    public static final RegistryObject<Item> NETHERITE_DETECTOR = ITEMS.register("netherite_detector",
            () -> new detector_item(new Item.Properties()
                    .durability(512)
                    .rarity(net.minecraft.world.item.Rarity.EPIC)
            ));
    public static final RegistryObject<Item> DETECTORISTS_MUSIC_DISC = ITEMS.register("detectorists_music_disc",
            () -> new Item(new Item.Properties().jukeboxPlayable(ModSounds.MUSIC_DETECTORISTS_KEY).stacksTo(1).rarity(Rarity.RARE)));




    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }

}
