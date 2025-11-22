package uid.infinity.shampoo.features.modules.client;

import com.google.common.eventbus.Subscribe;
import uid.infinity.shampoo.shampoo;
import uid.infinity.shampoo.event.impl.ClientEvent;
import uid.infinity.shampoo.features.commands.Command;
import uid.infinity.shampoo.features.gui.ShampooGui;
import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

public class ClickGui extends Module {
    private static ClickGui INSTANCE = new ClickGui();

    public Setting<String> prefix = str("Prefix", ".");
    public Setting<Gear> gear = mode("Gear", Gear.OFF);
    public Setting<Boolean> catcount = bool("CategoryCount", false);
    public Setting<Integer> scrollspeed = num("ScrollSpeed", 10, 1, 100);
    public Setting<Boolean> desk = bool("Desc", false);
    public final Setting<Integer> height = register(new Setting<>("ButtonHeight", 4, 1, 5));
    public Setting<Integer> alpha = num("HoverAlpha", 240, 0, 255);
    public Setting<Integer> alpha1 = num("Alpha", 150, 0, 255);
    public Setting<Boolean> resize = bool("Resize", true);
    public shampoo shampoo;

    public enum Gear {
        OFF, PLUS, GEAR
    }

    public ClickGui() {
        super("GUI", "Opens the click GUI", Module.Category.CLIENT, true, false, false);
        setBind(GLFW.GLFW_KEY_RIGHT_SHIFT);
        this.setInstance();
    }

    public static ClickGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClickGui();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Subscribe
    public void onSettingChange(ClientEvent event) {
        if (event.getStage() == 2 && event.getSetting().getFeature().equals(this)) {
            if (event.getSetting().equals(this.prefix)) {
                shampoo.commandManager.setPrefix(this.prefix.getPlannedValue());
                Command.sendMessage("Prefix set to " + Formatting.DARK_GRAY + shampoo.commandManager.getPrefix());
            }

        }
    }

    @Override
    public void onEnable() {
        if (fullNullCheck()) {
            return;
        }
        mc.setScreen(ShampooGui.getClickGui());
    }

    @Override
    public void onDisable() {
        mc.setScreen(null);
    }

    @Override
    public void onLoad() {
        shampoo.commandManager.setPrefix(this.prefix.getValue());
    }

    @Override
    public void onTick() {
        if (!(ClickGui.mc.currentScreen instanceof ShampooGui)) {
            this.disable();
        }
    }

    public int getButtonHeight() {
        return 11 + height.getValue();
    }
}