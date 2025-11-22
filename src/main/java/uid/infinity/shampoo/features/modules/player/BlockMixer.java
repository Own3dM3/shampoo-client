package uid.infinity.shampoo.features.modules.player;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import uid.infinity.shampoo.features.modules.Module;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockMixer extends Module {
    private int ticks = 0;
    private static final int MIX_INTERVAL = 20;

    public BlockMixer() {
        super("BlockMixer", "Randomly shuffles hotbar slots", Category.PLAYER, true, false, false);
    }

    @Override
    public void onTick() {
        if (!this.isEnabled()) return;

        ticks++;
        if (ticks >= MIX_INTERVAL) {
            mixHotbar();
            ticks = 0;
        }
    }

    private void mixHotbar() {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null || player.getInventory() == null) return;

        var inventory = player.getInventory();
        List<ItemStack> hotbar = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            hotbar.add(inventory.getStack(i).copy());
        }

        Collections.shuffle(hotbar);

        for (int i = 0; i < 9; i++) {
            inventory.setStack(i, hotbar.get(i));
        }

        if (client.interactionManager != null) {
            for (int i = 0; i < 9; i++) {
                client.interactionManager.clickSlot(0, i, 0, net.minecraft.screen.slot.SlotActionType.SWAP, player);
            }
        }
    }
}