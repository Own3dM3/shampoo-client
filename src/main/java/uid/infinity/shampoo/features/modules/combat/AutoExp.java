package uid.infinity.shampoo.features.modules.combat;

import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.util.InventoryUtil;
import net.minecraft.item.ExperienceBottleItem;
import net.minecraft.util.Hand;

public class AutoExp extends Module {

    public AutoExp() {
        super("AutoExp", "Fast exp", Category.COMBAT, true, false, false);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;
        int oldslot = mc.player.getInventory().getSelectedSlot();
        int slot = InventoryUtil.findHotbarItem(ExperienceBottleItem.class);
        if (slot != -1)  {
            InventoryUtil.switchSlot(slot);
            mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
            InventoryUtil.switchSlot(oldslot);
        }
    }
}
