package org.example.lewjun.modules.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.example.lewjun.util.EventBusUtil;

import java.util.Objects;

/**
 * Hello world!
 */
public class Main extends Application {
    public static void main(final String[] args) {
        launch(args);
    }

    private MainController controller;
    private Stage stage;

    @Override
    public void start(final Stage primaryStage) throws Exception {
        stage = primaryStage;

        final FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "view.fxml"
                )
        );
        primaryStage.setScene(new Scene(loader.load()));

        onCloseRequest();

        primaryStage.setTitle("Main");

        controller = loader.getController();

        handlerEventBus();

        primaryStage.show();
    }

    /**
     * 处理关闭事件
     */
    private void onCloseRequest() {
        stage.setOnCloseRequest(event -> {
            // allow user to decide between yes and no
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Do you really want to close this application?",
                    ButtonType.YES,
                    ButtonType.NO);

            // clicking X also means no
            ButtonType result = alert.showAndWait().orElse(ButtonType.NO);

            if (ButtonType.NO.equals(result)) {
                event.consume();
            }
        });
    }


    private void handlerEventBus() {
        if (Objects.isNull(controller)) return;
        // 注册controller
        EventBusUtil.register(controller);

        // 当stage关闭的时候，取消注册controller
        stage.setOnCloseRequest(event -> EventBusUtil.unregister(controller));
    }
}
