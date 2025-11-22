package uid.infinity.shampoo.features.modules.client;

import uid.infinity.shampoo.shampoo;
import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.util.discord.DiscordEventHandlers;
import uid.infinity.shampoo.util.discord.DiscordRichPresence;
import net.minecraft.client.gui.screen.TitleScreen;
import uid.infinity.shampoo.BuildConfig;
import net.minecraft.client.gui.screen.multiplayer.AddServerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;

public class RPC extends Module {
    public static uid.infinity.shampoo.util.discord.RPC rpc = uid.infinity.shampoo.util.discord.RPC.INSTANCE;
    public static DiscordRichPresence presence = new DiscordRichPresence();
    public static boolean started;
    public static Thread thread;
    public RPC(){
        super("RPC","Display status in discord",Category.CLIENT,true,false,false);
    }

    @Override
    public void onDisable() {
        started = false;
        if (thread != null && !thread.isInterrupted()) {
            thread.interrupt();
        }
        rpc.Discord_Shutdown();
    }

    @Override
    public void onUpdate() {
        startRpc();
    }

    public void startRpc() {
        if (isDisabled()) return;
        if (!started) {
            started = true; //1405418840828936323
            DiscordEventHandlers handlers = new DiscordEventHandlers();
            rpc.Discord_Initialize("1405418840828936323", handlers, true, "");
            presence.startTimestamp = (System.currentTimeMillis() / 1000L);
            presence.largeImageText = shampoo.NAME + " " + BuildConfig.VERSION;
            rpc.Discord_UpdatePresence(presence);
            thread = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    rpc.Discord_RunCallbacks();
                    presence.details = getDetails();
                    presence.state = shampoo.NAME + " " + BuildConfig.VERSION;
                    presence.largeImageKey = "https://i.postimg.cc/MHnpPwP3/223-20250621044828.png";
                    rpc.Discord_UpdatePresence(presence);
                    try {
                        Thread.sleep(2000L);
                    } catch (InterruptedException ignored) {
                    }
                }
            }, "RPC-Handler");
            thread.start();
        }
    }

    private String getDetails() {
        String result = "";
        if (mc.currentScreen instanceof TitleScreen) {
            result = "in title screen";
        } else if (mc.currentScreen instanceof MultiplayerScreen || mc.currentScreen instanceof AddServerScreen) {
            result = "picks a server";
        } else if (mc.getCurrentServerEntry() != null) {
            result = "playing on " + mc.getCurrentServerEntry().address;
        } else if (mc.isInSingleplayer()) {
            result = "in singleplayer";
        }
        return result;
    }
}