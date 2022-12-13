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
        this.setIcon(new ImageIcon("resources/hide.png"));
        this.setBorder(null);
        this.setOpaque(false);
        this.setContentAreaFilled(false);
        this.setSize(75,75);
    }
    public boolean canMoveTo(PieceComponent target) {
        return false;
    }
    public void Caputre(PieceComponent target) {

    }

    public String getPath() {
        String color = this.player == 0 ? "B" : "R";
        return "resources/" + color + Integer.toString(rank) + ".png";
    }
}
