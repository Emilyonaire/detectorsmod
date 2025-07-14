package net.emilyonaire.detectors.item.custom;


import net.emilyonaire.detectors.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.emilyonaire.detectors.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class detector_item extends Item {

    // Helper to convert a list of block registry names (strings) to a set of Block instances
    private static Set<Block> getBlocks(List<String> blockNames) {
        return blockNames.stream()
                .map(name -> ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryParse(name)))
                .filter(block -> block != null && block != Blocks.AIR)
                .collect(Collectors.toSet());
    }


    public detector_item(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {

        System.out.println("Used! Logging config stuff...");
        //log all the config values
        System.out.println("Tier 1 Blocks: " + Config.tier1Blocks);
        System.out.println("Tier 2 Blocks: " + Config.tier2Blocks);
        System.out.println("Tier 3 Blocks: " + Config.tier3Blocks);
        System.out.println("Tier 4 Blocks: " + Config.tier4Blocks);
        System.out.println("Detectable Blocks: " + Config.detectableBlocks);
        System.out.println("Ignorable Blocks: " + Config.ignorableBlocks);



        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        Block clickedBlock = level.getBlockState(clickedPos).getBlock();



        // Only react if this is a detectable block
        if (ConvertToBlocks(Config.detectableBlocks).contains(clickedBlock)) {
            if (!level.isClientSide()) {
                // If ignorable, look below
                Block actualBlock = clickedBlock;
                BlockPos pos = clickedPos;
                while (ConvertToBlocks(Config.ignorableBlocks).contains(actualBlock) && pos.getY() > 0) {
                    pos = pos.below();
                    actualBlock = level.getBlockState(pos).getBlock();
                    System.out.println("Checking below: " + actualBlock.getName().getString());
                }

                // Determine tier by rarity
                int tier = context.getItemInHand().getRarity().ordinal();
                System.out.println("Detector tier: " + tier);

                List<String> playerBlocksNames = Config.playerMadeBlocks;

                List<Block> playerBlocks = ConvertToBlocks(playerBlocksNames);

                // Get the combined block set up to our tier
                List<String> blockNamesToDetect = switch (tier) {
                    case 0 -> Config.tier1Blocks;
                    case 1 -> Combine(Config.tier1Blocks, Config.tier2Blocks);
                    case 2 -> Combine(Config.tier1Blocks, Config.tier2Blocks, Config.tier3Blocks);
                    case 3 -> Combine(Config.tier1Blocks, Config.tier2Blocks, Config.tier3Blocks, Config.tier4Blocks);
                    default -> Config.tier1Blocks; // Fallback to tier 1
                };
                // Log the blocks we are looking for
                System.out.println("Looking for blocks: " + blockNamesToDetect);

                //Convert to blocks.
                Set<Block> blocksToDetect = getBlocks(blockNamesToDetect);

                // Determine scan size
                int scanWidth = switch (tier) {
                    case 0 -> Config.tier1Width;
                    case 1 -> Config.tier2Width;
                    case 2 -> Config.tier3Width;
                    case 3 -> Config.tier4Width;
                    default -> Config.tier1Width;
                };
                int scanDepth = switch (tier) {
                    case 0 -> Config.tier1Depth;
                    case 1 -> Config.tier2Depth;
                    case 2 -> Config.tier3Depth;
                    case 3 -> Config.tier4Depth;
                    default -> Config.tier1Depth;
                };

                // Consume durability
                context.getItemInHand().hurtAndBreak(1, ((ServerLevel)level), ((ServerPlayer) context.getPlayer()),
                        item -> context.getPlayer().onEquippedItemBroken(item, EquipmentSlot.MAINHAND));

                // Scan surrounding blocks
                int detectedCount = 0;
                for (int x = -scanWidth; x <= scanWidth; x++) {
                    for (int y = -scanDepth; y <= scanDepth; y++) {
                        for (int z = -scanWidth; z <= scanWidth; z++) {
                            if (x == 0 && y == 0 && z == 0) continue;
                            BlockPos checkPos = pos.offset(x, y, z);
                            Block foundBlock = level.getBlockState(checkPos).getBlock();
                            if (blocksToDetect.contains(foundBlock)) {
                                if(playerBlocks.contains(foundBlock)) {
                                    System.out.println("Found player block: " + foundBlock.getName().getString() + " at " + checkPos);
                                    if(!Config.enablePlayerMadeBlocks){
                                        System.out.println("We have player blocks disabled. Continuing without action.");
                                        continue;
                                    }
                                } else {
                                    //do nothing, continue as normal
                                }
                                detectedCount++;
                                System.out.println("Detected: " + foundBlock.getName().getString());
                            }
                        }
                    }
                }

                if (detectedCount > 0) {
                    String message = "Detected " + detectedCount + " block(s) of interest!";
                    System.out.println(message);
                    level.playSound(null, pos, ModSounds.DETECTOR_SUCCESS.get(), SoundSource.PLAYERS);
                } else {
                    System.out.println("No blocks of interest detected.");
                    level.playSound(null, pos, ModSounds.DETECTOR_FAILURE.get(), SoundSource.PLAYERS);
                }
            }
        }

        return super.useOn(context);
    }

    // Utility to merge multiple lists
    //combine list of strings into a single list of strings
    private List<String> Combine(List<String>... lists) {
        List<String> combined = new ArrayList<>();
        for (List<String> list : lists) {
            combined.addAll(list);
        }
        return combined.stream().distinct().collect(Collectors.toList());
    }

    //utility to convert list of strings to a list of blocks
    private List<Block> ConvertToBlocks(List<String> blockNames) {

        System.out.println(
                "Converting block names to blocks: " + blockNames.stream().collect(Collectors.joining(", "))

        );

        return blockNames.stream()
                .map(name -> ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryParse(name)))
                .filter(block -> block != null && block != Blocks.AIR)
                .collect(Collectors.toList());
    }

    //utility to convert single string to block.
    private Block ConvertToBlock(String blockName) {
        System.out.println("Converting block name to block: " + blockName);
        return ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryParse(blockName));
    }

}