package org.example.lewjun.base

import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Modality
import javafx.stage.Stage
import org.example.lewjun.util.EventBusUtil

abstract class BaseApp<Controller extends BaseController> {
    protected Stage stage
    protected Controller controller

    BaseApp() {
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource(getResName()))
            stage = new Stage()
            stage.setScene(new Scene(loader.load()))
            stage.setResizable(false)
            stage.setMaximized(false)

            stage.getIcons().add(new Image("assets/images/user.png"))

            controller = loader.getController()

            handlerEventBus()
        } catch (Exception e) {
            e.printStackTrace()
        }
    }

    protected String getResName() {
        return "view.fxml"
    }

    private void handlerEventBus() {
        if (!controller) return
        // 注册controller
        EventBusUtil.register(controller)

        // 当stage关闭的时候，取消注册controller
        stage.setOnCloseRequest({ event -> EventBusUtil.unregister(controller) })
    }

    BaseApp setTitle(final String title) {
        stage.setTitle(title ?: "")
        return this
    }

    BaseApp initData(final BaseEvent event) {
        if (!event) {
            EventBusUtil.post(event)
        }
        return this
    }

    void showAndWait() {
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.showAndWait()
    }

}
