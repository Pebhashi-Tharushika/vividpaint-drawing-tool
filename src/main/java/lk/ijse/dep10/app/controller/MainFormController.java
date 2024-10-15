package lk.ijse.dep10.app.controller;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lk.ijse.dep10.app.controller.util.Rect;

import java.util.Arrays;

public class MainFormController {
    public Canvas cnvMain;
    public ColorPicker clrStroke;
    public ColorPicker clrFill;
    public AnchorPane root;
    public ToggleButton btnPencil;
    public JFXHamburger hamburger;
    public JFXDrawer drawer;
    public VBox vBox;
    private double x1;
    private double y1;
    private Rect[] rects = new Rect[50];
    private int index = 0;
    //    private WritableImage snapshot;
    private GraphicsContext gc;

    public void initialize() {
        /* It seems like the canvas is not resizing in spite of setting the anchors */
        cnvMain.widthProperty().bind(root.widthProperty());
        cnvMain.heightProperty().bind(root.heightProperty());

        gc = cnvMain.getGraphicsContext2D();
        gc.setStroke(clrStroke.getValue());
        gc.setFill(clrFill.getValue());

        // Set up the drawer and add side pane
        drawer.setSidePane(vBox);

        // Keep the drawer open initially
        drawer.open();

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


    }

    /*public void toPractice() {
        GraphicsContext gc = cnvMain.getGraphicsContext2D();
//        gc.setFill(Color.RED);
//        gc.fillRect(50,50, 150, 150);

        gc.setStroke(Color.RED);
        gc.strokeRect(50, 50, 150, 150);
        gc.clearRect(50, 50, 151, 151);

//        gc.setFill(Color.BLUE);
//        gc.fillRoundRect(250,50,150,150,20,20);

        gc.setStroke(Color.BLUE);
        gc.strokeRoundRect(250, 50, 150, 150, 20, 20);

        gc.setFont(new Font(32));
//        gc.setFill(Color.GREEN);
//        gc.fillText("IJSE", 50, 250);

        gc.setStroke(Color.GREEN);
        gc.strokeText("IJSE", 50, 250);

        gc.setFill(Color.ORANGE);
        gc.fillOval(450, 50, 150, 150);
    }*/

    public void cnvMainOnMouseDragged(MouseEvent mouseEvent) {
        System.out.println("drag");
        double x2 = mouseEvent.getX();
        double y2 = mouseEvent.getY();
        double width = x2 - x1;
        double height = y2 - y1;
//        GraphicsContext gc = cnvMain.getGraphicsContext2D();
        if (!btnPencil.isSelected()) {
            System.out.println("drag: pencil");
//            gc.drawImage(snapshot, 0,0);
            gc.clearRect(0, 0, cnvMain.getWidth(), cnvMain.getHeight());
            for (int i = 0; i < index; i++) {
                Rect rect = rects[i];
                gc.setStroke(rect.stroke);
                gc.setFill(rect.fill);
                gc.strokeRect(rect.x, rect.y, rect.width, rect.height);
                gc.fillRect(rect.x, rect.y, rect.width, rect.height);
            }
            gc.setFill(clrFill.getValue());
            gc.setStroke(clrStroke.getValue());
            gc.strokeRect(width < 0 ? x2 : x1, height < 0 ? y2 : y1, Math.abs(width), Math.abs(height));
            gc.fillRect(width < 0 ? x2 : x1, height < 0 ? y2 : y1, Math.abs(width), Math.abs(height));
        } else {
            System.out.println("drag: not pencil");
            gc.lineTo(x2, y2);
            gc.stroke();
        }
    }

    public void cnvMainOnMousePressed(MouseEvent mouseEvent) {
        System.out.println("press");
        x1 = mouseEvent.getX();
        y1 = mouseEvent.getY();
//        snapshot = cnvMain.snapshot(new SnapshotParameters(), null);
        if (btnPencil.isSelected()) {
            System.out.println("Press: pencil");
            gc.beginPath();
        }
    }

    public void cnvMainOnMouseReleased(MouseEvent mouseEvent) {
        System.out.println("Release");
        double x2 = mouseEvent.getX();
        double y2 = mouseEvent.getY();
        double width = x2 - x1;
        double height = y2 - y1;
        Rect rect = new Rect(x1, y1, width, height, clrFill.getValue(), clrStroke.getValue());
        rects[index++] = rect;
        System.out.println(Arrays.toString(rects));
    }

    public void clrFillOnAction(ActionEvent actionEvent) {
        gc.setFill(clrFill.getValue());
    }

    public void clrStrokeOnAction(ActionEvent actionEvent) {
        gc.setStroke(clrStroke.getValue());
    }
}


