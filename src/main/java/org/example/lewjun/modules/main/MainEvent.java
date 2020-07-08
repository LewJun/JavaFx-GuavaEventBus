package org.example.lewjun.modules.main;

import org.example.lewjun.base.BaseEvent;

public class MainEvent extends BaseEvent {
    String aac001;
    String aac002;

    public MainEvent(String aac001, String aac002) {
        this.aac001 = aac001;
        this.aac002 = aac002;
    }
}
