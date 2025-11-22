package uid.infinity.shampoo.mixin;

import uid.infinity.shampoo.shampoo;
import uid.infinity.shampoo.features.modules.render.NoRender;
import uid.infinity.shampoo.features.modules.render.ViewClip;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Camera.class)
public class CameraMixin {
    @Inject(method = "getSubmersionType", at = @At("HEAD"), cancellable = true)
    private void getSubmergedFluidState(CallbackInfoReturnable<CameraSubmersionType> ci) {
        NoRender noRender = shampoo.moduleManager.getModuleByClass(NoRender.class);
        ci.setReturnValue(CameraSubmersionType.NONE);
        if (noRender.isEnabled()&& noRender.liquidOverlay.getValue())
            ci.cancel();
    }
    @ModifyArgs(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;clipToSpace(F)F"))
    private void update(Args args) {
        ViewClip viewClip = shampoo.moduleManager.getModuleByClass(ViewClip.class);
        if (viewClip.isEnabled()&& viewClip.player.getValue())
            args.set(0, shampoo.moduleManager.getModuleByClass(ViewClip.class).range.getValue().floatValue());
    }
    @Inject(method = "clipToSpace", at = @At("HEAD"), cancellable = true)
    private void aVoid(float f, CallbackInfoReturnable<Float> cir) {
        ViewClip viewClip = shampoo.moduleManager.getModuleByClass(ViewClip.class);
        if (viewClip.isEnabled())
            cir.setReturnValue(f);
    }
}
