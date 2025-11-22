package uid.infinity.shampoo.features.modules.combat;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.block.*;
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
import uid.infinity.shampoo.*;
import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.*;
import java.util.*;
import uid.infinity.shampoo.features.modules.exploit.MultiTask;
// xz chto eto lol todo fix this maybe lol
public class HoleFill extends Module {
    private final MinecraftClient mc = MinecraftClient.getInstance();

    public Setting<Boolean> webs = bool("Webs", false);
    public Setting<Boolean> obsidian = bool("Obsidian", true);
    public Setting<Boolean> doubles = bool("Doubles", true);
    public Setting<Boolean> auto = bool("Auto", true);
    public Setting<Float> targetRange = num("TargetRange", 3.0f, 0.5f, 5.0f);
    public Setting<Float> enemyRange = num("EnemyRange", 10.0f, 0.1f, 15.0f);
    public Setting<Float> placeRange = num("PlaceRange", 4.0f, 0f, 6.0f);
    public Setting<Boolean> attack = bool("Attack", true);
    public Setting<Integer> shiftTicks = num("ShiftTicks", 1, 1, 10);
    public Setting<Float> shiftDelayButton = num("ShiftDelay", 1.0f, 0f, 5.0f);
    public Setting<Boolean> autoDisable = bool("AutoDisable", true);
    public Setting<Boolean> disableOnDeath = bool("DisableOnDeath", true);
    public Setting<Boolean> multiTask = bool("MultiTask", true);

    private int shiftDelay = 0;
    private List<BlockPos> fills = new ArrayList<>();

    public HoleFill() {
        super("HoleFill", "Fills safe holes with obsidian or webs", Category.COMBAT, true, false, false);

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.player != null && client.world != null && isEnabled()) {
                onTick();
            }
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            if (disableOnDeath.getValue()) disable();
        });
    }

    @Override
    public void onDisable() {
        fills.clear();
    }

    public void onTick() {
        if (!multiTask.getValue() && mc.player.isUsingItem()) {
            fills.clear();
            return;
        }

        int slot = findTargetSlot();
        if (slot == -1) {
            fills.clear();
            return;
        }

        if (shiftDelayButton.getValue() > 0 && shiftDelay < shiftDelayButton.getValue()) {
            shiftDelay++;
            return;
        }

        List<BlockPos> candidateHoles = findHoles();
        fills.clear();

        for (BlockPos pos : candidateHoles) {
            if (mc.player.getPos().distanceTo(Vec3d.ofCenter(pos)) > placeRange.getValue()) continue;
            if (!mc.world.getBlockState(pos).isReplaceable()) continue;

            boolean shouldFill = !auto.getValue();
            if (auto.getValue()) {
                for (PlayerEntity enemy : mc.world.getPlayers()) {
                    if (enemy == mc.player || isFriend(enemy)) continue;
                    if (mc.player.distanceTo(enemy) > enemyRange.getValue()) continue;
                    if (enemy.getY() < pos.getY()) continue;
                    if (enemy.getPos().distanceTo(Vec3d.ofCenter(pos)) <= targetRange.getValue()) {
                        shouldFill = true;
                        break;
                    }
                }
            }

            if (shouldFill) {
                fills.add(pos);
            }
        }

        if (fills.isEmpty()) {
            if (autoDisable.getValue()) disable();
            return;
        }

        if (attack.getValue()) attackBlockingCrystals(fills);

        int placed = 0;
        for (BlockPos pos : fills) {
            if (placed >= shiftTicks.getValue()) break;
            placeBlock(pos, slot);
            placed++;
            shiftDelay = 0;
        }
    }

    private List<BlockPos> findHoles() {
        List<BlockPos> holes = new ArrayList<>();
        int range = (int) Math.ceil(placeRange.getValue());

        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                BlockPos pos = mc.player.getBlockPos().add(x, 0, z);
                if (isSafeHole(pos)) {
                    holes.add(pos);
                }
                if (doubles.getValue()) {

                    if (isSafeHole(pos) && isSafeHole(pos.east())) holes.add(pos);
                    if (isSafeHole(pos) && isSafeHole(pos.south())) holes.add(pos);
                }
            }
        }
        return holes;
    }

    private boolean isSafeHole(BlockPos pos) {
        if (!obsidian.getValue()) return false;

        BlockState state = mc.world.getBlockState(pos);
        if (!state.isReplaceable()) return false;

        if (!mc.world.getBlockState(pos.down()).isSolidBlock(mc.world, pos.down())) return false;

        for (Direction dir : Direction.Type.HORIZONTAL) {
            Block block = mc.world.getBlockState(pos.offset(dir)).getBlock();
            if (!isBlastResistant(block)) return false;
        }

        if (!mc.world.getBlockState(pos.up()).isReplaceable()) return false;
        if (!mc.world.getBlockState(pos.up(2)).isReplaceable()) return false;

        return true;
    }

    private boolean isBlastResistant(Block block) {
        return block == Blocks.OBSIDIAN ||
                block == Blocks.BEDROCK ||
                block == Blocks.CRYING_OBSIDIAN ||
                block == Blocks.NETHERITE_BLOCK ||
                block == Blocks.ANCIENT_DEBRIS ||
                block == Blocks.ENDER_CHEST;
    }

    private void attackBlockingCrystals(List<BlockPos> positions) {
        for (BlockPos pos : positions) {
            List<EndCrystalEntity> crystals = mc.world.getEntitiesByClass(
                    EndCrystalEntity.class,
                    new Box(pos),
                    e -> true
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

    private void placeBlock(BlockPos pos, int slot) {
        int prev = mc.player.getInventory().getSelectedSlot();
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

        mc.player.getInventory().setSelectedSlot(prev);
    }

    private int findTargetSlot() {
        if (webs.getValue()) {
            for (int i = 0; i < 9; i++) {
                if (mc.player.getInventory().getStack(i).isOf(Items.COBWEB)) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < 9; i++) {
                var stack = mc.player.getInventory().getStack(i);
                if (stack.isOf(Items.OBSIDIAN) || stack.isOf(Items.CRYING_OBSIDIAN)) {
                    return i;
                }
            }
        }
        return -1;
    }

    private boolean isFriend(PlayerEntity player) {
        return false;
    }
}