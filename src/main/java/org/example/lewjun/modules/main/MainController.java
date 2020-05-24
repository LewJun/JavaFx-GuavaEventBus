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
import org.example.lewjun.modules.dialog.DialogApp;
import org.example.lewjun.util.ApiUrl;
import org.example.lewjun.util.HttpUtil;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MainController extends BaseController {

    public Button btnAc01;
    public Label lbl;
    public Button btnAc02;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {

        btnAc01.setOnAction(event -> new Ac01App().setTitle("Ac01").initData(new Ac01Event("ac01")).showAndWait());

        btnAc02.setOnAction(event -> new Ac02App().setTitle("Ac02").initData(new Ac02Event("ac02")).showAndWait());
    }

    /**
     * 订阅MainEvent
     */
    @Subscribe
    private void subscribeMainEvent(final MainEvent event) {
        lbl.setText(String.format("%s, %s", event.aac001, event.aac002));
    }

    public void openDialog(final ActionEvent actionEvent) {
        new DialogApp().showAndWait();
    }

    public void openThreading(final ActionEvent event) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("mocky-delay", "3s");
        HttpUtil.execute(ApiUrl.getObj,
                params,
                null, null, null,
                new HttpUtil.OnHttpCallback<ApiUrl.Obj>() {
                    @Override
                    public void onStart() {
                        System.out.println("开始转圈圈");
                    }

                    @Override
                    public void onSuccess(final ApiUrl.Obj data) {
                        // 只能在UI线程中更新UI
                        runOnUIThread(() -> lbl.setText(data.hello));
                    }

                    @Override
                    public void onError(final String err) {
                        System.out.println(err);
                    }

                    @Override
                    public void onFinish() {
                        System.out.println("停止转圈圈");
                    }
                });
    }
}
