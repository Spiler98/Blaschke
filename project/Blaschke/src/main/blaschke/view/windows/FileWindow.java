package main.blaschke.view.windows;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.blaschke.model.shapes.*;
import main.blaschke.controllers.MainController;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.*;

public abstract class FileWindow {

    private static final int scale = MainController.getScale();
    private static FileChooser fileChooser;
    private static File file;

    private static JSONObject joMain;
    private static JSONArray jaPoints;
    private static JSONArray jaLines;
    private static JSONArray jaArcs;
    private static JSONArray jaCircles;
    private static Map<String, ArrayList<TIShape>> tiShapes;

    private static void createWindow() {
        file = new File(System.getProperty("user.dir"), "data/json");
        if (!file.exists()) {
            file.mkdirs();
        }
        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(file);
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extensionFilter);
    }

    private static void exportWindow() {
        file = new File(System.getProperty("user.dir"), "data/img");
        if (!file.exists()) {
            file.mkdirs();
        }
        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(file);
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extensionFilter);
    }

    public static void exportImage(WritableImage screenshot) {
        exportWindow();
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(screenshot, null), "png", file);
            } catch (IOException e) {
                ErrorWindow.display("Cannot export image!");
                e.printStackTrace();
            }
        }
    }

    public static void saveFileWindow(List<TIShape> tiShapeList) {
        joMain = new JSONObject();
        jaPoints = new JSONArray();
        jaLines = new JSONArray();
        jaArcs = new JSONArray();
        jaCircles = new JSONArray();
        for (TIShape tiShape : tiShapeList) {
            if (tiShape instanceof TIPoint) {
                jaPoints.put(tiShape.getJSON());
            } else if (tiShape instanceof TILine) {
                jaLines.put(tiShape.getJSON());
            } else if (tiShape instanceof TIArc) {
                jaArcs.put(tiShape.getJSON());
            } else {
                jaCircles.put(tiShape.getJSON());
            }
        }
        joMain.put("Points", jaPoints).put("Lines", jaLines).put("Arcs", jaArcs).put("Circles", jaCircles);
        createWindow();
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
                pw.println(joMain);
            } catch (IOException e) {
                ErrorWindow.display("Cannot save the file!");
                e.printStackTrace();
            }
        }
    }

    public static Map<String, ArrayList<TIShape>> openFileWindow() {
        StringBuilder sb = new StringBuilder();
        createWindow();
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            try (Scanner sc = new Scanner(new BufferedReader(new FileReader(file)))) {
                while (sc.hasNextLine()) {
                    sb.append(sc.nextLine());
                }
                tiShapes = new HashMap<>();
                tiShapes.put("Points", new ArrayList<>());
                tiShapes.put("Lines", new ArrayList<>());
                tiShapes.put("Arcs", new ArrayList<>());
                tiShapes.put("Circles", new ArrayList<>());
                try {
                    joMain = new JSONObject(sb.toString());
                    jaPoints = joMain.getJSONArray("Points");
                    processJSON(jaPoints, "Points");
                    jaLines = joMain.getJSONArray("Lines");
                    processJSON(jaLines, "Lines");
                    jaArcs = joMain.getJSONArray("Arcs");
                    processJSON(jaArcs, "Arcs");
                    jaCircles = joMain.getJSONArray("Circles");
                    processJSON(jaCircles, "Circles");
                } catch (JSONException | NumberFormatException ex) {
                    ErrorWindow.display("JSON file error: " + ex.getMessage());
                }
                return tiShapes;

            } catch (FileNotFoundException e) {
                ErrorWindow.display("Cannot read the file!");
                e.printStackTrace();
            }
        }
        return null;
    }

    private static void processJSON(JSONArray jaShape, String key) {
        for (int i = 0; i < jaShape.length(); ++i) {
            JSONObject jo = jaShape.getJSONObject(i);
            String name = jo.getString("name");
            double x = jo.getDouble("x") * scale;
            double y = jo.getDouble("y") * scale;
            String color = jo.getString("color");
            switch (key) {
                case "Points":
                    TIPoint tiPoint = new TIPoint();
                    tiPoint.setName(name);
                    tiPoint.setX0(x);
                    tiPoint.setY0(y);
                    tiPoint.setColor(color);
                    tiShapes.get(key).add(tiPoint);
                    break;
                case "Lines":
                    TILine tiLine = new TILine();
                    tiLine.setName(name);
                    tiLine.setX0(jaShape.getJSONObject(i).getDouble("x0") * scale);
                    tiLine.setY0(jaShape.getJSONObject(i).getDouble("y0") * scale);
                    tiLine.setX(x);
                    tiLine.setY(y);
                    tiLine.setColor(color);
                    tiShapes.get(key).add(tiLine);
                    break;
                case "Arcs":
                    TIArc tiArc = new TIArc();
                    tiArc.setName(name);
                    tiArc.setX0(x);
                    tiArc.setY0(y);
                    tiArc.setR(jaShape.getJSONObject(i).getDouble("r") * scale);
                    tiArc.setStartAngle(jaShape.getJSONObject(i).getDouble("startAngle"));
                    tiArc.setAngle(jaShape.getJSONObject(i).getDouble("angle"));
                    tiArc.setColor(color);
                    tiShapes.get(key).add(tiArc);
                    break;
                case "Circles":
                    TICircle tiCircle = new TICircle();
                    tiCircle.setName(name);
                    tiCircle.setX0(x);
                    tiCircle.setY0(y);
                    tiCircle.setR(jaShape.getJSONObject(i).getDouble("r") * scale);
                    tiCircle.setColor(color);
                    tiShapes.get(key).add(tiCircle);
            }
        }
    }

}
