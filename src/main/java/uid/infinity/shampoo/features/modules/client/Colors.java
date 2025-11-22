package uid.infinity.shampoo.features.modules.client;

import uid.infinity.shampoo.shampoo;
import uid.infinity.shampoo.event.impl.ClientEvent;
import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;
import com.google.common.eventbus.Subscribe;

import java.awt.Color;

public class Colors extends Module {
    private static Colors INSTANCE;

    public final Setting<Color> color = register(
            new Setting<>("Color", new Color(121, 135, 242)).hideAlpha()
    );

    public Colors() {
        super("Colors", "Client color scheme", Module.Category.CLIENT, true, false, false);
        INSTANCE = this;
    }

    public static Colors getInstance() {
        return INSTANCE;
    }

    @Override
    public void onLoad() {
        updateColorManager();
    }

    @Subscribe
    public void onSettingChange(ClientEvent event) {
        if (event.getStage() == 2 && event.getSetting().getFeature() == this) {
            updateColorManager();
        }
    }

    private void updateColorManager() {
        Color c = color.getValue();

        shampoo.colorManager.setBaseColor(c.getRed(), c.getGreen(), c.getBlue());
    }

    public Color getColor() {
        Color c = color.getValue();
        return new Color(c.getRed(), c.getGreen(), c.getBlue());
    }

    public int getRGB() {
        return getColor().getRGB();
    }

    public int getRGB(int alpha) {
        Color c = getColor();
        alpha = Math.max(0, Math.min(255, alpha));
        return (alpha << 24) | (c.getRed() << 16) | (c.getGreen() << 8) | c.getBlue();
    }
}