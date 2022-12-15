package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener {
    public PieceComponent first;
    public PieceComponent second;
    public PieceComponent[][] GameBoard;

    @Override
    public void actionPerformed(ActionEvent e) {
        PieceComponent target =  (PieceComponent) e.getSource();
        if(first == null) {
            if(handleFirst(target)){ // not empty
                first = target;
                checkValid(target);
            }
        } else {
            if (target == first) { // reveal
                if( !target.isRevealed ) {
                    target.Reveal();
                    first = null;
                }
            } else {
                if(handleSecond(target)) { // move
                    second = target;
                    first.Move2(second);
                    first = null;
                }
            }
        }
    }

    public boolean handleFirst(PieceComponent o) {
        if (o.rank == 0) return false; // empty
        return true;
    }

    public boolean handleSecond(PieceComponent o) {
        if(!first.isRevealed) return false;
        // return first.canMoveTo(o);
        return true;
    }

    public void checkValid(PieceComponent target) {
        int x = target.x; int y = target.y;
        if(target.rank == 7) {
            for (int i = x; i < 4; i++) { // right

            } for (int i = x; i >=1 ; i--) { // left

            } for (int i = y; i <= 8; i++) { // down

            } for (int i = y; i >= 1; i--) { // up

            }
        } else {
            if(x > 1)  target.validCord[x - 1][y] = target.canMoveTo(GameBoard[x - 2][y - 1]); // left
            if(x < 4)  target.validCord[x + 1][y] = target.canMoveTo(GameBoard[x][y - 1]); //right
            if(y > 1)  target.validCord[x][y - 1] = target.canMoveTo(GameBoard[x - 1][y - 2]); // up
            if(y < 8)  target.validCord[x][y + 1] = target.canMoveTo(GameBoard[x - 1][y]); // down
        }
    }

    public PieceComponent getFirst() {
        while(true) {
            if(first != null) return first;
        }
    }

    public PieceComponent getSecond() {
        while(true) {
            if(second != null) return second;
        }
    }
}
