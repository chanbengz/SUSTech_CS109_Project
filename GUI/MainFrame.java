package GUI;

import javax.swing.*;

public class MainFrame extends JFrame {
    private int BoardX = 0;
    private int BoardY = 10;
    private int Distance_X = 4;
    private int Distance_Y = 7;
    public MainFrame(String title){
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setSize(1200,1200);
        this.setResizable(false);
        this.setLayout(null);

        JLabel Board = new ChessBoardFrame(BoardX, BoardY);
        this.add(Board);
    }
}
