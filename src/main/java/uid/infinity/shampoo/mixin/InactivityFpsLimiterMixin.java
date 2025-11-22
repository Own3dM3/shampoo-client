package uid.infinity.shampoo.mixin;

import uid.infinity.shampoo.shampoo;
import uid.infinity.shampoo.features.modules.misc.UnfocusedFPS;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.InactivityFpsLimiter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InactivityFpsLimiter.class)
public class InactivityFpsLimiterMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
     private void render(CallbackInfoReturnable<Integer> info) {
        UnfocusedFPS unfocusedFPS = shampoo.moduleManager.getModuleByClass(UnfocusedFPS.class);
        if (unfocusedFPS.isEnabled() && !client.isWindowFocused()) {
            info.setReturnValue(shampoo.moduleManager.getModuleByClass(UnfocusedFPS.class).limit.getValue().intValue());
        }
    }
}
