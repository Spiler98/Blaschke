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
import main.blaschke.controllers.ShapesSideController;

public abstract class SaveOnExitWindow {

    static String option;

    public static String display() {
        final String save = "Save";
        final String discard = "Discard";
        final String cancel = "Cancel";

        ImageView warning = new ImageView("resources/icons/windowicons/warning.png");
        Label label = new Label("Your calculations might not be saved. Are you sure you want to exit?");
        Button btnSave = new Button("Save");
        btnSave.setStyle("-fx-background-color: #56a9e0;");
        btnSave.setGraphic(new ImageView("resources/icons/popupicons/save.png"));
        Button btnDiscard = new Button("Discard");
        btnDiscard.setStyle("-fx-background-color: #e05656;");
        btnDiscard.setGraphic(new ImageView("resources/icons/popupicons/delete.png"));
        Button btnCancel = new Button("Cancel");
        HBox hbButtons = new HBox(10, btnSave, btnDiscard, btnCancel);
        VBox vbLayout = new VBox(10, new HBox(warning, label), hbButtons);

        Scene scene = new Scene(vbLayout);
        if (MainController.getMode().equals("Dark mode")) {
            scene.getStylesheets().add("main/blaschke/view/style/dark/PopupWindowStyle.css");
        } else {
            scene.getStylesheets().add("main/blaschke/view/style/light/LightPopupWindowStyle.css");
        }
        Stage window = new Stage();
        window.setResizable(false);

        option = cancel;
        btnSave.setOnAction(e -> {
            option = save;
            FileWindow.saveFileWindow(ShapesSideController.getTIShapeList());
            window.close();
        });
        btnDiscard.setOnAction(e -> {
            option = discard;
            window.close();
        });
        btnCancel.setOnAction(e -> window.close());

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Confirm exit");
        window.setMinWidth(250);
        window.setScene(scene);
        window.showAndWait();

        return option;
    }



}
