package uid.infinity.shampoo.mixin;

import uid.infinity.shampoo.shampoo;
import uid.infinity.shampoo.features.modules.client.Colors;
import uid.infinity.shampoo.features.modules.render.SkyColors;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.awt.*;

@Mixin(BackgroundRenderer.class)
public class MixinBackgroundRenderer
{
    @ModifyArgs(method = "applyFog", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Fog;<init>(FFLnet/minecraft/client/render/FogShape;FFFF)V"))
    private static void applyFog(Args args, Camera camera, BackgroundRenderer.FogType fogType, Vector4f originalColor, float viewDistance, boolean thickenFog, float tickDelta) {
        if (fogType == BackgroundRenderer.FogType.FOG_TERRAIN) {

            args.set(0, viewDistance * 4.0f);
            args.set(1, viewDistance * 4.25f);
        } else {

            if (shampoo.moduleManager.getModuleByClass(SkyColors.class).isEnabled()) {
                Color color = Colors.getInstance().getColor();

                args.set(3, color.getRed() / 255.0f);
                args.set(4, color.getGreen() / 255.0f);
                args.set(5, color.getBlue() / 255.0f);
                args.set(6, color.getAlpha() / 255.0f);
            }
        }
    }
} // пофикси этот миксин пожалуйста