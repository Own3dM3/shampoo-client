package uid.infinity.shampoo.event.impl;

import uid.infinity.shampoo.event.Event;
import uid.infinity.shampoo.event.Stage;

public class StageEvent extends Event {

    private Stage stage;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
