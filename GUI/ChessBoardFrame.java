package GUI;

import javax.swing.*;

public class ChessBoardFrame extends JLabel {
    ImageIcon img = new ImageIcon("data/board.jpg");
    public ChessBoardFrame(int x, int y) {
        JLabel bg = new JLabel(img);
        bg.setOpaque(false);
        bg.setBounds(x,y,img.getIconWidth(), img.getIconHeight());
    }
}
// TO-DO: add change background image feature