package uid.infinity.shampoo.mixin;

import uid.infinity.shampoo.shampoo;
import uid.infinity.shampoo.features.modules.client.Colors;
import uid.infinity.shampoo.features.modules.render.SkyColors;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.awt.Color;

@Mixin(ClientWorld.class)
public abstract class MixinClientWorld {

    @Inject(method = "getSkyColor", at = @At("HEAD"), cancellable = true)
    private void getSkyColor(Vec3d cameraPos, float tickDelta, CallbackInfoReturnable<Integer> info) {
        if (shampoo.moduleManager.getModuleByClass(SkyColors.class).isEnabled()) {
            Color color = Colors.getInstance().getColor();
            info.setReturnValue(color.getRGB());
        }
    }
}