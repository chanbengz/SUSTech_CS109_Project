package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener {
    public PieceComponent first;
    public PieceComponent second;

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public PieceComponent getFirst() {
        while(first == null) {}
        return first;
    }

    public PieceComponent getSecond() {
        while(second == null) {}
        return second;
    }
}
