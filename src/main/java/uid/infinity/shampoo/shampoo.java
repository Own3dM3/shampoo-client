package uid.infinity.shampoo;

import uid.infinity.shampoo.manager.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uid.infinity.shampoo.manager.*;

public class shampoo implements ModInitializer, ClientModInitializer {
    public static final String NAME = "shampooclient";
    public static final Logger LOGGER = LogManager.getLogger(NAME);
    public static float TIMER = 1.0f;
    public static ServerManager serverManager;
    public static ColorManager colorManager;
    public static PositionManager positionManager;
    public static HoleManager holeManager;
    public static EventManager eventManager;
    public static SpeedManager speedManager;
    public static CommandManager commandManager;
    public static FriendManager friendManager;
    public static ModuleManager moduleManager;
    public static ConfigManager configManager;

    @Override
    public void onInitialize() {
        eventManager = new EventManager();
        serverManager = new ServerManager();
        positionManager = new PositionManager();
        friendManager = new FriendManager();
        colorManager = new ColorManager();
        commandManager = new CommandManager();
        moduleManager = new ModuleManager();
        speedManager = new SpeedManager();
        holeManager = new HoleManager();
    }

    @Override
    public void onInitializeClient() {
        logStartupInfo();

        eventManager.init();
        moduleManager.init();
        configManager = new ConfigManager();
        configManager.load();
        colorManager.init();

        Runtime.getRuntime().addShutdownHook(new Thread(configManager::save));
    }

    private static void logStartupInfo() {
        String uid = System.getProperty("user.name").equalsIgnoreCase("root") ? "1" : "0";
        LOGGER.info("{} {}", NAME, BuildConfig.VERSION);
        LOGGER.info("BUILD: {} #{} ({})", BuildConfig.BUILD_IDENTIFIER, BuildConfig.BUILD_NUMBER, BuildConfig.HASH);
        LOGGER.info("BUILD_TIME: {}", BuildConfig.BUILD_TIME);
        LOGGER.info("UID: {}", uid);
    }
}