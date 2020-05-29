package org.example.lewjun.modules.ac02

import com.google.common.eventbus.Subscribe
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import org.example.lewjun.base.BaseController
import org.example.lewjun.modules.main.MainEvent

class Ac02Controller extends BaseController {

    public Label lbl
    public TextField txtGreet
    public Button btnSave

    @Override
    void initialize(final URL location, final ResourceBundle resources) {
        btnSave.setOnAction({ event ->
            postEvent(new MainEvent(aac001: txtGreet.getText(), aac002: thisObject.toString()))
        })
    }

    /**
     * 订阅Ac02Event
     */
    @Subscribe
    private void subscribeAc02Event(final Ac02Event event) {
        System.out.println(event.userId)
        lbl.setText(String.valueOf(event.userId))
    }
}
