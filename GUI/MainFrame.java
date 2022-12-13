package GUI;

import ChessBoard.ChessBoard;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private boolean started;
    public ChessBoard Game;
    public PieceComponent[][] GameBoard = new PieceComponent[5][9];
    final private int Distance_X = 4;
    final private int Distance_Y = 7;
    public int boardY = 50;
    public int boardX = 200;
    public JLabel TurnLabel;
    public JLabel RoundLabel;
    public ChessBoardFrame Board;
    public JLabel PlayerName1;
    public JLabel PlayerName2;
    public MainFrame(String title){
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setSize(1200,1000);
        this.setResizable(false);
        this.setLayout(null);

        this.started = false;
        this.Board = new ChessBoardFrame(boardX, boardY);
        this.add(Board);
        // AddButton();
        // AddLabel();
        // AddInfoArea();
        // AddPlayerInfo();
        // generate();
    }
    private void generate() {
        PieceComponent test = new PieceComponent(0,0);
        test.setLocation(boardX+10,boardY+10);
        test.addActionListener((e)->{
            JOptionPane.showMessageDialog(this, "success","null",1);
        });
        this.add(test);
    }

    private void AddButton() {
        JButton start = new JButton("Start");
        start.setBounds(15,0,100,48);
        start.setOpaque(true);
        start.addActionListener((e) -> {

        });
        this.add(start);

        JButton connect = new JButton("Connect");
        connect.setBounds(95+15, 0,100,48);
        connect.setOpaque(true);
        this.add(connect);

        JButton withdraw = new JButton("Withdraw");
        withdraw.setBounds(boardX,920,100,48);
        this.add(withdraw);
    }

    private void AddLabel() {
        int x = boardX + Board.width + 50;
        TurnLabel = new JLabel("Black's Turn");
        TurnLabel.setBounds(x,100,200,40);
        TurnLabel.setFont(new Font("Rockwell", Font.BOLD, 24));
        TurnLabel.setOpaque(true);
        this.add(TurnLabel);

        RoundLabel = new JLabel("Round 1");
        RoundLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
        RoundLabel.setBounds(x, 100+40,150,40);
        TurnLabel.setOpaque(true);
        this.add(RoundLabel);
    }

    private void AddInfoArea() {
        int x = boardX * 2 + Board.width;
        int y = boardY;

    }

    private void AddPlayerInfo() {
        JButton PlayerProfile1 = new JButton(new ImageIcon("resources/profile1.png"));
        PlayerProfile1.setBounds(56,boardY+70,96,96);
        PlayerProfile1.setContentAreaFilled(false);
        PlayerProfile1.setBorder(null);
        this.add(PlayerProfile1);

        JButton PlayerProfile2 = new JButton(new ImageIcon("resources/profile2.png"));
        PlayerProfile2.setBounds(boardX + Board.width+50,920-200,96,96);
        PlayerProfile2.setContentAreaFilled(false);
        PlayerProfile2.setBorder(null);
        this.add(PlayerProfile2);

        PlayerName1 = new JLabel("Player 1");
        PlayerName1.setBounds(56,boardY+10,148,50);
        PlayerName1.setFont(new Font("Rockwell", Font.PLAIN, 10));
        this.add(PlayerName1);

        PlayerName2 = new JLabel("Player 2");
        PlayerName2.setBounds(boardX + Board.width +50, 920-190, 148,50);
        PlayerName2.setFont(new Font("Rockwell", Font.PLAIN, 10));
        this.add(PlayerName2);
    }
}
