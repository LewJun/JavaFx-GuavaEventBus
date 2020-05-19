package org.example.lewjun.modules.dialog;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import org.example.lewjun.base.BaseController;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DialogController extends BaseController {
    public void openChoiceDialog(final ActionEvent event) {
        final List<String> choices = Arrays.asList("1225", "1107", "0702", "0102", "Other");
        final ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.setTitle("Favourite number?");
        dialog.setHeaderText("Choose your favourite number.");
        dialog.setContentText("Your favourite number:");
        final Optional<String> choice = dialog.showAndWait();
        final String ret = choice.map(it -> "Other".equals(it) ? "Unfortunately your favourite number is not available"
                : String.format("Nice! I like %s", it)).orElse("click the X or cancel");
        System.out.println(ret);
    }

    public void openTextInputDialog(final ActionEvent event) {
        final TextInputDialog dialog = new TextInputDialog("0702");
        dialog.setTitle("Favourite number?");
        dialog.setHeaderText("Choose your favourite number.");
        dialog.setContentText("Your favourite number:");

        final Optional<String> result = dialog.showAndWait();
        final String ret = result.map(it -> {
            try {
                return MessageFormat.format("Nice! I like {0}", Integer.parseInt(it));
            } catch (final Exception e) {
                return MessageFormat.format("Unfortunately {0} is not int", it);
            }
        }).orElse("click the X or cancel");
        System.out.println(ret);
    }

    /**
     * 自定义按钮文字
     *
     * @param event
     */
    public void customButtonText(final ActionEvent event) {
        final ButtonType hello = new ButtonType("hello");
        final ButtonType bye = new ButtonType("bye");

        final Alert alert = new Alert(Alert.AlertType.NONE, "Greeting", hello, bye);
        final ButtonType buttonType = alert.showAndWait().orElse(bye);
        System.out.println(buttonType.getText());
    }
}
