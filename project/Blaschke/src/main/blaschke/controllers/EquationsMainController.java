package main.blaschke.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import main.blaschke.model.util.MathUtil;
import main.blaschke.model.complex.Complex;
import main.blaschke.model.complex.TIBlaschke;

import java.net.URL;
import java.util.*;

public class EquationsMainController implements Initializable {

    private EquationsSideController equationsSideController;

    @FXML private Group complexPlane;
    @FXML private Circle circle;
    @FXML private LineChart<Number, Number> lcFunctions;
    @FXML private LineChart<Number, Number> lcSolutions;
    @FXML private NumberAxis xAxis;

    private XYChart.Series<Number, Number> blaschkeReal1;
    private XYChart.Series<Number, Number> blaschkeImaginary1;
    private XYChart.Series<Number, Number> blaschkeReal2;
    private XYChart.Series<Number, Number> blaschkeImaginary2;
    private XYChart.Series<Number, Number> blaschkeReal3;
    private XYChart.Series<Number, Number> blaschkeImaginary3;
    private XYChart.Series<Number, Number> blaschkeProductReal;
    private XYChart.Series<Number, Number> blaschkeProductImaginary;
    private XYChart.Series<Number, Number> root1;
    private XYChart.Series<Number, Number> root2;
    private XYChart.Series<Number, Number> root3;
    private boolean isProductCorrect;

    private TIBlaschke tiHighlighted;
    private StringProperty mouseCoords;
    private Map<TIBlaschke, Circle> blaschkes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /*isFunctionCorrect = */
        isProductCorrect = true;
        mouseCoords = new SimpleStringProperty();
        blaschkes = new HashMap<>();
        xAxis.setUpperBound(2 * Math.PI);

        blaschkeReal1 = new XYChart.Series<>();
        blaschkeImaginary1 = new XYChart.Series<>();
        blaschkeReal2 = new XYChart.Series<>();
        blaschkeImaginary2 = new XYChart.Series<>();
        blaschkeReal3 = new XYChart.Series<>();
        blaschkeImaginary3 = new XYChart.Series<>();
        blaschkeProductReal = new XYChart.Series<>();
        blaschkeProductImaginary = new XYChart.Series<>();
        blaschkeReal1.setName("Re(B1(a, z))");
        blaschkeImaginary1.setName("Im(B1(a, z))");
        blaschkeReal2.setName("Re(B2(a, z))");
        blaschkeImaginary2.setName("Im(B2(a, z))");
        blaschkeReal3.setName("Re(B3(a, z))");
        blaschkeImaginary3.setName("Im(B3(a, z))");
        blaschkeProductReal.setName("Re(P(z))");
        blaschkeProductImaginary.setName("Im(P(z))");
        lcFunctions.getData().addAll(
                blaschkeReal1, blaschkeReal2, blaschkeReal3, blaschkeProductReal,
                blaschkeImaginary1, blaschkeImaginary2, blaschkeImaginary3, blaschkeProductImaginary
        );

        root1 = new XYChart.Series<>();
        root2 = new XYChart.Series<>();
        root3 = new XYChart.Series<>();
        root1.setName("Root1");
        root2.setName("Root2");
        root3.setName("Root3");
        lcSolutions.getData().addAll(root1, root2, root3);
        root1.getNode().setVisible(false);
        root2.getNode().setVisible(false);
        root3.getNode().setVisible(false);
    }

    public void injectController(EquationsSideController equationsSideController) {
        this.equationsSideController = equationsSideController;
    }

    public void setVisibility(TIBlaschke tiBlaschke, boolean visibility) {
        if (tiBlaschke == null) {
            if (!isProductCorrect) {
                visibility = false;
            }
            setVisibilityToCharts(blaschkeProductReal, blaschkeProductImaginary, visibility);
        } else {
            if (!tiBlaschke.isCorrect()) {
                visibility = false;
            }
            switch (tiBlaschke.getInstanceCount()) {
                case 1:
                    setVisibilityToCharts(blaschkeReal1, blaschkeImaginary1, visibility);
                    break;
                case 2:
                    setVisibilityToCharts(blaschkeReal2, blaschkeImaginary2, visibility);
                    break;
                case 3:
                    setVisibilityToCharts(blaschkeReal3, blaschkeImaginary3, visibility);
                    break;
            }
        }
    }

    private void setVisibilityToCharts(XYChart.Series<Number, Number> blaschkeRealPart, XYChart.Series<Number, Number> blaschkeImaginaryPart, boolean visibility) {
        for (int i = 0; i < blaschkeRealPart.getData().size(); ++i) {
            blaschkeRealPart.getData().get(i).getNode().setVisible(visibility);
            blaschkeImaginaryPart.getData().get(i).getNode().setVisible(visibility);
        }
        blaschkeRealPart.getNode().setVisible(visibility);
        blaschkeImaginaryPart.getNode().setVisible(visibility);

    }

    public void draw(TIBlaschke tiBlaschke, boolean refreshChart) {
        if (blaschkes.containsKey(tiBlaschke)) {
            complexPlane.getChildren().remove(blaschkes.get(tiBlaschke));
        }
        if (refreshChart) {
            switch (tiBlaschke.getInstanceCount()) {
                case 1:
                    refreshChart(tiBlaschke, blaschkeReal1, blaschkeImaginary1);
                    break;
                case 2:
                    refreshChart(tiBlaschke, blaschkeReal2, blaschkeImaginary2);
                    break;
                case 3:
                    refreshChart(tiBlaschke, blaschkeReal3, blaschkeImaginary3);
                    break;
            }
            refresh();
        }
        Circle point = tiBlaschke.getPoint();
        point.setClip(new Circle(circle.getCenterX(), circle.getCenterY(), circle.getRadius()));
        blaschkes.put(tiBlaschke, point);
        complexPlane.getChildren().add(point);
    }

    private void refreshChart(TIBlaschke tiBlaschke, XYChart.Series<Number, Number> blaschkeRealPart, XYChart.Series<Number, Number> blaschkeImaginaryPart) {
        blaschkeRealPart.getData().clear();
        blaschkeImaginaryPart.getData().clear();
        setVisibility(tiBlaschke, true);
        for (double arg = 0; arg <= 2 * Math.PI; arg += 0.1) {
            Complex input = new Complex(arg);
            try {
                Complex value = tiBlaschke.getBlaschke().evaluate(input);
                blaschkeRealPart.getData().add(new XYChart.Data<>(arg, value.getReal()));
                blaschkeImaginaryPart.getData().add(new XYChart.Data<>(arg, value.getImaginary()));
            } catch (ArithmeticException ex) {
                tiBlaschke.setCorrect(false);
                setVisibility(tiBlaschke, false);
                throw ex;
            }
        }
        tiBlaschke.setCorrect(true);
        setVisibility(tiBlaschke, true);
        if (tiBlaschke.getTbHide().isSelected()) {
            setVisibility(tiBlaschke, false);
        }
    }

    public void refresh() {
        blaschkeProductReal.getData().clear();
        blaschkeProductImaginary.getData().clear();
        isProductCorrect = true;
        setVisibility(null, true);
        root1.getData().clear();
        root2.getData().clear();
        root3.getData().clear();
        Complex constant = equationsSideController.getConstant();
        List<TIBlaschke> selected = new ArrayList<>(equationsSideController.getTIBlaschkes());
        Complex c1, c2, c3;
        c1 = c2 = c3 = TIBlaschke.getDefaultComplex();
        Complex[] cs = {c1, c2, c3};
        for (double arg = 0; arg <= 2 * Math.PI; arg += 0.1) {
            Complex input = new Complex(arg);
            try {
                for (int i = 0; i < selected.size(); ++i) {
                    cs[i] = selected.get(i).getBlaschke().evaluate(input);
                }
                Complex productValue = cs[0].multiply(cs[1]).multiply(cs[2]);
                blaschkeProductReal.getData().add(new XYChart.Data<>(arg, productValue.getReal()));
                blaschkeProductImaginary.getData().add(new XYChart.Data<>(arg, productValue.getImaginary()));
            } catch (ArithmeticException ex) {
                isProductCorrect = false;
                setVisibility(null, false);
            }
        }
        List<Complex> solutions = new ArrayList<>();
        switch (selected.size()) {
            case 1:
                solutions.add(MathUtil.linear(selected.get(0).getBlaschke().getA(), constant));
                break;
            case 2:
                solutions = MathUtil.quadratic(selected.get(0).getBlaschke().getA(), selected.get(1).getBlaschke().getA(), constant);
                break;
            case 3:
                try {
                    solutions = MathUtil.cubic(selected.get(0).getBlaschke().getA(), selected.get(1).getBlaschke().getA(), selected.get(2).getBlaschke().getA(), constant);
                } catch (IllegalArgumentException ex) {
                    isProductCorrect = false;
                    setVisibility(null, false);
                    throw ex;
                }
                break;
        }
        isProductCorrect = true;
        setVisibility(null, true);
        if (equationsSideController.getTbProductHide().isSelected()) {
            setVisibility(null, false);
        }
        for (int i = 0; i < lcSolutions.getData().size(); ++i) {
            lcSolutions.getData().get(i).setName("-");
        }
        for (int i = 0; i < solutions.size(); ++i) {
            XYChart.Series<Number, Number> rootI = lcSolutions.getData().get(i);
            Complex c = solutions.get(i).round();
            rootI.setName(c.toString());
            rootI.getData().add(new XYChart.Data<>(c.argument(), c.getReal()));
            rootI.getData().add(new XYChart.Data<>(c.argument(), c.getImaginary()));
        }

    }

    @FXML
    private void setPoint(MouseEvent click) {
        tiHighlighted = equationsSideController.getTIHighlighted();
        if (tiHighlighted != null) {
            tiHighlighted.getPoint().setClip(new Circle(circle.getCenterX(), circle.getCenterY(), circle.getRadius()));
            double re = click.getX();
            double im = click.getY();
            tiHighlighted.setRe(re);
            tiHighlighted.setIm(im);
            try {
                draw(tiHighlighted, true);
                equationsSideController.setErrorMessage("");
            } catch (IllegalArgumentException ex) {
                equationsSideController.setErrorMessage("Blaschke parameter error: Division by 0");
            } catch (ArithmeticException ex) {
                equationsSideController.setErrorMessage("Blaschke parameter error: Denominator is 0");
            }
        }
    }

    @FXML
    private void dragPoint(MouseEvent drag) {
        double re = drag.getX();
        double im = drag.getY();
        if (tiHighlighted != null) {
            tiHighlighted.setRe(re);
            tiHighlighted.setIm(im);
            try {
                draw(tiHighlighted, false);
                equationsSideController.setErrorMessage("");
            } catch (IllegalArgumentException ex) {
                equationsSideController.setErrorMessage("Blaschke parameter error: Division by 0");
            } catch (ArithmeticException ex) {
                equationsSideController.setErrorMessage("Blaschke parameter error: Denominator is 0");
            }
        }
    }

    @FXML
    private void setChart() {
        if (tiHighlighted != null) {
            try {
                draw(tiHighlighted, true);
                equationsSideController.setErrorMessage("");
            } catch (IllegalArgumentException ex) {
                equationsSideController.setErrorMessage("Blaschke parameter error: Division by 0");
            } catch (ArithmeticException ex) {
                equationsSideController.setErrorMessage("Blaschke parameter error: Denominator is 0");
            }
        }
    }

    public StringProperty getMouseCoords() {
        return mouseCoords;
    }

    @FXML
    private void setMouseCoords(MouseEvent move) {
        int scale = MainController.getScale();
        double x = Math.round(100 * move.getX() / scale) / 100.0;
        double y = Math.round(100 * move.getY() / scale) / 100.0;
        mouseCoords.setValue("(" + x + "," + y + ")");
    }

}
