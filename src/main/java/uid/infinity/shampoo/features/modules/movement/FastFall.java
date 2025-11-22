package uid.infinity.shampoo.features.modules.movement;

import uid.infinity.shampoo.features.modules.Module;

public class FastFall extends Module {
    public FastFall() {
        super("FastFall", "Automatically steps down from blocks", Category.MOVEMENT, true, false, false);
    }

    @Override public void onUpdate() {
        if (nullCheck()) return;
        if (mc.player.isInLava() || mc.player.isTouchingWater() || !mc.player.isOnGround() || mc.player.isInLava() || mc.player.isGliding() || mc.player.getAbilities().flying) return;
        mc.player.addVelocity(0, -1, 0);
    }
}