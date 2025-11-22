package uid.infinity.shampoo.features.modules.combat;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
 // xz chto eto lol todo fix this maybe lol
public class AutoTrap extends Module {
    private final MinecraftClient mc = MinecraftClient.getInstance();

    public Setting<Float> range = num("Range", 4.5f, 1.0f, 6.0f);
    public Setting<Boolean> attack = bool("Attack", true);
    public Setting<Boolean> head = bool("CoverHead", true);
    public Setting<Boolean> autoDisable = bool("AutoDisable", true);

    private final List<BlockPos> targetsToPlace = new ArrayList<>();
    private static final int MAX_PLACES_PER_TICK = 2;

    public AutoTrap() {
        super("AutoTrap", "Places obsidian around enemy feet", Category.COMBAT, true, false, false);
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.player != null && client.world != null && isEnabled()) {
                onTick();
            }
        });
    }

    public void onTick() {
        targetsToPlace.clear();
        PlayerEntity target = getClosestEnemy();
        if (target == null) {
            if (autoDisable.getValue()) disable();
            return;
        }

        BlockPos targetPos = BlockPos.ofFloored(target.getPos());
        List<BlockPos> surround = getSurroundPositions(targetPos);

        for (BlockPos pos : surround) {
            if (mc.world.getBlockState(pos).isReplaceable() &&
                    mc.player.getPos().distanceTo(Vec3d.ofCenter(pos)) <= range.getValue()) {
                targetsToPlace.add(pos);
            }
        }

        if (targetsToPlace.isEmpty()) {
            if (autoDisable.getValue()) disable();
            return;
        }

        if (attack.getValue()) attackNearbyCrystals(targetsToPlace);
        targetsToPlace.sort(Comparator.comparingInt(BlockPos::getY));

        int placed = 0;
        for (BlockPos pos : targetsToPlace) {
            if (placed >= MAX_PLACES_PER_TICK) break;
            placeBlock(pos);
            placed++;
        }
    }

    private PlayerEntity getClosestEnemy() {
        return mc.world.getPlayers().stream()
                .filter(p -> p != mc.player && p.isAlive())
                .filter(p -> !isFriend(p))
                .min(Comparator.comparingDouble(p -> mc.player.squaredDistanceTo(p)))
                .orElse(null);
    }

    private boolean isFriend(PlayerEntity player) {
        return false;
    }

    private List<BlockPos> getSurroundPositions(BlockPos center) {
        List<BlockPos> positions = new ArrayList<>();
        for (Direction dir : Direction.Type.HORIZONTAL) {
            BlockPos side = center.offset(dir);
            positions.add(side);
            positions.add(side.up());
        }
        if (head.getValue()) {
            positions.add(center.up(2));
        }
        return positions;
    }

    private void attackNearbyCrystals(List<BlockPos> positions) {
        for (BlockPos pos : positions) {
            List<EndCrystalEntity> crystals = mc.world.getEntitiesByClass(
                    EndCrystalEntity.class,
                    new Box(pos),
                    entity -> true
            );
            if (!crystals.isEmpty()) {
                if (mc.interactionManager != null) {
                    mc.interactionManager.attackEntity(mc.player, crystals.get(0));
                    mc.player.swingHand(Hand.MAIN_HAND);
                    return;
                }
            }
        }
    }

    private void placeBlock(BlockPos pos) {
        int slot = findObsidianSlot();
        if (slot == -1) return;

        int prevSlot = mc.player.getInventory().getSelectedSlot();
        mc.player.getInventory().setSelectedSlot(slot);

        Direction face = Direction.UP;
        for (Direction dir : Direction.values()) {
            BlockPos support = pos.offset(dir.getOpposite());
            if (!mc.world.getBlockState(support).isReplaceable()) {
                face = dir;
                break;
            }
        }

        Vec3d hitPos = Vec3d.ofCenter(pos);
        BlockHitResult hit = new BlockHitResult(hitPos, face, pos.offset(face.getOpposite()), false);
        if (mc.interactionManager != null) {
            mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
            mc.player.swingHand(Hand.MAIN_HAND);
        }

        mc.player.getInventory().setSelectedSlot(prevSlot);
    }

    private int findObsidianSlot() {
        for (int i = 0; i < 9; i++) {
            var stack = mc.player.getInventory().getStack(i);
            if (stack.isOf(Items.OBSIDIAN) || stack.isOf(Items.CRYING_OBSIDIAN)) {
                return i;
            }
        }
        return -1;
    }
}