package uid.infinity.shampoo.features.modules.render;

import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;
import net.minecraft.client.MinecraftClient;

public class Zoom extends Module {
    public Setting<Integer> zoomFov = num("Zoom FOV", 30, 10, 120);
    private float originalFov = -1.0f;

    public Zoom() {
        super("Zoom", "Temporarily reduces the field of view to provide a zoom effect, useful for precise aiming or scouting distant areas", Category.RENDER, true, false, false);
    }

    @Override
    public void onEnable() {
        originalFov = MinecraftClient.getInstance().options.getFov().getValue().floatValue();
        MinecraftClient.getInstance().options.getFov().setValue(zoomFov.getValue());
    }

    @Override
    public void onDisable() {
        if (originalFov != -1.0f) {
            MinecraftClient.getInstance().options.getFov().setValue((int) originalFov);
            originalFov = -1.0f;
        }
    }
}