package org.example.lewjun.base;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import org.example.lewjun.util.EventBusUtil;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class BaseController implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void postEvent(BaseEvent event) {
        EventBusUtil.post(event);
    }

    public void runOnUIThread(RunOnUIThread runOnUIThread) {
        Platform.runLater(runOnUIThread::run);
    }

    interface RunOnUIThread {
        void run();
    }
}
