import javax.swing.*;
import java.awt.*;

public class ButtonPanel extends JPanel {

    protected int border;
    protected final Font buttonFont;

    public ButtonPanel(GameFrame gameFrame) {

        buttonFont = new Font(gameFrame.getFontType(), Font.PLAIN, gameFrame.getBorderValue());

        setBackground(gameFrame.getPanelBackground());

    }

}
