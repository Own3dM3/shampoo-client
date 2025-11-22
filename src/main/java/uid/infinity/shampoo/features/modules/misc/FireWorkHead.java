package uid.infinity.shampoo.features.modules.misc;

import uid.infinity.shampoo.shampoo;
import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;
import uid.infinity.shampoo.util.InteractionUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class FireWorkHead extends Module {
    private Setting<Float> range = num("Range",5f,1f,6f);

    public FireWorkHead() {
        super("FireWorkHead", "Spawn firework on enemy heads", Category.MISC, true, false, false);
    }
    public void onTick() {
        if (nullCheck()) return;
        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof PlayerEntity player) {
                if (player == mc.player || player.isDead() || shampoo.friendManager.isFriend(player.getName().getString()) || mc.player.distanceTo(player) > range.getValue())
                    continue;
                BlockPos block = player.getBlockPos().up().up();
                InteractionUtil.place(block,true);
            }
        }
    }
}