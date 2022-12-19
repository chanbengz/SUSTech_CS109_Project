package GUI;

import ChessBoard.ChessBoard;
import ChessBoard.Player;
import ChessBoard.FileOperation;
import ChessBoard.ChessException;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

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
    private JButton ReplayButton;
    private JButton CheatButton;
    private JLabel TurnLabel;
    private JLabel RoundLabel;
    private String Message = "";
    private JButton ReplayLast;
    private JButton ReplayNext;
    public Controller controller;
    public boolean pvp;
    public boolean cheat;
    public Player local;
    public ArrayList<Player> list;
    Clip bgm;

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
        list = FileOperation.ScanUser("User/");
        Collections.sort(list);
        StringBuilder rankness = new StringBuilder();
        for(Player o: list)
            rankness.append(String.format("%6s %7d %7d\n", o.id, o.rating, o.score));
        printRank(rankness.toString());

        try {
            bgm = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
        PlayBGM(0);
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
                int value = 9;
                if(who != -1) {
                    value = (int)Math.pow(-1,who) * this.Game.players[who].pieces.chess[this.Game.map[y+1][x+1].index].level;
                }
                if(value != 9) {
                    GameBoard[x][y].rank = value > 0 ? value : -value;
                    GameBoard[x][y].player = value > 0 ? 0 : 1;
                } else {
                    GameBoard[x][y].rank = 0;
                    GameBoard[x][y].player = -1;
                }
                GameBoard[x][y].isRevealed = this.Game.players[who].pieces.chess[this.Game.map[y+1][x+1].index].show;
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
        this.ReplayButton = new JButton();
        this.ReplayLast = new JButton();
        this.ReplayNext = new JButton();
        this.CheatButton = new JButton();

        //---- StartButton ----
        StartButton.setText("Start");
        this.add(StartButton);
        StartButton.setBounds(10, 615, 100, 45);
        StartButton.addActionListener((e)->{
            if(!started) {
                String[] logoption = {"Sign in", "Sign up"};
                int login = JOptionPane.showOptionDialog(this, "Sign up or Sign in", "Login",JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, logoption, null);
                if(login == 0) {
                    if(list.size()==0)
                    {
                        JOptionPane.showMessageDialog(this,"Empty User List","Warning",JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    while (local == null) {
                        String id = JOptionPane.showInputDialog(this, "Account: ", "Login",JOptionPane.PLAIN_MESSAGE);
                        if(id == null || id.equals("")) return;
                        for(Player o: list) {
                            if(o.id.equals(id)) {
                                local = o;
                            }
                        }
                        if(local == null) {
                            JOptionPane.showMessageDialog(this,"Invalid User!","Warning",JOptionPane.WARNING_MESSAGE);
                        }
                        if(local != null) {
                            String passwd;
                            do {
                                passwd = JOptionPane.showInputDialog(this, "Password: ", "Login", JOptionPane.PLAIN_MESSAGE);
                                if(passwd == null||passwd.equals("")) return;
                            } while(!local.login(passwd));
                        }
                    }
                } else {
                    String id;
                    do {
                        id = JOptionPane.showInputDialog(this, "Create User: ", "Sign up",JOptionPane.PLAIN_MESSAGE);
                        if(id == null || id.equals("")) return;
                        if(!id.matches("^[a-zA-Z0-9_]{0,15}$")) {
                            JOptionPane.showMessageDialog(this,"Invalid Name","Warning",JOptionPane.PLAIN_MESSAGE);
                        }
                    } while (!id.matches("^[a-zA-Z0-9_]{0,15}$"));
                    String passwd = JOptionPane.showInputDialog(this, "Password: ", "Login", JOptionPane.PLAIN_MESSAGE);
                    local = new Player(id, 3, passwd);
                    try {
                        FileOperation.SaveUser(local);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    JOptionPane.showMessageDialog(this,"User created","Success",JOptionPane.PLAIN_MESSAGE);
                }
            }
            String[] options = {"Connect", "Medium", "Easy", "Beginner"};
            int select = JOptionPane.showOptionDialog(this, "Please choose the level of AI or connect to others", "Start",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, null);
            this.started = true;
            Game = new ChessBoard();
            if(select != 0) {
                int level = 4;
                if(select == 2) {
                    level = 1;
                } else if(select == 1) {
                    level = 2;
                }
                Player AI = new Player("AI", level);
                Game.Init(local, AI);
                pvp = false;
            } else {
                pvp = true;
                Connect();
            }
            Game.mainFrame = this;
            Game.InitialMap();
            Game.Show();
            printTurnAndRound();
            pro1.setVisible(true);
            pro2.setVisible(true);
            PlayerName1.setText(Game.players[1].id);
            PlayerName2.setText(Game.players[0].id);
            CheatButton.setVisible(true);
            StartButton.setText("Restart");
            generate();
            PlayBGM(1);
        });

        //---- CheatButton ----
        CheatButton.setText("Cheat");
        this.add(CheatButton);
        CheatButton.setBounds(10,500,100,45);
        CheatButton.addActionListener((e)->{
            this.cheat = !this.cheat;
        });
        CheatButton.setVisible(false);

        //---- StopButton ----
        StopButton.setText("Stop");
        this.add(StopButton);
        StopButton.setBounds(120, 615, 100, 45);
        StopButton.addActionListener((e)->{
            if( started ) {
                String dir = null;
                try {
                    dir = FileOperation.GamePause(this.Game);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                JOptionPane.showMessageDialog(this, "Save at " + dir);
                Game = null;
                this.started = false;
                RoundLabel.setText("");
                TurnLabel.setText("");
                MessagePane.setText("");
                pro1.setVisible(false);
                pro2.setVisible(false);
                PlayerName1.setText("");
                PlayerName2.setText("");
                this.Message = "";
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
            PlayBGM(0);
        });

        //---- LoadButton ----
        LoadButton.setText("Load");
        this.add(LoadButton);
        LoadButton.setBounds(230, 615, 100, 45);
        LoadButton.addActionListener((e)->{
            JFileChooser fileChooser = new JFileChooser();
            if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
                File ret = fileChooser.getSelectedFile();
                String dir = ret.getPath(), data = "";
                try {
                    data=FileOperation.Load(dir);
                } catch (ChessException ex) {
                    JOptionPane.showMessageDialog(this,ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
                    return ;
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                String name=dir.substring(dir.lastIndexOf("/")+1);
                try {
                    Game = new ChessBoard();
                    Game.GameContinue(data,name.substring(0,name.length()-5));
                } catch (ChessException ex) {
                    JOptionPane.showMessageDialog(this,ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
                }
                generate();
                pro1.setVisible(true);
                pro2.setVisible(true);
                PlayerName1.setText(Game.players[1].id);
                PlayerName2.setText(Game.players[0].id);
            }
        });

        //---- WdButton ----
        WdButton.setText("Withdraw");
        this.add(WdButton);
        WdButton.setBounds(340, 615, 100, 45);
        WdButton.addActionListener((e)->{
            if(started) {
                if(Game.steps != 0){
                    Game.LoadPoint(false);
                    generate();
                    printTurnAndRound();
                    Game.Show();
                } else {
                    JOptionPane.showMessageDialog(this,"You can't withdraw", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        //---- ReplayButton ----
        ReplayButton.setText("Replay");
        this.add(ReplayButton);
        ReplayButton.setBounds(450, 615, 100, 45);
        ReplayButton.addActionListener((e)->{
            JFileChooser fileChooser = new JFileChooser();
            if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File ret = fileChooser.getSelectedFile();
                String dir = ret.getPath();
                String data;
                try {
                    data = FileOperation.Load(dir);
                } catch (ChessException ex) {
                    System.out.println(ex.getMessage());
                    JOptionPane.showMessageDialog(this,ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                ChessBoard Replay = new ChessBoard();
                String name = dir.substring(dir.lastIndexOf("/") + 1);
                this.Game = Replay;
                Game.mainFrame = this;
                try {
                    Game.LoadReplay(data, name.substring(0, name.length() - 6));
                } catch (ChessException ex) {
                    JOptionPane.showMessageDialog(this,ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                pro1.setVisible(true);
                pro2.setVisible(true);
                PlayerName1.setText(Game.players[1].id);
                PlayerName2.setText(Game.players[0].id);
                printTurnAndRound();
                generate();
                ReplayLast.setVisible(true);
                ReplayNext.setVisible(true);
            }
        });

        ReplayLast.setText("<");
        ReplayLast.setFont(new Font("Rockwell", Font.BOLD, 15));
        ReplayLast.setBounds(35, 300, 50,50);
        this.add(ReplayLast);
        ReplayLast.setVisible(false);
        ReplayLast.addActionListener((e)->{
            if(Game.steps != 0){
                Game.LoadPoint(true);
                generate();
                Game.Show();
                printTurnAndRound();
            }
        });

        ReplayNext.setText(">");
        ReplayNext.setFont(new Font("Rockwell", Font.BOLD, 15));
        ReplayNext.setBounds(450, 300, 50,50);
        this.add(ReplayNext);
        ReplayNext.setVisible(false);
        ReplayNext.addActionListener((e)->{
            try {
                Game.nextStep(Game.opt_stack.get(Game.steps),3);
            } catch (ChessException ex) {
                JOptionPane.showMessageDialog(this,ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
                Game = null;
                this.started = false;
                RoundLabel.setText("");
                TurnLabel.setText("");
                MessagePane.setText("");
                this.Message = "";
                for(int y = 0; y < 8; y++) {
                    for(int x = 0; x < 4; x++) {
                        GameBoard[x][y].setVisible(false);
                        GameBoard[x][y].rank = -1;
                        GameBoard[x][y].player = -1;
                        GameBoard[x][y].isRevealed = false;
                        GameBoard[x][y].repaint();
                    }
                }
                return;
            }
            printTurnAndRound();
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
        RankPane.setFont(new Font("Space Mono", Font.PLAIN, 14));
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
        pro1.setVisible(false);

        //---- pro2 ----
        pro2.setIcon(new ImageIcon("resources/profile2.png"));
        this.add(pro2);
        pro2.setBounds(460, 520, 33, 33);
        pro2.setVisible(false);

        //---- PlayerName1 ----
        PlayerName1.setText("");
        PlayerName1.setFont(PlayerName1.getFont().deriveFont(PlayerName1.getFont().getSize() + 6f));
        PlayerName1.setBorder(null);
        PlayerName1.setOpaque(false);
        PlayerName1.setHorizontalAlignment(SwingConstants.CENTER);
        PlayerName1.setHorizontalTextPosition(SwingConstants.CENTER);
        this.add(PlayerName1);
        PlayerName1.setBounds(15, 15, 85, 25);

        //---- PlayerName2 ----
        PlayerName2.setText("");
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
        String info = "+----+--------+-------+\n| ID | Rating | Score |\n+----+--------+-------+\n";
        RankPane.setText(info + mess);
    }

    public void printTurnAndRound() {
        int turn = Game.turn;
        String player = turn == 0 ? "BLACK" : "RED";
        int round = Game.steps;
        RoundLabel.setText(String.format("ROUND %2d", round));
        TurnLabel.setText(player + "'s Turn");
        if(turn == 0) TurnLabel.setForeground(Color.BLACK);
        else TurnLabel.setForeground(Color.RED);
    }

    public void showGameOver(String dir, int status) {
        if(status == 1) {
            JOptionPane.showMessageDialog(this,"You Won!\nSave at: " + dir);
        } else if(status == 0) {
            JOptionPane.showMessageDialog(this,"Draw!\nSave at: " + dir);
        } else {
            JOptionPane.showMessageDialog(this,"You Lost!\nSave at: " + dir);
        }
    }

    private void Connect() {
        String[] option = {"Guest", "Host"};
        int select = JOptionPane.showOptionDialog(this, "Decide if you're the host", "Connect",JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, option, null);
        String ip = "";
        if(select == 0) {
            do {
                ip = JOptionPane.showInputDialog(this, "Target IP: ", "Connect", JOptionPane.PLAIN_MESSAGE);
                if(!ip.matches("((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))"))
                    JOptionPane.showMessageDialog(this,"Invalid IP");
            } while(!ip.matches("((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))"));
        }
        int port = 0;
        do {
            port = Integer.parseInt(JOptionPane.showInputDialog(this, "Target port: ", "Connect", JOptionPane.PLAIN_MESSAGE));
            if(port < 1 || port > 65535)
                JOptionPane.showMessageDialog(this,"Invalid port");
        } while (port < 1 || port > 65535);

        Game.NetworkInit(ip, port, select == 0 ? 1 : 0, local);
    }

    public void PlayBGM(int mode) {
        if(bgm.isActive()) {
            bgm.stop();
            bgm.close();
        }

        AudioInputStream inputStream = null;
        if(mode == 0) {
            try {
                inputStream = AudioSystem.getAudioInputStream(new File("resources/hall.wav"));
            } catch (UnsupportedAudioFileException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if(mode == 1) {
            try {
                inputStream = AudioSystem.getAudioInputStream(new File("resources/combat.wav"));
            } catch (UnsupportedAudioFileException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            bgm.open(inputStream);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        bgm.loop(Clip.LOOP_CONTINUOUSLY);
    }
}
