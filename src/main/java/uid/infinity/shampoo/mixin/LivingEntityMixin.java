package uid.infinity.shampoo.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import uid.infinity.shampoo.shampoo;
import uid.infinity.shampoo.features.modules.exploit.AntiLevitation;
import uid.infinity.shampoo.features.modules.render.NoRender;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "spawnItemParticles", at = @At("HEAD"), cancellable = true)
    private void spawnItemParticles(ItemStack stack, int count, CallbackInfo info) {
        NoRender noRender = shampoo.moduleManager.getModuleByClass(NoRender.class);
        if (noRender.isEnabled() && stack.getComponents().contains(DataComponentTypes.FOOD) && noRender.eatparticle.getValue())
            info.cancel();
    }

    @ModifyExpressionValue(method = "travelMidAir", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getStatusEffect(Lnet/minecraft/registry/entry/RegistryEntry;)Lnet/minecraft/entity/effect/StatusEffectInstance;"))
    private StatusEffectInstance travelMidAir$getStatusEffect(StatusEffectInstance original) {
        AntiLevitation antiLevitation = shampoo.moduleManager.getModuleByClass(AntiLevitation.class);
        if (antiLevitation.isEnabled()) return null;
        return original;

    }
    @ModifyExpressionValue(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;hasStatusEffect(Lnet/minecraft/registry/entry/RegistryEntry;)Z", ordinal = 1))
    private boolean tickMovement$hasStatusEffect(boolean original) {
        AntiLevitation antiLevitation = shampoo.moduleManager.getModuleByClass(AntiLevitation.class);
        if (antiLevitation.isEnabled()) return false;
        return original;
    }
    }




