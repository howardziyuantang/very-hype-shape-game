import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class DragPanel extends JPanel implements MouseInputListener {

    private final GameFrame gameFrame;
    private final ShapePanel shapePanel;
    private final BoardPanel boardPanel;
    private final int panelWidth;
    private boolean snapped, dragClick, isFirstDrag;
    private int firstX, firstY, pathX, pathY, lastX, lastY, centerIndex, listIndex;
    private ColoredPolarPolygon draggedPolygon;
    private final ArrayList<Integer> usedCenters;

    public DragPanel(GameFrame gameFrame, Dimension dimension) {

        this.gameFrame = gameFrame;
        shapePanel = gameFrame.getShapePanel();
        boardPanel = gameFrame.getBoardPanel();

        setPreferredSize(dimension);
        panelWidth = dimension.width / 2;

        usedCenters = new ArrayList<>();
        for(int i = 0; i < boardPanel.getCenters().size(); i++) usedCenters.add(-1);

        addMouseListener(this);
        addMouseMotionListener(this);

        setOpaque(false);

    }

    public ArrayList<Integer> getUsedCenters() {
        return usedCenters;
    }

    private boolean shapeSnap() {

        int snapDistance = shapePanel.getSnapDistance();
        int[] shapeCenter = shapePanel.getShapeCenter();

        double distanceToCenter = Point.distance(pathX, pathY, shapeCenter[0], shapeCenter[1]);

        if(!isFirstDrag && distanceToCenter <= snapDistance) {

            draggedPolygon.setCenter(shapeCenter[0], shapeCenter[1]);
            centerIndex = -1;
            return true;

        }

        if(isFirstDrag && distanceToCenter > snapDistance) isFirstDrag = false;

        return false;

    }

    private boolean boardSnap() {

        int snapDistance = boardPanel.getSnapDistance();

        if(boardPanel.inBoardBounds(pathX - panelWidth, pathY)) {

            ArrayList<Integer[]> centers = boardPanel.getCenters();

            for(int i = 0; i < centers.size(); i++) {

                Integer[] center = centers.get(i);
                if(!isFirstDrag && Point.distance(pathX, pathY, center[0] + panelWidth, center[1]) <= snapDistance &&
                        (usedCenters.get(i) == -1 || usedCenters.get(i) == draggedPolygon.getIndex())) {

                    draggedPolygon.setCenter(center[0] + panelWidth, center[1]);
                    centerIndex = i;
                    return true;

                }

            }

        }

        if(isFirstDrag && Point.distance(pathX, pathY, lastX, lastY) > snapDistance) isFirstDrag = false;

        return false;

    }

    private void repaintPanels() {

        shapePanel.repaint();
        boardPanel.repaint();

    }

    @Override
    public void paint(Graphics g) {

        super.paint(g);

        if(dragClick) {

            Graphics2D g2d = (Graphics2D) g;
            gameFrame.paint(g2d, draggedPolygon);
            gameFrame.paintCenterDot(g2d, draggedPolygon.getCenterX(), draggedPolygon.getCenterY());

        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

        if(GamePanel.showTarget) return;

        listIndex = -1;

        if(e.getX() > panelWidth && boardPanel.inBoardBounds(e.getX() - panelWidth, e.getY())) {

            ArrayList<ColoredPolarPolygon> playerList = boardPanel.getPlayerList();

            for(int i = playerList.size() - 1; i >= 0; i--) {

                if(playerList.get(i).getPolygon().contains(e.getX() - panelWidth, e.getY())) {

                    dragClick = true;
                    listIndex = i;

                    draggedPolygon = playerList.get(i);
                    playerList.remove(i);
                    centerIndex = usedCenters.indexOf(draggedPolygon.getIndex());
                    draggedPolygon.setCenter(draggedPolygon.getCenterX() + panelWidth, draggedPolygon.getCenterY());

                    pathX = draggedPolygon.getCenterX();
                    pathY = draggedPolygon.getCenterY();

                    break;

                }

            }

        } else if(e.getX() < panelWidth && shapePanel.inShape(e)) {

            dragClick = true;

            draggedPolygon = shapePanel.getCurrentPolygon().getCopy();

            pathX = draggedPolygon.getCenterX();
            pathY = draggedPolygon.getCenterY();

            centerIndex = -1;

        }

        if(dragClick) {

            snapped = true;
            isFirstDrag = true;

            firstX = e.getX();
            firstY = e.getY();

            lastX = pathX;
            lastY = pathY;

        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {

        if(dragClick) {

            if(!snapped) draggedPolygon.setCenter(lastX, lastY);
            else {

                int index = usedCenters.indexOf(draggedPolygon.getIndex());
                if(index > -1) usedCenters.set(index, -1);
                if(centerIndex > -1) usedCenters.set(centerIndex, draggedPolygon.getIndex());
                if(draggedPolygon.getCenterX() > panelWidth || !isFirstDrag)
                    gameFrame.addAction(new Action(draggedPolygon.getIndex(), index, centerIndex, listIndex));

            }

            if(draggedPolygon.getCenterX() > panelWidth) {

                draggedPolygon.setCenter(draggedPolygon.getCenterX() - panelWidth, draggedPolygon.getCenterY());
                if(snapped) boardPanel.getPlayerList().add(draggedPolygon);
                else boardPanel.getPlayerList().add(listIndex, draggedPolygon);

            }

            dragClick = false;
            snapped = true;

            repaintPanels();

        }

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

        if(dragClick) {

            pathX += e.getX() - firstX;
            pathY += e.getY() - firstY;

            if((pathX < panelWidth && shapeSnap()) || (pathX > panelWidth && boardSnap())) snapped = true;
            else {

                snapped = false;
                draggedPolygon.setCenter(pathX, pathY);

            }

            firstX = e.getX();
            firstY = e.getY();

            repaintPanels();

        }

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

}
