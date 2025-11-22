package uid.infinity.shampoo.features.gui.items.buttons;

import uid.infinity.shampoo.features.modules.client.*;
import uid.infinity.shampoo.shampoo;
import uid.infinity.shampoo.features.gui.Component;
import uid.infinity.shampoo.features.gui.ShampooGui;
import uid.infinity.shampoo.features.gui.items.Item;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import uid.infinity.shampoo.util.RenderUtil;

public class Button extends Item {
    private boolean state;
    private Sounds sounds = shampoo.moduleManager.getModuleByClass(Sounds.class);

    public Button(String name) {
        super(name);
        this.height = ClickGui.getInstance().getButtonHeight();
    }

    @Override
    public void drawScreen(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        Colors colors = Colors.getInstance();
        int baseColor = colors.getRGB(200);
        int hoverColor = colors.getRGB(230);

        RenderUtil.rect(
                context.getMatrices(),
                this.x,
                this.y,
                this.x + (float) this.width,
                this.y + (float) this.height - 0.5f,
                this.getState()
                        ? (!this.isHovering(mouseX, mouseY) ? baseColor : hoverColor)
                        : (!this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515)
        );

        drawString(
                this.getName(),
                this.x + 2.3f,
                this.y - 3.0f - (float) ShampooGui.getClickGui().getTextOffset(),
                this.getState() ? -1 : -5592406
        );
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
            this.onMouseClick();
        }
    }

    public void onMouseClick() {
        this.state = !this.state;
        this.toggle();

        if (sounds.isEnabled()) {
            if (sounds.en.getValue() && this.state) {
                SoundEvent soundEvent = SoundEvent.of(Identifier.of("ive", "enable_sound"));
                mc.player.playSound(soundEvent, 1f, 1f);
                return;
            }

            if (sounds.di.getValue() && !this.state) {
                SoundEvent soundEvent = SoundEvent.of(Identifier.of("ive", "disable_sound"));
                mc.player.playSound(soundEvent, 1f, 1f);
                return;
            }
        }

        mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1f));
    }

    public void toggle() {
    }

    public boolean getState() {
        return this.state;
    }

    @Override
    public int getHeight() {
        return ClickGui.getInstance().getButtonHeight() + 2;
    }

    public boolean isHovering(int mouseX, int mouseY) {
        for (Component component : ShampooGui.getClickGui().getComponents()) {
            if (!component.drag) continue;
            return false;
        }
        return (float) mouseX >= this.getX() &&
                (float) mouseX <= this.getX() + (float) this.getWidth() &&
                (float) mouseY >= this.getY() &&
                (float) mouseY <= this.getY() + (float) this.height;
    }
}