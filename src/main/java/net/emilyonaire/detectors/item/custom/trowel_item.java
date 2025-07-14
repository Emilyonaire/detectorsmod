package net.emilyonaire.detectors.item.custom;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class trowel_item extends Item {
    public Item[] sherdItems = new Item[]{
            Items.ANGLER_POTTERY_SHERD,
            Items.ARCHER_POTTERY_SHERD,
            Items.ARMS_UP_POTTERY_SHERD,
            Items.BLADE_POTTERY_SHERD,
            Items.BREWER_POTTERY_SHERD,
            Items.BURN_POTTERY_SHERD,
            Items.DANGER_POTTERY_SHERD,
            Items.EXPLORER_POTTERY_SHERD,
            Items.FLOW_POTTERY_SHERD,
            Items.FRIEND_POTTERY_SHERD,
            Items.GUSTER_POTTERY_SHERD,
            Items.HEART_POTTERY_SHERD,
            Items.HEARTBREAK_POTTERY_SHERD,
            Items.HOWL_POTTERY_SHERD,
            Items.MINER_POTTERY_SHERD,
            Items.MOURNER_POTTERY_SHERD,
            Items.PLENTY_POTTERY_SHERD,
            Items.PRIZE_POTTERY_SHERD,
            Items.SCRAPE_POTTERY_SHERD,
            Items.SHEAF_POTTERY_SHERD,
            Items.SHELTER_POTTERY_SHERD,
            Items.SKULL_POTTERY_SHERD,
            Items.SNORT_POTTERY_SHERD
    };
    public trowel_item(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (!pContext.getLevel().isClientSide()) {
            System.out.println("Trowel used on: " + pContext.getClickedPos());
            //chat message to player
            pContext.getPlayer().sendSystemMessage(
                    net.minecraft.network.chat.Component.translatable(
                            "message.detectorsmod.trowel_used",
                            pContext.getClickedPos().getX(),
                            pContext.getClickedPos().getY(),
                            pContext.getClickedPos().getZ()
                    ));

            Block clickedBlock = pContext.getLevel().getBlockState(pContext.getClickedPos()).getBlock();
            if (clickedBlock == Blocks.SUSPICIOUS_SAND || clickedBlock == Blocks.SUSPICIOUS_GRAVEL) {
                System.out.println("Trowel used on a block that can be troweled: " + clickedBlock);
                System.out.println("DROPPING ITEM...");

                //do loot.
                // Pick a random sherd
                int randomIndex = pContext.getLevel().getRandom().nextInt(sherdItems.length);

                // Create the ItemStack for the sherd
                Item chosenSherd = sherdItems[randomIndex];
                // Drop the sherd in the world
                pContext.getLevel().addFreshEntity(
                        new net.minecraft.world.entity.item.ItemEntity(
                                pContext.getLevel(),
                                pContext.getClickedPos().getX() + 0.5,
                                pContext.getClickedPos().getY() + 0.5,
                                pContext.getClickedPos().getZ() + 0.5,
                                new net.minecraft.world.item.ItemStack(chosenSherd)
                        )
                );



                System.out.println("Dropped a random sherd: " + chosenSherd);
                //end loot.

                // Random chance to turn block into sand or gravel
                int randomChance = pContext.getLevel().getRandom().nextInt(100);
                if (randomChance < 50) {
                    System.out.println("you're arent. die.");
                    //done this way so if we add more sus blocks, we can just add them to the if statement to revert them.
                    if (clickedBlock == Blocks.SUSPICIOUS_SAND) {
                        pContext.getLevel().setBlockAndUpdate(pContext.getClickedPos(), Blocks.SAND.defaultBlockState());
                    } else if (clickedBlock == Blocks.SUSPICIOUS_GRAVEL) {
                        pContext.getLevel().setBlockAndUpdate(pContext.getClickedPos(), Blocks.GRAVEL.defaultBlockState());
                    }
                } else {
                    System.out.println("NOT setting block, ya got lucky! you're are winner");
                }

            } else {
                System.out.println("Trowel used on a block that cannot be troweled: " + clickedBlock);
            }
        }

        return InteractionResult.SUCCESS;
    }
}
