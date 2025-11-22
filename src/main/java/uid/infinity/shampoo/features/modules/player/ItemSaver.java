package uid.infinity.shampoo.features.modules.player;

import uid.infinity.shampoo.features.modules.Module;
import net.minecraft.item.ItemStack;

public class ItemSaver extends Module {
    public ItemSaver() {
        super("ItemSaver", "Save your items", Category.PLAYER, true,false,false);
    }
    @Override public void onUpdate() {
        ItemStack tool = mc.player.getMainHandStack();
        float dur = tool.getMaxDamage() - tool.getDamage();
        int prch = (int) ((dur / (float) tool.getMaxDamage() * 100));
        if (prch <= 10) {
            mc.player.getInventory().setSelectedSlot(findSlot());
        }
    }

    public static int findSlot() {
        int i = mc.player.getInventory().getSelectedSlot();
        if (i == 8) return 7;
        if (i == 0) return 1;
        return i - 1;
    }
}
