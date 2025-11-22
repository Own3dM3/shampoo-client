package uid.infinity.shampoo.manager;

import uid.infinity.shampoo.features.modules.client.Colors;

import java.awt.Color;

public class ColorManager {
    private Color baseColor = new Color(121, 135, 242);

    public void init() {
        Color userColor = Colors.getInstance().getColor();
        this.baseColor = new Color(userColor.getRed(), userColor.getGreen(), userColor.getBlue());
    }

    public void setBaseColor(Color color) {
        this.baseColor = new Color(color.getRed(), color.getGreen(), color.getBlue());
    }

    public void setBaseColor(int red, int green, int blue) {
        this.baseColor = new Color(red, green, blue);
    }

    public Color getBaseColor() {
        return baseColor;
    }

    public Color getColor() {
        return baseColor;
    }

    public int getRGBA() {
        return (255 << 24) | (baseColor.getRed() << 16) | (baseColor.getGreen() << 8) | baseColor.getBlue();
    }

    public int getRGBA(int alpha) {
        alpha = Math.max(0, Math.min(255, alpha));
        return (alpha << 24) | (baseColor.getRed() << 16) | (baseColor.getGreen() << 8) | baseColor.getBlue();
    }

    public int getColorAsInt() {
        return getRGBA();
    }

    public int getColorWithAlpha(int alpha) {
        return getRGBA(alpha);
    }

    public float getRedF() {
        return baseColor.getRed() / 255.0f;
    }

    public float getGreenF() {
        return baseColor.getGreen() / 255.0f;
    }

    public float getBlueF() {
        return baseColor.getBlue() / 255.0f;
    }
}