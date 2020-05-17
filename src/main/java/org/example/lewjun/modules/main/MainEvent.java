package org.example.lewjun.modules.main;

import org.example.lewjun.base.BaseEvent;

public class MainEvent extends BaseEvent {
    public String aac001;
    public String aac002;

    public MainEvent() {
    }

    public MainEvent(final String aac001, final String aac002) {
        this();
        this.aac001 = aac001;
        this.aac002 = aac002;
    }
}
