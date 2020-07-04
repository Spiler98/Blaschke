package main.blaschke.view.windows;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.blaschke.controllers.MainController;

public abstract class ErrorWindow {

    public static void display(String msg) {
        ImageView error = new ImageView("resources/icons/windowicons/error.png");
        Label label = new Label(msg);
        Button btnOK = new Button("OK");
        VBox vbLayout = new VBox(10, new HBox(error, label), btnOK);

        Scene scene = new Scene(vbLayout);
        if (MainController.getMode().equals("Dark mode")) {
            scene.getStylesheets().add("main/blaschke/view/style/dark/PopupWindowStyle.css");
        } else {
            scene.getStylesheets().add("main/blaschke/view/style/light/LightPopupWindowStyle.css");
        }
        Stage window = new Stage();
        btnOK.setOnAction(e -> window.close());

        window.setResizable(false);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Error");
        window.setMinWidth(250);
        window.setScene(scene);
        window.showAndWait();
    }

}
