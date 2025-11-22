package uid.infinity.shampoo.features.modules.render;

import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;

public class ViewClip extends Module {

    public final Setting<Float> range = num("Range", 3f, 1f, 10f);
    public Setting<Boolean> player = bool("PlayerDistance", true);

    public ViewClip() {
        super("ViewClip", "Change player camera distance", Category.RENDER, true,false,false);
    }
}