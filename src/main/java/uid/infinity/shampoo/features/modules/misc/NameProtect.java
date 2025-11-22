package uid.infinity.shampoo.features.modules.misc;

import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;

public class NameProtect extends Module {

    public final Setting<String> fakeName = register(new Setting<>("Name", "Player"));

    public NameProtect() {
        super("NameProtect", "Replaces your real name in chat, tab, and messages", Category.MISC, true, false, false);
    }
}