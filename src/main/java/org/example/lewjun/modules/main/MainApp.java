package org.example.lewjun.modules.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.lewjun.util.EventBusUtil;

public class MainApp extends Application {
    private MainController controller;
    private Stage stage;

    public static void main(String[] args) {
        launch(MainApp.class, args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        final FXMLLoader loader = new FXMLLoader(getClass().getResource("view.fxml"));

        primaryStage.setScene(new Scene(loader.load()));

        primaryStage.getIcons().add(new Image("assets/images/user.png"));

        primaryStage.setTitle("Main");

        controller = loader.getController();

        handlerEventBus();

        primaryStage.show();

        // WARNING: onCloseRequest这段代码放show()之前还不能得到调用
        onCloseRequest();
    }


    /**
     * 处理关闭事件
     */
    private void onCloseRequest() {
        stage.setOnCloseRequest(event -> {
            // allow user to decide between yes and no
            final Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Do you really want to close this application?",
                    ButtonType.YES,
                    ButtonType.NO);

            // clicking X also means no
            final ButtonType result = alert.showAndWait().orElse(ButtonType.NO);

            if (ButtonType.NO == result) {
                event.consume();
            }
        });
    }

    private void handlerEventBus() {
        if (null == controller) return;
        // 注册controller
        EventBusUtil.register(controller);

        // 当stage关闭的时候，取消注册controller
        stage.setOnCloseRequest(event -> EventBusUtil.unregister(controller));
    }
}
