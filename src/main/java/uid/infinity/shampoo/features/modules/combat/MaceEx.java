package uid.infinity.shampoo.features.modules.combat;

import com.google.common.eventbus.*;
import net.minecraft.entity.*;
import net.minecraft.entity.decoration.*;
import net.minecraft.item.*;
import net.minecraft.network.packet.c2s.play.*;
import uid.infinity.shampoo.event.impl.*;
import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.*;
 // beta super secret module!! // MAYBE ADD TO MODULEMANAGER!!!
public class MaceEx extends Module {
    private Setting<Float> x = num("x", 5.5f, 0f, 3000.0f);
    private Setting<Float> y = num("y", 5.5f, 0f, 3000.0f);
    private Setting<Float> z = num("z", 5.5f, 0f, 3000.0f);
    private Setting<Boolean> tridentOnly = bool("Mace Only", true);

    public MaceEx() {
        super("tp", "Guarantees critical hits by spoofing a jump on attack", Category.MISC, true, false, false);
    }

    @Subscribe
    private void onPacket(PacketEvent.Send event) {
        if (event.getPacket() instanceof PlayerInteractEntityC2SPacket packet &&
                packet.type.getType() == PlayerInteractEntityC2SPacket.InteractType.ATTACK) {

            Entity entity = mc.world.getEntityById(packet.entityId);
            if (entity == null
                    || entity instanceof EndCrystalEntity
                    || !mc.player.isOnGround()
                    || !(entity instanceof LivingEntity)) return;

            // Проверка на булаву (trident)
            if (tridentOnly.getValue()) {
                ItemStack mainHand = mc.player.getMainHandStack();
                if (mainHand.getItem() != Items.MACE) return;
            }

            boolean bl = mc.player.horizontalCollision;
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                    mc.player.getX() + x.getValue(), mc.player.getY() + y.getValue(), mc.player.getZ() + z.getValue(), false, bl));
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                    mc.player.getX(), mc.player.getY(), mc.player.getZ(), false, bl));
            mc.player.addCritParticles(entity);
        }
    }

    @Override
    public String getDisplayInfo() {
        return "Packet";
    }
}