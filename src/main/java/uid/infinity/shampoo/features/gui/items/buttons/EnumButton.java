package uid.infinity.shampoo.features.gui.items.buttons;

import uid.infinity.shampoo.features.gui.ShampooGui;
import uid.infinity.shampoo.features.modules.client.ClickGui;
import uid.infinity.shampoo.features.modules.client.Colors;
import uid.infinity.shampoo.features.settings.Setting;
import uid.infinity.shampoo.util.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Formatting;
import java.util.Objects;

public class EnumButton
        extends Button {
    public Setting<Enum<?>> setting;

    public EnumButton(Setting<Enum<?>> setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 15;
    }

    @Override
    public void drawScreen(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        ClickGui clickGui = ClickGui.getInstance();
        int idleAlpha = clickGui.alpha1.getValue();   // альфа, когда модуль ВЫКЛЮЧЕН и не hovered
        int hoverAlpha = clickGui.alpha.getValue();   // альфа, когда модуль ВЫКЛЮЧЕН, но hovered

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
        drawString(this.setting.getName() + " " + Formatting.GRAY + (this.setting.currentEnumName().equalsIgnoreCase("ABC") ? "ABC" : this.setting.currentEnumName()), this.x + 2.3f, this.y - 1.7f - (float) ShampooGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);

        int y = (int) this.y;

        if (setting.open) {
            for (Object o : setting.getValue().getClass().getEnumConstants()) {

                y += 12;
                String s = !Objects.equals(o.toString(), "ABC") ? Character.toUpperCase(o.toString().charAt(0)) + o.toString().toLowerCase().substring(1) : o.toString();

                drawString((setting.currentEnumName().equals(s) ? Formatting.WHITE : Formatting.GRAY) + s, width / 2.0f - mc.textRenderer.getWidth(s) / 2.0f + 2.0f + x, y + (12 / 2f) - (mc.textRenderer.fontHeight / 2f) + 3.5f, -1);
            }
        }
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
        if (mouseButton == 1 && isHovering(mouseX, mouseY)) {
            setting.open = !setting.open;
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1f));
        }

        if (setting.open) {
            for (Object o : setting.getValue().getClass().getEnumConstants()) {
                y += 12;
                if (mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + 12 + 3.5f && mouseButton == 0) {
                    setting.setEnumValue(String.valueOf(o));
                    mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1f));
                }
            }
        }
    }

    @Override
    public int getHeight() {
        return ClickGui.getInstance().getButtonHeight() - 1;
    }

    @Override
    public void toggle() {
        this.setting.increaseEnum();
    }

    @Override
    public boolean getState() {
        return true;
    }
}