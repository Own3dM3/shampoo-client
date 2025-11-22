package uid.infinity.shampoo.features.modules.combat;

import uid.infinity.shampoo.shampoo;
import uid.infinity.shampoo.event.impl.Render3DEvent;
import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;
import uid.infinity.shampoo.util.CaptureMark;
import uid.infinity.shampoo.util.MathUtil;
import uid.infinity.shampoo.util.models.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

public class Aura extends Module {
    private final Timer timer = new Timer();
    public Setting<Boolean> players = bool("Players", true);
    public Setting<Boolean> mobs = bool("Mobs", true);
    public Setting<Boolean> render = bool("Render", true);
    private final Setting<Float> range = num("Range", 4f, 1f, 6f);
    private PlayerEntity target;

    public Aura() {
        super("Aura", "Auto target players", Category.COMBAT, true, false, false);
    }

    @Override
    public void onTick() {
        if (fullNullCheck()) return;

        target = null;

        for (Entity entity : mc.world.getEntities()) {
            if (entity == mc.player || entity.getPos().distanceTo(mc.player.getPos()) > range.getValue())
                continue;

            if (entity instanceof PlayerEntity player && players.getValue()) {
                if (!shampoo.friendManager.isFriend(player.getName().getString())) {
                    target = player;
                    attackEntity(entity);
                    break;
                }
            }

            if (mobs.getValue()) {
                if ((entity instanceof MobEntity && !(entity instanceof PlayerEntity)) ||
                        entity instanceof WitherEntity) {
                    attackEntity(entity);
                    break;
                }
            }
        }
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (!render.getValue() || nullCheck() || target == null ||
                target.getPos().distanceTo(mc.player.getPos()) > range.getValue()) {
            return;
        }

        CaptureMark.render(target);
    }

    private void attackEntity(Entity entity) {
        if (timer.passedS(0.8)) {
            if (entity instanceof PlayerEntity) {
                if (shampoo.friendManager.isFriend(entity.getName().getString())) {
                    return;
                }
            }

            mc.interactionManager.attackEntity(mc.player, entity);
            mc.player.swingHand(Hand.MAIN_HAND);
            float[] angle = MathUtil.calcAngle(mc.player.getEyePos(), entity.getPos());
            mc.player.headYaw = angle[0];
            timer.reset();
        }
    }

    @Override
    public String getDisplayInfo() {
        return target != null ? target.getName().getString() : null;
    }
}