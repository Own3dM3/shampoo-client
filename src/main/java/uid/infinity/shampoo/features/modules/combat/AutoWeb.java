package uid.infinity.shampoo.features.modules.combat;

import uid.infinity.shampoo.shampoo;
import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;
import uid.infinity.shampoo.util.InteractionUtil;
import uid.infinity.shampoo.util.InventoryUtil;
import net.minecraft.block.CobwebBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class AutoWeb extends Module {
    private final Setting<Float> range = num("Range", 4f, 1f, 6f);
    public AutoWeb() {
        super("AutoWeb", "Auto web your enemy", Category.COMBAT, true,false,false);
    }
    @Override public void onTick() {
        if (fullNullCheck()) return;
        int oldslot = mc.player.getInventory().getSelectedSlot();
        int slot = InventoryUtil.findHotbarItem(CobwebBlock.class);
        for (PlayerEntity player: mc.world.getPlayers()) {
            if (player == mc.player || shampoo.friendManager.isFriend(player.getName().getString())) continue;
            if (mc.player.distanceTo(player) <= range.getValue()) {
                BlockPos pos = player.getBlockPos();
                if (slot != -1) {
                    InventoryUtil.switchSlot(slot);
                    InteractionUtil.placeblock(pos, false);
                    InteractionUtil.placeblock(pos.up(1), false);
                    InventoryUtil.switchSlot(oldslot);
                }
            }
        }
    }
}