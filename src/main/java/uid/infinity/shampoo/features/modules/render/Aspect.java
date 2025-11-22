package uid.infinity.shampoo.features.modules.render;

import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;

public class Aspect extends Module {

    public Setting<Float> ratio = num("Ratio", 1.78f, 0.0f, 5.0f);

    public Aspect() {
        super("Aspect", "Adjusts the game's aspect ratio to stretch or compress the screen", Category.RENDER, true, false, false);
    }
}