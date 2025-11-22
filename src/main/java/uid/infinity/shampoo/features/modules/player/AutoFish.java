package uid.infinity.shampoo.features.modules.player;

import com.google.common.eventbus.Subscribe;
import uid.infinity.shampoo.event.impl.PacketEvent;
import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import uid.infinity.shampoo.util.models.*;

public class AutoFish extends Module {
    private boolean hasFished = false;
    private final Setting<Integer> delay = register(new Setting<>("Delay", 0, 0, 60));
    private final Timer timer = new Timer();

    public AutoFish() {
        super("AutoFish", "Automatically reels in fish when the bobber splashes", Category.PLAYER, true, false, false);
    }

    @Subscribe
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof PlaySoundS2CPacket packet) {
            if (packet.getSound().value() == SoundEvents.ENTITY_FISHING_BOBBER_SPLASH) {
                if (!hasFished && mc.player != null && mc.interactionManager != null) {
                    mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                    hasFished = true;
                    timer.reset();
                }
            }
        }
    }

    @Override
    public void onTick() {
        if (hasFished && timer.passedS(delay.getValue())) {
            if (mc.player != null && mc.interactionManager != null) {

                mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                hasFished = false;
            }
        }
    }
}