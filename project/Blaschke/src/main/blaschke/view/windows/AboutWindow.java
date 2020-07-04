package main.blaschke.view.windows;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.blaschke.controllers.MainController;

public abstract class AboutWindow {

    public static void display() {
        ImageView about = new ImageView("resources/icons/windowicons/about.png");
        Label name = new Label("Blaschke v1.0");
        Label creator = new Label("Powered by Norbert Hegedüs");
        VBox vbLayout = new VBox(10, new HBox(about, name), creator);

        Scene scene = new Scene(vbLayout);
        if (MainController.getMode().equals("Dark mode")) {
            scene.getStylesheets().add("main/blaschke/view/style/dark/PopupWindowStyle.css");
        } else {
            scene.getStylesheets().add("main/blaschke/view/style/light/LightPopupWindowStyle.css");
        }
        Stage window = new Stage();
        window.setResizable(false);

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("About");
        window.setMinWidth(250);
        window.setScene(scene);
        window.showAndWait();
    }
}
