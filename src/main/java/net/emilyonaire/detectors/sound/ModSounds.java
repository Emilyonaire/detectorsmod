package net.emilyonaire.detectors.sound;

import net.emilyonaire.detectors.DetectorsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.JukeboxSong;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, DetectorsMod.MOD_ID);

    public static final RegistryObject<SoundEvent> DETECTOR_SUCCESS = registerSoundEvent("detector_success");
    public static final RegistryObject<SoundEvent> DETECTOR_FAILURE = registerSoundEvent("detector_failure");

    public static final RegistryObject<SoundEvent> MUSIC_DETECTORISTS = registerSoundEvent("jukebox_song.music_detectorists");
    public static final ResourceKey<JukeboxSong> MUSIC_DETECTORISTS_KEY = ResourceKey.create(Registries.JUKEBOX_SONG,
            ResourceLocation.fromNamespaceAndPath(DetectorsMod.MOD_ID, "music_detectorists"));

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(DetectorsMod.MOD_ID, name)));
    }


    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);

    }
}
