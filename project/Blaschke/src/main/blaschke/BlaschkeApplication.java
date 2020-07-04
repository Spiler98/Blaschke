package main.blaschke;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import main.blaschke.view.windows.SaveOnExitWindow;

public class BlaschkeApplication extends Application {

    private static Stage window;

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        BorderPane root = FXMLLoader.load(getClass().getResource("view/scenes/Main.fxml"));
        Scene scene = new Scene(root, 1100, 950);
        window.setTitle("Blaschke");
        window.getIcons().add(new Image("resources/icons/icon.png"));
        window.setOnCloseRequest(e -> {
            e.consume();
            close();
        });
        window.setScene(scene);
        window.show();

    }

    public static void close() {
        String option = SaveOnExitWindow.display();
        if (!option.equals("Cancel")) {
            window.close();
        }
    }

    public static ReadOnlyDoubleProperty getPrefTreeViewHeight() {
        return window.heightProperty();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
