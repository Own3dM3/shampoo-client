package uid.infinity.shampoo.features.modules.movement;

import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class TridentFly extends Module {

    private final Setting<Boolean> allowNoWater = register(new Setting<>("AllowNoWater", true));
    private final Setting<Boolean> instant = register(new Setting<>("Instant", true));
    private final Setting<Boolean> spam = register(new Setting<>("Spam", false));
    private final Setting<Integer> ticks = register(new Setting<>("Ticks", 3, 0, 20, v -> spam.getValue()));

    private int tickTimer = 0;

    public TridentFly() {
        super("TridentFly", "Allows you to fly using tridents with Riptide", Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onEnable() {
        tickTimer = 0;
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!this.isEnabled() || client.player == null || client.world == null) return;

            if (spam.getValue()) {
                if (client.player.getMainHandStack().getItem() == Items.TRIDENT) {
                    tickTimer++;
                    if (tickTimer >= ticks.getValue()) {

                        client.interactionManager.interactItem(client.player, client.player.getActiveHand());

                        client.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(
                                PlayerActionC2SPacket.Action.RELEASE_USE_ITEM,
                                BlockPos.ORIGIN,
                                Direction.DOWN
                        ));
                        tickTimer = 0;
                    }
                }
            }
        });
    }

    @Override
    public String getDisplayInfo() {
        if (!allowNoWater.getValue()) {
            return "Water Required";
        }
        return null;
    }
}