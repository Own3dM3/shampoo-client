package uid.infinity.shampoo.event.impl;

import uid.infinity.shampoo.event.Event;
import uid.infinity.shampoo.event.Stage;

public class UpdateWalkingPlayerEvent extends Event {
    private final Stage stage;

    public UpdateWalkingPlayerEvent(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }
}
