package uid.infinity.shampoo.features.modules.player;

import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.util.InteractionUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;

public class SpeedMine extends Module {
    public SpeedMine() {
        super("SpeedMine", "Auto mines everything", Category.PLAYER, true,false,false);
    }
    @Override public void onUpdate() {
        if (fullNullCheck()) return;
        BlockPos pos = ((BlockHitResult) mc.crosshairTarget).getBlockPos();
        if (mc.options.attackKey.isPressed()) {
            InteractionUtil.breakBlock(pos);
        }
    }
}