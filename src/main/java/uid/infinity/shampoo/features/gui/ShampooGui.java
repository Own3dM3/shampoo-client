package uid.infinity.shampoo.features.gui;

import uid.infinity.shampoo.shampoo;
import uid.infinity.shampoo.features.Feature;
import uid.infinity.shampoo.features.gui.items.Item;
import uid.infinity.shampoo.features.gui.items.buttons.ModuleButton;
import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.modules.client.ClickGui;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import java.util.ArrayList;
import java.util.Comparator;

public class ShampooGui extends Screen {
    private static ShampooGui INSTANCE;

    static {
        INSTANCE = new ShampooGui();
    }

    private final ArrayList<Component> components = new ArrayList<>();

    public ShampooGui() {
        super(Text.literal("shampoo"));
        setInstance();
        load();
    }

    public static ShampooGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ShampooGui();
        }
        return INSTANCE;
    }

    public static ShampooGui getClickGui() {
        return ShampooGui.getInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    private void load() {
        int x = -84;
        for (final Module.Category category : shampoo.moduleManager.getCategories()) {
            this.components.add(new Component(category.getName(), x += 90, 4, true) {
                @Override
                public void setupItems() {
                    shampoo.moduleManager.getModulesByCategory(category).forEach(module -> {
                        if (!module.hidden) {
                            this.addButton(new ModuleButton(module));
                        }
                    });
                }
            });
        }
        this.components.forEach(component -> component.getItems().sort(Comparator.comparing(Feature::getName)));
    }

    public void updateModule(Module module) {
        for (Component component : this.components) {
            for (Item item : component.getItems()) {
                if (!(item instanceof ModuleButton)) continue;
                ModuleButton button = (ModuleButton) item;
                Module mod = button.getModule();
                if (module == null || !module.equals(mod)) continue;
                button.initSettings();
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        Item.context = context;
        this.components.forEach(component -> component.drawScreen(context, mouseX, mouseY, delta));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int clickedButton) {
        this.components.forEach(component -> component.mouseClicked((int) mouseX, (int) mouseY, clickedButton));
        return super.mouseClicked(mouseX, mouseY, clickedButton);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int releaseButton) {
        this.components.forEach(component -> component.mouseReleased((int) mouseX, (int) mouseY, releaseButton));
        return super.mouseReleased(mouseX, mouseY, releaseButton);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int speed = ClickGui.getInstance().scrollspeed.getValue();
        if (verticalAmount < 0) {
            this.components.forEach(component -> component.setY(component.getY() - speed));
        } else if (verticalAmount > 0) {
            this.components.forEach(component -> component.setY(component.getY() + speed));
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        this.components.forEach(component -> component.onKeyPressed(keyCode));
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        this.components.forEach(component -> component.onKeyTyped(chr, modifiers));
        return super.charTyped(chr, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    public final ArrayList<Component> getComponents() {
        return this.components;
    }

    public int getTextOffset() {
        return -6;
    }

    public Component getComponentByName(String name) {
        for (Component component : this.components) {
            if (!component.getName().equalsIgnoreCase(name)) continue;
            return component;
        }
        return null;
    }
}