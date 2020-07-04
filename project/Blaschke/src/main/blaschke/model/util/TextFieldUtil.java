package main.blaschke.model.util;

import javafx.scene.control.TextField;

public abstract class TextFieldUtil {

    private static final String pattern = "-?(0|[1-9][0-9]*)((\\.[0-9]*[1-9])|(\\.0))?";

    public static void getChangeListenerForDecimalTextField(TextField tf) {
        tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(pattern)) {
                if (newValue.contains(" ")) {
                    tf.setText(newValue.replace(" ", ""));
                }
                tf.setStyle("-fx-border-color: red; -fx-border-radius: 0 4 4 0;");
            } else {
                tf.setStyle("-fx-background-color: #cdcdcd; -fx-border-radius: 0 4 4 0;");
            }
        });
    }

}
