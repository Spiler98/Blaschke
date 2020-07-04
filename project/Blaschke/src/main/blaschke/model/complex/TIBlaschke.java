package main.blaschke.model.complex;

import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.blaschke.controllers.MainController;

import java.util.ArrayList;
import java.util.List;

public class TIBlaschke extends TreeItem<HBox> {

    private final int scale = MainController.getScale();

    private static int counter = 0;
    private final int instanceCount;

    private boolean isCorrect;
    final Circle point;
    final ToggleButton tbHide;
    final ToggleButton tbProduct;
    final Blaschke blaschke;
    static final Complex defaultComplex = new Complex(1, 0);

    private final DropShadow dropShadow;
    private final TextField tfRe;
    private final TextField tfIm;
    private final TextField tfName;
    final List<TextField> listOfTFs;

    public TIBlaschke() {
        isCorrect = true;
        Label name = new Label();
        name.setStyle(
                "-fx-background-radius: 4 4 4 4;" +
                        "-fx-background-color: #8f56e0;" +
                        "-fx-text-fill: white;" +
                        "-fx-pref-width: 80;"
        );

        tbHide = new ToggleButton();
        tbHide.setGraphic(new ImageView(new Image("resources/icons/sideicons/show.png")));
        tbHide.setStyle("-fx-background-color: #5be056;");
        tbProduct = new ToggleButton();
        tbProduct.setGraphic(new ImageView(new Image("resources/icons/sideicons/off.png")));
        tbProduct.setStyle(
                "-fx-background-color: #dcd94f;" +
                        "-fx-text-fill: black;" +
                        "-fx-font-weight: bold;"
        );

        ColorPicker colorPicker = new ColorPicker(Color.WHITE);
        dropShadow = new DropShadow();
        dropShadow.setRadius(10);
        dropShadow.colorProperty().bind(colorPicker.valueProperty());
        HBox hBox = new HBox(name, new HBox(new Label("Color"), colorPicker), tbHide, tbProduct);
        hBox.setSpacing(5);

        point = new Circle();
        point.setRadius(10);
        point.fillProperty().bind(colorPicker.valueProperty());
        blaschke = new Blaschke();

        tfName = new TextField();
        name.textProperty().bind(tfName.textProperty());
        tfRe = new TextField();
        tfRe.setPromptText("Enter decimal numbers");
        tfRe.setFocusTraversable(false);
        tfIm = new TextField();
        tfIm.setPromptText("Enter decimal numbers");
        tfIm.setFocusTraversable(false);
        TreeItem<HBox> tiName = new TreeItem<>(new HBox(new Label("Name"), tfName));
        TreeItem<HBox> tiRe = new TreeItem<>(new HBox(new Label("Re(a)"), tfRe));
        TreeItem<HBox> tiIm = new TreeItem<>(new HBox(new Label("Im(a)"), tfIm));

        this.setValue(hBox);
        this.setExpanded(true);
        this.getChildren().addAll(tiName, tiRe, tiIm);
        listOfTFs = new ArrayList<>();
        listOfTFs.add(tfRe);
        listOfTFs.add(tfIm);

        instanceCount = ++counter;
        setName();

    }

    public int getInstanceCount() {
        return instanceCount;
    }

    public List<TextField> getTextFields() {
        return listOfTFs;
    }

    public ToggleButton getTbHide() {
        return tbHide;
    }

    public ToggleButton getTbProduct() {
        return tbProduct;
    }

    public Circle getPoint() {
        return point;
    }

    public Blaschke getBlaschke() {
        return blaschke;
    }

    public static Complex getDefaultComplex() {
        return defaultComplex;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setName() {
        tfName.setText("B" + instanceCount + "(a, z)");
    }

    public void setRe(double tfReValue) {
        tfRe.setText(String.valueOf(tfReValue / scale));
        point.setCenterX(tfReValue);
        blaschke.setAReal(tfReValue / scale);

    }

    public void setIm(double tfImValue) {
        tfIm.setText(String.valueOf(tfImValue / scale));
        point.setCenterY(tfImValue);
        blaschke.setAImaginary(tfImValue / scale);
    }

    public void setPoint() {
        String re = tfRe.getText();
        String im = tfIm.getText();
        if (!re.equals("") && !im.equals("")) {
            point.setCenterX(Double.parseDouble(re) * scale);
            point.setCenterY(Double.parseDouble(im) * scale);
            blaschke.setA(Double.parseDouble(re), Double.parseDouble(im));
        }
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public void setEffect() {
        point.setEffect(dropShadow);
    }

    public void removeEffect() {
        point.setEffect(null);
    }

}
