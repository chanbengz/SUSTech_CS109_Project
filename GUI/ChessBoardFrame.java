package GUI;

import javax.swing.*;

public class ChessBoardFrame extends JLabel {
    ImageIcon img = new ImageIcon("resources/board.jpg");
    public int width, height;
    public ChessBoardFrame(int x, int y) {
        setIcon(img);
        setOpaque(false);
        width = img.getIconWidth();
        height = img.getIconHeight();
        setBounds(x, y, width, height);
    }
}
// TO-DO: add change background image feature