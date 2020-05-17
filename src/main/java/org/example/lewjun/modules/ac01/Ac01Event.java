package org.example.lewjun.modules.ac01;

import org.example.lewjun.base.BaseEvent;

public class Ac01Event extends BaseEvent {
    public String userId;

    public Ac01Event(final String userId) {
        this.userId = userId;
    }
}
