import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StartPanel extends JPanel {

    private final JButton startButton;
    private final JLabel gameName;
    private final Font labelFont, buttonFont;

    public StartPanel(GameFrame gameFrame) {

        setBorder(new EmptyBorder(50, 75, 40, 75));
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(20, 0, 35, 0);

        labelFont = new Font(gameFrame.getFontType(), Font.BOLD, 75);
        buttonFont = new Font(gameFrame.getFontType(), Font.BOLD, 50);

        gameName = new JLabel("Drag and Drop Shape Game");
        gameName.setForeground(gameFrame.getLabelForeground());
        gameName.setFont(labelFont);
        add(gameName, gbc);

        startButton = new JButton("START");
        startButton.setBackground(gameFrame.getButtonColor());
        startButton.setFont(buttonFont);
        startButton.addActionListener(gameFrame);
        add(startButton, gbc);

        setBackground(gameFrame.getPanelBackground());
        setVisible(true);

    }

    public JButton getStartButton() {
        return startButton;
    }

    public Font getLabelFont() {
        return labelFont;
    }

    public Font getButtonFont() {
        return buttonFont;
    }

}
