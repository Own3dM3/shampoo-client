package uid.infinity.shampoo.features.modules.movement;

import uid.infinity.shampoo.features.modules.Module;

public class Sneak extends Module {

    public Sneak() {
        super("Sneak", "Automatically sneaks for you", Category.MOVEMENT, true, false, false);
    }
    @Override public void onTick() {
        if (!mc.options.sneakKey.isPressed()) {
        mc.options.sneakKey.setPressed(true);
        }
    }
}