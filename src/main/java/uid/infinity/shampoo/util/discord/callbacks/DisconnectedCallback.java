package uid.infinity.shampoo.util.discord.callbacks;

import com.sun.jna.Callback;

public interface DisconnectedCallback extends Callback {
    void apply(final int p0, final String p1);
}
