package org.example.lewjun.base;

import javafx.fxml.Initializable;
import org.example.lewjun.util.EventBusUtil;

public abstract class BaseController implements Initializable {
    public void postEvent(final BaseEvent event) {
        EventBusUtil.post(event);
    }
}
