package uid.infinity.shampoo.mixin;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.buffers.*;
import org.joml.*;
import uid.infinity.shampoo.shampoo;
import uid.infinity.shampoo.event.impl.Render3DEvent;
import uid.infinity.shampoo.features.modules.render.NoRender;
import net.minecraft.client.render.*;
import net.minecraft.client.util.ObjectAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import static uid.infinity.shampoo.util.traits.Util.EVENT_BUS;
import static uid.infinity.shampoo.util.traits.Util.mc;
// пофикси этот миксин пожалуйста
@Mixin( WorldRenderer.class )
public class MixinWorldRenderer {
    @Inject(method = "render", at = @At("RETURN"))
    private void render(ObjectAllocator allocator, RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, Matrix4f positionMatrix, Matrix4f projectionMatrix, CallbackInfo ci, @Local Profiler profiler) {
        MatrixStack stack = new MatrixStack();
        stack.push();
        stack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(mc.gameRenderer.getCamera().getPitch()));
        stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(mc.gameRenderer.getCamera().getYaw() + 180f));

        profiler.push("oyvey-render-3d");

        Render3DEvent event = new Render3DEvent(stack, tickCounter.getTickProgress(true));
        EVENT_BUS.post(event);
        stack.pop();
        profiler.pop();
    }
    @Inject(method = "renderWeather", at = @At("HEAD"), cancellable = true)
    private void renderWeatherHook(FrameGraphBuilder frameGraphBuilder, Vec3d cameraPos, float tickProgress, Fog fog, CallbackInfo ci) {
        NoRender noRender = shampoo.moduleManager.getModuleByClass(NoRender.class);
        if (noRender.isEnabled()&& noRender.weather.getValue())
            ci.cancel();
    }
}