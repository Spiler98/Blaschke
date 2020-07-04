package main.blaschke.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import main.blaschke.BlaschkeApplication;
import main.blaschke.model.util.TextFieldUtil;
import main.blaschke.model.complex.Complex;
import main.blaschke.model.complex.TIBlaschke;

import java.net.URL;
import java.util.*;

public class EquationsSideController implements Initializable {

    private EquationsMainController equationsMainController;

    private StringProperty errorMessage;
    private Complex constant;
    private Set<TIBlaschke> blaschkesInProduct;

    @FXML private ToggleButton tbProductHide;
    @FXML private TextField tfCX;
    @FXML private TextField tfCY;
    @FXML private TreeView<HBox> tvBlaschke;
    @FXML private TreeItem<HBox> tiBlaschkeRoot;
    private TIBlaschke tiHighlighted;
    private TIBlaschke tiOldHighlighted;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        blaschkesInProduct = new HashSet<>();
        errorMessage = new SimpleStringProperty();
        for (int i = 0; i < 3; ++i) {
            TIBlaschke tiBlaschke = new TIBlaschke();
            toggleEquationVisibility(tiBlaschke);
            toggleBlaschkeInProduct(tiBlaschke);
            setUpBlaschke(tiBlaschke);
            tiBlaschkeRoot.getChildren().add(tiBlaschke);
        }
        tvBlaschke.prefHeightProperty().bind(BlaschkeApplication.getPrefTreeViewHeight().subtract(250));
        tvBlaschke.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            if (tvBlaschke.getTreeItemLevel(newValue) == 1) {
                tiHighlighted = (TIBlaschke) newValue;
                tiHighlighted.setEffect();
                if (oldValue != null && tvBlaschke.getTreeItemLevel(oldValue) == 1) {
                    tiOldHighlighted = (TIBlaschke) oldValue;
                    tiOldHighlighted.removeEffect();
                }
            } else {
                tiHighlighted = null;
            }
        });
        setUpConstant();
    }

    public void injectController(EquationsMainController equationsMainController) {
        this.equationsMainController = equationsMainController;
    }

    @FXML
    public void clear() {
        for (TreeItem<HBox> ti: tiBlaschkeRoot.getChildren()) {
            TIBlaschke tiBlaschke = (TIBlaschke) ti;
            tiBlaschke.setRe(0);
            tiBlaschke.setIm(0);
            try {
                equationsMainController.draw(tiBlaschke, true);
                errorMessage.setValue("");
                tfCX.setStyle("-fx-border-color: #7b7b7b;");
                tfCY.setStyle("-fx-border-color: #7b7b7b;");
            } catch (IllegalArgumentException ex) {
                errorMessage.setValue("Constant parameter error: Division by 0");
                tfCX.setStyle("-fx-border-color: red; -fx-border-radius: 0 4 4 0;");
                tfCY.setStyle("-fx-border-color: red; -fx-border-radius: 0 4 4 0;");
            } catch (ArithmeticException ex) {
                errorMessage.setValue("Blaschke parameter error: Denominator is 0");
            }
        }
    }

    private void toggleEquationVisibility(TIBlaschke tiBlaschke) {
        ToggleButton tbHide = tiBlaschke.getTbHide();
        tbHide.setOnAction(e -> {
            if (tbHide.isSelected()) {
                tbHide.setGraphic(new ImageView(new Image("resources/icons/sideicons/hide.png")));
                equationsMainController.setVisibility(tiBlaschke, false);
            } else {
                tbHide.setGraphic(new ImageView(new Image("resources/icons/sideicons/show.png")));
                equationsMainController.setVisibility(tiBlaschke, true);
            }
        });
    }

    @FXML
    private void toggleProductVisibility() {
        if (tbProductHide.isSelected()) {
            tbProductHide.setGraphic(new ImageView(new Image("resources/icons/sideicons/hide.png")));
            equationsMainController.setVisibility(null, false);
        } else {
            tbProductHide.setGraphic(new ImageView(new Image("resources/icons/sideicons/show.png")));
            equationsMainController.setVisibility(null, true);
        }
    }

    private void toggleBlaschkeInProduct(TIBlaschke tiBlaschke) {
        ToggleButton tbProduct = tiBlaschke.getTbProduct();
        tbProduct.setOnAction(e -> {
            if (tbProduct.isSelected()) {
                tbProduct.setGraphic(new ImageView(new Image("resources/icons/sideicons/on.png")));
                blaschkesInProduct.add(tiBlaschke);
            } else {
                tbProduct.setGraphic(new ImageView(new Image("resources/icons/sideicons/off.png")));
                blaschkesInProduct.remove(tiBlaschke);
            }
            try {
                equationsMainController.refresh();
                errorMessage.setValue("");
                tfCX.setStyle("-fx-border-color: #7b7b7b;");
                tfCY.setStyle("-fx-border-color: #7b7b7b;");
            } catch (IllegalArgumentException ex) {
                errorMessage.setValue("Constant parameter error: Division by 0");
                tfCX.setStyle("-fx-border-color: red; -fx-border-radius: 0 4 4 0;");
                tfCY.setStyle("-fx-border-color: red; -fx-border-radius: 0 4 4 0;");
            } catch (ArithmeticException ex) {
                errorMessage.setValue("Blaschke parameter error: Denominator is 0");
            }
        });
    }

    private void setUpBlaschke(TIBlaschke tiBlaschke) {
        List<TextField> tiList = tiBlaschke.getTextFields();
        for (int i = 0; i < tiList.size(); ++i) {
            TextFieldUtil.getChangeListenerForDecimalTextField(tiList.get(i));
            int finalI = i;
            tiList.get(i).addEventHandler(KeyEvent.KEY_RELEASED, e -> {
                try {
                    tiBlaschke.setPoint();
                    equationsMainController.draw(tiBlaschke, true);
                    errorMessage.setValue("");
                    switch (finalI) {
                        case 0:
                            tiList.get(1).setStyle("-fx-border-color: #7b7b7b;");
                            break;
                        case 1:
                            tiList.get(0).setStyle("-fx-border-color: #7b7b7b;");
                            break;
                    }
                    tfCX.setStyle("-fx-border-color: #7b7b7b;");
                    tfCY.setStyle("-fx-border-color: #7b7b7b;");
                } catch (NumberFormatException ex) {
                    errorMessage.setValue("Blaschke parameter error: Only decimal values are accepted");
                } catch (ArithmeticException ex) {
                    tiList.get(finalI).setStyle("-fx-border-color: red; -fx-border-radius: 0 4 4 0;");
                    errorMessage.setValue("Blaschke parameter error: Denominator is 0");
                } catch (IllegalArgumentException ex) {
                    errorMessage.setValue("Constant parameter error: Division by 0");
                    tfCX.setStyle("-fx-border-color: red; -fx-border-radius: 0 4 4 0;");
                    tfCY.setStyle("-fx-border-color: red; -fx-border-radius: 0 4 4 0;");
                }
            });
        }
    }

    private void setUpConstant() {
        constant = new Complex(1, 0);
        TextFieldUtil.getChangeListenerForDecimalTextField(tfCX);
        TextFieldUtil.getChangeListenerForDecimalTextField(tfCY);
        tfCX.addEventHandler(KeyEvent.KEY_RELEASED, e -> {
            try {
                constant.setReal(Double.parseDouble(tfCX.getText()));
                equationsMainController.refresh();
                errorMessage.setValue("");
                tfCX.setStyle("-fx-border-color: #7b7b7b;");
                tfCY.setStyle("-fx-border-color: #7b7b7b;");
            } catch (NumberFormatException ex) {
                errorMessage.setValue("Constant parameter error: Only decimal values are accepted");
            } catch (IllegalArgumentException ex) {
                errorMessage.setValue("Constant parameter error: Division by 0");
                tfCX.setStyle("-fx-border-color: red; -fx-border-radius: 0 4 4 0;");
            } catch (ArithmeticException ex) {
                errorMessage.setValue("Blaschke parameter error: Denominator is 0");
            }
        });
        tfCY.addEventHandler(KeyEvent.KEY_RELEASED, e -> {
            try {
                constant.setImaginary(Double.parseDouble(tfCY.getText()));
                equationsMainController.refresh();
                errorMessage.setValue("");
                tfCX.setStyle("-fx-border-color: #7b7b7b;");
                tfCY.setStyle("-fx-border-color: #7b7b7b;");
            } catch (NumberFormatException ex) {
                errorMessage.setValue("Constant parameter error: Only decimal values are accepted");
            } catch (IllegalArgumentException ex) {
                errorMessage.setValue("Constant parameter error: Division by 0");
                tfCY.setStyle("-fx-border-color: red; -fx-border-radius: 0 4 4 0;");
            } catch (ArithmeticException ex) {
                errorMessage.setValue("Blaschke parameter error: Denominator is 0");
            }
        });
    }


    public Complex getConstant() {
        return constant;
    }

    public TIBlaschke getTIHighlighted() {
        return tiHighlighted;
    }

    public ToggleButton getTbProductHide() { return tbProductHide; }

    public Set<TIBlaschke> getTIBlaschkes() {
        return blaschkesInProduct;
    }

    public StringProperty getTextFieldError() {
        return errorMessage;
    }

    public void setErrorMessage(String errormsg) {
        errorMessage.setValue(errormsg);
    }

}
