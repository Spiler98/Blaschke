package main.blaschke.model.shapes;

import javafx.scene.shape.Circle;
import org.json.JSONObject;

public class TIPoint extends TIShape {

    private static int counter = -1;
    private static final double radius = 4;

    final Circle zPoint;
    final Circle wPoint;

    public TIPoint() {
        super();
        incrementCounter();
        zPoint = new Circle();
        zPoint.setRadius(radius);
        zPoint.fillProperty().bind(colorPicker.valueProperty());
        wPoint = new Circle();
        wPoint.setRadius(radius);
        wPoint.fillProperty().bind(colorPicker.valueProperty());
        tfName.setText("Point " + counter);
        this.getChildren().addAll(tiName, tiX0, tiY0);
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

    public static double getRadius() {
        return radius;
    }

    @Override
    public Circle getZShape() { return zPoint; }

    @Override
    public Circle getWShape() {
        return wPoint;
    }

    @Override
    public JSONObject getJSON() {
        JSONObject jsonPoint = new JSONObject();
        jsonPoint.put("name", tfName.getText());
        jsonPoint.put("x", zPoint.getCenterX() / scale);
        jsonPoint.put("y", zPoint.getCenterY() / scale);
        jsonPoint.put("color", colorPicker.getValue().toString());
        return jsonPoint;
    }

    @Override
    public void setX0(double tfX0Value) {
        super.setX0(tfX0Value);
        zPoint.setCenterX(tfX0Value);
    }

    @Override
    public void setY0(double tfY0Value) {
        super.setY0(tfY0Value);
        zPoint.setCenterY(tfY0Value);
    }

    @Override
    public void setZShapeFromTextFields() {
        String x = tfX0.getText();
        String y = tfY0.getText();
        if (!x.equals("") && !y.equals("")) {
            zPoint.setCenterX(Double.parseDouble(x) * scale);
            zPoint.setCenterY(Double.parseDouble(y) * scale);
        }
    }

    @Override
    public void setEffect() { zPoint.setEffect(dropShadow); }

    @Override
    public void removeEffect() { zPoint.setEffect(null); }

}
