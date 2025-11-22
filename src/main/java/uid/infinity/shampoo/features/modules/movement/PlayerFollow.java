package uid.infinity.shampoo.features.modules.movement;

import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
// TODO ОН НЕ ПРЫГАЕТ И НЕ ХОДИТ САМ, ФИКСАНУТЬ
public class PlayerFollow extends Module {

    private final Setting<Integer> range = register(new Setting<>("Range", 30, 1, 100));

    private PlayerEntity target;

    public PlayerFollow() {
        super("PlayerFollow", "Automatically looks at the nearest player.", Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onEnable() {
        target = null;
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!this.isEnabled() || client.player == null || client.world == null) return;

            target = findClosestPlayer(client, range.getValue());
            if (target == null) return;

            double dx = target.getX() - client.player.getX();
            double dz = target.getZ() - client.player.getZ();
            double yawToTarget = Math.toDegrees(Math.atan2(dz, dx)) - 90.0;
            yawToTarget = MathHelper.wrapDegrees(yawToTarget);
            float currentYaw = client.player.getYaw();
            float yawDiff = MathHelper.wrapDegrees((float) (yawToTarget - currentYaw));
            client.player.setYaw(currentYaw + yawDiff * 0.3f);
        });
    }

    private PlayerEntity findClosestPlayer(MinecraftClient client, int maxRange) {
        PlayerEntity closest = null;
        double closestDist = Double.MAX_VALUE;
        double maxDistSq = maxRange * maxRange;

        for (AbstractClientPlayerEntity player : client.world.getPlayers()) {
            if (player == client.player || !player.isAlive()) continue;

            double distSq = client.player.squaredDistanceTo(player);
            if (distSq < closestDist && distSq <= maxDistSq) {
                closest = player;
                closestDist = distSq;
            }
        }
        return closest;
    }

    @Override
    public void onDisable() {

    }
}