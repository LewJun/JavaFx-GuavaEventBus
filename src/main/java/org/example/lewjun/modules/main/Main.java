package org.example.lewjun.modules.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.lewjun.util.EventBusUtil;

/**
 * Hello world!
 */
public class Main extends Application {
    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        final FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "view.fxml"
                )
        );
        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.setTitle("Main");

        final MainController controller = loader.getController();
        EventBusUtil.register(controller);

        primaryStage.setOnCloseRequest(event -> EventBusUtil.unregister(controller));

        primaryStage.show();
    }


}
