package GUI;

import javax.swing.*;
import java.awt.*;

public class PieceComponent extends JButton {
    public boolean isRevealed = false;
    public int rank;
    public int player;
    public PieceComponent(int player, int rank) {
        this.rank = rank;
        this.player = player;
        setOpaque(true);
        setContentAreaFilled(false);
        setBackground(Color.white);
        setBorder(null);
    }

    public String getPath() {
        String color = player == 0 ? "B" : "R";
        return "data/" + color + Integer.toString(rank) + ".png";
    }
}
