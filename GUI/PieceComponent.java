package GUI;

import javax.swing.*;
// rank 0 is empty
// player -1 is empty

public class PieceComponent extends JButton {
    public boolean isRevealed;
    public boolean selected;
    public int rank;
    public int player;
    public int x, y;
    public boolean [][]validCord = new boolean[5][9];

    public PieceComponent(int player, int rank) {
        this.rank = rank;
        this.player = player;
        selected = false;
        isRevealed = false;
        update();
        this.setSize(75,75);
    }

    public boolean canMoveTo(PieceComponent target) { // rank is not 7
        if(!target.isRevealed || target.player == this.player) {
            return false;
        } else if (this.rank == 6 && target.rank == 1) {
            return true;
        } else if (target.rank > this.rank) {
            return false;
        } else {
            return true;
        }
    }
    public boolean canMoveTo7(PieceComponent target) { // rank is 7
        if (!target.isRevealed) return true;
        return target.player != this.player;
    }

    public void Move2(PieceComponent target) {
        if( this.rank != 7) {
            this.transfer2(target);
            this.Empty();
            target.update();
        } else {
            target.Empty();
        }
    }

    public void Reveal() {
        this.isRevealed = true;
        update();
    }

    public void transfer2(PieceComponent target) {
        target.player = this.player;
        target.rank = this.rank;
        target.isRevealed = this.isRevealed;
        EmptyValid();
    }

    public void EmptyValid() {
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 9; j++) {
                validCord[i][j] = false;
            }
        }
    }

    public void Empty() {
        isRevealed = true;
        selected = false;
        rank = 0;
        player = -1;
        EmptyValid();
        this.setIcon(null);
        this.setBorder(null);
        this.setOpaque(false);
        this.setContentAreaFilled(false);
        this.repaint();
    }

    public void update() {
        if(isRevealed) {
            this.setIcon(new ImageIcon(this.getPath()));
        } else if(rank == 0) {
            this.setIcon(null);
        } else {
            this.setIcon(new ImageIcon("resources/hide.png"));
        }
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
