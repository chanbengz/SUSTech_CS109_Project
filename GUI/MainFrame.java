package GUI;

import ChessBoard.ChessBoard;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private boolean started;
    public ChessBoard Game;
    final private int Distance_X = 4;
    final private int Distance_Y = 7;
    public int boardY = 10;
    public int boardX = 120;

    public PieceComponent[][] GameBoard;
    private JLabel ChessboardBackg;
    private JButton Startbutton;
    private JButton StopButton;
    private JButton LoadButton;
    private JButton WdButton;
    private JLabel pro1;
    private JLabel pro2;
    private JTextPane PlayerName1;
    private JTextPane PlayerName2;
    private JScrollPane scrollPane1;
    private JTextPane MessagePane;
    private JScrollPane scrollPane2;
    private JTextPane RankPane;
    private JButton ConnectButton;
    private JLabel TurnLabel;
    private JLabel RoundLabel;

    public MainFrame(String title) {
        super(title);
        this.setLocation(200,200);
        this.setSize(785,700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
        this.setLayout(null);
        this.ChessboardBackg = new JLabel();
        this.started = false;

        AddChess();
        AddButton();
        AddLabel();
        AddInfoArea();
        AddPlayerInfo();
        //---- ChessboardBackg ----
        ChessboardBackg.setIcon(new ImageIcon("resources/board.jpg"));
        this.add(ChessboardBackg);
        ChessboardBackg.setBounds(115, 0, 305, 610);
    }

    private void AddChess() {
        this.GameBoard = new PieceComponent[4][8];
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 4; x++) {
                GameBoard[x][y] = new PieceComponent(0,0);
                GameBoard[x][y].setLocation(boardX + 73 * x, boardY + 74 * y);
                GameBoard[x][y].setVisible(false);
                this.add(GameBoard[x][y]);
            }
        }
    }

    private void generate() {
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 4; x++) {
                GameBoard[x][y].setVisible(true);

            }
        }
    }

    private void AddButton() {
        //---- Init ----
        this.Startbutton = new JButton();
        this.StopButton = new JButton();
        this.LoadButton = new JButton();
        this.WdButton = new JButton();
        this.ConnectButton = new JButton();

        //---- Startbutton ----
        Startbutton.setText("Start");
        this.add(Startbutton);
        Startbutton.setBounds(10, 615, 100, 45);
        Startbutton.addActionListener((e)->{
            if (this.started) {
                JOptionPane.showMessageDialog(this,"You've started a game!");
            } else {
                this.started = true;
                Game = new ChessBoard();
                generate();
            }
        });

        //---- StopButton ----
        StopButton.setText("Stop");
        this.add(StopButton);
        StopButton.setBounds(120, 615, 100, 45);

        //---- LoadButton ----
        LoadButton.setText("Load");
        this.add(LoadButton);
        LoadButton.setBounds(230, 615, 100, 45);

        //---- WdButton ----
        WdButton.setText("Withdraw");
        this.add(WdButton);
        WdButton.setBounds(340, 615, 100, 45);

        //---- ConnectButton ----
        ConnectButton.setText("Connect");
        this.add(ConnectButton);
        ConnectButton.setBounds(450, 615, 100, 45);
    }

    private void AddLabel() {
        //---- Init ----
        this.TurnLabel = new JLabel();
        this.RoundLabel = new JLabel();

        //---- TurnLabel ----
        TurnLabel.setText("BLACK's TURN");
        TurnLabel.setFont(new Font("Rockwell", Font.PLAIN, 14));
        this.add(TurnLabel);
        TurnLabel.setBounds(430, 60, 115, 40);

        //---- RoundLabel ----
        RoundLabel.setText("ROUND 1");
        RoundLabel.setFont(new Font("Rockwell", Font.PLAIN, 22));
        this.add(RoundLabel);
        RoundLabel.setBounds(430, 25, 105, 40);
    }

    private void AddInfoArea() {
        //---- Init ----
        this.scrollPane1 = new JScrollPane();
        this.MessagePane = new JTextPane();
        this.scrollPane2 = new JScrollPane();
        this.RankPane = new JTextPane();

        //======== scrollPane1 ========
        //---- MessagePane ----
        MessagePane.setEditable(false);
        scrollPane1.setViewportView(MessagePane);
        this.add(scrollPane1);
        scrollPane1.setBounds(560, 5, 215, 425);

        //======== scrollPane2 ========
        //---- RankPane ----
        RankPane.setEditable(false);
        scrollPane2.setViewportView(RankPane);
        this.add(scrollPane2);
        scrollPane2.setBounds(560, 435, 215, 230);

    }

    private void AddPlayerInfo() {
        //---- Init ----
        this.pro1 = new JLabel();
        this.pro2 = new JLabel();
        this.PlayerName1 = new JTextPane();
        this.PlayerName2 = new JTextPane();

        //---- pro1 ----
        pro1.setIcon(new ImageIcon("resources/profile1.png"));
        this.add(pro1);
        pro1.setBounds(40, 50, 35, 40);

        //---- pro2 ----
        pro2.setIcon(new ImageIcon("resources/profile2.png"));
        this.add(pro2);
        pro2.setBounds(460, 520, 35, 40);

        //---- PlayerName1 ----
        PlayerName1.setText("Player 1");
        PlayerName1.setFont(PlayerName1.getFont().deriveFont(PlayerName1.getFont().getSize() + 6f));
        PlayerName1.setEditable(false);
        PlayerName1.setBorder(null);
        PlayerName1.setOpaque(false);
        this.add(PlayerName1);
        PlayerName1.setBounds(25, 20, 75, PlayerName1.getPreferredSize().height);

        //---- PlayerName2 ----
        PlayerName2.setText("Player 2");
        PlayerName2.setFont(PlayerName2.getFont().deriveFont(PlayerName2.getFont().getSize() + 6f));
        PlayerName2.setEditable(false);
        PlayerName2.setAutoscrolls(false);
        PlayerName2.setDragEnabled(false);
        PlayerName2.setBorder(null);
        PlayerName2.setOpaque(false);
        this.add(PlayerName2);
        PlayerName2.setBounds(445, 565, 75, 23);
    }
}
