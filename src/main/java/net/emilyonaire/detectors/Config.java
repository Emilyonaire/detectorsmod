package net.emilyonaire.detectors;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = DetectorsMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

//    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> TIER_1_BLOCKS;

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> TIER_1_BLOCKS = BUILDER
            .comment("A list of tier 1 blocks to detect.")
            .defineListAllowEmpty("tier1Blocks", List.of(
                    "minecraft:coal_ore",
                    "minecraft:deepslate_coal_ore",
                    "minecraft:copper_ore",
                    "minecraft:deepslate_copper_ore",
                    "minecraft:iron_ore",
                    "minecraft:deepslate_iron_ore"
            ), Config::validateItemName);

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> TIER_2_BLOCKS = BUILDER
            .comment("A list of tier 2 blocks to detect.")
            .defineListAllowEmpty("tier2Blocks", List.of(
                    "minecraft:gold_ore",
                    "minecraft:deepslate_gold_ore",
                    "minecraft:redstone_ore",
                    "minecraft:deepslate_redstone_ore",
                    "minecraft:lapis_ore",
                    "minecraft:deepslate_lapis_ore",
                    "minecraft:nether_quartz_ore"
            ), Config::validateItemName);

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> TIER_3_BLOCKS = BUILDER
            .comment("A list of tier 3 blocks to detect.")
            .defineListAllowEmpty("tier3Blocks", List.of(
                    "minecraft:diamond_ore",
                    "minecraft:deepslate_diamond_ore",
                    "minecraft:emerald_ore",
                    "minecraft:deepslate_emerald_ore",
                    "minecraft:nether_gold_ore",
                    "minecraft:iron_block",
                    "minecraft:copper_block",
                    "minecraft:lapis_block",
                    "minecraft:redstone_block"
            ), Config::validateItemName);

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> TIER_4_BLOCKS = BUILDER
            .comment("A list of tier 4 blocks to detect.")
            .defineListAllowEmpty("tier4Blocks", List.of(
                    "minecraft:ancient_debris",
                    "minecraft:diamond_block",
                    "minecraft:emerald_block",
                    "minecraft:netherite_block",
                    "minecraft:gilded_blackstone",
                    "minecraft:chest",
                    "minecraft:trapped_chest",
                    "minecraft:barrel",
                    "minecraft:ender_chest"
            ), Config::validateItemName);

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> IGNORABLE_BLOCKS = BUILDER
            .comment("A list of blocks that should be ignored by the detector.")
            .defineListAllowEmpty("ignorableBlocks", List.of(
                    "minecraft:short_grass",
                    "minecraft:tall_grass",
                    "minecraft:fern",
                    "minecraft:large_fern",
                    "minecraft:poppy",
                    "minecraft:dandelion",
                    "minecraft:blue_orchid",
                    "minecraft:allium",
                    "minecraft:azure_bluet",
                    "minecraft:red_tulip",
                    "minecraft:orange_tulip",
                    "minecraft:white_tulip",
                    "minecraft:pink_tulip",
                    "minecraft:oxeye_daisy",
                    "minecraft:cornflower",
                    "minecraft:lily_of_the_valley",
                    "minecraft:sunflower",
                    "minecraft:lilac",
                    "minecraft:rose_bush",
                    "minecraft:peony",
                    "minecraft:vine",
                    "minecraft:sweet_berry_bush",
                    "minecraft:cactus",
                    "minecraft:brown_mushroom",
                    "minecraft:red_mushroom",
                    "minecraft:melon_stem",
                    "minecraft:pumpkin_stem",
                    "minecraft:sugar_cane",
                    "minecraft:bamboo",
                    "minecraft:cobweb",
                    "minecraft:snow",
                    "minecraft:snow_block",
                    "minecraft:dead_bush"
            ), Config::validateItemName);

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> DETECTABLE_BLOCKS = BUILDER
            .comment("A list of blocks that the detector can detect when clicked.")
            .defineListAllowEmpty("detectableBlocks", List.of(
                    "minecraft:dirt",
                    "minecraft:grass_block",
                    "minecraft:gravel",
                    "minecraft:suspicious_gravel",
                    "minecraft:sand",
                    "minecraft:suspicious_sand"
            ), Config::validateItemName);


    private static final ForgeConfigSpec.BooleanValue LOG_DIRT_BLOCK = BUILDER
            .comment("Whether to log the dirt block on common setup")
            .define("logDirtBlock", true);

    private static final ForgeConfigSpec.IntValue MAGIC_NUMBER = BUILDER
            .comment("A magic number")
            .defineInRange("magicNumber", 42, 0, Integer.MAX_VALUE);

    public static final ForgeConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER
            .comment("What you want the introduction message to be for the magic number")
            .define("magicNumberIntroduction", "The magic number is... ");

    // a list of strings that are treated as resource locations for items
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
            .comment("A list of items to log on common setup.")
            .defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), Config::validateItemName);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean logDirtBlock;
    public static int magicNumber;
    public static String magicNumberIntroduction;
    public static Set<Item> items;
    public static List<String> tier1Blocks;
    public static List<String> tier2Blocks;
    public static List<String> tier3Blocks;
    public static List<String> tier4Blocks;
    public static List<String> ignorableBlocks;
    public static List<String> detectableBlocks;

    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(ResourceLocation.tryParse(itemName));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        logDirtBlock = LOG_DIRT_BLOCK.get();
        magicNumber = MAGIC_NUMBER.get();
        magicNumberIntroduction = MAGIC_NUMBER_INTRODUCTION.get();

        //lists
        tier1Blocks = TIER_1_BLOCKS.get().stream().map(s -> (String) s).toList();
        tier2Blocks = TIER_2_BLOCKS.get().stream().map(s -> (String) s).toList();
        tier3Blocks = TIER_3_BLOCKS.get().stream().map(s -> (String) s).toList();
        tier4Blocks = TIER_4_BLOCKS.get().stream().map(s -> (String) s).toList();
        ignorableBlocks = IGNORABLE_BLOCKS.get().stream().map(s -> (String) s).toList();
        detectableBlocks = DETECTABLE_BLOCKS.get().stream().map(s -> (String) s).toList();








        // convert the list of strings into a set of items
        items = ITEM_STRINGS.get().stream()
                .map(itemName -> ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(itemName)))
                .collect(Collectors.toSet());


    }
}
