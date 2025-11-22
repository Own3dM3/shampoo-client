package uid.infinity.shampoo.features.modules.misc;

import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;

public class BetterChat extends Module {
    public Setting<Boolean> suffix = register(new Setting<>("Suffix", true));
    public Setting<String> message = register(new Setting<>("Message", "| shampooclient", v -> suffix.getValue()));

    public BetterChat() {
        super("BetterChat", "Adds a suffix to chat messages", Category.MISC, true, false, false);
    }
}