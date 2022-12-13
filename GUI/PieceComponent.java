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
        this.setContentAreaFilled(false);
        this.setBackground(Color.white);
        this.setBorder(null);
        this.setIcon(new ImageIcon("resources/hide.png"));
        this.setSize(100,98);
    }
    public boolean canMoveTo(PieceComponent target) {
        return false;
    }
    public boolean caputre(PieceComponent target) {
        return false;
    }

    public String getPath() {
        String color = player == 0 ? "B" : "R";
        return "data/" + color + Integer.toString(rank) + ".png";
    }
}
