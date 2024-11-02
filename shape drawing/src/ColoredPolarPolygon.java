import java.awt.*;

public class ColoredPolarPolygon {

    private int centerX, centerY, rotation;
    private final int sides, index;
    private final double[][] polarValues;
    private final Color color;

    public ColoredPolarPolygon(double[][] polarValues, int centerX, int centerY, int rotation, Color color, int index) {

        this.polarValues = polarValues;
        this.sides = polarValues.length;
        this.centerX = centerX;
        this.centerY = centerY;
        this.rotation = rotation;
        this.color = color;
        this.index = index;

    }

    public double[][] getPolarValues() {
        return polarValues;
    }

    public int getSides() {
        return this.sides;
    }

    public int getCenterX() {
        return centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public int getRotation() {
        return rotation;
    }

    public Color getColor() {

        return this.color;

    }

    public int getIndex() {
        return index;
    }

    public void setCenter(int centerX, int centerY) {

        this.centerX = centerX;
        this.centerY = centerY;

    }

    public void rotate(int amount) {

        rotation = (rotation + amount) % (sides * 2);

    }

    public Polygon getPolygon() {

        Polygon polygon = new Polygon();

        for(double[] points : polarValues) {

            double angle = points[0] + Math.PI / sides * rotation;
            polygon.addPoint((int) (points[1] * Math.cos(angle)) + centerX, (int) (points[1] * Math.sin(angle)) + centerY);

        }

        return polygon;

    }

    public ColoredPolarPolygon getCopy() {

        double[][] copyArray = new double[polarValues.length][];
        for(int i = 0; i < copyArray.length; i++) copyArray[i] = polarValues[i].clone();

        return new ColoredPolarPolygon(copyArray, centerX, centerY, rotation, color, index);

    }

}
