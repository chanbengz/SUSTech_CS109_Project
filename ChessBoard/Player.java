package ChessBoard;

import java.util.ArrayList;

public class Player {
    public ChessPieces pieces=new ChessPieces();
    public int score=0;
    public String id="";
    public int rating=1500;
    public ArrayList<String> history=new ArrayList<>();
    public Player(String id)
    {
        this.id=id;
    }
}
