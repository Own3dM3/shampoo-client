package uid.infinity.shampoo.features.modules.combat;

import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.util.InteractionUtil;
import uid.infinity.shampoo.util.InventoryUtil;
import net.minecraft.block.CobwebBlock;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public class SelfWeb extends Module {

    public SelfWeb() {
        super("SelfWeb", "Web your self", Category.COMBAT, true, false, false);
    }

    @Override
    public void onTick() {
        if (fullNullCheck()) return;
        int oldslot = mc.player.getInventory().getSelectedSlot();
        int slot = InventoryUtil.findHotbarItem(CobwebBlock.class);
        BlockPos pos = mc.player.getBlockPos();
        if (slot != -1) {
            InventoryUtil.switchSlot(slot);
            InteractionUtil.placeblock(pos, false);
            mc.player.swingHand(Hand.MAIN_HAND);
            disable();
            InventoryUtil.switchSlot(oldslot);
        }
    }
}