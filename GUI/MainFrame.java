package GUI;

import ChessBoard.ChessBoard;
import ChessBoard.Player;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private boolean started;
    public ChessBoard Game;
    final private int boardY = 10;
    final private int boardX = 120;

    public PieceComponent[][] GameBoard;
    private JLabel ChessboardBackg;
    private JButton StartButton;
    private JButton StopButton;
    private JButton LoadButton;
    private JButton WdButton;
    private JLabel pro1;
    private JLabel pro2;
    private JLabel PlayerName1;
    private JLabel PlayerName2;
    private JScrollPane scrollPane1;
    private JTextPane MessagePane;
    private JScrollPane scrollPane2;
    private JTextPane RankPane;
    private JButton ConnectButton;
    private JLabel TurnLabel;
    private JLabel RoundLabel;
    private String Message = "";
    public Controller controller;

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
        this.controller = new Controller();
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 4; x++) {
                GameBoard[x][y] = new PieceComponent(0,0);
                GameBoard[x][y].setLocation(boardX + 73 * x, boardY + 74 * y);
                GameBoard[x][y].setVisible(false);
                GameBoard[x][y].addActionListener(this.controller);
                this.add(GameBoard[x][y]);
            }
        }
    }

    private void generate() {
        this.controller.mainFrame = this;
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 4; x++) {
                GameBoard[x][y].setVisible(true);
                int who = this.Game.map[y + 1][x + 1].player;
                int value = (int)Math.pow(-1,who) * this.Game.players[who].pieces.chess[this.Game.map[y+1][x+1].index].level;
                GameBoard[x][y].rank = value > 0 ? value : -value;
                GameBoard[x][y].player = value > 0 ? 0 : 1;
                GameBoard[x][y].x = x + 1; GameBoard[x][y].y = y + 1;
                GameBoard[x][y].update();
            }
        }
    }

    private void AddButton() {
        //---- Init ----
        this.StartButton = new JButton();
        this.StopButton = new JButton();
        this.LoadButton = new JButton();
        this.WdButton = new JButton();
        this.ConnectButton = new JButton();

        //---- Startbutton ----
        StartButton.setText("Start");
        this.add(StartButton);
        StartButton.setBounds(10, 615, 100, 45);
        StartButton.addActionListener((e)->{
            if (this.started) {
                JOptionPane.showMessageDialog(this,"You've started a game!");
            } else {

                this.started = true;
                Game = new ChessBoard();
                Player Tim = new Player("Tim",3);
                Player AI = new Player("AI",1);
                Game.Init(Tim, AI);
                Game.mainFrame = this;
                Game.InitialMap();
                Game.Show();
                printTurnAndRound();
                generate();
            }
        });

        //---- StopButton ----
        StopButton.setText("Stop");
        this.add(StopButton);
        StopButton.setBounds(120, 615, 100, 45);
        StopButton.addActionListener((e)->{
            if( started ) {
                Game = null;
                this.started = false;
                RoundLabel.setText("");
                TurnLabel.setText("");
                for(int y = 0; y < 8; y++) {
                    for(int x = 0; x < 4; x++) {
                        GameBoard[x][y].setVisible(false);
                        GameBoard[x][y].rank = -1;
                        GameBoard[x][y].player = -1;
                        GameBoard[x][y].isRevealed = false;
                        GameBoard[x][y].repaint();
                    }
                }
            }
        });

        //---- LoadButton ----
        LoadButton.setText("Load");
        this.add(LoadButton);
        LoadButton.setBounds(230, 615, 100, 45);
        LoadButton.addActionListener((e)->{

        });

        //---- WdButton ----
        WdButton.setText("Withdraw");
        this.add(WdButton);
        WdButton.setBounds(340, 615, 100, 45);
        LoadButton.addActionListener((e)->{

        });

        //---- ConnectButton ----
        ConnectButton.setText("Connect");
        this.add(ConnectButton);
        ConnectButton.setBounds(450, 615, 100, 45);
        ConnectButton.addActionListener((e)->{

        });
    }

    private void AddLabel() {
        //---- Init ----
        this.TurnLabel = new JLabel();
        this.RoundLabel = new JLabel();

        //---- TurnLabel ----
        TurnLabel.setFont(new Font("Rockwell", Font.PLAIN, 14));
        TurnLabel.setHorizontalAlignment(JLabel.CENTER);
        TurnLabel.setHorizontalTextPosition(JLabel.CENTER);
        this.add(TurnLabel);
        TurnLabel.setBounds(430, 60, 115, 40);

        //---- RoundLabel ----
        RoundLabel.setFont(new Font("Rockwell", Font.PLAIN, 18));
        RoundLabel.setHorizontalAlignment(JLabel.CENTER);
        RoundLabel.setHorizontalTextPosition(JLabel.CENTER);
        this.add(RoundLabel);
        RoundLabel.setBounds(430, 25, 110, 40);
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
        this.PlayerName1 = new JLabel();
        this.PlayerName2 = new JLabel();

        //---- pro1 ----
        pro1.setIcon(new ImageIcon("resources/profile1.png"));
        this.add(pro1);
        pro1.setBounds(40, 50, 33, 33);

        //---- pro2 ----
        pro2.setIcon(new ImageIcon("resources/profile2.png"));
        this.add(pro2);
        pro2.setBounds(460, 520, 33, 33);

        //---- PlayerName1 ----
        PlayerName1.setText("AI");
        PlayerName1.setFont(PlayerName1.getFont().deriveFont(PlayerName1.getFont().getSize() + 6f));
        PlayerName1.setBorder(null);
        PlayerName1.setOpaque(false);
        PlayerName1.setHorizontalAlignment(SwingConstants.CENTER);
        PlayerName1.setHorizontalTextPosition(SwingConstants.CENTER);
        this.add(PlayerName1);
        PlayerName1.setBounds(15, 15, 85, 25);

        //---- PlayerName2 ----
        PlayerName2.setText("Tim");
        PlayerName2.setFont(PlayerName2.getFont().deriveFont(PlayerName2.getFont().getSize() + 6f));
        PlayerName2.setBorder(null);
        PlayerName2.setOpaque(false);
        PlayerName2.setHorizontalAlignment(SwingConstants.CENTER);
        PlayerName2.setHorizontalTextPosition(SwingConstants.CENTER);
        this.add(PlayerName2);
        PlayerName2.setBounds(435, 570, 85, 25);
    }

    public void printMess(String mess) {
        this.Message += mess;
        MessagePane.setText(this.Message);
    }

    public void printRank(String mess) {
        RankPane.setText(mess);
    }

    public void printTurnAndRound() {
        int turn = Game.turn;
        String player = turn == 0 ? "BLACK" : "RED";
        int round = Game.steps;
        RoundLabel.setText(String.format("ROUND %2d", round));
        TurnLabel.setText(player + "'s Turn");
    }

    public void showSuccess() {
        JOptionPane.showMessageDialog(this,"You Won!");
    }
}
