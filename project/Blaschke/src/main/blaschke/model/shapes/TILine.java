package main.blaschke.model.shapes;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import org.json.JSONObject;

public class TILine extends TIShape {

    private static int counter = -1;

    final Line zLine;
    Shape wShape;

    final TextField tfX;
    final TextField tfY;

    public TILine() {
        super();
        incrementCounter();
        zLine = new Line();
        zLine.setStrokeWidth(2);
        zLine.strokeProperty().bind(colorPicker.valueProperty());

        tfName.setText("Line " + counter);
        tfX = new TextField();
        tfX.setPromptText("Enter decimal numbers");
        tfX.setFocusTraversable(false);
        TreeItem<HBox> tiX = new TreeItem<>(new HBox(new Label("x"), tfX));
        tfY = new TextField();
        tfY.setPromptText("Enter decimal numbers");
        tfY.setFocusTraversable(false);
        TreeItem<HBox> tiY = new TreeItem<>(new HBox(new Label("y"), tfY));
        this.getChildren().addAll(tiName, tiX0, tiY0, tiX, tiY);
        listOfTFs.add(tfX);
        listOfTFs.add(tfY);
    }

    public static void resetCounter() {
        counter = -1;
    }

    @Override
    public void incrementCounter() {
        ++counter;
    }

    @Override
    public void decrementCounter() {
        --counter;
    }

    @Override
    public Line getZShape() {
        return zLine;
    }

    @Override
    public Shape getWShape() {
        return wShape;
    }

    public void setWShapeToLine() {
        wShape = new Line(zLine.getStartX(), zLine.getStartY(), zLine.getEndX(), zLine.getEndY());
        wShape.setStrokeWidth(2);
        wShape.strokeProperty().bind(colorPicker.valueProperty());
    }

    public void setWShapeToCircle() {
        wShape = new Circle();
        wShape.setFill(Color.color(0,0,0,0));
        wShape.setStrokeWidth(2);
        wShape.strokeProperty().bind(colorPicker.valueProperty());
    }

    @Override
    public JSONObject getJSON() {
        JSONObject jsonLine = new JSONObject();
        jsonLine.put("name", tfName.getText());
        jsonLine.put("x0", zLine.getStartX() / scale);
        jsonLine.put("y0", zLine.getStartY() / scale);
        jsonLine.put("x", zLine.getEndX() / scale);
        jsonLine.put("y", zLine.getEndY() / scale);
        jsonLine.put("color", colorPicker.getValue().toString());
        return jsonLine;
    }

    @Override
    public void setX0(double tfX0Value) {
        super.setX0(tfX0Value);
        zLine.setStartX(tfX0Value);
    }

    @Override
    public void setY0(double tfY0Value) {
        super.setY0(tfY0Value);
        zLine.setStartY(tfY0Value);
    }

    public void setX(double tfXValue) {
        tfX.setText(String.valueOf(tfXValue / scale));
        zLine.setEndX(tfXValue);
    }

    public void setY(double tfYValue) {
        tfY.setText(String.valueOf(tfYValue / scale));
        zLine.setEndY(tfYValue);
    }

    @Override
    public void setZShapeFromTextFields() {
        String x0 = tfX0.getText();
        String y0 = tfY0.getText();
        String x = tfX.getText();
        String y = tfY.getText();
        if (!x.equals("") && !y.equals("") && !x0.equals("") && !y0.equals("")) {
            zLine.setStartX(Double.parseDouble(x0) * scale);
            zLine.setStartY(Double.parseDouble(y0) * scale);
            zLine.setEndX(Double.parseDouble(x) * scale);
            zLine.setEndY(Double.parseDouble(y) * scale);
        }
    }

    @Override
    public void setEffect() { zLine.setEffect(dropShadow); }

    @Override
    public void removeEffect() { zLine.setEffect(null); }


}
