package main.blaschke.model.shapes;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.json.JSONObject;

public class TICircle extends TIShape {

    private static int counter = -1;

    final Circle zCircle;
    final Circle wCircle;

    final TextField tfR;

    public TICircle() {
        super();
        incrementCounter();
        zCircle = new Circle();
        zCircle.setStrokeWidth(2);
        zCircle.setFill(Color.color(0,0,0,0));
        zCircle.strokeProperty().bind(colorPicker.valueProperty());
        wCircle = new Circle();
        wCircle.setStrokeWidth(2);
        wCircle.setFill(Color.color(0,0,0,0));
        wCircle.strokeProperty().bind(colorPicker.valueProperty());

        tfName.setText("Circle " + counter);
        tfR = new TextField();
        tfR.setPromptText("Enter decimal numbers");
        tfR.setFocusTraversable(false);
        TreeItem<HBox> tiR = new TreeItem<>(new HBox(new Label("r"), tfR));
        this.getChildren().addAll(tiName, tiX0, tiY0, tiR);
        listOfTFs.add(tfR);
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
    public Circle getZShape() {
        return zCircle;
    }

    @Override
    public Circle getWShape() {
        return wCircle;
    }

    @Override
    public JSONObject getJSON() {
        JSONObject jsonCircle = new JSONObject();
        jsonCircle.put("name", tfName.getText());
        jsonCircle.put("x", zCircle.getCenterX() / scale);
        jsonCircle.put("y", zCircle.getCenterY() / scale);
        jsonCircle.put("r", zCircle.getRadius() / scale);
        jsonCircle.put("color", colorPicker.getValue().toString());
        return jsonCircle;
    }

    @Override
    public void setX0(double tfX0Value) {
        super.setX0(tfX0Value);
        zCircle.setCenterX(tfX0Value);
    }

    @Override
    public void setY0(double tfY0Value) {
        super.setY0(tfY0Value);
        zCircle.setCenterY(tfY0Value);
    }

    public void setR(double tfRValue) {
        tfR.setText(String.valueOf(tfRValue / scale));
        zCircle.setRadius(tfRValue);
    }

    @Override
    public void setZShapeFromTextFields() {
        String x = tfX0.getText();
        String y = tfY0.getText();
        String r = tfR.getText();
        if (!x.equals("") && !y.equals("") && !r.equals("")) {
            zCircle.setCenterX(Double.parseDouble(x) * scale);
            zCircle.setCenterY(Double.parseDouble(y) * scale);
            zCircle.setRadius(Double.parseDouble(r) * scale);
        }
    }

    @Override
    public void setEffect() { zCircle.setEffect(dropShadow); }

    @Override
    public void removeEffect() { zCircle.setEffect(null); }

}
