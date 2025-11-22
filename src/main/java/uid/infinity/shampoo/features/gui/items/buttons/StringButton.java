package uid.infinity.shampoo.features.gui.items.buttons;

import uid.infinity.shampoo.features.gui.ShampooGui;
import uid.infinity.shampoo.features.modules.client.ClickGui;
import uid.infinity.shampoo.features.modules.client.Colors;
import uid.infinity.shampoo.features.settings.Setting;
import uid.infinity.shampoo.util.RenderUtil;
import uid.infinity.shampoo.util.models.Timer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

public class StringButton
        extends Button {
    private static final Timer idleTimer = new Timer();
    private static boolean idle;
    private final Setting<String> setting;
    public boolean isListening;
    private CurrentString currentString = new CurrentString("");

    public StringButton(Setting<String> setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 15;
    }

    public static String removeLastChar(String str) {
        String output = "";
        if (str != null && str.length() > 0) {
            output = str.substring(0, str.length() - 1);
        }
        return output;
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
        if (this.isListening) {
            drawString(this.currentString.string() + "_", this.x + 2.3f, this.y - 1.7f - (float) ShampooGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
        } else {
            drawString((this.setting.getName().equals("Buttons") ? "Buttons " : (this.setting.getName().equals("Prefix") ? "Prefix  " + Formatting.GRAY : "")) + this.setting.getValue(), this.x + 2.3f, this.y - 1.7f - (float) ShampooGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1f));
        }
    }

    @Override
    public void onKeyTyped(char typedChar, int keyCode) {
        if (this.isListening) {
            this.setString(this.currentString.string() + typedChar);
        }
    }

    @Override public void onKeyPressed(int key) {
        if (isListening) {
            switch (key) {
                case GLFW.GLFW_KEY_ENTER: {
                    this.enterString();
                }
                case GLFW.GLFW_KEY_BACKSPACE: {
                    this.setString(StringButton.removeLastChar(this.currentString.string()));
                }
            }
        }
    }

    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }

    private void enterString() {
        if (this.currentString.string().isEmpty()) {
            this.setting.setValue(this.setting.getDefaultValue());
        } else {
            this.setting.setValue(this.currentString.string());
        }
        this.setString("");
        this.onMouseClick();
    }

    @Override
    public int getHeight() {
        return ClickGui.getInstance().getButtonHeight() - 1;
    }

    @Override
    public void toggle() {
        this.isListening = !this.isListening;
    }

    @Override
    public boolean getState() {
        return !this.isListening;
    }

    public void setString(String newString) {
        this.currentString = new CurrentString(newString);
    }

    public static String getIdleSign() {
        if (idleTimer.passedMs(500)) {
            idle = !idle;
            idleTimer.reset();
        }
        if (idle) return "_";
        return "";
    }

    public record CurrentString(String string) {
    }
}