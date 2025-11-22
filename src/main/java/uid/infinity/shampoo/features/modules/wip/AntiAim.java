package uid.infinity.shampoo.features.modules.wip;

import uid.infinity.shampoo.features.modules.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import uid.infinity.shampoo.features.settings.*;

public class AntiAim extends Module {

    public enum Mode {
        OFF,
        SPIN,
        JITTER,
        BACKWARDS,
        RANDOM_SPIN
    }

    public Setting<Mode> mode = register(new Setting<>("Mode", Mode.BACKWARDS));
    public Setting<Boolean> onlyHead = register(new Setting<>("OnlyHead", false));
    public Setting<Boolean> always180 = register(new Setting<>("Always180", true));

    public Setting<Integer> spinSpeed = register(new Setting<>("SpinSpeed", 8, 1, 360));
    public Setting<Boolean> smoothSpin = register(new Setting<>("SmoothSpin", false));

    public Setting<Integer> jitterAmplitude = register(new Setting<>("JitterAmp", 25, 5, 60));
    public Setting<Integer> jitterSpeed = register(new Setting<>("JitterSpeed", 6, 1, 20));

    public Setting<Integer> randomSpeed = register(new Setting<>("RandomSpeed", 100, 10, 500));
    public Setting<Integer> randomAngle = register(new Setting<>("RandomAngle", 180, 30, 360));

    private float rotation = 0f;
    private boolean jitterDirection = true;
    private int jitterTimer = 0;
    private long lastRandomChange = 0;
    private float randomTarget = 0f;
    private boolean registered = false;

    public AntiAim() {
        super("AntiAim", "Confuse aimbots by rotating your model", Category.WIP, true, false, false);
    }

    @Override
    public void onEnable() {
        if (!registered) {
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                if (this.isEnabled() && client.player != null && client.world != null) {
                    applyAntiAim(client.player);
                }
            });
            registered = true;
        }
    }

    private void applyAntiAim(ClientPlayerEntity player) {
        Mode currentMode = mode.getValue();
        if (currentMode == Mode.OFF) return;

        float baseYaw = player.getYaw();
        float targetYaw = baseYaw;

        switch (currentMode) {
            case BACKWARDS -> {
                targetYaw = baseYaw + 180.0f;
            }

            case SPIN -> {
                if (smoothSpin.getValue()) {
                    rotation += spinSpeed.getValue();
                } else {
                    rotation = (rotation + spinSpeed.getValue()) % 360.0f;
                }
                targetYaw = rotation;
            }

            case JITTER -> {
                jitterTimer++;
                int ticksPerFlip = Math.max(1, 20 / jitterSpeed.getValue());
                if (jitterTimer >= ticksPerFlip) {
                    jitterDirection = !jitterDirection;
                    jitterTimer = 0;
                }
                float offset = jitterDirection ? jitterAmplitude.getValue() : -jitterAmplitude.getValue();
                targetYaw = baseYaw + offset;
            }

            case RANDOM_SPIN -> {
                long now = System.currentTimeMillis();
                if (now - lastRandomChange > randomSpeed.getValue()) {

                    float angleRange = randomAngle.getValue();
                    randomTarget = (float) (Math.random() * angleRange);
                    lastRandomChange = now;
                }
                targetYaw = randomTarget;
            }

            default -> targetYaw = baseYaw;
        }

        if (always180.getValue() && currentMode != Mode.BACKWARDS) {
            targetYaw += 180.0f;
        }

        targetYaw = (targetYaw % 360.0f + 360.0f) % 360.0f;

        if (onlyHead.getValue()) {
            player.headYaw = targetYaw;

        } else {
            player.bodyYaw = targetYaw;
            player.headYaw = targetYaw;
        }
    }
}