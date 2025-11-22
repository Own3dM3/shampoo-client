package uid.infinity.shampoo.features.modules.misc;

import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;
import uid.infinity.shampoo.util.models.Timer;

public class AntiAFK extends Module {
    public Setting<Integer> delay = this.register(new Setting<>("Delay", 30, 1, 60));
    public Setting<Boolean> jump = bool("Jump", true);
    public Setting<Boolean> sneak = bool("Sneak", true);
    public Setting<Boolean> message = bool("Message", true);
    private Timer timer = new Timer();


    public AntiAFK() {
        super("AntiAFK", "Prevents you from being kicked for inactivity by simulating player activity", Category.MISC, true, false, false);
    }

    @Override
    public void onTick() {
        if (fullNullCheck()) return;
        if (timer.passedS(delay.getValue())) {
            if (jump.getValue()) {
                mc.player.jump();
            }
            if (message.getValue()) {
                mc.player.networkHandler.sendChatMessage("I dont AFK!");
            }
            if (sneak.getValue()) {
                mc.options.sneakKey.setPressed(false);
                mc.options.sneakKey.setPressed(true);

            } else {
                mc.options.sneakKey.setPressed(false);
            }
            timer.reset();
        }
    }
}