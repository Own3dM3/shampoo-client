package uid.infinity.shampoo.features.modules.render;

import uid.infinity.shampoo.features.modules.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class NoWeather extends Module {

    public NoWeather() {
        super("NoWeather", "Disables all weather rendering", Category.RENDER, true, false, false);
    }

    @Override
    public void onEnable() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (this.isEnabled() && client.world != null) {
                client.world.setRainGradient(0.0f);
                client.world.setThunderGradient(0.0f);

            }
        });
    }
}