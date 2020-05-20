package org.example.lewjun.modules.dialog;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
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

    /**
     * +----------------------------------------+
     * |           Information Dialog           |
     * |----------------------------------------|
     * |  Look, an Information Dialog    [icon] |
     * |                                        |
     * |----------------------------------------|
     * |                                        |
     * |  I have a great message for you!       |
     * |                                        |
     * |----------------------------------------|
     * |                                        |
     * |                             [OK]       |
     * +----------------------------------------+
     *
     * @param evt
     */
    public void informationDialog(final ActionEvent evt) {
        final Alert alert = new Alert(Alert.AlertType.INFORMATION, "",
                ButtonType.OK, ButtonType.NO, ButtonType.YES, ButtonType.CANCEL, ButtonType.APPLY, ButtonType.CLOSE
                , ButtonType.FINISH, ButtonType.NEXT, ButtonType.PREVIOUS, new ButtonType("我知道了")
        );
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Look, an Information Dialog");
        // 不显示HeaderText
        alert.setHeaderText(null);
        alert.setContentText("I have a great message for you!");

        final ButtonType buttonType = alert.showAndWait().orElse(ButtonType.NO);
        if (buttonType.equals(ButtonType.OK)) {
            System.out.println("ok");
        } else {
            System.out.println("click X or not OK button");
        }

        System.out.println(buttonType.getText());
    }

    public void customLoginDialog(final ActionEvent event) {
        // Create the custom dialog.
        final Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Login Dialog");
        dialog.setHeaderText("Look, a Custom Login Dialog");

// Set the button types.
        final ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

// Create the username and password labels and fields.
        final GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        final TextField username = new TextField();
        username.setPromptText("Username");
        final PasswordField password = new PasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);

// Enable/Disable login button depending on whether a username was entered.
        final Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

// Do some validation (using the Java 8 lambda syntax).
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

// Request focus on the username field by default.
        Platform.runLater(username::requestFocus);

// Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });

        final Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(usernamePassword -> System.out.println("Username=" + usernamePassword.getKey() + ", Password=" + usernamePassword.getValue()));
    }
}
