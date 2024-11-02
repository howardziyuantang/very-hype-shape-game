import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LevelPanel extends JPanel {

    private final JLabel title;
    private final JButton easy, medium, hard;
    private final Font labelFont, buttonFont;

    public LevelPanel(GameFrame gameFrame) {

        setBorder(new EmptyBorder(40, 50, 0, 50));
        setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 40, 0);
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        buttonFont = gameFrame.getStartPanel().getButtonFont();
        labelFont = buttonFont;

        title = new JLabel("Choose Difficulty:");
        title.setForeground(gameFrame.getLabelForeground());
        title.setFont(labelFont);
        add(title, gbc);

        easy = new JButton("Easy");
        easy.setBackground(gameFrame.getButtonColor());
        easy.setFont(buttonFont);
        easy.addActionListener(gameFrame);
        add(easy, gbc);

        medium = new JButton("Medium");
        medium.setBackground(gameFrame.getButtonColor());
        medium.setFont(buttonFont);
        medium.addActionListener(gameFrame);
        add(medium, gbc);

        hard = new JButton("Hard");
        hard.setBackground(gameFrame.getButtonColor());
        hard.setFont(buttonFont);
        hard.addActionListener(gameFrame);
        add(hard, gbc);

        setBackground(gameFrame.getPanelBackground());
        setVisible(true);

    }

    public JButton getEasyButton() {
        return easy;
    }

    public JButton getMediumButton() {
        return medium;
    }

    public JButton getHardButton() {
        return hard;
    }

}
