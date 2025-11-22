package uid.infinity.shampoo.features.modules.misc;

import uid.infinity.shampoo.features.modules.Module;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;

public class NoBreakDelay extends Module {

    public NoBreakDelay() {
        super("NoBreakDelay", "Removes the block breaking delay", Category.MISC, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.interactionManager == null || mc.crosshairTarget == null) return;
        if (mc.crosshairTarget instanceof BlockHitResult hitResult) {
            BlockPos pos = hitResult.getBlockPos();
            mc.interactionManager.cancelBlockBreaking();
        }
    }
}