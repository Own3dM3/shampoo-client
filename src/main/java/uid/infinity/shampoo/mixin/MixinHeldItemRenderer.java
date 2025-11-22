package uid.infinity.shampoo.mixin;

import uid.infinity.shampoo.shampoo;
import uid.infinity.shampoo.event.impl.HeldItemRendererEvent;
import uid.infinity.shampoo.features.Feature;
import uid.infinity.shampoo.features.modules.render.ViewModel;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import static uid.infinity.shampoo.util.traits.Util.EVENT_BUS;
import static uid.infinity.shampoo.util.traits.Util.mc;

@Mixin(HeldItemRenderer.class)
public abstract class MixinHeldItemRenderer {

    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"))
    private void onRenderItem(
            AbstractClientPlayerEntity player,
            float tickDelta,
            float pitch,
            Hand hand,
            float swingProgress,
            ItemStack item,
            float equipProgress,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            CallbackInfo ci
    ) {
        if (Feature.fullNullCheck()) return;
        HeldItemRendererEvent event = new HeldItemRendererEvent(hand, item, equipProgress, matrices);
        EVENT_BUS.post(event);
    }

    private void applyEatOrDrinkTransformationCustom(MatrixStack matrices, float tickDelta, Arm arm, @NotNull ItemStack stack) {
        if (mc.player == null) return;

        float f = (float) mc.player.getItemUseTimeLeft() - tickDelta + 1.0F;
        float g = f / (float) stack.getMaxUseTime(mc.player);
        float h;

        if (g < 0.8F) {
            h = MathHelper.abs(MathHelper.cos(f / 4.0F * (float) Math.PI) * 0.005F);
            matrices.translate(0.0F, h, 0.0F);
        }

        h = 1.0F - (float) Math.pow(g, 27.0);
        int i = arm == Arm.RIGHT ? 1 : -1;
        ViewModel viewModel = shampoo.moduleManager.getModuleByClass(ViewModel.class);

        matrices.translate(
                h * 0.6F * (float) i * viewModel.eatX.getValue(),
                h * -0.5F * viewModel.eatY.getValue(),
                0.0F
        );
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) i * h * 90.0F));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(h * 10.0F));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) i * h * 30.0F));
    }

    @Inject(method = "applyEatOrDrinkTransformation", at = @At("HEAD"), cancellable = true)
    private void applyEatOrDrinkTransformationHook(
            MatrixStack matrices,
            float tickDelta,
            Arm arm,
            ItemStack stack,
            PlayerEntity player,
            CallbackInfo ci
    ) {
        if (shampoo.moduleManager.getModuleByClass(ViewModel.class).isEnabled() &&
                shampoo.moduleManager.getModuleByClass(ViewModel.class).eatAnimation.getValue()) {
            applyEatOrDrinkTransformationCustom(matrices, tickDelta, arm, stack);
            ci.cancel();
        }
    }
}