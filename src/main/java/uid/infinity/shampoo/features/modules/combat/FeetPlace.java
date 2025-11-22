package uid.infinity.shampoo.features.modules.combat;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.BundleS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.*;
import java.util.*;
 //maybe norm ya xz todo sdelyat lyche
public class FeetPlace extends Module {
    private final MinecraftClient mc = MinecraftClient.getInstance();

    public Setting<Timing> timing = mode("Timing", Timing.VANILLA);
    public Setting<Boolean> prePlaceExplosion = bool("Pre-Place-Explosions", false);
    public Setting<Boolean> prePlaceTick = bool("Pre-Place-Tick", true);
    public Setting<Float> placeRange = num("Place-Range", 4.0f, 0f, 6.0f);
    public Setting<Boolean> attack = bool("Attack", true);
    public Setting<Boolean> extend = bool("Extend", false);
    public Setting<Boolean> head = bool("Cover-Head", false);
    public Setting<Boolean> support = bool("Support-Blocks", true);
    public Setting<Integer> shiftTicks = num("Shift-Ticks", 1, 1, 10);
    public Setting<Float> shiftDelay = num("Shift-Delay", 1.0f, 0f, 5.0f);
    public Setting<Boolean> jumpDisable = bool("JumpDisable", true);
    public Setting<Boolean> disableOnDeath = bool("DisableOnDeath", true);

    private double prevY;
    private final List<BlockPos> placements = new ArrayList<>();
    private final Map<BlockPos, Long> lastPlaced = new HashMap<>();
    private int blocksPlacedThisTick = 0;

    public FeetPlace() {
        super("FeetPlace", "Surrounds your feet with obsidian", Category.COMBAT, true, false, false);

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.player != null && client.world != null && isEnabled()) {
                onTick();
            }
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            if (disableOnDeath.getValue()) disable();
        });

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {

        });
    }

    @Override
    public void onEnable() {
        if (mc.player != null) prevY = mc.player.getY();
    }

    @Override
    public void onDisable() {
        placements.clear();
        lastPlaced.clear();
    }

    public void onTick() {
        blocksPlacedThisTick = 0;

        if (jumpDisable.getValue() && mc.player != null) {
            if (mc.player.getY() - prevY > 0.5 || mc.player.fallDistance > 1.5f) {
                disable();
                return;
            }
            prevY = mc.player.getY();
        }

        int slot = findObsidianSlot();
        if (slot == -1) return;

        List<BlockPos> surround = getSurround(mc.player);
        if (surround.isEmpty()) return;

        if (attack.getValue()) attackBlockingCrystals(surround);

        placements.clear();
        for (BlockPos pos : surround) {
            Long last = lastPlaced.get(pos);
            if (shiftDelay.getValue() > 0 && last != null &&
                    System.currentTimeMillis() - last < (long)(shiftDelay.getValue() * 50)) {
                continue;
            }
            if (!mc.world.getBlockState(pos).isReplaceable()) continue;
            if (mc.player.getPos().distanceTo(Vec3d.ofCenter(pos)) > placeRange.getValue()) continue;
            placements.add(pos);
        }

        if (placements.isEmpty()) return;

        if (support.getValue()) {
            List<BlockPos> extra = new ArrayList<>();
            for (BlockPos pos : placements) {
                if (pos.getY() <= mc.player.getBlockY() + 1) {
                    BlockPos down = pos.down();
                    if (mc.world.getBlockState(down).isReplaceable()) {
                        extra.add(down);
                    }
                }
            }
            placements.addAll(extra);
        }

        placements.sort(Comparator.comparingInt(BlockPos::getY));

        for (BlockPos pos : placements) {
            if (blocksPlacedThisTick >= shiftTicks.getValue()) break;
            placeBlock(pos, slot);
            blocksPlacedThisTick++;
        }
    }

    private void handleIncomingPacket(Packet<?> packet) {
        if (timing.getValue() != Timing.SEQUENTIAL) return;

        if (packet instanceof BlockUpdateS2CPacket blockPacket) {
            BlockPos pos = blockPacket.getPos();
            if (placements.contains(pos) || getSurround(mc.player).contains(pos)) {
                if (mc.world.getBlockState(pos).isReplaceable()) {
                    int slot = findObsidianSlot();
                    if (slot != -1) placeBlock(pos, slot);
                } else {
                    lastPlaced.remove(pos);
                }
            }
        }

        if (blocksPlacedThisTick > shiftTicks.getValue() * 2) return;

        if (packet instanceof ExplosionS2CPacket exp && prePlaceExplosion.getValue()) {
            BlockPos center = BlockPos.ofFloored(exp.center());
            if (getSurround(mc.player).contains(center)) {
                int slot = findObsidianSlot();
                if (slot != -1) placeBlock(center, slot);
            }
        }

        if (packet instanceof EntitySpawnS2CPacket spawn && prePlaceTick.getValue()) {
            if (spawn.getEntityType() == EntityType.END_CRYSTAL) {
                BlockPos crystalPos = BlockPos.ofFloored(spawn.getX(), spawn.getY(), spawn.getZ());
                if (getSurround(mc.player).contains(crystalPos)) {
                    int slot = findObsidianSlot();
                    if (slot != -1) placeBlock(crystalPos, slot);
                }
            }
        }
    }

    static {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
        });
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
        lastPlaced.put(pos, System.currentTimeMillis());
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

    private List<BlockPos> getSurround(PlayerEntity player) {
        List<BlockPos> result = new ArrayList<>();
        List<BlockPos> playerBlocks = getPlayerBlocks(player);

        for (BlockPos base : playerBlocks) {
            for (Direction dir : Direction.Type.HORIZONTAL) {
                BlockPos side = base.offset(dir);
                if (!result.contains(side) && !playerBlocks.contains(side)) {
                    result.add(side);
                }
            }
        }

        // Add bottom blocks (for "feet" placement)
        for (BlockPos base : playerBlocks) {
            if (!base.equals(player.getBlockPos())) {
                result.add(base.down());
            }
        }

        if (head.getValue()) {
            result.add(player.getBlockPos().up(2));
        }

        return result;
    }
    private List<BlockPos> getPlayerBlocks(PlayerEntity player) {
        BlockPos pos = BlockPos.ofFloored(player.getPos());
        if (!extend.getValue()) {
            return Collections.singletonList(pos);
        }

        List<BlockPos> blocks = new ArrayList<>();
        blocks.add(pos);
        Box bb = player.getBoundingBox();
        if (bb.maxY - bb.minY > 1.0) {
            blocks.add(pos.up());
        }
        return blocks;
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

    public enum Timing {
        VANILLA,
        SEQUENTIAL
    }
}