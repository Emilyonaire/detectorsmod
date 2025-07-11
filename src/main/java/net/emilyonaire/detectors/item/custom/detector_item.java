package net.emilyonaire.detectors.item.custom;

import net.emilyonaire.detectors.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class detector_item extends Item {
    private static final List<Block> DETECTABLE_BLOCKS = List.of(
            Blocks.DIRT,
            Blocks.GRASS_BLOCK,
            Blocks.GRAVEL,
            Blocks.SUSPICIOUS_GRAVEL,
            Blocks.SAND,
            Blocks.SUSPICIOUS_SAND
    );

    private static final List<Block> TIER_1_BLOCKS = List.of(
            Blocks.COAL_ORE,
            Blocks.DEEPSLATE_COAL_ORE,
            Blocks.COPPER_ORE,
            Blocks.DEEPSLATE_COPPER_ORE,
            Blocks.IRON_ORE,
            Blocks.DEEPSLATE_IRON_ORE
    );
    private static final List<Block> TIER_2_BLOCKS = List.of(
            Blocks.GOLD_ORE,
            Blocks.DEEPSLATE_GOLD_ORE,
            Blocks.REDSTONE_ORE,
            Blocks.DEEPSLATE_REDSTONE_ORE,
            Blocks.LAPIS_ORE,
            Blocks.DEEPSLATE_LAPIS_ORE,
            Blocks.NETHER_QUARTZ_ORE
    );
    private static final List<Block> TIER_3_BLOCKS = List.of(
            Blocks.DIAMOND_ORE,
            Blocks.DEEPSLATE_DIAMOND_ORE,
            Blocks.EMERALD_ORE,
            Blocks.DEEPSLATE_EMERALD_ORE,
            Blocks.NETHER_GOLD_ORE,
            Blocks.IRON_BLOCK,
            Blocks.COPPER_BLOCK,
            Blocks.LAPIS_BLOCK,
            Blocks.REDSTONE_BLOCK
    );
    private static final List<Block> TIER_4_BLOCKS = List.of(
            Blocks.ANCIENT_DEBRIS,
            Blocks.DIAMOND_BLOCK,
            Blocks.EMERALD_BLOCK,
            Blocks.NETHERITE_BLOCK,
            Blocks.GILDED_BLACKSTONE,
            Blocks.CHEST,
            Blocks.TRAPPED_CHEST,
            Blocks.BARREL,
            Blocks.ENDER_CHEST
    );
    private static final List<Block> IGNORABLES = List.of(
            Blocks.SHORT_GRASS,
            Blocks.TALL_GRASS,
            Blocks.FERN,
            Blocks.LARGE_FERN,
            Blocks.POPPY,
            Blocks.DANDELION,
            Blocks.BLUE_ORCHID,
            Blocks.ALLIUM,
            Blocks.AZURE_BLUET,
            Blocks.RED_TULIP,
            Blocks.ORANGE_TULIP,
            Blocks.WHITE_TULIP,
            Blocks.PINK_TULIP,
            Blocks.OXEYE_DAISY,
            Blocks.CORNFLOWER,
            Blocks.LILY_OF_THE_VALLEY,
            Blocks.SUNFLOWER,
            Blocks.LILAC,
            Blocks.ROSE_BUSH,
            Blocks.PEONY,
            Blocks.VINE,
            Blocks.SWEET_BERRY_BUSH,
            Blocks.CACTUS,
            Blocks.BROWN_MUSHROOM,
            Blocks.RED_MUSHROOM,
            Blocks.MELON_STEM,
            Blocks.PUMPKIN_STEM,
            Blocks.SUGAR_CANE,
            Blocks.BAMBOO,
            Blocks.COBWEB,
            Blocks.SNOW,
            Blocks.SNOW_BLOCK,
            Blocks.DEAD_BUSH
    );


    public detector_item(Properties properties) {
        super(properties);
    }

    // You can add custom methods or properties here if needed
    // For example, you might want to add a method to detect something
    // or to provide additional functionality specific to this item.

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        Block clickedBlock = level.getBlockState(pContext.getClickedPos()).getBlock();

        //log the block that was clicked
        System.out.println("You clicked on: " + clickedBlock.getName().getString());

        // Check if the clicked block is in the list of detectable blocks
        if (DETECTABLE_BLOCKS.contains(clickedBlock)) {
            if(!level.isClientSide()){
                // If the block is detectable, log a message AS TITLE TOAST THING THAT POPS UP ABOVE HOTBAR
                        System.out.println("Detected: " + clickedBlock.getName().getString());

                        pContext.getItemInHand().hurtAndBreak(1, ((ServerLevel) level), ((ServerPlayer) pContext.getPlayer()),
                                item -> pContext.getPlayer().onEquippedItemBroken(item, EquipmentSlot.MAINHAND));

//                        level.playSound(null, pContext.getClickedPos(), SoundEvents.GRAVEL_STEP, SoundSource.BLOCKS);

                        //WE NOW DO STUFF HERE!!
                        //if we have clicked on an ignorable, act as if we clicked the block below it, if that is also ignorable, keep going down until we find a block that is not ignorable. STOP AT AIR OR WATER BLOCKS.

                        // If clicked block is ignorable, go down until a non-ignorable, non-air, non-water block is found (or reach bottom)
                        Block actualBlock = clickedBlock;
                        BlockPos pos = pContext.getClickedPos();
                        while (IGNORABLES.contains(actualBlock) && pos.getY() > 0) {
                            pos = pos.below();
                            actualBlock = level.getBlockState(pos).getBlock();
                            System.out.println("Checking below: " + actualBlock.getName().getString());
                        }

                        //GET THE TIER OF OUR TOOL. (THIS IS HANDLED USING THE ITEM'S RARITY, WHICH IS SET IN THE ITEM'S PROPERTIES)
                        int tier = pContext.getItemInHand().getRarity().ordinal();
                        // SCAN SURROUNDING BLOCKS FOR THE BLOCKS WE WANT TO DETECT!
                        //scan all up to our teir. (if teir 3, scan for tiers 1 2 and 3, but not 4)

                        // Create a list of blocks to detect based on the tier, where we include all blocks up to the current tier
                        List<Block> blocksToDetect = switch (tier) {
                            case 0 -> TIER_1_BLOCKS;
                            case 1 -> {
                                List<Block> list = new java.util.ArrayList<>(TIER_1_BLOCKS);
                                list.addAll(TIER_2_BLOCKS);
                                yield list;
                            }
                            case 2 -> {
                                List<Block> list = new java.util.ArrayList<>(TIER_1_BLOCKS);
                                list.addAll(TIER_2_BLOCKS);
                                list.addAll(TIER_3_BLOCKS);
                                yield list;
                            }
                            case 3 -> {
                                List<Block> list = new java.util.ArrayList<>(TIER_1_BLOCKS);
                                list.addAll(TIER_2_BLOCKS);
                                list.addAll(TIER_3_BLOCKS);
                                list.addAll(TIER_4_BLOCKS);
                                yield list;
                            }
                            default -> TIER_1_BLOCKS;
                        };
                        //switch for width and depth of scan based on tier also
                        int scanWidth = switch (tier) {
                            case 0 -> 1; // Tier 1: 1 block radius
                            case 1 -> 2; // Tier 2: 2 blocks radius
                            case 2 -> 3; // Tier 3: 3 blocks radius
                            case 3 -> 4; // Tier 4: 4 blocks radius
                            default -> 1; // Default to Tier 1 if something goes wrong
                        };
                        int scanDepth = switch (tier) {
                            case 0 -> 8; // Tier 1: 1 block depth
                            case 1 -> 16; // Tier 2: 2 blocks depth
                            case 2 -> 32; // Tier 3: 3 blocks depth
                            case 3 -> 64; // Tier 4: 4 blocks depth
                            default -> 1; // Default to Tier 1 if something goes wrong
                        };
                        //scan the surrounding blocks for the blocks we want to detect, SCAN EVERY TIER UP TO OUR TIER
                        int detectedCount = 0;
                        for (int x = -scanWidth; x <= scanWidth; x++) {
                            for (int y = -scanDepth; y <= scanDepth; y++) {
                                for (int z = -scanWidth; z <= scanWidth; z++) {
                                    if (x == 0 && y == 0 && z == 0) continue; // Skip the clicked block itself
                                    Block blockToCheck = level.getBlockState(pos.offset(x, y, z)).getBlock();
                                    if (blocksToDetect.contains(blockToCheck)) {
                                        detectedCount++;
                                        System.out.println("Detected: " + blockToCheck.getName().getString());
                                    }
                                }
                            }
                        }

                        // If we detected any blocks, send a message to the player
                        if (detectedCount > 0) {
                            String message = "Detected " + detectedCount + " blocks of interest around you!";
                            System.out.println(message);
                            // Optionally, you can play a sound or give some feedback
                            level.playSound(null, pContext.getClickedPos(), ModSounds.DETECTOR_SUCCESS.get(), SoundSource.PLAYERS);

                        } else {
                            System.out.println("No blocks of interest detected around you.");
                            //play bad sound
                            level.playSound(null, pContext.getClickedPos(), ModSounds.DETECTOR_FAILURE.get(), SoundSource.PLAYERS);
                        }







            }
        }


        return super.useOn(pContext);
    }
}
