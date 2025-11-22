package uid.infinity.shampoo.features.modules.movement;

import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;

public class AutoWalk extends Module {
    private final Setting<Boolean> autoJump = this.register(new Setting<>("AutoJump", false));

    public AutoWalk() {
        super("AutoWalk", "Automatically walks forward (and optionally jumps)", Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onUpdate() {
        mc.options.forwardKey.setPressed(true);
        if (autoJump.getValue())
        mc.options.jumpKey.setPressed(true);
    }
    @Override
    public void onDisable() {
        mc.options.forwardKey.setPressed(false);
        mc.options.jumpKey.setPressed(false);

    }
}