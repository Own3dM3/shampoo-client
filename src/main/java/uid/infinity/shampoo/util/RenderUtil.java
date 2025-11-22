package uid.infinity.shampoo.util;

import com.mojang.blaze3d.vertex.VertexFormat;
import uid.infinity.shampoo.util.traits.Util;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.*;
import org.joml.Matrix4f;
import java.awt.*;

public class RenderUtil implements Util {

    public static void rect(MatrixStack stack, float x1, float y1, float x2, float y2, int color) {
        rectFilled(stack, x1, y1, x2, y2, color);
    }

    public static void rect(MatrixStack stack, float x1, float y1, float x2, float y2, int color, float width) {
        drawHorizontalLine(stack, x1, x2, y1, color, width);
        drawVerticalLine(stack, x2, y1, y2, color, width);
        drawHorizontalLine(stack, x1, x2, y2, color, width);
        drawVerticalLine(stack, x1, y1, y2, color, width);
    }

    public static void renderQuad(MatrixStack matrices, float left, float top, float right, float bottom, Color color) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        buffer.vertex(matrix, left, top, 0.0f).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        buffer.vertex(matrix, left, bottom, 0.0f).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        buffer.vertex(matrix, right, bottom, 0.0f).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        buffer.vertex(matrix, right, top, 0.0f).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        Layers.getGlobalQuads().draw(buffer.end());
    }

    public static void renderOutline(MatrixStack matrices, float left, float top, float right, float bottom, Color color) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        buffer.vertex(matrix, left, top, 0.0f).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        buffer.vertex(matrix, left, bottom, 0.0f).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        buffer.vertex(matrix, left + 0.5f, bottom, 0.0f).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        buffer.vertex(matrix, left + 0.5f, top, 0.0f).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        buffer.vertex(matrix, right - 0.5f, top, 0.0f).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        buffer.vertex(matrix, right - 0.5f, bottom, 0.0f).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        buffer.vertex(matrix, right, bottom, 0.0f).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        buffer.vertex(matrix, right, top, 0.0f).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        buffer.vertex(matrix, left, bottom - 0.5f, 0.0f).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        buffer.vertex(matrix, left, bottom, 0.0f).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        buffer.vertex(matrix, right, bottom, 0.0f).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        buffer.vertex(matrix, right, bottom - 0.5f, 0.0f).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        buffer.vertex(matrix, left, top, 0.0f).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        buffer.vertex(matrix, left, top + 0.5f, 0.0f).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        buffer.vertex(matrix, right, top + 0.5f, 0.0f).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        buffer.vertex(matrix, right, top, 0.0f).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        Layers.getGlobalQuads().draw(buffer.end());
    }

    public static void drawThickLine(MatrixStack matrices, float x1, float y1, float x2, float y2, float thickness, int color) {
        float a = color / 255f;
        float r = color / 255f;
        float g = color / 255f;
        float b = color / 255f;

        float dx = x2 - x1;
        float dy = y2 - y1;
        float length = (float) Math.sqrt(dx * dx + dy * dy);
        if (length == 0) return;
        dx /= length;
        dy /= length;

        float px = -dy * thickness / 2;
        float py = dx * thickness / 2;

        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        buffer.vertex(matrix, x1 + px, y1 + py, 0).color(r, g, b, a);
        buffer.vertex(matrix, x1 - px, y1 - py, 0).color(r, g, b, a);
        buffer.vertex(matrix, x2 + px, y2 + py, 0).color(r, g, b, a);
        buffer.vertex(matrix, x2 - px, y2 - py, 0).color(r, g, b, a);

        Layers.getGlobalTriangleStrip().draw((Shape) buffer.end());
    }

    protected static void drawHorizontalLine(MatrixStack matrices, float x1, float x2, float y, int color) {
        if (x2 < x1) {
            float i = x1;
            x1 = x2;
            x2 = i;
        }

        rectFilled(matrices, x1, y, x2 + 1, y + 1, color);
    }

    protected static void drawVerticalLine(MatrixStack matrices, float x, float y1, float y2, int color) {
        if (y2 < y1) {
            float i = y1;
            y1 = y2;
            y2 = i;
        }

        rectFilled(matrices, x, y1 + 1, x + 1, y2, color);
    }

    protected static void drawHorizontalLine(MatrixStack matrices, float x1, float x2, float y, int color, float width) {
        if (x2 < x1) {
            float i = x1;
            x1 = x2;
            x2 = i;
        }

        rectFilled(matrices, x1, y, x2 + width, y + width, color);
    }

    protected static void drawVerticalLine(MatrixStack matrices, float x, float y1, float y2, int color, float width) {
        if (y2 < y1) {
            float i = y1;
            y1 = y2;
            y2 = i;
        }

        rectFilled(matrices, x, y1 + width, x + width, y2, color);
    }

    public static void rectFilled(MatrixStack matrix, float x1, float y1, float x2, float y2, int color) {

        if (x1 > x2) {
            float temp = x1;
            x1 = x2;
            x2 = temp;
        }
        if (y1 > y2) {
            float temp = y1;
            y1 = y2;
            y2 = temp;
        }

        float f = (float) (color >> 24 & 255) / 255.0F;
        float g = (float) (color >> 16 & 255) / 255.0F;
        float h = (float) (color >> 8 & 255) / 255.0F;
        float j = (float) (color & 255) / 255.0F;

        BufferBuilder bufferBuilder = Tessellator.getInstance()
                .begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        Matrix4f mat = matrix.peek().getPositionMatrix();
        bufferBuilder.vertex(mat, x1, y1, 0.0F).color(g, h, j, f);
        bufferBuilder.vertex(mat, x1, y2, 0.0F).color(g, h, j, f);
        bufferBuilder.vertex(mat, x2, y2, 0.0F).color(g, h, j, f);
        bufferBuilder.vertex(mat, x2, y1, 0.0F).color(g, h, j, f);

        Layers.getGlobalQuads().draw(bufferBuilder.end());
    }

    public static void drawBoxFilled(MatrixStack stack, Box box, Color c) {
        float minX = (float) (box.minX - mc.getEntityRenderDispatcher().camera.getPos().getX());
        float minY = (float) (box.minY - mc.getEntityRenderDispatcher().camera.getPos().getY());
        float minZ = (float) (box.minZ - mc.getEntityRenderDispatcher().camera.getPos().getZ());
        float maxX = (float) (box.maxX - mc.getEntityRenderDispatcher().camera.getPos().getX());
        float maxY = (float) (box.maxY - mc.getEntityRenderDispatcher().camera.getPos().getY());
        float maxZ = (float) (box.maxZ - mc.getEntityRenderDispatcher().camera.getPos().getZ());

        BufferBuilder bufferBuilder = Tessellator.getInstance()
                .begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, minY, minZ).color(c.getRGB());
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, minY, minZ).color(c.getRGB());
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, minY, maxZ).color(c.getRGB());
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, minY, maxZ).color(c.getRGB());

        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, maxY, minZ).color(c.getRGB());
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, maxY, maxZ).color(c.getRGB());
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, maxY, maxZ).color(c.getRGB());
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, maxY, minZ).color(c.getRGB());

        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, minY, minZ).color(c.getRGB());
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, maxY, minZ).color(c.getRGB());
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, maxY, minZ).color(c.getRGB());
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, minY, minZ).color(c.getRGB());

        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, minY, minZ).color(c.getRGB());
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, maxY, minZ).color(c.getRGB());
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, maxY, maxZ).color(c.getRGB());
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, minY, maxZ).color(c.getRGB());

        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, minY, maxZ).color(c.getRGB());
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, minY, maxZ).color(c.getRGB());
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), maxX, maxY, maxZ).color(c.getRGB());
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, maxY, maxZ).color(c.getRGB());

        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, minY, minZ).color(c.getRGB());
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, minY, maxZ).color(c.getRGB());
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, maxY, maxZ).color(c.getRGB());
        bufferBuilder.vertex(stack.peek().getPositionMatrix(), minX, maxY, minZ).color(c.getRGB());

        Layers.getGlobalQuads().draw(bufferBuilder.end());
    }

    public static void drawBoxFilled(MatrixStack stack, Vec3d vec, Color c) {
        drawBoxFilled(stack, Box.from(vec), c);
    }

    public static void drawBoxFilled(MatrixStack stack, BlockPos bp, Color c) {
        drawBoxFilled(stack, new Box(bp), c);
    }

    public static void drawBox(MatrixStack stack, Box box, Color c, double lineWidth) {
        float minX = (float) (box.minX - mc.getEntityRenderDispatcher().camera.getPos().getX());
        float minY = (float) (box.minY - mc.getEntityRenderDispatcher().camera.getPos().getY());
        float minZ = (float) (box.minZ - mc.getEntityRenderDispatcher().camera.getPos().getZ());
        float maxX = (float) (box.maxX - mc.getEntityRenderDispatcher().camera.getPos().getX());
        float maxY = (float) (box.maxY - mc.getEntityRenderDispatcher().camera.getPos().getY());
        float maxZ = (float) (box.maxZ - mc.getEntityRenderDispatcher().camera.getPos().getZ());

        BufferBuilder bufferBuilder = Tessellator.getInstance()
                .begin(VertexFormat.DrawMode.LINES, VertexFormats.POSITION_COLOR_NORMAL);

        VertexRendering.drawBox(stack, bufferBuilder, minX, minY, minZ, maxX, maxY, maxZ,
                c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);

        Layers.getGlobalLines(lineWidth).draw(bufferBuilder.end());
    }

    public static void drawBox(MatrixStack stack, Vec3d vec, Color c, double lineWidth) {
        drawBox(stack, Box.from(vec), c, lineWidth);
    }

    public static void drawBox(MatrixStack stack, BlockPos bp, Color c, double lineWidth) {
        drawBox(stack, new Box(bp), c, lineWidth);
    }

    public static MatrixStack matrixFrom(Vec3d pos) {
        MatrixStack matrices = new MatrixStack();
        Camera camera = mc.gameRenderer.getCamera();
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));
        matrices.translate(pos.getX() - camera.getPos().x, pos.getY() - camera.getPos().y, pos.getZ() - camera.getPos().z);
        return matrices;
    }
}