package lk.ijse.dep10.app.controller;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXSlider;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.util.List;

public class MainFormController {
    public Canvas cnvMain;
    public ColorPicker clrStroke;
    public ColorPicker clrFill;
    public AnchorPane root;
    public ToggleButton btnPencil;
    public JFXHamburger hamburger;
    public JFXDrawer drawer;
    public VBox vBox;
    public ToggleButton btnRect;
    public ToggleButton btnRoundRect;
    public ToggleButton btnCircle;
    public ToggleButton btnText;
    public ToggleButton btnEraser;
    public JFXSlider fontSizeSlider;
    public ComboBox<Integer> fontSizeComboBox;
    public VBox vBoxFont;
    public ComboBox<String> fontFamilyComboBox;
    private double x1;
    private double y1;
    private WritableImage snapshot;
    private GraphicsContext gc;
    private final String selectedColor = "-fx-base: #D8D2E7;";
    private String enteredText = "";
    private TextField textField;


    public void initialize() {
        /* It seems like the canvas is not resizing in spite of setting the anchors */
        cnvMain.widthProperty().bind(root.widthProperty());
        cnvMain.heightProperty().bind(root.heightProperty());

        gc = cnvMain.getGraphicsContext2D();
        gc.setStroke(clrStroke.getValue());
        gc.setFill(clrFill.getValue());

        drawer.setSidePane(vBox); // Add side pane to the drawer
        drawer.open(); // Keep the drawer open initially

        // Add a click event for the hamburger icon to toggle the drawer
        hamburger.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (drawer.isOpened()) {
                drawer.close(); // Close the drawer
                cnvMain.toFront();
            } else {
                drawer.open();  // Open the drawer
                cnvMain.toBack();
            }
            // Make sure hamburger is always on top
            root.getChildren().remove(hamburger);
            root.getChildren().add(hamburger);
        });

        ToggleGroup group = new ToggleGroup(); // Create toggle group to select only clicked button and unselect others.
        btnRect.setToggleGroup(group);
        btnRoundRect.setToggleGroup(group);
        btnCircle.setToggleGroup(group);
        btnText.setToggleGroup(group);
        btnPencil.setToggleGroup(group);
        btnEraser.setToggleGroup(group);

        btnRect.setSelected(true);// Select rectangle initially
        btnRect.setStyle(selectedColor);

        btnRect.setOnMouseClicked(event -> changeSelectedButtonColorAndFontAppearance(btnRect));
        btnRoundRect.setOnMouseClicked(event -> changeSelectedButtonColorAndFontAppearance(btnRoundRect));
        btnCircle.setOnMouseClicked(event -> changeSelectedButtonColorAndFontAppearance(btnCircle));
        btnText.setOnMouseClicked(event -> changeSelectedButtonColorAndFontAppearance(btnText));
        btnPencil.setOnMouseClicked(event -> changeSelectedButtonColorAndFontAppearance(btnPencil));
        btnEraser.setOnMouseClicked(event -> changeSelectedButtonColorAndFontAppearance(btnEraser));

        fontSizeSlider.setStyle("-jfx-default-track: #7d7d7d;" + " -jfx-default-thumb: #8c1af6;");

        fontSizeComboBox.setItems(FXCollections.observableList(List.of(8,9,10,11,12,14,16,18,24,32,48,60,72)));
        fontSizeComboBox.setValue(32); // Default font size

        fontFamilyComboBox.setItems(FXCollections.observableArrayList(Font.getFamilies()));
        fontFamilyComboBox.setValue("Arial"); // Default font family

        fontSizeComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal!= null)fontSizeSlider.setValue(newVal);
        });

        fontSizeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            int round = (int) (Math.round(newVal.doubleValue()));
            System.out.println(round + " - " + newVal.doubleValue());
            fontSizeComboBox.setValue(round);
        });

        vBoxFont.setVisible(false);
        vBoxFont.setManaged(false);

        clrFill.setValue(Color.WHITE);
        clrStroke.setValue(Color.BLACK);

        clrFill.setStyle("-fx-font-weight: bold;");
        clrStroke.setStyle("-fx-font-weight: bold;");
    }


    private void changeSelectedButtonColorAndFontAppearance(ToggleButton selectedButton) {
        vBoxFont.setVisible(selectedButton == btnText);
        vBoxFont.setManaged(selectedButton == btnText); // Show font options only if btnText is selected

         // Change color for the selected button, reset others
        btnRect.setStyle(btnRect == selectedButton ? selectedColor : "");
        btnRoundRect.setStyle(btnRoundRect == selectedButton ? selectedColor : "");
        btnCircle.setStyle(btnCircle == selectedButton ? selectedColor : "");
        btnText.setStyle(btnText == selectedButton ? selectedColor : "");
        btnPencil.setStyle(btnPencil == selectedButton ? selectedColor : "");
        btnEraser.setStyle(btnEraser == selectedButton ? selectedColor : "");
    }

    public void cnvMainOnMouseDragged(MouseEvent mouseEvent) {

        double x2 = mouseEvent.getX();
        double y2 = mouseEvent.getY();
        double width = x2 - x1;
        double height = y2 - y1;

        if (btnRect.isSelected()) {
            drawRectangle(x1, y1, width, height);
        } else if (btnRoundRect.isSelected()) {
            drawRoundRectangle(x1, y1, width, height);
        } else if (btnCircle.isSelected()) {
            drawCircle(x1, y1, width, height);
        } else if (btnPencil.isSelected()) {
            gc.lineTo(x2, y2);
            gc.stroke();
        } else if (btnEraser.isSelected()) {
            // Implement eraser functionality if needed
        }

    }

    public void cnvMainOnMousePressed(MouseEvent mouseEvent) {
        x1 = mouseEvent.getX();
        y1 = mouseEvent.getY();
        snapshot = cnvMain.snapshot(new SnapshotParameters(), null);

        if (btnText.isSelected()) {
            showTextField(x1, y1); // Show TextField at mouse click
        } else if (btnPencil.isSelected()) {
            gc.beginPath();
        }
    }

    private void showTextField(double x, double y) {
        if (textField != null) {
            root.getChildren().remove(textField); // Remove the TextField from the root pane
            textField = null;
        }
        // Create and configure the TextField
        textField = new TextField();
        textField.setText("Text Here");
        textField.selectAll();
        textField.setLayoutX(x);
        textField.setLayoutY(y);
        textField.setPrefWidth(150);
        textField.setPrefHeight(30);

        root.getChildren().add(textField);

        textField.requestFocus();

        // Handle Enter key press or focus loss to finalize text entry
        textField.setOnKeyPressed(evt -> {
            if (!textField.isFocused() || evt.getCode() == KeyCode.ENTER || evt.getCode() == KeyCode.TAB) {
                finalizeTextEntry();
            }
        });

    }

    private void finalizeTextEntry() {
        if (textField == null) return;

        enteredText = textField.getText();
        if (enteredText != null && !enteredText.isEmpty()) {
            drawText(); // Draw the entered text on the canvas
        }

        root.getChildren().remove(textField); // Remove the TextField from the root pane
        textField = null;
    }

    private void drawRectangle(double x, double y, double width, double height) {
        gc.drawImage(snapshot, 0, 0);
        gc.setFill(clrFill.getValue());
        gc.setStroke(clrStroke.getValue());
        gc.strokeRect(width < 0 ? x : x1, height < 0 ? y : y1, Math.abs(width), Math.abs(height));
        gc.fillRect(width < 0 ? x : x1, height < 0 ? y : y1, Math.abs(width), Math.abs(height));
    }

    private void drawRoundRectangle(double x, double y, double width, double height) {
        gc.drawImage(snapshot, 0, 0);
        gc.setFill(clrFill.getValue());
        gc.setStroke(clrStroke.getValue());
        gc.strokeRoundRect(width < 0 ? x : x1, height < 0 ? y : y1, Math.abs(width), Math.abs(height), 30.0, 30.0);
        gc.fillRoundRect(width < 0 ? x : x1, height < 0 ? y : y1, Math.abs(width), Math.abs(height), 30.0, 30.0);
    }

    private void drawCircle(double x, double y, double width, double height) {
        gc.drawImage(snapshot, 0, 0);
        gc.setFill(clrFill.getValue());
        gc.setStroke(clrStroke.getValue());
        gc.strokeOval(width < 0 ? x : x1, height < 0 ? y : y1, Math.abs(width), Math.abs(height));
        gc.fillOval(width < 0 ? x : x1, height < 0 ? y : y1, Math.abs(width), Math.abs(height));
    }

    private void drawText() {
        String fontFamily = fontFamilyComboBox.getValue();
        double fontSize = fontSizeSlider.getValue();
        Font font = Font.font(fontFamily, FontWeight.NORMAL, FontPosture.REGULAR, fontSize);

        gc.drawImage(snapshot, 0, 0);
        gc.setFont(font);
        gc.setFill(clrFill.getValue());
        gc.setStroke(clrStroke.getValue());
        gc.fillText(enteredText, x1, y1); // Place text at pressed location
        gc.strokeText(enteredText, x1, y1);
    }

    public void cnvMainOnMouseReleased(MouseEvent mouseEvent) {
        System.out.println("Release");
        /*double x2 = mouseEvent.getX();
        double y2 = mouseEvent.getY();
        double width = x2 - x1;
        double height = y2 - y1;*/

    }

    public void clrFillOnAction(ActionEvent actionEvent) {
        gc.setFill(clrFill.getValue());
    }

    public void clrStrokeOnAction(ActionEvent actionEvent) {
        gc.setStroke(clrStroke.getValue());
    }


}


