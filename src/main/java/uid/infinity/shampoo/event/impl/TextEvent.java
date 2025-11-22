package uid.infinity.shampoo.event.impl;

import uid.infinity.shampoo.event.Event;

public class TextEvent extends Event {
    private String text;
    public TextEvent(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
}
