package uid.infinity.shampoo.features.modules.movement;

import uid.infinity.shampoo.features.modules.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.util.math.Vec2f;

public class Speed extends Module {

    public Speed() {
        super("Speed", "Automatically jumps to maintain high speed", Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onEnable() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!this.isEnabled() || client.player == null || client.world == null) return;

            Vec2f forward = client.player.input.getMovementInput();
            Vec2f sideways = client.player.input.getMovementInput();

            if (client.player.isOnGround()) {
                client.player.isSneaking();
            }
        });
    }
}