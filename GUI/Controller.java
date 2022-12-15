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
            if(handleFirst(target)){
                first = target;
                first.selected = true;
                checkValid(target);
            }
        } else {
            if (target == first) {
                if( target.isRevealed ) {
                    first.selected = false;
                    first = null;
                    second = null;
                } else {
                    target.Reveal();
                }
            } else {
                if(handleSecond(target)) {
                    second = target;
                    first.Move2(second);
                }
            }
        }
    }

    public boolean handleFirst(PieceComponent o) {
        if (o.rank == 0) return false;
        return true;
    }

    public boolean handleSecond(PieceComponent o) {
        return first.canMoveTo(o);
    }

    public void checkValid(PieceComponent target) {
        int x = target.x - 1; int y = target.y - 1;
        if(target.rank == 7) {

        } else {

        }
    }

    public PieceComponent getFirst() {
        while(first == null) {}
        return first;
    }

    public PieceComponent getSecond() {
        while(second == null) {}
        PieceComponent rec = second;
        first = null; second = null;
        return rec;
    }
}
