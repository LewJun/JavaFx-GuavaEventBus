package org.example.lewjun.base

import javafx.application.Platform
import javafx.fxml.Initializable
import org.example.lewjun.util.EventBusUtil

abstract class BaseController implements Initializable {
    @Override
    void initialize(URL location, ResourceBundle resources) {

    }

    void postEvent(BaseEvent event) {
        EventBusUtil.post(event)
    }

    void runOnUIThread(RunOnUIThread runOnUIThread) {
        Platform.runLater({
            runOnUIThread.run()
        })
    }

    interface RunOnUIThread {
        void run()
    }
}
