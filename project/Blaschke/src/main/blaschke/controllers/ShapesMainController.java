package main.blaschke.controllers;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Line;
import main.blaschke.model.util.MathUtil;
import main.blaschke.model.complex.Blaschke;
import main.blaschke.model.complex.Complex;
import main.blaschke.model.shapes.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ShapesMainController implements Initializable {

    private ShapesSideController shapesSideController;

    @FXML private Group zPlane;
    @FXML private Group wPlane;
    @FXML private Circle zCircle;
    @FXML private Circle wCircle;
    @FXML private LineChart<Number, Number> lcFunctions;
    @FXML private NumberAxis xAxis;
    private XYChart.Series<Number, Number> realPart;
    private XYChart.Series<Number, Number> imaginaryPart;

    private StringProperty mouseCoords;
    private Map<TIShape, Shape> zShapes;
    private Map<TIShape, Shape> wShapes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mouseCoords = new SimpleStringProperty();
        zShapes = new HashMap<>();
        wShapes = new HashMap<>();
        xAxis.setUpperBound(2 * Math.PI);
        realPart = new XYChart.Series<>();
        realPart.setName("Re(B(a, z))");
        imaginaryPart = new XYChart.Series<>();
        imaginaryPart.setName("Im(B(a, z))");
        lcFunctions.getData().add(realPart);
        lcFunctions.getData().add(imaginaryPart);

    }

    public void injectController(ShapesSideController shapesSideController) {
        this.shapesSideController = shapesSideController;
    }

    public void refreshShapes() {
        Blaschke blaschke = shapesSideController.getBlaschke();
        wPlane.getChildren().remove(3, wPlane.getChildren().size());
        zShapes.forEach((tiShape, shape) -> {
            Blaschke.transform(tiShape, blaschke);
            Shape wShape = tiShape.getWShape();
            wShape.setClip(new Circle(wCircle.getCenterX(), wCircle.getCenterY(), wCircle.getRadius()));
            wShapes.replace(tiShape, wShape);
            wPlane.getChildren().add(wShape);
        });
        realPart.getData().clear();
        imaginaryPart.getData().clear();
        showChart(true);
        for (double arg = 0; arg <= 2 * Math.PI; arg += 0.1) {
            Complex input = new Complex(arg);
            try {
                realPart.getData().add(new XYChart.Data<>(arg, blaschke.evaluate(input).getReal()));
                imaginaryPart.getData().add(new XYChart.Data<>(arg, blaschke.evaluate(input).getImaginary()));
            } catch (ArithmeticException ex) {
                showChart(false);
                throw ex;
            }
        }
    }

    public void showChart(boolean visibility) {
        realPart.getNode().setVisible(visibility);
        imaginaryPart.getNode().setVisible(visibility);
    }

    public void removeAllShapes() {
        zShapes.clear();
        wShapes.clear();
        zPlane.getChildren().remove(3, zPlane.getChildren().size());
        wPlane.getChildren().remove(3, wPlane.getChildren().size());
    }

    public void removeShape(TIShape tiShape) {
        Shape zShape = zShapes.remove(tiShape);
        Shape wShape = wShapes.remove(tiShape);
        zPlane.getChildren().remove(zShape);
        wPlane.getChildren().remove(wShape);
    }

    public void drawShape(TIShape tiShape) {
        if (zShapes.containsKey(tiShape)) {
            zPlane.getChildren().remove(zShapes.get(tiShape));
            wPlane.getChildren().remove(wShapes.get(tiShape));
        }
        Blaschke.transform(tiShape, shapesSideController.getBlaschke());
        Shape zShape = tiShape.getZShape();
        Shape wShape = tiShape.getWShape();
        zShape.setClip(new Circle(zCircle.getCenterX(), zCircle.getCenterY(), zCircle.getRadius()));
        wShape.setClip(new Circle(wCircle.getCenterX(), wCircle.getCenterY(), wCircle.getRadius()));
        zShapes.put(tiShape, zShape);
        wShapes.put(tiShape, wShape);
        zPlane.getChildren().add(zShape);
        wPlane.getChildren().add(wShape);
    }

    @FXML
    private void setStart(MouseEvent press) {
        TIShape tiHighlighted = shapesSideController.getTIHighlighted();
        if (tiHighlighted != null) {
            tiHighlighted.getZShape().setClip(new Circle(zCircle.getCenterX(), zCircle.getCenterY(), zCircle.getRadius()));
            double x0 = press.getX();
            double y0 = press.getY();
            if (!press.isShiftDown()) {
                tiHighlighted.setX0(x0);
                tiHighlighted.setY0(y0);
                drawShape(tiHighlighted);
            }
        }
    }

    @FXML
    private void setEnd(MouseEvent drag) {
        TIShape tiHighlighted = shapesSideController.getTIHighlighted();
        double x = drag.getX();
        double y = drag.getY();
        if (tiHighlighted != null) {
            if (drag.isShiftDown()) {
                if (tiHighlighted instanceof TILine) {
                    TILine tiLine = (TILine) tiHighlighted;
                    Line line = tiLine.getZShape();
                    tiLine.setX(x + (line.getEndX() - line.getStartX()));
                    tiLine.setY(y + (line.getEndY() - line.getStartY()));
                    tiLine.setX0(x);
                    tiLine.setY0(y);
                    drawShape(tiLine);
                } else {
                    tiHighlighted.setX0(x);
                    tiHighlighted.setY0(y);
                    drawShape(tiHighlighted);
                }
            } else {
                if (tiHighlighted instanceof TILine) {
                    TILine tiLine = (TILine) tiHighlighted;
                    tiLine.setX(x);
                    tiLine.setY(y);
                    drawShape(tiLine);
                } else if (tiHighlighted instanceof TIArc) {
                    TIArc tiArc = (TIArc) tiHighlighted;
                    Arc arc = tiArc.getZShape();
                    tiArc.setR(30);
                    tiArc.setStartAngle(0);
                    tiArc.setAngle(MathUtil.getDistance(arc.getCenterX(), arc.getCenterY(), x, y));
                    drawShape(tiArc);
                } else if (tiHighlighted instanceof TICircle) {
                    TICircle tiCircle = (TICircle) tiHighlighted;
                    Circle circle = tiCircle.getZShape();
                    tiCircle.setR(MathUtil.getDistance(circle.getCenterX(), circle.getCenterY(), x, y));
                    drawShape(tiCircle);
                }
            }
        }
    }

    public StringProperty getMouseCoords() {
        return mouseCoords;
    }

    @FXML
    private void setMouseCoords(MouseEvent move) {
        int scale = MainController.getScale();
        double x = Math.round(100*move.getX() / scale) / 100.0;
        double y = Math.round(100*move.getY() / scale) / 100.0;
        mouseCoords.setValue("(" + x + "," + y + ")");
    }

}
