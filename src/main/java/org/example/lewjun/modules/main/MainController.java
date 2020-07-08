package org.example.lewjun.modules.main;

import com.google.common.eventbus.Subscribe;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.example.lewjun.base.BaseController;
import org.example.lewjun.modules.ac01.Ac01App;
import org.example.lewjun.modules.ac01.Ac01Event;
import org.example.lewjun.modules.ac02.Ac02App;
import org.example.lewjun.modules.ac02.Ac02Event;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController extends BaseController {
    public Label lbl;
    public Button btnAc01;
    public Button btnAc02;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        btnAc01.setOnAction(event ->
                new Ac01App()
                        .setTitle("Ac01")
                        .initData(new Ac01Event("ac01"))
                        .showAndWait()
        );

        btnAc02.setOnAction(event ->
                new Ac02App()
                        .setTitle("Ac02")
                        .initData(new Ac02Event("ac02"))
                        .showAndWait()
        );
    }

    /**
     * 订阅MainEvent
     */
    @Subscribe
    private void subscribeMainEvent(final MainEvent event) {
        lbl.setText(String.format("%s, %s", event.aac001, event.aac002));
    }

    public void openDialog(final ActionEvent actionEvent) {
        System.out.println("openDialog");
    }

}
