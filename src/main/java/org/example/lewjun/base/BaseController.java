package org.example.lewjun.base;

import javafx.fxml.Initializable;
import org.example.lewjun.util.EventBusUtil;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class BaseController implements Initializable {
    public void postEvent(final BaseEvent event) {
        EventBusUtil.post(event);
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {

    }
}
