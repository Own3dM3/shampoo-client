package uid.infinity.shampoo.features.modules.movement;

import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;

public class Sprint extends Module {
    public Setting<Integer> motion = num("Motion", 1, 0, 1);

    public Sprint() {
        super("Sprint", "Auto sprint", Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;
        if (!mc.player.horizontalCollision && mc.player.forwardSpeed > 0 && !mc.player.isSneaking() && !mc.player.isUsingItem()) {
            mc.player.setSprinting(true);

        }
    }
}