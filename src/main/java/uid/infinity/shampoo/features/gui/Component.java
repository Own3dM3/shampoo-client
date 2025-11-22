package uid.infinity.shampoo.features.gui;

import uid.infinity.shampoo.features.Feature;
import uid.infinity.shampoo.features.gui.items.Item;
import uid.infinity.shampoo.features.gui.items.buttons.Button;
import uid.infinity.shampoo.features.modules.client.ClickGui;
import uid.infinity.shampoo.features.modules.client.Colors;
import uid.infinity.shampoo.util.Animation;
import uid.infinity.shampoo.util.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Component extends Feature {
    public static int[] counter1 = new int[]{1};
    private final Animation animation;
    protected DrawContext context;
    private final List<Item> items = new ArrayList<>();
    public boolean drag;

    private boolean resizing = false;
    private int resizeStartX, resizeStartY;
    private int originalWidth, originalHeight;
    private static final int RESIZE_HANDLE_SIZE = 8;

    private int x;
    private int y;
    private int x2;
    private int y2;
    private int width;
    private int height;
    private boolean open;
    private boolean hidden = false;

    public Component(String name, int x, int y, boolean open) {
        super(name);
        this.animation = new Animation(Animation.Easing.SINE_OUT, 200);
        this.x = x;
        this.y = y;
        this.width = 88;
        this.height = ClickGui.getInstance().getButtonHeight() + 3;
        this.open = open;
        this.setupItems();
    }

    public void setupItems() {
    }

    private void drag(int mouseX, int mouseY) {
        if (!this.drag) {
            return;
        }
        this.x = this.x2 + mouseX;
        this.y = this.y2 + mouseY;
    }

    private void resize(int mouseX, int mouseY) {
        if (!resizing || !open || !ClickGui.getInstance().resize.getValue()) return;

        int deltaX = mouseX - resizeStartX;
        int deltaY = mouseY - resizeStartY;

        int newWidth = originalWidth + deltaX;
        int newHeight = originalHeight + deltaY;

        newWidth = Math.max(80, newWidth);
        newHeight = Math.max(18, newHeight);

        int finalWidth = newWidth;
        int finalHeight = newHeight;
        ShampooGui.getClickGui().getComponents().stream()
                .filter(c -> c.open)
                .forEach(c -> {
                    c.width = finalWidth;
                    c.height = finalHeight;
                });
    }

    private boolean isResizing(int mouseX, int mouseY) {
        float totalItemHeight = getTotalItemHeight();
        int handleX = x + width - RESIZE_HANDLE_SIZE;
        int handleY = y + height + (int) totalItemHeight - RESIZE_HANDLE_SIZE;
        return mouseX >= handleX && mouseX <= x + width &&
                mouseY >= handleY && mouseY <= y + height + (int) totalItemHeight;
    }

    public void drawScreen(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        this.context = context;
        this.drag(mouseX, mouseY);
        this.resize(mouseX, mouseY);
        counter1 = new int[]{1};

        float totalItemHeight = this.open ? this.getTotalItemHeight() - 2.0f : 0.0f;
        int color = Colors.getInstance().getRGB(255);
        context.fill(this.x, this.y - 1, this.x + this.width, this.y + this.height - 6, color);

        if (this.open) {
            RenderUtil.rect(context.getMatrices(),
                    (float) this.x,
                    (float) this.y + 12.5f,
                    (float) (this.x + this.width),
                    (float) ((float) (this.y + this.height) + totalItemHeight * animation.getEase()),
                    new Color(1, 1, 1, ClickGui.getInstance().alpha1.getValue()).getRGB());

            if (ClickGui.getInstance().resize.getValue()) {
                float contentBottom = (float) (y + height + totalItemHeight * animation.getEase());
                Color handleColor = new Color(color).darker();
                context.fill(
                        x + width - RESIZE_HANDLE_SIZE,
                        (int) contentBottom - RESIZE_HANDLE_SIZE,
                        x + width,
                        (int) contentBottom,
                        handleColor.getRGB()
                );
            }
        }

        if (ClickGui.getInstance().catcount.getValue())
            drawString("[" + this.getItems().size() + "]",
                    this.x - 1.0f + (float) this.width - mc.textRenderer.getWidth("[" + this.getItems().size() + "]"),
                    this.y + 2.5f, -1);

        drawString(this.getName(), (float) this.x + 3.0f, (float) this.y - 4.0f - (float) ShampooGui.getClickGui().getTextOffset(), -1);

        if (this.open) {
            float y = (float) (this.getY() + this.getHeight()) - 3.0f;
            for (Item item : this.getItems()) {
                Component.counter1[0] = counter1[0] + 1;
                if (item.isHidden()) continue;

                item.setLocation((float) this.x + 2.0f, y);
                item.setWidth(this.getWidth() - 4);

                item.drawScreen(context, mouseX, mouseY, partialTicks);

                y += (float) item.getHeight() + 1.5f;
            }
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
            this.x2 = this.x - mouseX;
            this.y2 = this.y - mouseY;
            ShampooGui.getClickGui().getComponents().forEach(component -> component.drag = false);
            this.drag = true;
            return;
        }

        if (mouseButton == 0 && open && ClickGui.getInstance().resize.getValue() && isResizing(mouseX, mouseY)) {
            ShampooGui.getClickGui().getComponents().forEach(c -> c.resizing = false);
            this.resizing = true;
            this.resizeStartX = mouseX;
            this.resizeStartY = mouseY;
            this.originalWidth = this.width;
            this.originalHeight = this.height;
            return;
        }

        if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
            this.open = !this.open;
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1f));
            return;
        }

        if (!this.open) {
            return;
        }

        this.getItems().forEach(item -> item.mouseClicked(mouseX, mouseY, mouseButton));
    }

    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        if (releaseButton == 0) {
            this.drag = false;
            this.resizing = false;
        }

        if (!this.open) {
            return;
        }

        this.getItems().forEach(item -> item.mouseReleased(mouseX, mouseY, releaseButton));
    }

    public void onKeyTyped(char typedChar, int keyCode) {
        if (!this.open) {
            return;
        }
        this.getItems().forEach(item -> item.onKeyTyped(typedChar, keyCode));
    }

    public void onKeyPressed(int key) {
        if (!open) return;
        this.getItems().forEach(item -> item.onKeyPressed(key));
    }

    public void addButton(Button button) {
        this.items.add(button);
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isOpen() {
        return this.open;
    }

    public final List<Item> getItems() {
        return this.items;
    }

    private boolean isHovering(int mouseX, int mouseY) {
        return mouseX >= this.getX() && mouseX <= this.getX() + this.getWidth() &&
                mouseY >= this.getY() && mouseY <= this.getY() + this.getHeight() - (this.open ? 2 : 0);
    }

    private float getTotalItemHeight() {
        float height = 0.0f;
        for (Item item : this.getItems()) {
            height += (float) item.getHeight() + 1.5f;
        }
        return height;
    }

    protected void drawString(String text, double x, double y, Color color) {
        drawString(text, x, y, color.hashCode());
    }

    protected void drawString(String text, double x, double y, int color) {
        context.drawTextWithShadow(mc.textRenderer, text, (int) x, (int) y, color);
    }
}