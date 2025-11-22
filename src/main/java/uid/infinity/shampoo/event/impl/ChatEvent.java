package uid.infinity.shampoo.event.impl;

import uid.infinity.shampoo.event.Event;

public class ChatEvent extends Event {
    private final String content;

    public ChatEvent(String content) {
        this.content = content;
    }

    public String getMessage() {
        return content;
    }
}
