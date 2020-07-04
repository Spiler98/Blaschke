package main.blaschke.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.blaschke.BlaschkeApplication;
import main.blaschke.model.util.TextFieldUtil;
import main.blaschke.model.complex.Blaschke;
import main.blaschke.model.shapes.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.*;

public class ShapesSideController implements Initializable {

    private ShapesMainController shapesMainController;

    private StringProperty errorMessage;
    private Blaschke blaschke;
    @FXML private TextField tfBPX;
    @FXML private TextField tfBPY;

    @FXML private ComboBox<String> cbChooseShape;
    @FXML private TreeView<HBox> tvShapes;
    @FXML private TreeItem<HBox> tiPoints, tiLines, tiArcs, tiCircles;
    private static List<TIShape> tiShapeList;
    private TIShape tiHighlighted;
    private TIShape tiOldHighlighted;

    private Stack<TIShapeCache> undoCache;
    private Stack<TIShapeCache> redoCache;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorMessage = new SimpleStringProperty();
        undoCache = new Stack<>();
        redoCache = new Stack<>();
        tiShapeList = new ArrayList<>();
        tvShapes.prefHeightProperty().bind(BlaschkeApplication.getPrefTreeViewHeight().subtract(250));
        tvShapes.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            if (tvShapes.getTreeItemLevel(newValue) == 2) {
                tiHighlighted = (TIShape) newValue;
                tiHighlighted.setEffect();
                if (oldValue != null && tvShapes.getTreeItemLevel(oldValue) == 2) {
                    tiOldHighlighted = (TIShape) oldValue;
                    tiOldHighlighted.removeEffect();
                }
            } else {
                tiHighlighted = null;
            }
        });
        setUpBlaschkeParameter();

    }

    public void injectController(ShapesMainController shapesMainController) {
        this.shapesMainController = shapesMainController;
    }

    @FXML
    public void clearShape() {
        redoCache.clear();
        for (TIShape tiShape : tiShapeList) {
            undoCache.add(new TIShapeCache(tiShape, "Delete"));
        }
        tiPoints.getChildren().remove(0, tiPoints.getChildren().size());
        tiLines.getChildren().remove(0, tiLines.getChildren().size());
        tiArcs.getChildren().remove(0, tiArcs.getChildren().size());
        tiCircles.getChildren().remove(0, tiCircles.getChildren().size());
        TICircle.resetCounter();
        TILine.resetCounter();
        TIArc.resetCounter();
        TIPoint.resetCounter();
        shapesMainController.removeAllShapes();
        tiShapeList.clear();
    }

    @FXML
    private void addShape() {
        redoCache.clear();
        String selectedShape = cbChooseShape.getValue();
        switch (selectedShape) {
            case "Point":
                TIPoint tiPoint = new TIPoint();
                tiPoint.getBtnDelete().setOnAction(e -> deleteShape(tiPoints, tiPoint));
                toggleShapeVisibility(tiPoint);
                setUpTextField(tiPoint);
                tiPoints.getChildren().add(tiPoint);
                tiShapeList.add(tiPoint);
                undoCache.add(new TIShapeCache(tiPoint, "Add"));
                break;
            case "Line":
                TILine tiLine = new TILine();
                tiLine.getBtnDelete().setOnAction(e -> deleteShape(tiLines, tiLine));
                toggleShapeVisibility(tiLine);
                setUpTextField(tiLine);
                tiLines.getChildren().add(tiLine);
                tiShapeList.add(tiLine);
                undoCache.add(new TIShapeCache(tiLine, "Add"));
                break;
            case "Arc":
                TIArc tiArc = new TIArc();
                tiArc.getBtnDelete().setOnAction(e -> deleteShape(tiArcs, tiArc));
                toggleShapeVisibility(tiArc);
                setUpTextField(tiArc);
                tiArcs.getChildren().add(tiArc);
                tiShapeList.add(tiArc);
                undoCache.add(new TIShapeCache(tiArc, "Add"));
                break;
            case "Circle":
                TICircle tiCircle = new TICircle();
                tiCircle.getBtnDelete().setOnAction(e -> deleteShape(tiCircles, tiCircle));
                toggleShapeVisibility(tiCircle);
                setUpTextField(tiCircle);
                tiCircles.getChildren().add(tiCircle);
                tiShapeList.add(tiCircle);
                undoCache.add(new TIShapeCache(tiCircle, "Add"));
                break;
        }
    }

    private void toggleShapeVisibility(TIShape tiShape) {
        ToggleButton tbHide = tiShape.getTbHide();
        tbHide.setOnAction(e -> {
            if (tbHide.isSelected()) {
                tbHide.setGraphic(new ImageView(new Image("resources/icons/sideicons/hide.png")));
                tiShape.getZShape().setVisible(false);
                tiShape.getWShape().setVisible(false);
            } else {
                tbHide.setGraphic(new ImageView(new Image("resources/icons/sideicons/show.png")));
                tiShape.getZShape().setVisible(true);
                tiShape.getWShape().setVisible(true);
            }
        });
    }

    public void deleteShape(TreeItem<HBox> shapeType, TIShape tiShape) {
        redoCache.clear();
        undoCache.add(new TIShapeCache(tiShape, "Delete"));
        shapeType.getChildren().remove(tiShape);
        tiShape.decrementCounter();
        shapesMainController.removeShape(tiShape);
        tiShapeList.remove(tiShape);
    }

    private void setUpTextField(TIShape tiShape) {
        for (TextField tf : tiShape.getTextFields()) {
            TextFieldUtil.getChangeListenerForDecimalTextField(tf);
            tf.addEventHandler(KeyEvent.KEY_RELEASED, e -> {
                try {
                    tiShape.setZShapeFromTextFields();
                    shapesMainController.drawShape(tiShape);
                    errorMessage.setValue("");
                } catch (NumberFormatException ex) {
                    errorMessage.setValue("Shape parameter error: Only decimal values are accepted");
                }
            });
        }
    }

    private void setUpBlaschkeParameter() {
        blaschke = new Blaschke();
        TextFieldUtil.getChangeListenerForDecimalTextField(tfBPX);
        TextFieldUtil.getChangeListenerForDecimalTextField(tfBPY);
        tfBPX.addEventHandler(KeyEvent.KEY_RELEASED, e -> {
            try {
                blaschke.setAReal(Double.parseDouble(tfBPX.getText()));
                shapesMainController.refreshShapes();
                errorMessage.setValue("");
            } catch (NumberFormatException ex) {
                errorMessage.setValue("Blaschke parameter error: Only decimal values are accepted");
            } catch (ArithmeticException ex) {
                tfBPX.setStyle("-fx-border-color: red; -fx-border-radius: 0 4 4 0;");
                errorMessage.setValue("Blaschke parameter error: Denominator is 0");
            }
        });
        tfBPY.addEventHandler(KeyEvent.KEY_RELEASED, e -> {
            try {
                blaschke.setAImaginary(Double.parseDouble(tfBPY.getText()));
                shapesMainController.refreshShapes();
                errorMessage.setValue("");
            } catch (NumberFormatException ex) {
                errorMessage.setValue("Blaschke parameter error: Only decimal values are accepted");
            } catch (ArithmeticException ex) {
                tfBPY.setStyle("-fx-border-color: red; -fx-border-radius: 0 4 4 0;");
                errorMessage.setValue("Blaschke parameter error: Denominator is 0");
            }
        });
    }

    public Blaschke getBlaschke() {
        return blaschke;
    }

    public TIShape getTIHighlighted() {
        return tiHighlighted;
    }

    public StringProperty getTextFieldError() {
        return errorMessage;
    }

    public static List<TIShape> getTIShapeList() {
        return tiShapeList;
    }

    public void addShapesFromFile(Map<String, ArrayList<TIShape>> tiShapes) {
        redoCache.clear();
        tiShapes.forEach((shapeName, tiShapeArrayList) -> {
            for (TIShape tiShape : tiShapeArrayList) {
                undoCache.add(new TIShapeCache(tiShape, "Add"));
                setUpTextField(tiShape);
                toggleShapeVisibility(tiShape);
                shapesMainController.drawShape(tiShape);
                tiShapeList.add(tiShape);
                switch (shapeName) {
                    case "Points":
                        tiShape.getBtnDelete().setOnAction(e -> deleteShape(tiPoints, tiShape));
                        tiPoints.getChildren().add(tiShape);
                        break;
                    case "Lines":
                        tiShape.getBtnDelete().setOnAction(e -> deleteShape(tiLines, tiShape));
                        tiLines.getChildren().add(tiShape);
                        break;
                    case "Arcs":
                        tiShape.getBtnDelete().setOnAction(e -> deleteShape(tiArcs, tiShape));
                        tiArcs.getChildren().add(tiShape);
                        break;
                    case "Circles":
                        tiShape.getBtnDelete().setOnAction(e -> deleteShape(tiCircles, tiShape));
                        tiCircles.getChildren().add(tiShape);
                        break;
                }
            }
        });
    }

    public Stack<TIShapeCache> getUndoCache() {
        return undoCache;
    }

    public Stack<TIShapeCache> getRedoCache() {
        return redoCache;
    }

    public void addShapeViaEdit(TIShape tiShape) {
        setUpTextField(tiShape);
        toggleShapeVisibility(tiShape);
        shapesMainController.drawShape(tiShape);
        tiShapeList.add(tiShape);
        if (tiShape instanceof TIPoint) {
            tiShape.getBtnDelete().setOnAction(e -> deleteShape(tiPoints, tiShape));
            tiPoints.getChildren().add(tiShape);
        } else if (tiShape instanceof TILine) {
            tiShape.getBtnDelete().setOnAction(e -> deleteShape(tiLines, tiShape));
            tiLines.getChildren().add(tiShape);
        } else if (tiShape instanceof TIArc) {
            tiShape.getBtnDelete().setOnAction(e -> deleteShape(tiArcs, tiShape));
            tiArcs.getChildren().add(tiShape);
        } else {
            tiShape.getBtnDelete().setOnAction(e -> deleteShape(tiCircles, tiShape));
            tiCircles.getChildren().add(tiShape);
        }
    }

    public void deleteShapeViaEdit(TIShape tiShape) {
        shapesMainController.removeShape(tiShape);
        tiShapeList.remove(tiShape);
        if (tiShape instanceof TIPoint) {
            tiPoints.getChildren().remove(tiShape);
        } else if (tiShape instanceof TILine) {
            tiLines.getChildren().remove(tiShape);
        } else if (tiShape instanceof TIArc) {
            tiArcs.getChildren().remove(tiShape);
        } else {
            tiCircles.getChildren().remove(tiShape);
        }
    }
}
