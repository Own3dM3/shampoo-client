package uid.infinity.shampoo.manager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import uid.infinity.shampoo.event.impl.Render2DEvent;
import uid.infinity.shampoo.event.impl.Render3DEvent;
import uid.infinity.shampoo.features.Feature;
import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.modules.client.*;
import uid.infinity.shampoo.features.modules.combat.*;
import uid.infinity.shampoo.features.modules.exploit.*;
import uid.infinity.shampoo.features.modules.misc.*;
import uid.infinity.shampoo.features.modules.movement.*;
import uid.infinity.shampoo.features.modules.player.*;
import uid.infinity.shampoo.features.modules.wip.*;
import uid.infinity.shampoo.util.traits.Jsonable;
import uid.infinity.shampoo.util.traits.Util;
import net.minecraft.entity.player.PlayerEntity;
import uid.infinity.shampoo.features.modules.render.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager implements Jsonable, Util {
    public List<Module> modules = new ArrayList<>();
    public List<Module> sortedModules = new ArrayList<>();
    public List<String> sortedModulesABC = new ArrayList<>();

    public void init() {
        modules.add(new HUD());
        modules.add(new ClickGui());
        modules.add(new Avoid());
        modules.add(new Aspect());
        modules.add(new Zoom());
        modules.add(new NoEntityTrace());
        modules.add(new FreeLook());
        modules.add(new Crasher());
        modules.add(new Colors());
        modules.add(new FastDrop());
        modules.add(new NoPacketKick());
        modules.add(new Server());
        modules.add(new Yaw());
        modules.add(new Speed());
        modules.add(new NameProtect());
        modules.add(new ESP());
        modules.add(new Parkour());
        modules.add(new Rotations());
        modules.add(new IceSpeed());
        modules.add(new AntiAim());
        modules.add(new NoBreakDelay());
        modules.add(new TridentFly());
        modules.add(new Sneak());
        modules.add(new TickShift());
        modules.add(new PlayerFollow());
        modules.add(new Trajectory());
        modules.add(new Announcer());
        modules.add(new BlockMixer());
        modules.add(new AutoTotem());
        modules.add(new AutoFish());
        modules.add(new Burrow());
        modules.add(new AutoTrap());
        modules.add(new AutoConcrete());
        modules.add(new Sounds());
        modules.add(new EntityControl());
        modules.add(new RPC());
        modules.add(new FeetPlace());
        modules.add(new FullBright());
        modules.add(new AutoBuilder());
        modules.add(new Criticals());
        modules.add(new MiddleClick());
        modules.add(new NoWeather());
        modules.add(new Step());
        modules.add(new ExtraTab());
        modules.add(new Capes());
        modules.add(new Replenish());
        modules.add(new Crosshair());
        modules.add(new FireWorkHead());
        modules.add(new FastFall());
        modules.add(new FastPlace());
        modules.add(new Velocity());
        modules.add(new BlockHighlight());
        modules.add(new NoFall());
        modules.add(new Sprint());
        modules.add(new AutoWalk());
        modules.add(new AirPlace());
        modules.add(new SkyColors());
        modules.add(new HitboxDesync());
        modules.add(new NoInteract());
        modules.add(new XCarry());
        modules.add(new SafeWalk());
        modules.add(new NoJumpDelay());
        modules.add(new AutoRespawn());
        modules.add(new NoRender());
        modules.add(new AntiHunger());
        modules.add(new UnfocusedFPS());
        modules.add(new AutoCrystal());
        modules.add(new MemCleaner());
        modules.add(new MultiTask());
        modules.add(new NoBob());
        modules.add(new PingSpoof());
        modules.add(new AntiSpam());
        modules.add(new BetterChat());
        modules.add(new KillSound());
        modules.add(new Flight());
        modules.add(new Scaffold());
        modules.add(new Search());
        modules.add(new Aura());
        modules.add(new HoleFill());
        modules.add(new NoNarrator());
        modules.add(new AutoBowRelease());
        modules.add(new FakePlayer());
        modules.add(new HighJump());
        modules.add(new Notifications());
        modules.add(new AutoLog());
        modules.add(new Phase());
        modules.add(new RotationLock());
        modules.add(new HoleESP());
        modules.add(new ViewModel());
        modules.add(new Reach());
        modules.add(new ItemSaver());
        modules.add(new Spammer());
        modules.add(new AntiLevitation());
        modules.add(new KillEffects());
        modules.add(new NoSlow());
        modules.add(new NoRotate());
        modules.add(new AutoExp());
        modules.add(new AntiAFK());
        modules.add(new FakeVanilla());
        modules.add(new ElytraFly());
        modules.add(new SpeedMine());
        modules.add(new ViewClip());
        modules.add(new AutoWeb());
        modules.add(new SelfWeb());
        modules.add(new AntiVanish());
        modules.add(new NoSoundLag());
    }

    public Module getModuleByName(String name) {
        for (Module module : this.modules) {
            if (!module.getName().equalsIgnoreCase(name)) continue;
            return module;
        }
        return null;
    }

    public <T extends Module> T getModuleByClass(Class<T> clazz) {
        for (Module module : this.modules) {
            if (!clazz.isInstance(module)) continue;
            return (T) module;
        }
        return null;
    }

    public void enableModule(Class<Module> clazz) {
        Module module = this.getModuleByClass(clazz);
        if (module != null) {
            module.enable();
        }
    }

    public void disableModule(Class<Module> clazz) {
        Module module = this.getModuleByClass(clazz);
        if (module != null) {
            module.disable();
        }
    }

    public void enableModule(String name) {
        Module module = this.getModuleByName(name);
        if (module != null) {
            module.enable();
        }
    }

    public void disableModule(String name) {
        Module module = this.getModuleByName(name);
        if (module != null) {
            module.disable();
        }
    }

    public boolean isModuleEnabled(String name) {
        Module module = this.getModuleByName(name);
        return module != null && module.isOn();
    }

    public boolean isModuleEnabled(Class<Module> clazz) {
        Module module = this.getModuleByClass(clazz);
        return module != null && module.isOn();
    }

    public Module getModuleByDisplayName(String displayName) {
        for (Module module : this.modules) {
            if (!module.getDisplayName().equalsIgnoreCase(displayName)) continue;
            return module;
        }
        return null;
    }

    public ArrayList<Module> getEnabledModules() {
        ArrayList<Module> enabledModules = new ArrayList<>();
        for (Module module : this.modules) {
            if (!module.isEnabled()) continue;
            enabledModules.add(module);
        }
        return enabledModules;
    }

    public ArrayList<String> getEnabledModulesName() {
        ArrayList<String> enabledModules = new ArrayList<>();
        for (Module module : this.modules) {
            if (!module.isEnabled() || !module.isDrawn()) continue;
            enabledModules.add(module.getFullArrayString());
        }
        return enabledModules;
    }

    public ArrayList<Module> getModulesByCategory(Module.Category category) {
        ArrayList<Module> modulesCategory = new ArrayList<Module>();
        this.modules.forEach(module -> {
            if (module.getCategory() == category) {
                modulesCategory.add(module);
            }
        });
        return modulesCategory;
    }

    public List<Module.Category> getCategories() {
        return Arrays.asList(Module.Category.values());
    }

    public void onLoad() {
        this.modules.stream().filter(Module::listening).forEach(EVENT_BUS::register);
        this.modules.forEach(Module::onLoad);
    }

    public void onUpdate() {
        this.modules.stream().filter(Feature::isEnabled).forEach(Module::onUpdate);
    }

    public void onTick() {
        this.modules.stream().filter(Feature::isEnabled).forEach(Module::onTick);
    }
    public void onDeath(PlayerEntity player) {
        this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onDeath(player));
    }

    public void onRender2D(Render2DEvent event) {
        this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender2D(event));
    }

    public void onRender3D(Render3DEvent event) {
        this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender3D(event));
    }

    public void sortModules(boolean reverse) {
        this.sortedModules = this.getEnabledModules().stream().filter(Module::isDrawn)
                .sorted(Comparator.comparing(module -> mc.textRenderer.getWidth(module.getFullArrayString()) * (reverse ? -1 : 1)))
                .collect(Collectors.toList());
    }

    public void sortModulesABC() {
        this.sortedModulesABC = new ArrayList<>(this.getEnabledModulesName());
        this.sortedModulesABC.sort(String.CASE_INSENSITIVE_ORDER);
    }

    public void onUnload() {
        this.modules.forEach(EVENT_BUS::unregister);
        this.modules.forEach(Module::onUnload);
    }

    public void onUnloadPost() {
        for (Module module : this.modules) {
            module.enabled.setValue(false);
        }
    }

    public void onKeyPressed(int eventKey) {
        if (eventKey <= 0 || mc.currentScreen != null) return;
        this.modules.forEach(module -> {
            if (module.getBind().getKey() == eventKey) {
                module.toggle();
            }
        });
    }

    @Override public JsonElement toJson() {
        JsonObject object = new JsonObject();
        for (Module module : modules) {
            object.add(module.getName(), module.toJson());
        }
        return object;
    }

    @Override public void fromJson(JsonElement element) {
        for (Module module : modules) {
            module.fromJson(element.getAsJsonObject().get(module.getName()));
        }
    }

    @Override public String getFileName() {
        return "modules.json";
    }
}
