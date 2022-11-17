package ChessBoard;

import java.util.ArrayList;

public class Cache {
    int turn, steps;
    ChessBoard.Pair[][] map = new ChessBoard.Pair[10][10];
    ArrayList<Operation> opt_stack = new ArrayList<>();
    Player[] players = new Player[2];

    public Cache(int turn, int steps, ChessBoard.Pair[][] map, ArrayList<Operation> opt_stack, Player[] players) {
        this.turn = turn;
        this.steps = steps;
        for (int i = 1; i <= 8; i++)
            for (int j = 1; j <= 4; j++)
                this.map[i][j] = new ChessBoard.Pair(map[i][j]);
        this.opt_stack.addAll(opt_stack);
        for (int i = 0; i <= 1; i++)
            this.players[i] = new Player(players[i]);
    }
    public Cache(int turn,ChessBoard.Pair[][] map,Player[] players)
    {
        this.turn=turn;
        for (int i = 1; i <= 8; i++)
            for (int j = 1; j <= 4; j++)
                this.map[i][j] = new ChessBoard.Pair(map[i][j]);
        for (int i = 0; i <= 1; i++)
            this.players[i] = new Player(players[i]);
    }
}