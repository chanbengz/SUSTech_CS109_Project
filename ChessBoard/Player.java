package ChessBoard;

public class Player {
    public ChessPieces pieces=new ChessPieces();
    public int score=0;
    public String id="";
    public int rating=1500;

    public Player(String id)
    {
        this.id=id;
    }
}
