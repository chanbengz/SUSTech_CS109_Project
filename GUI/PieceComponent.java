package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PieceComponent extends JButton {
    private boolean isRevealed = false;
    public PieceComponent() {
        setOpaque(true);
        setContentAreaFilled(false);
        setBackground(Color.white);
        setBorder(null);
    }
}
