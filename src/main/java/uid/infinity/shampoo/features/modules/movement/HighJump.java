package uid.infinity.shampoo.features.modules.movement;

import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;
import net.minecraft.entity.player.PlayerEntity;

public class HighJump extends Module {
    private final Setting<Float> height = register(new Setting<>("Height", 1.0f, 0.5f, 3.0f));

    public HighJump() {
        super("HighJump", "Increases your jump height when jumping", Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (mc.player.isOnGround() && mc.options.jumpKey.isPressed()) {
            PlayerEntity player = mc.player;
            player.setVelocity(player.getVelocity().x, height.getValue(), player.getVelocity().z);
        }
    }
}