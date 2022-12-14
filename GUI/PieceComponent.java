package GUI;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class PieceComponent extends JButton {
    public boolean isRevealed;
    public boolean selected;
    public int rank;
    public int player;
    public int x, y;

    public PieceComponent(int player, int rank) {
        this.rank = rank;
        this.player = player;
        selected = false;
        isRevealed = false;
        this.setIcon(new ImageIcon("resources/hide.png"));
        this.setBorder(null);
        this.setOpaque(false);
        this.setContentAreaFilled(false);
        this.setSize(75,75);
    }

    public boolean canMoveTo(PieceComponent target) {
        if(target.isRevealed||target.player == this.player||target.rank > this.rank) {
            return false;
        } else {
            return true;
        }
    }

    public void Move(PieceComponent target) {

    }

    public void Reveal() {
        this.isRevealed = true;
        this.setIcon(new ImageIcon(this.getPath()));
        this.setBorder(null);
        this.setOpaque(false);
        this.setContentAreaFilled(false);
        this.repaint();
    }

    public String getPath() {
        String color = this.player == 0 ? "B" : "R";
        return "resources/" + color + Integer.toString(rank) + ".png";
    }
}
