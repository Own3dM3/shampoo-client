package uid.infinity.shampoo.features.modules.misc;

import com.google.common.eventbus.Subscribe;
import uid.infinity.shampoo.event.impl.PacketEvent;
import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.mixin.PlayerPositionMixin;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.PositionFlag;

public class NoRotate extends Module {
    public NoRotate() {
        super("NoRotate", "Prevents the server from rotating your player", Category.MISC, true, false, false);
    }

    @Subscribe
    public void onPacket(PacketEvent.Receive event) {
        if (nullCheck()) return;
        if (event.getPacket() instanceof PlayerPositionLookS2CPacket packet) {
            ((PlayerPositionMixin) (Object) packet.change()).setYaw(mc.player.getYaw());
            ((PlayerPositionMixin) (Object) packet.change()).setPitch(mc.player.getPitch());
            packet.relatives().remove(PositionFlag.X_ROT);
            packet.relatives().remove(PositionFlag.Y_ROT);

        }
    }
}