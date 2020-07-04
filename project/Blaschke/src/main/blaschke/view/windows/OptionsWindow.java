package main.blaschke.view.windows;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.blaschke.controllers.MainController;

public abstract class OptionsWindow {

    static String mode;

    public static String display(String currentMode) {
        ImageView img = new ImageView("resources/icons/windowicons/about.png");
        Label label = new Label("Choose mode");

        ToggleGroup toggleMode = new ToggleGroup();
        RadioButton dark = new RadioButton("Dark mode");
        RadioButton light = new RadioButton("Light mode");
        dark.setToggleGroup(toggleMode);
        light.setToggleGroup(toggleMode);
        if ((currentMode.equals("Dark mode"))) {
            dark.setSelected(true);
        } else {
            light.setSelected(true);
        }

        Button btnOk = new Button("OK");
        Button btnCancel = new Button("Cancel");

        HBox hbButtons = new HBox(10, btnOk, btnCancel);
        VBox vbLayout = new VBox(10, new HBox(img, label), new HBox(5, dark, light), hbButtons);

        Scene scene = new Scene(vbLayout);
        if (MainController.getMode().equals("Dark mode")) {
            scene.getStylesheets().add("main/blaschke/view/style/dark/PopupWindowStyle.css");
        } else {
            scene.getStylesheets().add("main/blaschke/view/style/light/LightPopupWindowStyle.css");
        }
        Stage window = new Stage();
        window.setResizable(false);

        mode = "Cancel";
        btnOk.setOnAction(e -> {
            mode = ((RadioButton) toggleMode.getSelectedToggle()).getText();
            window.close();
        });
        btnCancel.setOnAction(e -> window.close());

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Options");
        window.setMinWidth(250);
        window.setScene(scene);
        window.showAndWait();

        return mode;
    }
}
