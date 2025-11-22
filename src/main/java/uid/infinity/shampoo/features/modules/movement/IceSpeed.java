package uid.infinity.shampoo.features.modules.movement;

import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;
import uid.infinity.shampoo.mixin.AccessorAbstractBlock;
import net.minecraft.block.Blocks;

public class IceSpeed extends Module {

    private final Setting<Float> slipperiness = register(new Setting<>("Slipperiness", 0.98f, 0.01f, 1.0f));

    private static final float ORIGINAL_ICE = 0.98f;
    private static final float ORIGINAL_PACKED_ICE = 0.98f;
    private static final float ORIGINAL_BLUE_ICE = 0.98f;
    private static final float ORIGINAL_FROSTED_ICE = 0.75f;

    public IceSpeed() {
        super("IceSpeed", "Modifies slipperiness of ice blocks", Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onEnable() {
        float value = slipperiness.getValue();
        setIceSlipperiness(value);
    }

    @Override
    public void onDisable() {

        ((AccessorAbstractBlock) (Object) Blocks.ICE).setSlipperiness(ORIGINAL_ICE);
        ((AccessorAbstractBlock) (Object) Blocks.PACKED_ICE).setSlipperiness(ORIGINAL_PACKED_ICE);
        ((AccessorAbstractBlock) (Object) Blocks.BLUE_ICE).setSlipperiness(ORIGINAL_BLUE_ICE);
        ((AccessorAbstractBlock) (Object) Blocks.FROSTED_ICE).setSlipperiness(ORIGINAL_FROSTED_ICE);
    }

    private void setIceSlipperiness(float value) {
        ((AccessorAbstractBlock) (Object) Blocks.ICE).setSlipperiness(value);
        ((AccessorAbstractBlock) (Object) Blocks.PACKED_ICE).setSlipperiness(value);
        ((AccessorAbstractBlock) (Object) Blocks.BLUE_ICE).setSlipperiness(value);
        ((AccessorAbstractBlock) (Object) Blocks.FROSTED_ICE).setSlipperiness(value);
    }

    @Override
    public String getDisplayInfo() {
        return String.format("%.2f", slipperiness.getValue());
    }
}