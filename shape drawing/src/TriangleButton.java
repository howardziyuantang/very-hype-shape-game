import javax.swing.*;
import java.awt.*;

public class TriangleButton extends JButton {

    private final Shape triangle;
    private final Dimension borderDimension;
    private final Color color;

    public TriangleButton(char direction, Dimension borderDimension, Color color) {

        triangle = createTriangle(direction, borderDimension.width, borderDimension.height);

        this.borderDimension = borderDimension;

        this.color = color;

    }

    private Shape createTriangle(char direction, int borderWidth, int borderHeight) {

        int xCorner = (direction == 'L') ? borderWidth : 0;
        return new Polygon(new int[]{xCorner, xCorner, (direction == 'L') ? 0 : borderWidth},
                new int[]{borderHeight, 0, borderHeight / 2}, 3);

    }

    @Override
    public void paintBorder(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(color);
        g2d.draw(triangle);

    }

    @Override
    public void paintComponent(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(color);
        g2d.fill(triangle);

    }

    @Override
    public Dimension getPreferredSize() {

        return borderDimension;

    }

    @Override
    public boolean contains(int x, int y) {

        return triangle.contains(x, y);

    }

}
