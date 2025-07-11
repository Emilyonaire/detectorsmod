package net.emilyonaire.detectors.item.custom;

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
            // Add the blocks you want to detect here
            // Example: Blocks.STONE, Blocks.DIRT, Blocks.GRASS_BLOCK

            // For demonstration, let's assume we want to detect stone and dirt
            Blocks.DIRT,
            Blocks.GRASS_BLOCK,
            Blocks.GRAVEL,
            Blocks.SUSPICIOUS_GRAVEL,
            Blocks.SAND,
            Blocks.SUSPICIOUS_SAND
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

                        level.playSound(null, pContext.getClickedPos(), SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS);




            }
        }


        return super.useOn(pContext);
    }
}
