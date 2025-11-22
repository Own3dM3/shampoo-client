package uid.infinity.shampoo.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;
import uid.infinity.shampoo.features.modules.client.Colors;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;

import static uid.infinity.shampoo.util.traits.Util.mc;

public class CaptureMark {
    private static float espValue = 1f, lastEspValue;
    private static float espSpeed = 1f;
    private static boolean flipSpeed;

    public static void render(Entity target) {
        Camera camera = mc.gameRenderer.getCamera();

        double tPosX = interpolate(target.lastX, target.getX(), getTickDelta()) - camera.getPos().x;
        double tPosY = interpolate(target.lastY, target.getY(), getTickDelta()) - camera.getPos().y;
        double tPosZ = interpolate(target.lastZ, target.getZ(), getTickDelta()) - camera.getPos().z;

        MatrixStack matrices = new MatrixStack();
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));
        matrices.translate(tPosX, (tPosY + target.getEyeHeight(target.getPose()) / 2f), tPosZ);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.getYaw()));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(interpolateFloat(lastEspValue, espValue, getTickDelta())));
        matrices.translate(-0.75, -0.75, -0.01);
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        int color = Colors.getInstance().getRGB();
        buffer.vertex(matrix, 0, 1.5f, 0).texture(0f, 1f).color(color);
        buffer.vertex(matrix, 1.5f, 1.5f, 0).texture(1f, 1f).color(color);
        buffer.vertex(matrix, 1.5f, 0, 0).texture(1f, 0).color(color);
        buffer.vertex(matrix, 0, 0, 0).texture(0, 0).color(color);
        Layers.getGlobalQuads().draw(buffer.end());
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }

    public static void tick() {
        lastEspValue = espValue;
        espValue += espSpeed;
        if (espSpeed > 25) flipSpeed = true;
        if (espSpeed < -25) flipSpeed = false;
        espSpeed = flipSpeed ? espSpeed - 0.5f : espSpeed + 0.5f;
    }
    public static double interpolate(double oldValue, double newValue, double interpolationValue) {
        return (oldValue + (newValue - oldValue) * interpolationValue);
    }

    public static float interpolateFloat(float oldValue, float newValue, double interpolationValue) {
        return (float) interpolate(oldValue, newValue, (float) interpolationValue);
    }
    
    private static float getTickDelta(){
        return mc.getRenderTickCounter().getTickProgress(true);
    }
}