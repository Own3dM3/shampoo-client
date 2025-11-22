package uid.infinity.shampoo.features.modules.wip;

import com.google.common.eventbus.Subscribe;
import uid.infinity.shampoo.event.impl.PacketEvent;
import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.text.Text;

public class Server extends Module {

    private final Setting<Boolean> noPacketKick = register(new Setting<>("NoPacketKick", true));
    private final Setting<Boolean> noDemo = register(new Setting<>("NoDemo", true));

    public Server() {
        super("Server", "Prevents servers from doing shady shit", Category.WIP, false, false, false);
    }


    @Subscribe
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof GameStateChangeS2CPacket packet) {
            if (packet.getReason() == GameStateChangeS2CPacket.DEMO_MESSAGE_SHOWN
                && !mc.isDemo()
                && noDemo.getValue()) {
                sendMessage("§cServer tried to force demo mode! Blocked.");
                event.setCancelled(true);
            }
        }
    }

    public boolean isNoPacketKick() {
        return noPacketKick.getValue();
    }

    private void sendMessage(String message) {
        mc.player.sendMessage(Text.literal(message), false);
    }
}