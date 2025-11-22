package uid.infinity.shampoo.features.modules.combat;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.*;
import java.util.List;
 // xz chto eto lol todo fix this LOL
public class Burrow extends Module {
    private final MinecraftClient mc = MinecraftClient.getInstance();

    public Setting<Mode> mode = mode("Mode", Mode.BLOCK_LAG);
    public Setting<Boolean> attack = bool("Attack", false);
    public Setting<Boolean> autoDisable = bool("AutoDisable", true);

    private double prevY;

    public Burrow() {
        super("Burrow", "Places a block under you to burrow", Category.COMBAT, true, false, false);

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.player != null && client.world != null && isEnabled()) {
                onTick();
            }
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> disable());
    }

    @Override
    public void onEnable() {
        if (mc.player != null) prevY = mc.player.getY();
    }

    public void onTick() {
        if (mc.player == null || mc.world == null) return;

        if (Math.abs(mc.player.getY() - prevY) > 0.5) {
            disable();
            return;
        }

        BlockPos playerPos = BlockPos.ofFloored(mc.player.getPos());
        boolean inBlock = !mc.world.getBlockState(playerPos).isReplaceable();

        if (!inBlock && mc.player.isOnGround()) {
            if (mode.getValue() == Mode.BLOCK_LAG) {

                double x = mc.player.getX();
                double y = mc.player.getY();
                double z = mc.player.getZ();
                boolean onGround = mc.player.isOnGround();

                //mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y + 0.42, z, onGround));
                //mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y + 0.75, z, onGround));
                //mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y + 1.01, z, onGround));
                //mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y + 1.16, z, onGround));

                if (!mc.world.isSpaceEmpty(mc.player.getBoundingBox().offset(0, 2.34, 0))) {
                    if (autoDisable.getValue()) disable();
                    return;
                }

                mc.player.setPosition(x, y + 1.167, z);
                attackAndPlace(playerPos);

                mc.player.setPosition(x, y, z);

                //mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y + 2.34, z, false));
            } else if (mode.getValue() == Mode.WEB) {
                int slot = findWebSlot();
                if (slot == -1) {
                    if (autoDisable.getValue()) disable();
                    return;
                }
                placeBlock(playerPos, slot);
            }
        }

        if (autoDisable.getValue()) disable();
    }

    private void attackAndPlace(BlockPos pos) {
        int slot = findObsidianSlot();
        if (slot == -1) return;

        if (attack.getValue()) {
            List<EndCrystalEntity> crystals = mc.world.getEntitiesByClass(
                    EndCrystalEntity.class,
                    new Box(pos),
                    e -> true
            );
            if (!crystals.isEmpty()) {
                if (mc.interactionManager != null) {
                    mc.interactionManager.attackEntity(mc.player, crystals.get(0));
                    mc.player.swingHand(Hand.MAIN_HAND);
                }
            }
        }

        placeBlock(pos, slot);
    }

    private void placeBlock(BlockPos pos, int slot) {
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

    private int findWebSlot() {
        for (int i = 0; i < 9; i++) {
            var stack = mc.player.getInventory().getStack(i);
            if (stack.isOf(Items.COBWEB)) {
                return i;
            }
        }
        return -1;
    }

    private enum Mode {
        BLOCK_LAG,
        WEB
    }
}