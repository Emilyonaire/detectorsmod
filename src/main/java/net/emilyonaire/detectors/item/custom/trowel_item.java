package net.emilyonaire.detectors.item.custom;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

public class trowel_item extends Item {
    public trowel_item(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        //here is where we implement func for trowel
        //when we right click a block, log to console for now.

        if (!pContext.getLevel().isClientSide()) {
//            pContext.getPlayer().sendSystemMessage("You used the trowel on: " + pContext.getClickedPos());
            System.out.println("Trowel used on: " + pContext.getClickedPos());
        }



        return super.useOn(pContext);
    }
}
