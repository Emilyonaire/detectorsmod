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
    //list of player made blocks. (Chest, crafting table, furnace, etc.)
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> PLAYER_MADE_BLOCKS = BUILDER
            .comment("A list of blocks that are made by players and should be scanned.")
            .defineListAllowEmpty("playerMadeBlocks", List.of(
                    "minecraft:chest",
                    "minecraft:crafting_table",
                    "minecraft:furnace",
                    "minecraft:blast_furnace",
                    "minecraft:smoker",
                    "minecraft:anvil",
                    "minecraft:enchanting_table",
                    "minecraft:brewing_stand"
            ), Config::validateItemName);

    //individual ints for t1 width, t2 width, t3 width, t4 width, and t1 depth, t2 depth, t3 depth, t4 depth
    private static final ForgeConfigSpec.IntValue TIER_1_WIDTH = BUILDER
            .comment("The width of the tier 1 detector.")
            .defineInRange("tier1Width", 1, 1, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.IntValue TIER_2_WIDTH = BUILDER
            .comment("The width of the tier 2 detector.")
            .defineInRange("tier2Width", 2, 1, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.IntValue TIER_3_WIDTH = BUILDER
            .comment("The width of the tier 3 detector.")
            .defineInRange("tier3Width", 3, 1, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.IntValue TIER_4_WIDTH = BUILDER
            .comment("The width of the tier 4 detector.")
            .defineInRange("tier4Width", 4, 1, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue TIER_1_DEPTH = BUILDER
            .comment("The depth of the tier 1 detector.")
            .defineInRange("tier1Depth", 8, 1, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.IntValue TIER_2_DEPTH = BUILDER
            .comment("The depth of the tier 2 detector.")
            .defineInRange("tier2Depth", 16, 1, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.IntValue TIER_3_DEPTH = BUILDER
            .comment("The depth of the tier 3 detector.")
            .defineInRange("tier3Depth", 32, 1, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.IntValue TIER_4_DEPTH = BUILDER
            .comment("The depth of the tier 4 detector.")
            .defineInRange("tier4Depth", 64, 1, Integer.MAX_VALUE);



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
    //Boolean to determine if we enable scanning player made blocks.
    private static final ForgeConfigSpec.BooleanValue ENABLE_PLAYER_MADE_BLOCKS = BUILDER
            .comment("Whether to enable scanning player made blocks.")
            .define("enablePlayerMadeBlocks", true);

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
    public static List<String> playerMadeBlocks;
    public static int tier1Width;
    public static int tier2Width;
    public static int tier3Width;
    public static int tier4Width;
    public static int tier1Depth;
    public static int tier2Depth;
    public static int tier3Depth;
    public static int tier4Depth;
    public static boolean enablePlayerMadeBlocks;



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
        playerMadeBlocks = PLAYER_MADE_BLOCKS.get().stream().map(s -> (String) s).toList();

        //boolean for player made blocks
        enablePlayerMadeBlocks = ENABLE_PLAYER_MADE_BLOCKS.get();

        //widths and depths
        tier1Width = TIER_1_WIDTH.get();
        tier2Width = TIER_2_WIDTH.get();
        tier3Width = TIER_3_WIDTH.get();
        tier4Width = TIER_4_WIDTH.get();
        tier1Depth = TIER_1_DEPTH.get();
        tier2Depth = TIER_2_DEPTH.get();
        tier3Depth = TIER_3_DEPTH.get();
        tier4Depth = TIER_4_DEPTH.get();









        // convert the list of strings into a set of items
        items = ITEM_STRINGS.get().stream()
                .map(itemName -> ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(itemName)))
                .collect(Collectors.toSet());


    }
}
