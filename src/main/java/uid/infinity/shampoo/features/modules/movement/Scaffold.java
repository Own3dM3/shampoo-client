package uid.infinity.shampoo.features.modules.movement;

import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;
import uid.infinity.shampoo.util.InteractionUtil;
import uid.infinity.shampoo.util.InventoryUtil;
import net.minecraft.block.AirBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.BlockPos;

public class Scaffold extends Module {

    public Setting<Boolean> airPlace = bool("AirPlace",true);

    public Scaffold(){
        super("Scaffold","Places Blocks down",Category.MOVEMENT,true,false,false);
    }

    @Override
    public void onTick(){
        if (!fullNullCheck()) {

            int oldSlot = mc.player.getInventory().getSelectedSlot();
            int slot = InventoryUtil.findHotbarItem(BlockItem.class);
            BlockPos pos = mc.player.getBlockPos().down();

            if (slot == -1) return;
            if (mc.world.getBlockState(pos).getBlock() instanceof AirBlock) {
                InventoryUtil.switchSlot(slot);

                InteractionUtil.place(pos, airPlace.getValue());

                InventoryUtil.switchSlot(oldSlot);
            }
        }
    }
}