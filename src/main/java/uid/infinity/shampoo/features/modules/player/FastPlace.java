package uid.infinity.shampoo.features.modules.player;

import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;
import net.minecraft.item.Items;
import net.minecraft.item.BlockItem;

public class FastPlace extends Module {
    public Setting<Boolean> exp = bool("Exp", true);
    public Setting<Boolean> crystals = bool("Crystals", true);
    public Setting<Boolean> obsidian = bool("Obsidian", true);
    public Setting<Boolean> allBlocks = bool("AllBlocks", false);

    public FastPlace() {
        super("FastPlace", "Instantly places items/blocks without use cooldown", Category.PLAYER, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (nullCheck()) return;

        if (mc.player.isHolding(Items.END_CRYSTAL) && crystals.getValue()) {
            mc.itemUseCooldown = 0;
        }
        if (mc.player.isHolding(Items.OBSIDIAN) && obsidian.getValue()) {
            mc.itemUseCooldown = 0;
        }
        if (mc.player.isHolding(Items.EXPERIENCE_BOTTLE) && exp.getValue()) {
            mc.itemUseCooldown = 0;
        }

        if (allBlocks.getValue() && mc.player.getMainHandStack().getItem() instanceof BlockItem) {
            mc.itemUseCooldown = 0;
        }
    }
}