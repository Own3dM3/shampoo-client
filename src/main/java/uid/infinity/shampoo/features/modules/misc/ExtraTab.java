package uid.infinity.shampoo.features.modules.misc;

import net.minecraft.util.*;
import uid.infinity.shampoo.event.impl.*;
import uid.infinity.shampoo.shampoo;
import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.text.Text;

public class ExtraTab extends Module {

    private static ExtraTab INSTANCE = new ExtraTab();

    public final Setting<Boolean> self = register(new Setting<>("Self", false));
    public final Setting<Boolean> friends = register(new Setting<>("Friends", false));

    public ExtraTab() {
        super("ExtraTab", "Highlights yourself and friends in the tab list", Module.Category.MISC, true, false, false);
        setInstance();
    }

    public static ExtraTab getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ExtraTab();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Subscribe
    public void onPlayerListName(PlayerListNameEvent event) {
        String originalName = event.getPlayerName().getString();
        String[] nameParts = originalName.split(" ");

        if (self.getValue()) {
            for (String part : nameParts) {
                String clean = stripFormatting(part);
                if (clean.equalsIgnoreCase(mc.getGameProfile().getName())) {
                    event.cancel();
                    event.setPlayerName(Text.literal(Formatting.DARK_PURPLE + originalName));
                    return;
                }
            }
        }

        if (friends.getValue()) {
            for (String part : nameParts) {
                String clean = stripFormatting(part);
                if (shampoo.friendManager.isFriend(clean)) {
                    event.cancel();
                    event.setPlayerName(Text.literal(Formatting.AQUA + originalName));
                    return;
                }
            }
        }
    }

    private String stripFormatting(String input) {
        StringBuilder result = new StringBuilder();
        boolean skipNext = false;
        for (char c : input.toCharArray()) {
            if (skipNext) {
                skipNext = false;
                continue;
            }
            if (c == Formatting.FORMATTING_CODE_PREFIX) {
                skipNext = true;
                continue;
            }
            result.append(c);
        }
        return result.toString();
    }
}