package lk.ijse.dep10.app.controller.util;

import javafx.scene.paint.Color;

public class Rect {
    public double x;
    public double y;
    public double width;
    public double height;
    public Color fill;
    public Color stroke;

    public Rect(double x, double y, double width, double height, Color fill, Color stroke) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.fill = fill;
        this.stroke = stroke;
    }
}
