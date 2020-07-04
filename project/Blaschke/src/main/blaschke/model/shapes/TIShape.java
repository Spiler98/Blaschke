package main.blaschke.model.shapes;

import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import main.blaschke.controllers.MainController;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class TIShape extends TreeItem<HBox> {

    protected final int scale = MainController.getScale();

    protected HBox hBox;

    protected Label name;
    protected Button btnDelete;
    protected ToggleButton tbHide;
    protected ColorPicker colorPicker;
    protected DropShadow dropShadow;

    protected TextField tfX0;
    protected TextField tfY0;
    protected TextField tfName;
    protected TreeItem<HBox> tiX0;
    protected TreeItem<HBox> tiY0;
    protected TreeItem<HBox> tiName;
    protected List<TextField> listOfTFs;

    protected TIShape() {
        name = new Label();
        name.setStyle(
                "-fx-background-radius: 4 4 4 4;" +
                        "-fx-background-color: #dcd94f;" +
                        "-fx-text-fill: #282828;" +
                        "-fx-pref-width: 60;"
        );

        btnDelete = new Button("Delete");
        btnDelete.setGraphic(new ImageView(new Image("resources/icons/deleteblack.png")));
        btnDelete.setStyle("-fx-background-color: #e05656;");
        tbHide = new ToggleButton();
        tbHide.setGraphic(new ImageView(new Image("resources/icons/sideicons/show.png")));
        tbHide.setStyle("-fx-background-color: #5be056;");

        colorPicker = new ColorPicker(Color.WHITE);
        dropShadow = new DropShadow();
        dropShadow.setRadius(10);
        dropShadow.colorProperty().bind(colorPicker.valueProperty());
        hBox = new HBox(name, new HBox(new Label("Color"), colorPicker), btnDelete, tbHide);
        hBox.setSpacing(5);

        tfName = new TextField();
        name.textProperty().bind(tfName.textProperty());
        tfX0 = new TextField();
        tfX0.setPromptText("Enter decimal numbers");
        tfX0.setFocusTraversable(false);
        tfY0 = new TextField();
        tfY0.setPromptText("Enter decimal numbers");
        tfY0.setFocusTraversable(false);
        tiName = new TreeItem<>(new HBox(new Label("Name"), tfName));
        tiX0 = new TreeItem<>(new HBox(new Label("x0"), tfX0));
        tiY0 = new TreeItem<>(new HBox(new Label("y0"), tfY0));

        this.setValue(hBox);
        this.setExpanded(true);
        listOfTFs = new ArrayList<>();
        listOfTFs.add(tfX0);
        listOfTFs.add(tfY0);
    }

    public abstract void incrementCounter();

    public abstract void decrementCounter();

    public List<TextField> getTextFields() {
        return listOfTFs;
    }

    public Button getBtnDelete() {
        return btnDelete;
    }

    public ToggleButton getTbHide() {
        return tbHide;
    }

    public abstract Shape getZShape();

    public abstract Shape getWShape();

    public abstract JSONObject getJSON();

    public void setName(String name) {
        tfName.setText(name);
    }

    public void setX0(double tfX0Value) {
        tfX0.setText(String.valueOf(tfX0Value / scale));
    }

    public void setY0(double tfY0Value) {
        tfY0.setText(String.valueOf(tfY0Value / scale));
    }

    public abstract void setZShapeFromTextFields();

    public abstract void setEffect();

    public abstract void removeEffect();

    public void setColor(String color) {
        colorPicker.setValue(Color.valueOf(color));
    }

}
