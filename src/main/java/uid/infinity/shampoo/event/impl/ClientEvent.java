package uid.infinity.shampoo.event.impl;

import uid.infinity.shampoo.event.Event;
import uid.infinity.shampoo.features.Feature;
import uid.infinity.shampoo.features.settings.Setting;

public class ClientEvent extends Event {
    private Feature feature;
    private Setting<?> setting;
    private int stage;

    public ClientEvent(int stage, Feature feature) {
        this.stage = stage;
        this.feature = feature;
    }

    public ClientEvent(Setting<?> setting) {
        this.stage = 2;
        this.setting = setting;
    }

    public Feature getFeature() {
        return this.feature;
    }

    public Setting<?> getSetting() {
        return this.setting;
    }

    public int getStage() {
        return stage;
    }
}
