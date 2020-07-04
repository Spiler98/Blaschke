package main.blaschke.controllers;

import javafx.scene.SnapshotParameters;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import main.blaschke.BlaschkeApplication;
import main.blaschke.model.shapes.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import main.blaschke.view.windows.AboutWindow;
import main.blaschke.view.windows.ErrorWindow;
import main.blaschke.view.windows.FileWindow;
import main.blaschke.view.windows.OptionsWindow;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {

    private static final int scale = 150;
    private static String mode = "Dark mode";

    @FXML private BorderPane borderPane;
    @FXML private VBox vbShapesSide;
    @FXML private VBox vbShapesMain;
    @FXML private VBox vbEquationsSide;
    @FXML private VBox vbEquationsMain;
    @FXML private VBox vbActiveMain;

    @FXML private ShapesMainController vbShapesMainController;
    @FXML private ShapesSideController vbShapesSideController;
    @FXML private EquationsSideController vbEquationsSideController;
    @FXML private EquationsMainController vbEquationsMainController;

    @FXML private TabPane tabPane;
    @FXML private Tab tEquations;

    @FXML private Label lMouseCoords;
    @FXML private Label lMessages;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        vbShapesSideController.injectController(vbShapesMainController);
        vbShapesMainController.injectController(vbShapesSideController);
        vbEquationsSideController.injectController(vbEquationsMainController);
        vbEquationsMainController.injectController(vbEquationsSideController);
        vbActiveMain = vbShapesMain;
        lMouseCoords.textProperty().bind(vbShapesMainController.getMouseCoords());
        lMessages.textProperty().bind(vbShapesSideController.getTextFieldError());
        tabPane.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            if (newValue == tEquations) {
                vbActiveMain = vbEquationsMain;
                lMouseCoords.textProperty().unbind();
                lMouseCoords.textProperty().bind(vbEquationsMainController.getMouseCoords());
                lMessages.textProperty().unbind();
                lMessages.textProperty().bind(vbEquationsSideController.getTextFieldError());
                borderPane.getStylesheets().remove("main/blaschke/view/style/BlueSwitch.css");
                borderPane.getStylesheets().add("main/blaschke/view/style/PurpleSwitch.css");
            } else {
                vbActiveMain = vbShapesMain;
                lMouseCoords.textProperty().unbind();
                lMouseCoords.textProperty().bind(vbShapesMainController.getMouseCoords());
                lMessages.textProperty().unbind();
                lMessages.textProperty().bind(vbShapesSideController.getTextFieldError());
                borderPane.getStylesheets().remove("main/blaschke/view/style/PurpleSwitch.css");
                borderPane.getStylesheets().add("main/blaschke/view/style/BlueSwitch.css");
            }
        });

    }

    @FXML
    private void newCalculation() {
        vbShapesSideController.clearShape();
        vbShapesSideController.getUndoCache().clear();
        vbShapesSideController.getRedoCache().clear();
        vbEquationsSideController.clear();
    }

    @FXML
    private void openCalculation() {
        Map<String, ArrayList<TIShape>> shapes = FileWindow.openFileWindow();
        if (shapes != null) {
            vbShapesSideController.addShapesFromFile(shapes);
        }
    }

    @FXML
    private void saveCalculation() {
        FileWindow.saveFileWindow(ShapesSideController.getTIShapeList());
    }

    @FXML
    private void exportCalculationAsImage() {
        WritableImage screenshot = vbActiveMain.snapshot(new SnapshotParameters(), null);
        FileWindow.exportImage(screenshot);
    }

    @FXML
    private void close() {
        BlaschkeApplication.close();
    }

    @FXML
    private void undo() {
        Stack<TIShapeCache> undoCache = vbShapesSideController.getUndoCache();
        if (!undoCache.empty()) {
            TIShapeCache tiShapeCache = undoCache.pop();
            vbShapesSideController.getRedoCache().add(tiShapeCache);
            if (tiShapeCache.getOperation().equals("Delete")) {
                vbShapesSideController.addShapeViaEdit(tiShapeCache.getTIShape());
            } else {
                vbShapesSideController.deleteShapeViaEdit(tiShapeCache.getTIShape());
            }
        }
    }

    @FXML
    private void redo() {
        Stack<TIShapeCache> redoCache = vbShapesSideController.getRedoCache();
        if (!redoCache.empty()) {
            TIShapeCache tiShapeCache = redoCache.pop();
            vbShapesSideController.getUndoCache().add(tiShapeCache);
            if (tiShapeCache.getOperation().equals("Delete")) {
                vbShapesSideController.deleteShapeViaEdit(tiShapeCache.getTIShape());
            } else {
                vbShapesSideController.addShapeViaEdit(tiShapeCache.getTIShape());
            }
        }
    }

    @FXML
    private void cut() {
        TIShape tiHighlighted = vbShapesSideController.getTIHighlighted();
        if (tiHighlighted != null) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(tiHighlighted.getJSON().toString());
            clipboard.setContent(content);
            vbShapesSideController.deleteShape(tiHighlighted.getParent(), tiHighlighted);
        }
    }

    @FXML
    private void copy() {
        TIShape tiHighlighted = vbShapesSideController.getTIHighlighted();
        if (tiHighlighted != null) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(tiHighlighted.getJSON().toString());
            clipboard.setContent(content);
        }
    }

    @FXML
    private void paste() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        if (clipboard.getString() != null) {
            try {
                JSONObject jo = new JSONObject(clipboard.getString());
                String name = jo.getString("name");
                double x = jo.getDouble("x") * scale;
                double y = jo.getDouble("y") * scale;
                String color = jo.getString("color");
                switch (jo.length()) {
                    case 4:
                        TIPoint tiPoint = new TIPoint();
                        tiPoint.setName(name);
                        tiPoint.setX0(x);
                        tiPoint.setY0(y);
                        tiPoint.setColor(color);
                        vbShapesSideController.addShapeViaEdit(tiPoint);
                        vbShapesSideController.getUndoCache().add(new TIShapeCache(tiPoint, "Add"));
                        break;
                    case 6:
                        TILine tiLine = new TILine();
                        tiLine.setName(name);
                        tiLine.setX0(jo.getDouble("x0") * scale);
                        tiLine.setY0(jo.getDouble("y0") * scale);
                        tiLine.setX(x);
                        tiLine.setY(y);
                        tiLine.setColor(color);
                        vbShapesSideController.addShapeViaEdit(tiLine);
                        vbShapesSideController.getUndoCache().add(new TIShapeCache(tiLine, "Add"));
                        break;
                    case 7:
                        TIArc tiArc = new TIArc();
                        tiArc.setName(name);
                        tiArc.setX0(x);
                        tiArc.setY0(y);
                        tiArc.setR(jo.getDouble("r") * scale);
                        tiArc.setStartAngle(jo.getDouble("startAngle"));
                        tiArc.setAngle(jo.getDouble("angle"));
                        tiArc.setColor(color);
                        vbShapesSideController.addShapeViaEdit(tiArc);
                        vbShapesSideController.getUndoCache().add(new TIShapeCache(tiArc, "Add"));
                        break;
                    case 5:
                        TICircle tiCircle = new TICircle();
                        tiCircle.setName(name);
                        tiCircle.setX0(x);
                        tiCircle.setY0(y);
                        tiCircle.setR(jo.getDouble("r") * scale);
                        tiCircle.setColor(color);
                        vbShapesSideController.addShapeViaEdit(tiCircle);
                        vbShapesSideController.getUndoCache().add(new TIShapeCache(tiCircle, "Add"));
                        break;
                }
            } catch (JSONException | NumberFormatException ex) {
                ErrorWindow.display("JSON file error: " + ex.getMessage());
            }
        }
    }

    @FXML
    private void delete() {
        TIShape tiHighlighted = vbShapesSideController.getTIHighlighted();
        if (tiHighlighted != null) {
            vbShapesSideController.deleteShape(tiHighlighted.getParent(), tiHighlighted);
        }
    }

    @FXML
    private void options() {
        String newMode = OptionsWindow.display(mode);
        if (!newMode.equals(mode)) {
            if (newMode.equals("Dark mode")) {
                borderPane.getStylesheets().remove("main/blaschke/view/style/light/LightMainStyle.css");
                vbShapesSide.getStylesheets().remove("main/blaschke/view/style/light/LightShapesSideStyle.css");
                vbShapesMain.getStylesheets().remove("main/blaschke/view/style/light/LightShapesMainStyle.css");
                vbEquationsSide.getStylesheets().remove("main/blaschke/view/style/light/LightEquationsSideStyle.css");
                vbEquationsMain.getStylesheets().remove("main/blaschke/view/style/light/LightEquationsMainStyle.css");
                borderPane.getStylesheets().add("main/blaschke/view/style/dark/MainStyle.css");
                vbShapesSide.getStylesheets().add("main/blaschke/view/style/dark/ShapesSideStyle.css");
                vbShapesMain.getStylesheets().add("main/blaschke/view/style/dark/ShapesMainStyle.css");
                vbEquationsSide.getStylesheets().add("main/blaschke/view/style/dark/EquationsSideStyle.css");
                vbEquationsMain.getStylesheets().add("main/blaschke/view/style/dark/EquationsMainStyle.css");
                mode = "Dark mode";
            } else {
                borderPane.getStylesheets().remove("main/blaschke/view/style/dark/MainStyle.css");
                vbShapesSide.getStylesheets().remove("main/blaschke/view/style/dark/ShapesSideStyle.css");
                vbShapesMain.getStylesheets().remove("main/blaschke/view/style/dark/ShapesMainStyle.css");
                vbEquationsSide.getStylesheets().remove("main/blaschke/view/style/dark/EquationsSideStyle.css");
                vbEquationsMain.getStylesheets().remove("main/blaschke/view/style/dark/EquationsMainStyle.css");
                borderPane.getStylesheets().add("main/blaschke/view/style/light/LightMainStyle.css");
                vbShapesSide.getStylesheets().add("main/blaschke/view/style/light/LightShapesSideStyle.css");
                vbShapesMain.getStylesheets().add("main/blaschke/view/style/light/LightShapesMainStyle.css");
                vbEquationsSide.getStylesheets().add("main/blaschke/view/style/light/LightEquationsSideStyle.css");
                vbEquationsMain.getStylesheets().add("main/blaschke/view/style/light/LightEquationsMainStyle.css");
                mode = "Light mode";
            }
            if (vbActiveMain == vbEquationsMain) {
                borderPane.getStylesheets().remove("main/blaschke/view/style/PurpleSwitch.css");
                borderPane.getStylesheets().add("main/blaschke/view/style/PurpleSwitch.css");
            }
        }
    }

    @FXML
    private void about() {
        AboutWindow.display();
    }

    @FXML
    private void openHelpPDF() {
        if (Desktop.isDesktopSupported()) {
            try {
                File file = new File("help.pdf");
                if (!file.exists()) {
                    InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("resources/pdf/help.pdf");
                    OutputStream outputStream = new FileOutputStream(file);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }
                    outputStream.close();
                    inputStream.close();
                }
                Desktop.getDesktop().open(file);
            } catch (IOException ex) {
                ErrorWindow.display("No application registered to PDF files");
            }
        } else {
            ErrorWindow.display("Desktop class is not supported on this platform");
        }
    }

    public static int getScale() {
        return scale;
    }

    public static String getMode() {
        return mode;
    }

}
