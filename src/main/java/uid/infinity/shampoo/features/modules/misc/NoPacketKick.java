package uid.infinity.shampoo.features.modules.misc;

import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient; // xz navernoe ne pabotaet
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;

public class NoPacketKick extends Module {

    public final Setting<Boolean> packetSpam = register(new Setting<>("PacketSpam", true));

    private final Setting<Boolean> validateActions = register(new Setting<>("ValidateActions", true));

    private long lastMovePacket = 0;
    private static final long MOVE_DELAY_MS = 50;

    public NoPacketKick() {
        super("NoPacketKick", "Reduces packet spam to avoid anti-cheat kicks", Category.MISC, false, false, false);
    }

    @Override
    public void onEnable() {

        ClientPlayNetworking.getGlobalReceivers();
    }

    @Override
    public void onDisable() {

        ClientPlayNetworking.getGlobalReceivers();
    }

    private Packet<?> onSendPacket(
            MinecraftClient client,
            ClientPlayNetworkHandler networkHandler,
            Packet<?> packet,
            PacketSender sender
    ) {
        if (packetSpam.getValue()) {

            if (packet instanceof PlayerMoveC2SPacket) {
                long now = System.currentTimeMillis();
                if (now - lastMovePacket < MOVE_DELAY_MS) {
                    return null;
                }
                lastMovePacket = now;
            }
        }

        if (validateActions.getValue()) {

            if (packet instanceof PlayerActionC2SPacket actionPacket) {
                if (!isValidBlockAction(actionPacket)) {
                    return null;
                }
            }
        }

        return packet;
    }

    private boolean isValidBlockAction(PlayerActionC2SPacket packet) {
        if (mc.player == null || mc.world == null || mc.crosshairTarget == null) return false;

        BlockPos pos = packet.getPos();

        if (mc.world.getBlockState(pos).isAir()) return false;

        if (mc.crosshairTarget instanceof BlockHitResult hit) {
            return hit.getBlockPos().equals(pos);
        }
        return false;
    }
}