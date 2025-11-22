package uid.infinity.shampoo.mixin;

import uid.infinity.shampoo.shampoo;
import uid.infinity.shampoo.features.modules.render.Aspect;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    @Shadow
    @Final
    MinecraftClient client;

    @Shadow
    private float lastFovMultiplier;

    @Shadow
    private float fovMultiplier;
    @Shadow private float zoom;

    @Shadow private float zoomX;

    @Shadow private float zoomY;

    // пофикси этот миксин пожалуйста
    @Inject(method = "getBasicProjectionMatrix",at = @At("TAIL"), cancellable = true)
    public void getBasicProjectionMatrix(float fovDegrees, CallbackInfoReturnable<Matrix4f> info) {
        if (shampoo.moduleManager.getModuleByClass(Aspect.class).isEnabled()) {
            MatrixStack matrixStack = new MatrixStack();
            matrixStack.peek().getPositionMatrix().identity();
            if (zoom != 1.0f) {
                matrixStack.translate(zoomX, -zoomY, 0.0f);
                matrixStack.scale(zoom, zoom, 1.0f);
            }

            matrixStack.peek().getPositionMatrix().mul(new Matrix4f().setPerspective((float)(fovDegrees * 0.01745329238474369), (shampoo.moduleManager.getModuleByClass(Aspect.class).ratio.getValue()), 0.05f, fovDegrees * 4.0f));
            info.setReturnValue(matrixStack.peek().getPositionMatrix());
        }
    }
}
