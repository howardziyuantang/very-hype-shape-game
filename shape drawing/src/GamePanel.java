import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements ActionListener {

    protected final GameFrame gameFrame;
    protected final int triangleButtonSpace, maxComponentHeight, textUnderhang;
    protected int maxDistanceFromCenter;
    protected final Dimension triangleButtonDimension;
    protected final TriangleButton left, right;
    protected final JLabel label;
    protected final Font labelFont;

    protected static boolean showTarget;
    protected static int shapeCenterDistance, difficultyNum;
    protected static String difficulty;

    public GamePanel(GameFrame gameFrame) {

        this.gameFrame = gameFrame;

        textUnderhang = 15;

        triangleButtonDimension = new Dimension();
        triangleButtonDimension.height = gameFrame.getBorderValue() * 5 / 4;
        triangleButtonDimension.width = triangleButtonDimension.height / 2;

        triangleButtonSpace = triangleButtonDimension.width;

        left = new TriangleButton('L', triangleButtonDimension, gameFrame.getButtonColor());
        left.addActionListener(this);

        right = new TriangleButton('R', triangleButtonDimension, gameFrame.getButtonColor());
        right.addActionListener(this);

        labelFont = new Font(gameFrame.getFontType(), Font.BOLD, triangleButtonDimension.height);

        label = new JLabel("DEFAULT");
        label.setFont(labelFont);
        label.setForeground(gameFrame.getLabelForeground());

        maxComponentHeight = Math.max(label.getHeight(), triangleButtonDimension.height);

        maxDistanceFromCenter = Math.min(gameFrame.getFullScreenDimension().width / 2 - 2 * gameFrame.getBorderValue(),
                gameFrame.getFullScreenDimension().height - 2 * gameFrame.getBorderValue() - maxComponentHeight) / 4;

        add(left);
        add(Box.createHorizontalStrut(triangleButtonSpace));
        add(label);
        add(Box.createHorizontalStrut(triangleButtonSpace));
        add(right);

        setBackground(gameFrame.getPanelBackground());

        showTarget = true;

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

}
