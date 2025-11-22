package uid.infinity.shampoo.features.modules.movement;

import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class NoSlow extends Module {
    public Setting<Boolean> food = bool("Food", true);
    public Setting<Boolean> portals = bool("Portals", true);
    public Setting<Boolean> guimove = bool("GuiMove", true);

    public NoSlow() {
        super("NoSlow", "Prevents slowdown from eating, portals, and allows movement in GUIs", Category.MOVEMENT, true, false, false);
    }
    @Override public void onTick() {
        if (fullNullCheck()) return;
        if (guimove.getValue() && mc.currentScreen != null && !(mc.currentScreen instanceof ChatScreen)) {
            for (KeyBinding binding : new KeyBinding[]{mc.options.forwardKey, mc.options.jumpKey, mc.options.backKey, mc.options.sprintKey, mc.options.leftKey, mc.options.rightKey}) {
                binding.setPressed(InputUtil.isKeyPressed(mc.getWindow().getHandle(), InputUtil.fromTranslationKey(binding.getBoundKeyTranslationKey()).getCode()));

            }
        }
    }
}