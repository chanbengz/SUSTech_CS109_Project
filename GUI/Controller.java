package GUI;


import ChessBoard.ChessException;
import ChessBoard.Operation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener {
    public PieceComponent first;
    public MainFrame mainFrame;

    @Override
    public void actionPerformed(ActionEvent e) {
        PieceComponent target =  (PieceComponent) e.getSource();
        if(first == null) {
            if(handleFirst(target)){ // not empty
                target.EmptyValid();
                first = target;
                checkValid(target);
            }
        } else {
            if (target == first) { // reveal
                if( !target.isRevealed ) {
                    int x = first.x, y = first.y;
                    first = null;
                    try {
                        mainFrame.Game.nextStep(new Operation(y,x,y,x),3);
                    } catch (ChessException ex) {
                        throw new RuntimeException(ex);
                    }
                    SwapPlayer();
                    if (!mainFrame.pvp){
                        try {
                            mainFrame.Game.nextStep(null,0);
                        } catch (ChessException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    SwapPlayer();
                } else {
                    first = null; // cancel
                }
            } else {
                if(handleSecond(target)) { // move
                    int x1 = first.x, y1 = first.y, x2 = target.x, y2 = target.y;
                    try {
                        mainFrame.Game.nextStep(new Operation(y1,x1,y2,x2),3);
                    } catch (ChessException ex) {
                        throw new RuntimeException(ex);
                    }
                    SwapPlayer();
                    if (!mainFrame.pvp){
                        try {
                            mainFrame.Game.nextStep(null,0);
                        } catch (ChessException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    SwapPlayer();
                    first = null;
                }
            }
        }
    }

    public boolean handleFirst(PieceComponent o) {
        return o.rank != 0 || o.player == mainFrame.Game.turn; // empty or other's piece
    }

    public boolean handleSecond(PieceComponent o) {
        int x = o.x, y = o.y;
        return first.validCord[x][y];
    }

    public void checkValid(PieceComponent target) {
        int x = target.x; int y = target.y;
        boolean right = false, left = false, up = false, down = false;
        if(target.rank == 7) {
            for (int i = x; i < 4; i++) { // right
                PieceComponent term = mainFrame.GameBoard[i-1][y-1];
                if(right && target.canMoveTo7(term)) {
                    target.validCord[i][y] = true;
                }
                if(term.rank != 0) {
                    right = true;
                }
            } for (int i = x; i >=1 ; i--) { // left
                PieceComponent term = mainFrame.GameBoard[i-1][y-1];
                if(left && target.canMoveTo7(term)) {
                    target.validCord[i][x] = true;
                }
                if(term.rank != 0) {
                    left = true;
                }
            } for (int i = y; i <= 8; i++) { // down
                PieceComponent term = mainFrame.GameBoard[x-1][i-1];
                if(down && target.canMoveTo7(term)) {
                    target.validCord[x][i] = true;
                }
                if(term.rank != 0) {
                    down = true;
                }
            } for (int i = y; i >= 1; i--) { // up
                PieceComponent term = mainFrame.GameBoard[x-1][i-1];
                if(up && target.canMoveTo7(term)) {
                    target.validCord[x][i] = true;
                }
                if(term.rank != 0) {
                    up = true;
                }
            }
        }

        if(x > 1)  target.validCord[x - 1][y] = target.canMoveTo(mainFrame.GameBoard[x - 2][y - 1]); // left
        if(x < 4)  target.validCord[x + 1][y] = target.canMoveTo(mainFrame.GameBoard[x][y - 1]); //right
        if(y > 1)  target.validCord[x][y - 1] = target.canMoveTo(mainFrame.GameBoard[x - 1][y - 2]); // up
        if(y < 8)  target.validCord[x][y + 1] = target.canMoveTo(mainFrame.GameBoard[x - 1][y]); // down
    }

    private void SwapPlayer() {
        mainFrame.printTurnAndRound();
    }
}
