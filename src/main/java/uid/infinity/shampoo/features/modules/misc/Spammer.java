package uid.infinity.shampoo.features.modules.misc;

import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;
import uid.infinity.shampoo.util.models.Timer;
import java.util.Random;

public class Spammer extends Module {
    private Setting<Integer> delay = num("Delay:", 5, 1, 30);
    private Timer timer = new Timer();


    public Spammer() {
        super("Spammer", "SPAAAAAAAAAAAAM", Category.MISC, true, false, false);
    }

    @Override
    public void onTick() {
        if (fullNullCheck()) return;
        if (timer.passedS(delay.getValue())) {
            mc.player.networkHandler.sendChatMessage(walkingmessage());
            timer.reset();
        }
    }

    private String walkingmessage() {
        String[] walking = {
                "lolololololo",
        };
        return walking[new Random().nextInt(walking.length)];
    }
}
