import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class BottomButtonPanel extends ButtonPanel {

    private final JButton submit, restart, quit;
    private final int buttonSpace;

    public BottomButtonPanel(GameFrame gameFrame) {

        super(gameFrame);

        submit = new JButton("Submit");
        submit.setForeground(gameFrame.getButtonForeground());
        submit.setFont(super.buttonFont);
        submit.setBackground(gameFrame.getButtonColor());
        submit.addActionListener(gameFrame);

        restart = new JButton("Restart");
        restart.setForeground(gameFrame.getButtonForeground());
        restart.setFont(super.buttonFont);
        restart.setBackground(gameFrame.getButtonColor());
        restart.addActionListener(gameFrame);

        quit = new JButton("Quit");
        quit.setForeground(gameFrame.getButtonForeground());
        quit.setFont(super.buttonFont);
        quit.setBackground(gameFrame.getButtonColor());
        quit.addActionListener(gameFrame);

        super.border = quit.getPreferredSize().height;
        setBorder(new EmptyBorder(super.border / 2, super.border, gameFrame.getBorderValue(), super.border));

        buttonSpace = quit.getPreferredSize().height / 2;

        add(submit);
        add(Box.createHorizontalStrut(buttonSpace));
        add(restart);
        add(Box.createHorizontalStrut(buttonSpace));
        add(quit);

        setVisible(true);

    }

    public int getButtonSpace() {
        return buttonSpace;
    }

}
