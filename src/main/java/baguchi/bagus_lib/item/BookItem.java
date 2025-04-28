package baguchi.bagus_lib.item;

import baguchi.bagus_lib.client.ClientEventHandler;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class BookItem extends Item {
    public BookItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResult use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        if (p_41432_.isClientSide()) {
            ClientEventHandler.handleOpenPageTest(p_41433_);
        }

        return super.use(p_41432_, p_41433_, p_41434_);
    }
}
