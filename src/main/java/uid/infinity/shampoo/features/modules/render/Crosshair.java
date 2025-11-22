package uid.infinity.shampoo.features.modules.render;

import uid.infinity.shampoo.event.impl.Render2DEvent;
import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;
import uid.infinity.shampoo.util.RenderUtil;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import java.awt.*;

public class Crosshair extends Module {
    // нахуя тут дескрипшоны??
    private final Setting<Float> thickness = register(new Setting<>("Thickness", 2.0f, 0.5f, 10.0f, "Толщина лучей крестика."));
    private final Setting<Float> length = register(new Setting<>("Length", 8.0f, 1.0f, 20.0f, "Длина лучей крестика."));
    private final Setting<Float> gap = register(new Setting<>("Gap", 2.0f, 0.0f, 10.0f, "Расстояние от центра до начала лучей."));
    private final Setting<Boolean> dynamic = register(new Setting<>("Dynamic", true, "Увеличивать зазор при движении/атаке."));
    private final Setting<Boolean> outline = register(new Setting<>("Outline", true, "Рисовать чёрную обводку."));
    private final Setting<Boolean> dotEnabled = register(new Setting<>("Dot", true, "Показывать точку в центре прицела."));
    private final Setting<Float> dotSize = register(new Setting<>("DotSize", 2.0f, 0.5f, 10.0f, v -> dotEnabled.getValue(), "Размер центральной точки."));
    private final Setting<Boolean> blockColorDot = register(new Setting<>("BlockColorDot", false, "Точка красная на Obsidian/Bedrock, зелёная на других."));
    private final Setting<Boolean> renderTop = register(new Setting<>("TopRay", true, "Рисовать верхний луч."));
    private final Setting<Boolean> renderRight = register(new Setting<>("RightRay", true, "Рисовать правый луч."));
    private final Setting<Boolean> renderBottom = register(new Setting<>("BottomRay", true, "Рисовать нижний луч."));
    private final Setting<Boolean> renderLeft = register(new Setting<>("LeftRay", true, "Рисовать левый луч."));
    private final Setting<Boolean> renderSideBars = register(new Setting<>("SideBars", true, "Показывать мигающие палочки слева и справа."));
    private final Setting<Float> sideBarWidth = register(new Setting<>("BarWidth", 1.0f, 0.5f, 5.0f, "Ширина палочек."));
    private final Setting<Float> sideBarLength = register(new Setting<>("BarLength", 12.0f, 4.0f, 20.0f, "Длина палочек."));
    private final Setting<Float> sideBarOffset = register(new Setting<>("BarOffset", 10.0f, 5.0f, 30.0f, "Отступ от центра до палочек."));
    private final Setting<Color> sideBarColor = register(new Setting<>("BarColor", new Color(0xFFFFFF), "Цвет мигающих палочек."));
    private final Setting<Boolean> sideBarsOnlyMode = register(new Setting<>("SideBarsOnly", false, "Отключить ВСЁ, кроме боковых палочек."));

    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public Crosshair() {
        super("Crosshair", "Crosshair like CS:GO", Category.RENDER, true, false, false);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        if (fullNullCheck()) return;

        DrawContext drawContext = event.getContext();
        MatrixStack matrices = drawContext.getMatrices();

        int scaledWidth = mc.getWindow().getScaledWidth();
        int scaledHeight = mc.getWindow().getScaledHeight();

        float centerX = scaledWidth / 2.0f;
        float centerY = scaledHeight / 2.0f;

        if (sideBarsOnlyMode.getValue()) {
            renderSideBars(centerX, centerY, matrices);
            return;
        }

        float t = thickness.getValue() / 2f;
        float l = length.getValue();
        float g = gap.getValue() + (dynamic.getValue() && isMoving() ? 2.0f : 0.0f);

        Color color = Color.WHITE;

        if (renderTop.getValue()) {
            RenderUtil.renderQuad(matrices, centerX - t, centerY - l - g, centerX + t, centerY - g, color);
            if (outline.getValue()) {
                RenderUtil.renderOutline(matrices, centerX - t, centerY - l - g, centerX + t, centerY - g, Color.BLACK);
            }
        }

        if (renderRight.getValue()) {
            RenderUtil.renderQuad(matrices, centerX + g, centerY - t, centerX + g + l, centerY + t, color);
            if (outline.getValue()) {
                RenderUtil.renderOutline(matrices, centerX + g, centerY - t, centerX + g + l, centerY + t, Color.BLACK);
            }
        }

        if (renderBottom.getValue()) {
            RenderUtil.renderQuad(matrices, centerX - t, centerY + g, centerX + t, centerY + g + l, color);
            if (outline.getValue()) {
                RenderUtil.renderOutline(matrices, centerX - t, centerY + g, centerX + t, centerY + g + l, Color.BLACK);
            }
        }

        if (renderLeft.getValue()) {
            RenderUtil.renderQuad(matrices, centerX - g - l, centerY - t, centerX - g, centerY + t, color);
            if (outline.getValue()) {
                RenderUtil.renderOutline(matrices, centerX - g - l, centerY - t, centerX - g, centerY + t, Color.BLACK);
            }
        }

        Color dotColor = Color.WHITE;
        if (dotEnabled.getValue() && blockColorDot.getValue() && mc.player != null && mc.world != null) {
            HitResult hit = mc.player.raycast(20.0, 0.0f, false);
            if (hit.getType() == HitResult.Type.BLOCK) {
                BlockPos blockPos = ((BlockHitResult) hit).getBlockPos();
                var block = mc.world.getBlockState(blockPos).getBlock();

                if (block == Blocks.OBSIDIAN || block == Blocks.BEDROCK) {
                    dotColor = Color.RED;
                } else {
                    dotColor = Color.GREEN;
                }
            } else {
                dotColor = Color.GREEN;
            }
        }

        if (dotEnabled.getValue()) {
            float ds = dotSize.getValue();
            float halfDs = ds / 2f;
            RenderUtil.renderQuad(matrices, centerX - halfDs, centerY - halfDs, centerX + halfDs, centerY + halfDs, dotColor);
            if (outline.getValue()) {
                RenderUtil.renderOutline(matrices, centerX - halfDs, centerY - halfDs, centerX + halfDs, centerY + halfDs, Color.BLACK);
            }
        }

        renderSideBars(centerX, centerY, matrices);
    }

    private void renderSideBars(float centerX, float centerY, MatrixStack matrices) {
        if (!renderSideBars.getValue()) return;

        boolean flash = (System.currentTimeMillis() % 100) < 50;

        float barWidth = sideBarWidth.getValue();
        float barLength = sideBarLength.getValue();
        float barOffset = sideBarOffset.getValue();
        Color barColor = sideBarColor.getValue();

        if (flash) {
            RenderUtil.renderQuad(matrices,
                    centerX - barOffset - barWidth / 2f,
                    centerY - barLength / 2f,
                    centerX - barOffset + barWidth / 2f,
                    centerY + barLength / 2f,
                    barColor);

            RenderUtil.renderQuad(matrices,
                    centerX + barOffset - barWidth / 2f,
                    centerY - barLength / 2f,
                    centerX + barOffset + barWidth / 2f,
                    centerY + barLength / 2f,
                    barColor);
        }
    }

    private boolean isMoving() {
        return false;
    }
}