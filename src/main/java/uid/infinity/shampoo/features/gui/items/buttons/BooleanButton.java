package uid.infinity.shampoo.features.gui.items.buttons;

import uid.infinity.shampoo.features.gui.ShampooGui;
import uid.infinity.shampoo.features.modules.client.ClickGui;
import uid.infinity.shampoo.features.modules.client.Colors;
import uid.infinity.shampoo.features.settings.Setting;
import uid.infinity.shampoo.util.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

public class BooleanButton
        extends Button {
    private final Setting<Boolean> setting;

    public BooleanButton(Setting<Boolean> setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 15;
    }

    @Override
    public void drawScreen(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        ClickGui clickGui = ClickGui.getInstance();
        int idleAlpha = clickGui.alpha1.getValue();
        int hoverAlpha = clickGui.alpha.getValue();

        int enabledIdleColor = Colors.getInstance().getRGB(idleAlpha);
        int enabledHoverColor = Colors.getInstance().getRGB(hoverAlpha);

        RenderUtil.rect(
                context.getMatrices(),
                this.x,
                this.y,
                this.x + (float) this.width + 7.4f,
                this.y + (float) this.height - 0.5f,
                this.getState()
                        ? (!this.isHovering(mouseX, mouseY) ? enabledIdleColor : enabledHoverColor)
                        : (!this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515)
        );
        drawString(this.getName(), this.x + 2.3f, this.y - 1.7f - (float) ShampooGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
    }

    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1f));
        }
    }

    @Override
    public int getHeight() {
        return ClickGui.getInstance().getButtonHeight() - 1 ;
    }

    @Override
    public void toggle() {
        this.setting.setValue(!this.setting.getValue());
    }

    @Override
    public boolean getState() {
        return this.setting.getValue();
    }
}