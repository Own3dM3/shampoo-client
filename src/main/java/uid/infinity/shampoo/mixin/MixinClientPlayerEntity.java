package uid.infinity.shampoo.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import uid.infinity.shampoo.shampoo;
import uid.infinity.shampoo.event.Stage;
import uid.infinity.shampoo.event.impl.UpdateEvent;
import uid.infinity.shampoo.event.impl.UpdateWalkingPlayerEvent;
import uid.infinity.shampoo.features.modules.movement.NoSlow;
import uid.infinity.shampoo.features.modules.player.Velocity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import static uid.infinity.shampoo.util.traits.Util.EVENT_BUS;
import static uid.infinity.shampoo.util.traits.Util.mc;

@Mixin(ClientPlayerEntity.class)
public class MixinClientPlayerEntity {
    @Inject(method = "tick", at = @At("TAIL"))
    private void tickHook(CallbackInfo ci) {
        EVENT_BUS.post(new UpdateEvent());
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;tick()V", shift = At.Shift.AFTER))
    private void tickHook2(CallbackInfo ci) {
        EVENT_BUS.post(new UpdateWalkingPlayerEvent(Stage.PRE));
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;sendMovementPackets()V", shift = At.Shift.AFTER))
    private void tickHook3(CallbackInfo ci) {
        EVENT_BUS.post(new UpdateWalkingPlayerEvent(Stage.POST));
    }
    @Inject(method = "pushOutOfBlocks", at = @At("HEAD"), cancellable = true)
    private void pushOutOfBlocks(double x, double z, CallbackInfo callbackInfo) {
        Velocity velocity = shampoo.moduleManager.getModuleByClass(Velocity.class);
        if (velocity.isEnabled() && velocity.blockPush.getValue()) {
            callbackInfo.cancel();
        }

    }
    @ModifyExpressionValue(method = "tickNausea", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;currentScreen:Lnet/minecraft/client/gui/screen/Screen;"))
    private Screen tickNausea(Screen original) {
        NoSlow noSlow = shampoo.moduleManager.getModuleByClass(NoSlow.class);
        if (noSlow.isEnabled() && noSlow.portals.getValue()) return null;
        return original;
    }
    @ModifyExpressionValue(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"))
    private boolean tickMovement$isUsingItem(boolean original) {
        NoSlow noSlow = shampoo.moduleManager.getModuleByClass(NoSlow.class);
        if (noSlow.isEnabled() && noSlow.food.getValue()) {
            mc.player.setSprinting(true);
            return false;
        }

        return original;
    }
    @ModifyExpressionValue(method = "canStartSprinting", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"))
    private boolean canStartSprinting(boolean original) {
        NoSlow noSlow = shampoo.moduleManager.getModuleByClass(NoSlow.class);
        if (noSlow.isEnabled() && noSlow.food.getValue()) {
            mc.player.setSprinting(true);
            return false;
        }
        return original;
    }



}



