package uid.infinity.shampoo.util.discord.callbacks;

import com.sun.jna.Callback;
import uid.infinity.shampoo.util.discord.DiscordUser;

public interface ReadyCallback extends Callback {
    void apply(final DiscordUser p0);
}
