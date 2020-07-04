package main.blaschke.model.shapes;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import org.json.JSONObject;

public class TIArc extends TIShape {

    private static int counter = -1;

    final Arc zArc;
    Shape wArc;

    final TextField tfR;
    final TextField tfStartAngle;
    final TextField tfAngle;

    public TIArc() {
        super();
        incrementCounter();
        zArc = new Arc();
        zArc.setStrokeWidth(2);
        zArc.setType(ArcType.OPEN);
        zArc.setFill(Color.color(0,0,0,0));
        zArc.strokeProperty().bind(colorPicker.valueProperty());

        tfName.setText("Arc " + counter);
        tfR = new TextField();
        tfR.setPromptText("Enter decimal numbers");
        tfR.setFocusTraversable(false);
        TreeItem<HBox> tiR = new TreeItem<>(new HBox(new Label("r"), tfR));
        tfStartAngle = new TextField();
        tfStartAngle.setPromptText("Enter decimal numbers");
        tfStartAngle.setFocusTraversable(false);
        TreeItem<HBox> tiStartAngle = new TreeItem<>(new HBox(new Label("Start"), tfStartAngle));
        tfAngle = new TextField();
        tfAngle.setPromptText("Enter decimal numbers");
        tfAngle.setFocusTraversable(false);
        TreeItem<HBox> tiAngle = new TreeItem<>(new HBox(new Label("Angle"), tfAngle));
        this.getChildren().addAll(tiName, tiX0, tiY0, tiR, tiStartAngle, tiAngle);
        listOfTFs.add(tfR);
        listOfTFs.add(tfStartAngle);
        listOfTFs.add(tfAngle);
    }

    @Override
    public void incrementCounter() {
        ++counter;
    }

    @Override
    public void decrementCounter() {
        --counter;
    }

    public static void resetCounter() {
        counter = -1;
    }

    @Override
    public Arc getZShape() {
        return zArc;
    }

    @Override
    public Shape getWShape() {
        return wArc;
    }

    public void setWShapeToArc() {
        Arc arc = new Arc(
                zArc.getCenterX(),
                zArc.getCenterY(),
                zArc.getRadiusX(),
                zArc.getRadiusY(),
                zArc.getStartAngle(),
                zArc.getLength()
        );
        arc.setStrokeWidth(2);
        arc.setType(ArcType.OPEN);
        arc.setFill(Color.color(0,0,0,0));
        arc.strokeProperty().bind(colorPicker.valueProperty());
        wArc = arc;
    }

    public void setWShapeToPath() {
        wArc = new Path();
        wArc.setStrokeWidth(2);
        wArc.setFill(Color.color(0,0,0,0));
        wArc.strokeProperty().bind(colorPicker.valueProperty());
    }

    @Override
    public JSONObject getJSON() {
        JSONObject jsonArc = new JSONObject();
        jsonArc.put("name", tfName.getText());
        jsonArc.put("x", zArc.getCenterX() / scale);
        jsonArc.put("y", zArc.getCenterY() / scale);
        jsonArc.put("r", zArc.getRadiusX() / scale);
        jsonArc.put("startAngle", zArc.getStartAngle());
        jsonArc.put("angle", zArc.getLength());
        jsonArc.put("color", colorPicker.getValue().toString());
        return jsonArc;
    }

    @Override
    public void setX0(double tfX0Value) {
        super.setX0(tfX0Value);
        zArc.setCenterX(tfX0Value);
    }

    @Override
    public void setY0(double tfY0Value) {
        super.setY0(tfY0Value);
        zArc.setCenterY(tfY0Value);
    }

    public void setR(double tfRValue) {
        tfR.setText(String.valueOf(tfRValue / scale));
        zArc.setRadiusX(tfRValue);
        zArc.setRadiusY(tfRValue);
    }

    public void setStartAngle(double tfStartAngleValue) {
        tfStartAngle.setText(String.valueOf(tfStartAngleValue));
        zArc.setStartAngle(tfStartAngleValue);
    }

    public void setAngle(double tfAngleValue) {
        tfAngle.setText(String.valueOf(tfAngleValue));
        zArc.setLength(tfAngleValue);
    }

    @Override
    public void setZShapeFromTextFields() {
        String x = tfX0.getText();
        String y = tfY0.getText();
        String r = tfR.getText();
        String sa = tfStartAngle.getText();
        String a = tfAngle.getText();
        if (!x.equals("") && !y.equals("") && !r.equals("") && !sa.equals("") && !a.equals("")) {
            zArc.setCenterX(Double.parseDouble(x) * scale);
            zArc.setCenterY(Double.parseDouble(y) * scale);
            zArc.setRadiusX(Double.parseDouble(r) * scale);
            zArc.setRadiusY(Double.parseDouble(r) * scale);
            zArc.setStartAngle(Double.parseDouble(sa));
            zArc.setLength(Double.parseDouble(a));
        }
    }

    @Override
    public void setEffect() { zArc.setEffect(dropShadow); }

    @Override
    public void removeEffect() { zArc.setEffect(null); }

}
