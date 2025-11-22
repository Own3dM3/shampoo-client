package uid.infinity.shampoo.mixin;

import uid.infinity.shampoo.shampoo;
import uid.infinity.shampoo.features.modules.exploit.Reach;
import uid.infinity.shampoo.features.modules.movement.SafeWalk;
import uid.infinity.shampoo.features.modules.movement.Sprint;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import static uid.infinity.shampoo.util.traits.Util.mc;

@Mixin(value = PlayerEntity.class, priority = 800)
public class MixinPlayerEntity {
   @Inject(method = "clipAtLedge", at = @At("HEAD"), cancellable = true)
   private void clipAtLedge(CallbackInfoReturnable<Boolean> info) {
       SafeWalk safeWalk = shampoo.moduleManager.getModuleByClass(SafeWalk.class);
       if (safeWalk.isEnabled())
           info.setReturnValue(true);
   }
   @Inject(method = "getBlockInteractionRange", at = @At("HEAD"), cancellable = true)
   public void getBlockInteractionRange(CallbackInfoReturnable<Double> cir) {
       Reach reach = shampoo.moduleManager.getModuleByClass(Reach.class);
       if (reach.isEnabled())
       cir.setReturnValue(reach.range.getValue());
   }
   @Inject(method = "getEntityInteractionRange", at = @At("HEAD"), cancellable = true)
   public void getEntityInteractionRange(CallbackInfoReturnable<Double> cir) {
       Reach reach = shampoo.moduleManager.getModuleByClass(Reach.class);
       if (reach.isEnabled())
           cir.setReturnValue(reach.range.getValue());
   }
   @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setSprinting(Z)V", shift = At.Shift.AFTER))
   private void attack(CallbackInfo callbackInfo) {
       Sprint sprint = shampoo.moduleManager.getModuleByClass(Sprint.class);
       float multiplier = 0.6f + 0.4f * sprint.motion.getValue().floatValue();
       mc.player.setVelocity(mc.player.getVelocity().x / 0.6 * multiplier, mc.player.getVelocity().y, mc.player.getVelocity().z / 0.6 * multiplier);
       mc.player.setSprinting(true);
   }
    }