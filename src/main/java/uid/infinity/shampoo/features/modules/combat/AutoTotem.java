package uid.infinity.shampoo.features.modules.combat;

import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

public class AutoTotem extends Module {
    private final Setting<Mode> mode = register(new Setting<>("Mode", Mode.Totem));
    private final Setting<Integer> healthThreshold = register(new Setting<>("Health", 2, 1, 36));

    private enum Mode {
        Totem, Crystal, Apple
    }

    public AutoTotem() {
        super("AutoTotem", "Automatically swaps to a totem (or selected item) in offhand when health is low. Prioritizes totem over crystal if HP is critically low", Category.COMBAT, true, false, false);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.interactionManager == null) return;

        float totalHealth = mc.player.getHealth() + mc.player.getAbsorptionAmount();
        int threshold = healthThreshold.getValue();

        if (totalHealth <= threshold) {
            if (!isHoldingTotem()) {
                swapToItem(Items.TOTEM_OF_UNDYING);
            }
        } else {

            switch (mode.getValue()) {
                case Totem -> {
                    if (!isHoldingTotem()) {
                        swapToItem(Items.TOTEM_OF_UNDYING);
                    }
                }
                case Crystal -> {
                    if (!isHoldingCrystal()) {
                        swapToItem(Items.END_CRYSTAL);
                    }
                }
                case Apple -> {
                    if (!isHoldingEnchantedApple()) {
                        swapToItem(Items.ENCHANTED_GOLDEN_APPLE);
                    }
                }
            }
        }
    }

    private boolean isHoldingTotem() {
        return mc.player.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING;
    }

    private boolean isHoldingCrystal() {
        return mc.player.getOffHandStack().getItem() == Items.END_CRYSTAL;
    }

    private boolean isHoldingEnchantedApple() {
        return mc.player.getOffHandStack().getItem() == Items.ENCHANTED_GOLDEN_APPLE;
    }

    private void swapToItem(net.minecraft.item.Item targetItem) {

        if (!(mc.currentScreen instanceof InventoryScreen) && mc.currentScreen != null) return;

        for (int i = 9; i < 45; i++) {
            if (mc.player.getInventory().getStack(i).getItem() == targetItem) {

                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, i, 0, SlotActionType.PICKUP, mc.player);

                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 45, 0, SlotActionType.PICKUP, mc.player);

                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, i, 0, SlotActionType.PICKUP, mc.player);
                return;
            }
        }
    }
}