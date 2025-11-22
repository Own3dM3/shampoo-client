package uid.infinity.shampoo.features.modules.wip;

import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;

public class Rotations extends Module {

    private final Setting<Float> preserveTicks = register(new Setting<>("PreserveTicks", 10.0f, 0.0f, 20.0f));
    private final Setting<Boolean> movementFix = register(new Setting<>("MovementFix", false));

    private float prevYaw;

    public Rotations() {
        super("Rotations", "Manages client rotations for smoother aim and anti-cheat compatibility", Category.WIP, false, false, false);
    }

    public boolean isMovementFix() {
        return movementFix.getValue();
    }

    public float getPreserveTicks() {
        return preserveTicks.getValue();
    }

    public void setPrevYaw(float yaw) {
        this.prevYaw = yaw;
    }

    public float getPrevYaw() {
        return this.prevYaw;
    }
}