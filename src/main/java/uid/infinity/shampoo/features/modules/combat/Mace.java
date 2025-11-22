package uid.infinity.shampoo.features.modules.combat;

import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;
import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;
import uid.infinity.shampoo.util.models.Timer;
// beta super secret module number two!! // MAYBE ADD TO MODULEMANAGER!!!
public class Mace extends Module {
    public Setting<Float> range = register(new Setting<>("Range", 5f, 1f, 6f));
    public Setting<Float> delay = register(new Setting<>("Delay", 0.9f, 0.1f, 1.0f));
    public Setting<Boolean> weapon = register(new Setting<>("WeaponCheck", false));
    public Setting<Boolean> swing = register(new Setting<>("Swing", true));
    public Setting<Boolean> rotate = register(new Setting<>("Rotate", true));
    public Setting<Boolean> targets = register(new Setting<>("Targets", true));
    public Setting<Boolean> players = register(new Setting<>("Players", true, v -> targets.getValue()));

    private final Timer timer = new Timer();
    private PlayerEntity target;

    public Mace() {
        super("Mace", "Automatically attacks nearby players when holding a valid weapon", Category.COMBAT, true, false, false);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;

        Item item = mc.player.getMainHandStack().getItem();

        if (weapon.getValue() && !(item instanceof AxeItem
                || item instanceof TridentItem
                || item instanceof MaceItem
                || item == Items.DIAMOND_SWORD
                || item == Items.STONE_SWORD
                || item == Items.GOLDEN_SWORD
                || item == Items.NETHERITE_SWORD
                || item == Items.IRON_SWORD
                || item == Items.WOODEN_SWORD)) {
            return;
        }

        target = null;

        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof PlayerEntity player && players.getValue()) {
                if (player == mc.player || !player.isAlive() || mc.player.distanceTo(player) > range.getValue()) {
                    continue;
                }
                target = player;
                attackEntity(player, false);
                break;
            }
        }
    }

    private void attackEntity(Entity entity, boolean packet) {
        if (!timer.passedS(delay.getValue())) return;

        if (!packet) {
            mc.interactionManager.attackEntity(mc.player, entity);
        } else {
            mc.getNetworkHandler().sendPacket(PlayerInteractEntityC2SPacket.attack(entity, mc.player.isSneaking()));
        }

        if (swing.getValue()) {
            mc.player.swingHand(Hand.MAIN_HAND);
        }

        timer.reset();
    }

    @Override
    public String getDisplayInfo() {
        return target == null ? null : target.getName().getString();
    }
}