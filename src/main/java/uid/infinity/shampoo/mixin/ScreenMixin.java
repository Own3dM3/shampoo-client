package uid.infinity.shampoo.mixin;

import uid.infinity.shampoo.shampoo;
import uid.infinity.shampoo.features.modules.render.NoRender;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class ScreenMixin {
    @Inject(method = "renderInGameBackground", at = @At("HEAD"), cancellable = true)
    private void renderInGameBackground(CallbackInfo callbackInfo) {
        NoRender noRender = shampoo.moduleManager.getModuleByClass(NoRender.class);
        if (noRender.isEnabled() && noRender.gui.getValue())
            callbackInfo.cancel();
    }
}
