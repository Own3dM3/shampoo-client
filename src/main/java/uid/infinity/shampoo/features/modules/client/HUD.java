package uid.infinity.shampoo.features.modules.client;

import net.minecraft.client.network.*;
import uid.infinity.shampoo.BuildConfig;
import uid.infinity.shampoo.shampoo;
import uid.infinity.shampoo.event.impl.Render2DEvent;
import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;
import uid.infinity.shampoo.util.TextUtil;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import java.awt.*;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class HUD extends Module {

    private final Setting<Boolean> watermark = bool("WaterMark", true);
    private final Setting<WatermarkType> watermarkType = mode("WatermarkType", WatermarkType.DEFAULT);
    private final Setting<String> customWatermarkText = register(new Setting<>("CustomWatermark", "yourwatermark"));
    private final Setting<Boolean> showVersion = bool("ShowVersion", true);
    private final Setting<Boolean> uid = bool("Uid", true);
    private final Setting<Boolean> greter = bool("Greater", true);
    private final Setting<Boolean> arraylist = bool("ArrayList", true);
    private final Setting<Boolean> gradient = bool("Gradient", true);

    private final Setting<Boolean> lagcompIndicator = bool("LagcompIndicator", true);
    private final Setting<Integer> lagcompTicks = register(new Setting<>("LagcompTicks", 13, 0, 64));
    private final Setting<Float> lagcompProgress = register(new Setting<>("LagcompProgress", 0.75f, 0.0f, 1.0f));

    private long lastTickTime = System.currentTimeMillis();
    private float tickPhase = 0.0f;

    private final String BASE_1 = "shampoo";
    private final String BASE_2 = "shampooclient";
    private final String BASE_3 = "selfinflictionskid";
    private final String BASE_4 = "Shoreline (dev-6-9ad4b76+modified)";
    private final String BASE_5 = "burger.cc 0.9-18hGwfy-beta";

    public HUD() {
        super("HUD", "Custom HUD with global color scheme", Category.CLIENT, true, false, false);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        if (fullNullCheck()) return;

        int width = mc.getWindow().getScaledWidth();
        AtomicInteger y = new AtomicInteger();

        Color primary = Colors.getInstance().getColor();
        Color secondary = primary;

        if (watermark.getValue()) {
            String displayText = getWatermarkText();
            if (gradient.getValue()) {
                TextUtil.drawStringPulse(event.getContext(), displayText, 2, 2, primary, secondary, 1, 10);
            } else {
                event.getContext().drawTextWithShadow(mc.textRenderer, displayText, 2, 2, secondary.getRGB());
            }
        }

        if (uid.getValue()) {
            String uidStr = System.getProperty("user.name").equalsIgnoreCase("root") ? "1" : "0";
            String text = "uid " + Formatting.WHITE + uidStr;
            int yOff = watermark.getValue() ? 13 : 2;
            if (gradient.getValue()) {
                TextUtil.drawStringPulse(event.getContext(), text, 2, yOff, primary, secondary, 1, 10);
            } else {
                event.getContext().drawTextWithShadow(mc.textRenderer, text, 2, yOff, secondary.getRGB());
            }
        }

        if (greter.getValue()) {
            String greetText = "good to see you, " + Formatting.WHITE + mc.player.getName().getString();
            int greetX = (width / 2) - mc.textRenderer.getWidth(greetText) / 2;
            if (gradient.getValue()) {
                TextUtil.drawStringPulse(event.getContext(), greetText, greetX, 2, primary, secondary, 1, 10);
            } else {
                event.getContext().drawTextWithShadow(mc.textRenderer, greetText, greetX, 2, secondary.getRGB());
            }
        }

        if (arraylist.getValue()) {
            y.set(0);
            shampoo.moduleManager.getEnabledModules().stream()
                    .filter(Module::isDrawn)
                    .sorted(Comparator.comparing(module -> -mc.textRenderer.getWidth(module.getFullArrayString())))
                    .collect(Collectors.toList())
                    .forEach(module -> {
                        String str = module.getName() + Formatting.GRAY +
                                (module.getDisplayInfo() != null ? " [" + Formatting.WHITE + module.getDisplayInfo() + Formatting.GRAY + "]" : "");

                        if (gradient.getValue()) {
                            TextUtil.drawStringPulse(event.getContext(), str,
                                    (int) (width - mc.textRenderer.getWidth(str) - 2f),
                                    (2 + y.getAndIncrement() * 10),
                                    primary, secondary, 1, 10);
                        } else {
                            event.getContext().drawTextWithShadow(mc.textRenderer, str,
                                    (int) (width - mc.textRenderer.getWidth(str) - 2f),
                                    (2 + y.getAndIncrement() * 10),
                                    secondary.getRGB());
                        }
                    });
        }

        if (lagcompIndicator.getValue()) {
            renderLagcompIndicator(event, 2, 30);
        }
    }

    private void renderLagcompIndicator(Render2DEvent event, int x, int y) {
        Color primary = Colors.getInstance().getColor();
        Color panelBg = new Color(0, 0, 0, 180);
        Color red = new Color(255, 50, 50);
        Color white = Color.WHITE;

        int panelWidth = 100;
        int panelHeight = 60;
        int barHeight = 4;
        int barY = y + 40;

        event.getContext().fill(x, y, x + panelWidth, y + panelHeight, panelBg.getRGB());

        event.getContext().fill(x, y, x + 2, y + panelHeight, primary.getRGB());

        event.getContext().drawTextWithShadow(mc.textRenderer, "Defensive", x + 6, y + 4, white.getRGB());

        event.getContext().drawTextWithShadow(mc.textRenderer, "Lagcomp", x + 6, y + 16, white.getRGB());

        int ping = 0;
        if (mc.player != null && mc.getNetworkHandler() != null) {
            PlayerListEntry entry = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
            if (entry != null) {
                ping = entry.getLatency();
            }
        }
        String ticksText = ping + " ms";

        event.getContext().drawTextWithShadow(mc.textRenderer, ticksText, x + 6, y + 28, white.getRGB());

        float maxPing = 200f;
        float progress = Math.min(1.0f, ping / maxPing);
        int barWidth = (int) (panelWidth - 12);
        int filledWidth = (int) (barWidth * progress);

        event.getContext().fill(x + 6, barY, x + 6 + barWidth, barY + barHeight, new Color(50, 50, 50).getRGB());
        event.getContext().fill(x + 6, barY, x + 6 + filledWidth, barY + barHeight, red.getRGB());
    }

    private String getWatermarkText() {
        return switch (watermarkType.getValue()) {
            case DEFAULT -> shampoo.NAME + Formatting.WHITE + " " + BuildConfig.VERSION;
            case CUSTOM1 -> BASE_1 + (showVersion.getValue() ? " " + BuildConfig.VERSION : "");
            case CUSTOM2 -> BASE_2 + (showVersion.getValue() ? " " + BuildConfig.VERSION : "");
            case CUSTOM3 -> BASE_3 + (showVersion.getValue() ? " " + BuildConfig.VERSION : "");
            case CUSTOM4 -> BASE_4 + (showVersion.getValue() ? " " + BuildConfig.VERSION : "");
            case CUSTOM5 -> BASE_5 + (showVersion.getValue() ? " " + BuildConfig.VERSION : "");
            case CUSTOM_INPUT -> {
                String input = customWatermarkText.getValue();
                int spaceIndex = input.indexOf(' ');
                if (spaceIndex != -1) {
                    String before = input.substring(0, spaceIndex);
                    String after = input.substring(spaceIndex);
                    yield before + Formatting.WHITE + after;
                } else {
                    yield input;
                }
            }
            case BUILD_INFO -> {
                String ver = BuildConfig.VERSION;
                String id = BuildConfig.BUILD_IDENTIFIER;
                String num = BuildConfig.BUILD_NUMBER;
                String hashShort = BuildConfig.HASH.length() > 7 ? BuildConfig.HASH.substring(0, 7) : BuildConfig.HASH;

                yield shampoo.NAME + Formatting.WHITE + " " + ver +
                        Formatting.DARK_GRAY + "-" + Formatting.GRAY + id +
                        Formatting.DARK_GRAY + " (" + Formatting.GRAY + num + Formatting.DARK_GRAY + ")" +
                        Formatting.DARK_GRAY + " [" + Formatting.GRAY + hashShort + Formatting.DARK_GRAY + "]";
            }
        };
    }

    public String getDirection4D() {
        return switch (getYaw4D()) {
            case 0 -> "South (+Z)";
            case 1 -> "West (-X)";
            case 2 -> "North (-Z)";
            case 3 -> "East (+X)";
            default -> "Loading...";
        };
    }

    private int getYaw4D() {
        return MathHelper.floor((double) (mc.player.getYaw() * 4.0f / 360.0f) + 0.5) & 3;
    }

    public enum WatermarkType {
        DEFAULT,
        CUSTOM1,
        CUSTOM2,
        CUSTOM3,
        CUSTOM4,
        CUSTOM5,
        CUSTOM_INPUT,
        BUILD_INFO
    }
}