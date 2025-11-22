package uid.infinity.shampoo.features.modules.misc;

import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;

public class UnfocusedFPS extends Module {
    public final Setting<Integer> limit = num("Limit", 5, 1, 120);
    public UnfocusedFPS() {
        super("UnfocusedFPS", "Limits FPS when the game window is not focused to reduce resource usage", Category.MISC, true,false,false);
    }
}