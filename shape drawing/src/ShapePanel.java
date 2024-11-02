import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ShapePanel extends GamePanel implements ActionListener, MouseInputListener {

    private final int numColors, topRotateBound, snapDistance;
    private int shapeIndex, rotateX, rotateY, totalRotation;
    private double excessRotation;
    private final int[] shapeCenter;
    private final ArrayList<Boolean> used;
    private final ArrayList<ColoredPolarPolygon> shapeList;
    private final ArrayList<Color> colorList;
    private boolean rotateClick, isFirstRotation;
    private ColoredPolarPolygon currentPolygon;

    public ShapePanel(GameFrame gameFrame, String difficulty) {

        super(gameFrame);

        addMouseListener(this);
        addMouseMotionListener(this);

        setBorder(new EmptyBorder(gameFrame.getBorderValue(), gameFrame.getBorderValue(), 0, gameFrame.getBorderValue()));

        topRotateBound = getBorder().getBorderInsets(this).top + super.maxComponentHeight + super.textUnderhang;

        GamePanel.difficulty = difficulty;
        difficultyNum = (difficulty.equals("Easy")) ? 1 : (difficulty.equals("Medium")) ? 2 : 3;

        shapeCenterDistance = 125 - 25 * difficultyNum;

        if(difficulty.equals("Hard")) numColors = (int) (Math.random() * 4) + 5;
        else numColors = (int) (Math.random() * 3) + 3;

        shapeIndex = 0;

        setPreferredSize(new Dimension(gameFrame.getFullScreenDimension().width / 2, gameFrame.getFullScreenDimension().height -
                gameFrame.getBottomButtonPanel().getPreferredSize().height - gameFrame.getTopButtonPanel().getPreferredSize().height));

        snapDistance = getPreferredSize().width / 8;

        shapeCenter = new int[]{getPreferredSize().width / 2, (getPreferredSize().height + gameFrame.getBorderValue() + super.maxComponentHeight) / 2};

        super.maxDistanceFromCenter = Math.min((getPreferredSize().height - shapeCenter[1]) * 9 / 10, super.maxDistanceFromCenter);

        shapeList = new ArrayList<>();
        used = new ArrayList<>();

        colorList = new ArrayList<>();
        colorList.add(Color.RED);
        colorList.add(Color.GREEN);
        colorList.add(Color.BLUE);
        colorList.add(Color.ORANGE);
        colorList.add(Color.YELLOW);
        colorList.add(Color.MAGENTA);
        colorList.add(Color.CYAN);
        colorList.add(Color.PINK);

        setShapeDifficulty();

        currentPolygon = shapeList.get(0);

        super.label.setText("Shape 1/" + shapeList.size());

        setVisible(true);
        
    }

    public void setShapeDifficulty() {

        if(difficulty.equals("Easy")) {

            boolean rand = (Math.random() < 0.5);

            generateShapes((rand) ? 3 : 2, 3);
            generateShapes(2, 4);
            generateShapes((rand) ? 1 : 2, 5);

        } else if(difficulty.equals("Medium")) {

            boolean rotatePair = (Math.random() < 0.5), secondPair = (Math.random() < 0.5);

            generateShapes((rotatePair) ? 3 : 2, 3);
            generateShapes((rotatePair) ? 2 : 3, 4);
            generateShapes((secondPair) ? 3 : 2, 5);
            generateShapes((secondPair) ? 2 : 3, 6);
            generateShapes(2, 7);

        } else {

            boolean lastPair = (Math.random() < 0.5);

            generateShapes(3, 3);
            generateShapes(3, 4);
            generateShapes(3, 5);
            generateShapes(3, 6);
            generateShapes(3, 7);
            generateShapes((lastPair) ? 3 : 2, 8);
            generateShapes((lastPair) ? 2 : 3, 9);

        }

    }

    @Override
    public void paint(Graphics g) {

        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        gameFrame.paint(g2d, currentPolygon);
        if(gameFrame.getDragPanel().getUsedCenters().contains(currentPolygon.getIndex())) {

            String message = "Used";
            g2d.setFont(new Font(gameFrame.getFontType(), Font.BOLD, 30));

            int stringWidth = g.getFontMetrics().stringWidth(message), stringHeight = g.getFontMetrics().getHeight(),
                    rectWidth = stringWidth * 5 / 4, rectHeight = stringHeight * 5 / 4;

            g2d.setPaint(gameFrame.getPanelBackground());
            g2d.fillRect(shapeCenter[0] - rectWidth / 2, shapeCenter[1] - rectHeight / 2, rectWidth, rectHeight);
            g2d.setPaint(gameFrame.getLabelForeground());
            g2d.drawString(message, shapeCenter[0] - stringWidth / 2, shapeCenter[1] + stringHeight / 3);

        } else gameFrame.paintCenterDot(g2d, shapeCenter[0], shapeCenter[1]);
        gameFrame.getDragPanel().repaint();

    }

    private void generateShapes(int amount, int sides) {

        for(int i = 0; i < amount; i++) {

            double[][] polarValues = new double[sides][2];
            double centralAngle = 2 * Math.PI / sides;

            for(int j = 0; j < sides; j++) {

                double angleDifference = (Math.random() - 0.5) * centralAngle / (difficultyNum + 2);
                double distanceFromCenter = (Math.random() * super.maxDistanceFromCenter * 2 / 3) + super.maxDistanceFromCenter / 3d;
                polarValues[j] = new double[]{j * centralAngle + angleDifference, distanceFromCenter};

            }

            shapeList.add(new ColoredPolarPolygon(polarValues, shapeCenter[0], shapeCenter[1], 0, colorList.get((int) (Math.random() * numColors)), shapeList.size()));
            used.add(false);

        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == super.left) shapeIndex = (shapeIndex == 0) ? shapeList.size() - 1 : shapeIndex - 1;
        else shapeIndex = (shapeIndex == shapeList.size() - 1) ? 0 : shapeIndex + 1;

        currentPolygon = shapeList.get(shapeIndex);

        super.label.setText("Shape " + (shapeIndex + 1) + "/" + shapeList.size());

        repaint();

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

        if(used.get(shapeIndex)) return;

        if(!difficulty.equals("Easy") && withinRotateBounds(e) && !inShape(e)) {

            rotateClick = true;
            isFirstRotation = true;

            excessRotation = 0;
            totalRotation = 0;
            rotateX = e.getX();
            rotateY = e.getY();

        } else {

            rotateClick = false;
            gameFrame.getDragPanel().mousePressed(e);

        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {

        if(rotateClick) {

            if(totalRotation != 0) gameFrame.addAction(new Action(currentPolygon.getIndex(), totalRotation));
            rotateClick = false;

        }

        gameFrame.getDragPanel().mouseReleased(e);

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

        gameFrame.getDragPanel().mouseDragged(e);

        if(rotateClick) {

            if(inShape(e) || !withinRotateBounds(e)) {

                rotateClick = false;
                if(totalRotation != 0) gameFrame.addAction(new Action(currentPolygon.getIndex(), totalRotation));
                return;

            }

            int rotation = 0;
            double rotatedAngle = calculateAngle(e) + excessRotation, angleDivisions = Math.PI / currentPolygon.getSides();
            boolean isNegativeRotation = rotatedAngle < 0;

            rotatedAngle = Math.abs(rotatedAngle);

            if(isFirstRotation && rotatedAngle >= angleDivisions / 2) {

                rotation++;
                rotatedAngle -= angleDivisions / 2;
                isFirstRotation = false;

            }

            rotation += (int) (rotatedAngle / angleDivisions);

            double modValue = rotatedAngle % angleDivisions;

            if(rotation > 0) {

                if(isNegativeRotation) {

                    rotation = -rotation;
                    excessRotation = -modValue;

                } else excessRotation = modValue;

                currentPolygon.rotate(rotation);
                totalRotation += rotation;

                rotateX = e.getX();
                rotateY = e.getY();

                repaint();

            }

        }

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    private boolean withinRotateBounds(MouseEvent e) {

        return e.getY() >= topRotateBound && e.getY() <= getPreferredSize().height && e.getX() >= 0 && e.getX() <= getPreferredSize().width;

    }

    public boolean inShape(MouseEvent e) {

        return currentPolygon.getPolygon().contains(e.getPoint());

    }

    private double calculateAngle(MouseEvent e) {

        double a = Point.distance(rotateX, rotateY, shapeCenter[0], shapeCenter[1]), b = Point.distance(e.getX(), e.getY(), shapeCenter[0], shapeCenter[1]),
                c = Point.distance(rotateX, rotateY, e.getX(), e.getY());
        double angle = Math.acos((a * a + b * b - c * c) / (2 * a * b));

        if((e.getX() >= shapeCenter[0] && rotateX >= shapeCenter[0] && e.getY() < rotateY)
                || (e.getX() <= shapeCenter[0] && rotateX <= shapeCenter[0] && e.getY() > rotateY)
                || (e.getY() >= shapeCenter[1] && rotateY >= shapeCenter[1] && e.getX() > rotateX)
                || (e.getY() <= shapeCenter[1] && rotateY <= shapeCenter[1] && e.getX() < rotateX)) angle *= -1;

        return angle;

    }

    public int getNumColors() {
        return numColors;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getShapeIndex() {
        return shapeIndex;
    }

    public int[] getShapeCenter() {
        return shapeCenter;
    }

    public ArrayList<ColoredPolarPolygon> getShapeList() {
        return shapeList;
    }

    public ArrayList<Color> getColorList() {
        return colorList;
    }

    public ColoredPolarPolygon getCurrentPolygon() {
        return currentPolygon;
    }

    public int getSnapDistance() {
        return snapDistance;
    }

    public ArrayList<Boolean> getUsed() {
        return used;
    }

}