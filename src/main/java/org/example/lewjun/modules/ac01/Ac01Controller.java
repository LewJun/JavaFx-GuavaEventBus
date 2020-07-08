package org.example.lewjun.modules.ac01;

import com.google.common.eventbus.Subscribe;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.lewjun.base.BaseController;
import org.example.lewjun.modules.main.MainEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class Ac01Controller extends BaseController {
    public Label lbl;
    public TextField txtGreet;
    public Button btnSave;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        btnSave.setOnAction( event ->
            postEvent(new MainEvent( txtGreet.getText(), Ac01Controller.this.toString()))
        );
    }

    /**
     * 订阅Ac01Event
     */
    @Subscribe
    private void subscribeAc01Event(final Ac01Event event) {
        System.out.println(event.userId);
        lbl.setText(String.valueOf(event.userId));
    }
}
