package uid.infinity.shampoo.features.modules.movement;

import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.util.math.MathHelper;

public class TickShift extends Module {

    private final Setting<Integer> maxCharge = register(new Setting<>("MaxCharge", 20, 1, 40));
    private final Setting<Integer> drainSpeed = register(new Setting<>("DrainSpeed", 2, 1, 5));
    private final Setting<Integer> chargeSpeed = register(new Setting<>("ChargeSpeed", 1, 1, 5));
    private final Setting<Float> boostFactor = register(new Setting<>("BoostFactor", 1.5f, 1.0f, 3.0f));

    private int charge = 0;

    public TickShift() {
        super("TickShift", "Boosts speed using stored charge", Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onEnable() {
        charge = 0;
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!this.isEnabled() || client.player == null || client.world == null) return;

            boolean isMoving = (false);
            boolean onGround = client.player.isOnGround();

            if (isMoving && onGround) {

                if (charge > 0) {
                    charge -= drainSpeed.getValue();
                    if (charge < 0) charge = 0;

                    float boost = 1.0f + (charge * boostFactor.getValue() * 0.05f);
                    boost = MathHelper.clamp(boost, 1.0f, 3.0f);

                    double motionX = client.player.getVelocity().x * boost;
                    double motionZ = client.player.getVelocity().z * boost;

                    client.player.setVelocity(motionX, client.player.getVelocity().y, motionZ);
                }
            } else if (!isMoving && onGround) {

                charge += chargeSpeed.getValue();
                if (charge > maxCharge.getValue()) {
                    charge = maxCharge.getValue();
                }
            }

        });
    }

    @Override
    public String getDisplayInfo() {
        return String.valueOf(charge);
    }
}