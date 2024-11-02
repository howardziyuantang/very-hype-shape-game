import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class TopButtonPanel extends ButtonPanel {

    private final JButton undo, redo;

    public TopButtonPanel(GameFrame gameFrame) {

        super(gameFrame);

        undo = new JButton("Undo");
        undo.setForeground(gameFrame.getButtonForeground());
        undo.setFont(super.buttonFont);
        undo.setBackground(gameFrame.getButtonColor());
        undo.addActionListener(gameFrame);

        redo = new JButton("Redo");
        redo.setForeground(gameFrame.getButtonForeground());
        redo.setFont(super.buttonFont);
        redo.setBackground(gameFrame.getButtonColor());
        redo.addActionListener(gameFrame);

        setBorder(new EmptyBorder(super.border, super.border, super.border / 2, super.border));

        add(undo);
        add(Box.createHorizontalStrut(gameFrame.getBottomButtonPanel().getButtonSpace()));
        add(redo);

        setVisible(true);

    }

    public JButton getRedo() {
        return redo;
    }

    public JButton getUndo() {
        return undo;
    }

}
