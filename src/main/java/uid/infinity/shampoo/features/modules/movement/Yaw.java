package uid.infinity.shampoo.features.modules.movement;

import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.LlamaEntity;

public class Yaw extends Module {

    private final Setting<Boolean> lock = register(new Setting<>("Lock", false));

    public Yaw() {
        super("Yaw", "Locks player yaw to cardinal directions", Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onEnable() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!this.isEnabled() || client.player == null || client.world == null) return;

            float currentYaw = client.player.getYaw();
            float snappedYaw = Math.round(currentYaw / 45.0f) * 45.0f;

            Entity vehicle = client.player.getVehicle();
            if (vehicle != null) {
                vehicle.setYaw(snappedYaw);
                vehicle.setHeadYaw(snappedYaw);
                if (vehicle instanceof LlamaEntity llama) {
                    llama.setBodyYaw(snappedYaw);
                }
            } else {
                client.player.setYaw(snappedYaw);
                client.player.setHeadYaw(snappedYaw);
                client.player.setBodyYaw(snappedYaw);
            }
        });
    }
}