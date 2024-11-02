import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class GameFrame extends JFrame implements ActionListener {

    private final int border;
    private final Dimension fullScreenDimension;
    private final Color buttonColor, buttonForeground, labelForeground, panelBackground;
    private StartPanel startPanel;
    private LevelPanel levelPanel;
    private DragPanel dragPanel;
    private ShapePanel shapePanel;
    private BoardPanel boardPanel;
    private TopButtonPanel topButtonPanel;
    private BottomButtonPanel bottomButtonPanel;
    private final LinkedList<Action> actionList;
    private final String fontType;
    private final Color centerDotColor;
    private final int centerDotSize;
    private int actionIndex, listIndex;

    public GameFrame() {

        buttonColor = Color.WHITE;
        buttonForeground = Color.BLACK;
        labelForeground = Color.WHITE;
        panelBackground = Color.BLACK;

        border = 40;

        fullScreenDimension = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize();

        centerDotSize = 10;
        centerDotColor = Color.WHITE;

        actionList = new LinkedList<>();
        actionIndex = 0;
        listIndex = -1;

        fontType = "Times New Roman";

        setTitle("Drag and Drop Shape Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

    }

    public void start() {

        startPanel = new StartPanel(this);
        addPanel(startPanel);

    }

    public void addPanel(JPanel panel) {

        add(panel);
        pack();
        setLocationRelativeTo(null);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        JButton source = (JButton) e.getSource();

        if(source == startPanel.getStartButton()) {

            remove(startPanel);

            levelPanel = new LevelPanel(this);
            addPanel(levelPanel);

        } else if(source == levelPanel.getEasyButton() || source == levelPanel.getMediumButton() || source == levelPanel.getHardButton()) {

            remove(levelPanel);

            bottomButtonPanel = new BottomButtonPanel(this);
            topButtonPanel = new TopButtonPanel(this);
            shapePanel = new ShapePanel(this, source.getText());
            boardPanel = new BoardPanel(this);
            dragPanel = new DragPanel(this,
                    new Dimension(shapePanel.getPreferredSize().width + boardPanel.getPreferredSize().width, boardPanel.getPreferredSize().height));

            JPanel leftPanel = new JPanel();
            leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));

            leftPanel.add(shapePanel);
            leftPanel.add(topButtonPanel);
            leftPanel.add(bottomButtonPanel);

            dragPanel.setLayout(new GridLayout(1, 2));
            dragPanel.add(leftPanel);
            dragPanel.add(boardPanel);

            addPanel(dragPanel);
            setExtendedState(MAXIMIZED_BOTH);

        } else {

            if(source == topButtonPanel.getUndo() || source == topButtonPanel.getRedo()) {

                boolean undo = source == topButtonPanel.getUndo();
                if(undo) {

                    if(actionIndex == 0) return;
                    actionIndex--;

                } else if(actionIndex == actionList.size()) return;

                Action action = actionList.get(actionIndex);
                if(action.getActionType() == 'R') {

                    shapePanel.getShapeList().get(action.getShapeIndex()).rotate(action.getRotation() * ((undo) ? -1 : 1));

                } else {

                    if(action.getOriginalCenterIndex() == -1 || action.getNewCenterIndex() == -1) {

                        if((action.getOriginalCenterIndex() == -1 && undo) || (action.getNewCenterIndex() == -1 && !undo)) {

                            boardPanel.getPlayerList().remove(listIndex);
                            dragPanel.getUsedCenters().set(((undo) ? action.getNewCenterIndex() : action.getOriginalCenterIndex()), -1);
                            listIndex--;

                        } else {

                            int centerIndex = (undo) ? action.getOriginalCenterIndex() : action.getNewCenterIndex();
                            ColoredPolarPolygon copy = shapePanel.getShapeList().get(action.getShapeIndex()).getCopy();
                            Integer[] center = boardPanel.getCenters().get(centerIndex);
                            copy.setCenter(center[0], center[1]);
                            boardPanel.getPlayerList().add(copy);
                            dragPanel.getUsedCenters().set(centerIndex, action.getShapeIndex());
                            listIndex++;

                        }

                    } else {

                        ColoredPolarPolygon shape = boardPanel.getPlayerList().remove((undo) ? listIndex : action.getListIndex());
                        Integer[] center = boardPanel.getCenters().get((undo) ? action.getOriginalCenterIndex() : action.getNewCenterIndex());
                        shape.setCenter(center[0], center[1]);
                        boardPanel.getPlayerList().add((undo) ? action.getListIndex() : listIndex, shape);
                        dragPanel.getUsedCenters().set((undo) ? action.getNewCenterIndex() : action.getOriginalCenterIndex(), -1);
                        dragPanel.getUsedCenters().set((undo) ? action.getOriginalCenterIndex() : action.getNewCenterIndex(), action.getShapeIndex());

                    }

                }

                if(!undo) actionIndex++;

            }

        }

    }


    public int getBorderValue() {
        return border;
    }

    public Dimension getFullScreenDimension() {
        return fullScreenDimension;
    }

    public Color getButtonColor() {
        return buttonColor;
    }

    public Color getButtonForeground() {
        return buttonForeground;
    }

    public Color getLabelForeground() {
        return labelForeground;
    }

    public Color getPanelBackground() {
        return panelBackground;
    }

    public String getFontType() {
        return fontType;
    }

    public StartPanel getStartPanel() {
        return startPanel;
    }

    public LevelPanel getLevelPanel() {
        return levelPanel;
    }

    public ShapePanel getShapePanel() {
        return shapePanel;
    }

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }

    public TopButtonPanel getTopButtonPanel() {
        return topButtonPanel;
    }

    public BottomButtonPanel getBottomButtonPanel() {
        return bottomButtonPanel;
    }

    public DragPanel getDragPanel() {
        return dragPanel;
    }

    public LinkedList<Action> getActionList() {
        return actionList;
    }

    public int getActionIndex() {
        return actionIndex;
    }

    public void paint(Graphics2D g2d, ColoredPolarPolygon polygon) {

        g2d.setPaint(polygon.getColor());
        g2d.fill(polygon.getPolygon());

    }

    public void paintCenterDot(Graphics2D g2d, int x, int y) {

        g2d.setPaint(centerDotColor);
        g2d.fillOval(x - centerDotSize / 2, y - centerDotSize / 2, centerDotSize, centerDotSize);

    }

    public void addAction(Action action) {

        for(int i = actionIndex; i < actionList.size(); i++) actionList.remove();
        actionList.add(actionIndex, action);
        actionIndex++;

        if(actionList.size() > 100) {

            if(actionIndex > 50) {

                actionList.remove();
                actionIndex--;

            } else actionList.remove(100);

        }

        if(action.getActionType() == 'T') {

            if(action.getNewCenterIndex() == -1) listIndex--;
            else if(action.getOriginalCenterIndex() == -1) listIndex++;

        }

    }

}
