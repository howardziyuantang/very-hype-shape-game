import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class BoardPanel extends GamePanel implements ActionListener {

    private final ArrayList<ColoredPolarPolygon> targetList;
    private final ArrayList<ColoredPolarPolygon> playerList;
    private final ArrayList<Integer[]> centers;
    private final int boardX, boardY, boardWidth, boardHeight, taskbarHeight, snapDistance, boardStroke;

    public BoardPanel(GameFrame gameFrame) {

        super(gameFrame);

        taskbarHeight = 25;

        boardStroke = 10;

        snapDistance = shapeCenterDistance / 3;

        boardWidth = gameFrame.getFullScreenDimension().width / 2 - 2 * gameFrame.getBorderValue();
        boardHeight = gameFrame.getFullScreenDimension().height - 2 * gameFrame.getBorderValue() -
                super.maxComponentHeight - super.textUnderhang - taskbarHeight - super.maxDistanceFromCenter / 2;
        
        targetList = new ArrayList<>();
        playerList = new ArrayList<>();

        centers = new ArrayList<>();

        setPreferredSize(new Dimension(gameFrame.getFullScreenDimension().width / 2, gameFrame.getFullScreenDimension().height));
        setBorder(new EmptyBorder(gameFrame.getBorderValue(), gameFrame.getBorderValue(), gameFrame.getBorderValue(), gameFrame.getBorderValue()));

        boardX = gameFrame.getBorderValue();
        boardY = gameFrame.getBorderValue() + super.maxComponentHeight + super.textUnderhang + super.maxDistanceFromCenter / 2;

        super.label.setText("Target Board");

        generateBoard();

        setVisible(true);

    }
    
    @Override
    public void paint(Graphics g) {

        super.label.setText((showTarget) ? "Target Board" : "Your Board");

        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;

        ArrayList<ColoredPolarPolygon> currentList = (showTarget) ? targetList : playerList;

        for(ColoredPolarPolygon polygon : currentList) gameFrame.paint(g2d, polygon);

        if(!showTarget)
            for(Integer[] center : centers) gameFrame.paintCenterDot(g2d, center[0], center[1]);

        g2d.setPaint(Color.WHITE);
        g2d.setStroke(new BasicStroke(boardStroke));
        g2d.drawRect(boardX, boardY, boardWidth, boardHeight);

        g2d.setPaint(gameFrame.getPanelBackground());
        g2d.fillRect(0, boardY - super.maxDistanceFromCenter / 2, getPreferredSize().width, super.maxDistanceFromCenter / 2);
        g2d.fillRect(0, boardY, boardX, boardHeight);
        g2d.fillRect(boardX + boardWidth, boardY, gameFrame.getBorderValue(), boardHeight);
        g2d.fillRect(0, boardY + boardHeight, getPreferredSize().width, gameFrame.getBorderValue() + taskbarHeight);

        gameFrame.getDragPanel().repaint();

    }
    
    private int generateNumExtra(String difficulty) {

        double random = Math.random();

        if(difficulty.equals("Easy")) {

            if(random < 0.5) return 0;
            else if(random < 0.85) return 1;
            else return 2;

        } else if(difficulty.equals("Medium")) {

            if(random < 0.2) return (random < 0.08) ? 0 : 1;
            else if(random < 0.65) return (random < 0.425) ? 2 : 3;
            else return (random < 0.85) ? 4 : 5;

        } else {

            if(random < 0.15) return (random < 0.03) ? 0 : (random < 0.08) ? 1 : 2;
            else if(random < 0.55) return (random < 0.25) ? 3 : (random < 0.4) ? 4 : 5;
            else return (random < 0.8) ? 6 : (random < 0.92) ? 7 : 8;

        }

    }

    private void generateBoard() {
        
        ArrayList<ColoredPolarPolygon> shapeList = gameFrame.getShapePanel().getShapeList();
        
        ArrayList<ArrayList<Integer[]>> boardArray = new ArrayList<>();
        for(int i = 0; i < 9; i++) boardArray.add(new ArrayList<>());

        int numExtra = generateNumExtra(difficulty);
        boolean listDone = false;

        for(int i = 0; i < shapeList.size() + numExtra; i++) {

            if(i == shapeList.size()) listDone = true;

            while(true) {

                int x = (int) (Math.random() * boardWidth), y = (int) (Math.random() * boardHeight);

                int row;
                if(y < boardHeight / 3) {
                    y = Math.max(y, super.maxDistanceFromCenter / 2);
                    y = Math.min(y, boardHeight / 3 - shapeCenterDistance / 2);
                    row = 0;
                } else if(y < boardHeight / 3 * 2) {
                    y = Math.max(y, boardHeight / 3 + shapeCenterDistance / 2);
                    y = Math.min(y, boardHeight / 3 * 2 - shapeCenterDistance / 2);
                    row = 1;
                } else {
                    y = Math.max(y, boardHeight / 3 * 2 + shapeCenterDistance / 2);
                    y = Math.min(y, boardHeight - super.maxDistanceFromCenter / 2);
                    row = 2;
                }

                int column;
                if(x < boardWidth / 3) {
                    x = Math.max(x, super.maxDistanceFromCenter / 2);
                    x = Math.min(x, boardWidth / 3 - shapeCenterDistance / 2);
                    column = 0;
                } else if(x < boardWidth / 3 * 2) {
                    x = Math.max(x, boardWidth / 3 + shapeCenterDistance / 2);
                    x = Math.min(x, boardWidth / 3 * 2 - shapeCenterDistance / 2);
                    column = 1;
                } else {
                    x = Math.max(x, boardWidth / 3 * 2 + shapeCenterDistance / 2);
                    x = Math.min(x, boardWidth - super.maxDistanceFromCenter / 2);
                    column = 2;
                }
                
                ArrayList<Integer[]> nearbyCenters = boardArray.get(row * 3 + column);

                if((!listDone && nearbyCenters.size() == shapeList.size() / 9 + 2) || 
                        (listDone && nearbyCenters.size() == shapeList.size() / 9 + 2 + difficultyNum)) continue;

                boolean enoughSpace = true;

                for(Integer[] nearbyCenter : nearbyCenters) {

                    if(Point.distance(x, y, nearbyCenter[0], nearbyCenter[1]) < shapeCenterDistance) {

                        enoughSpace = false;
                        break;

                    }

                }

                if(enoughSpace) {

                    if(!listDone) {

                        ColoredPolarPolygon copyPolarPolygon = shapeList.get(i).getCopy();
                        copyPolarPolygon.setCenter(x + boardX, y + boardY);

                        if((difficulty.equals("Medium") && Math.random() < 0.4) || (difficulty.equals("Hard") && Math.random() < 0.7))
                            copyPolarPolygon.rotate((int) (Math.random() * (copyPolarPolygon.getSides() * 2 - 1) + 1));

                        targetList.add(copyPolarPolygon);

                    }

                    centers.add(new Integer[]{x + boardX, y + boardY});
                    nearbyCenters.add(new Integer[]{x, y});

                    break;

                }

            }

        }

    }

    public boolean inBoardBounds(int x, int y) {

        int halfStroke = boardStroke / 2;
        return x > boardX + halfStroke && x < boardX + boardWidth - halfStroke && y > boardY + halfStroke && y < boardY + boardHeight - halfStroke;

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if((e.getSource() == super.left && !showTarget) || (e.getSource() == super.right && showTarget)) {

            showTarget = !showTarget;
            repaint();

        }

    }

    public ArrayList<ColoredPolarPolygon> getPlayerList() {
        return playerList;
    }

    public ArrayList<ColoredPolarPolygon> getTargetList() {
        return targetList;
    }

    public ArrayList<Integer[]> getCenters() {
        return centers;
    }

    public int getSnapDistance() {
        return snapDistance;
    }

}
