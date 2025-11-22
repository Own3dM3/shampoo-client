package uid.infinity.shampoo.features.modules.render;

import com.google.common.eventbus.Subscribe;
import uid.infinity.shampoo.event.impl.Render3DEvent;
import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;
import uid.infinity.shampoo.util.RenderUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;

import java.awt.*;

public class BlockHighlight extends Module {
    public Setting<Float> lines = this.register(new Setting<>("Line:", 1.5f, 0.1f, 5f));
    public Setting<Color> color = color("Color",new Color(0x5151FB));


    public BlockHighlight() {
        super("BlockHighlight", "Draws box at the block that you are looking at", Category.RENDER, true, false, false);
    }

    @Subscribe public void onRender3D(Render3DEvent event) {
        if (mc.crosshairTarget instanceof BlockHitResult result) {
            VoxelShape shape = mc.world.getBlockState(result.getBlockPos()).getOutlineShape(mc.world, result.getBlockPos());
            if (shape.isEmpty()) return;
            Box box = shape.getBoundingBox();
            box = box.offset(result.getBlockPos());
            RenderUtil.drawBox(event.getMatrix(), box, color.getValue(), lines.getValue());
            RenderUtil.drawBoxFilled(event.getMatrix(), box, color.getValue());
        }
    }
}
