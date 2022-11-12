package ChessBoard;

public class ChessPieces {
    Point[] chess = new Point[16];

    public void init(Point[] A)
    {
        System.arraycopy(A, 0, this.chess, 0, 16);
    }
    public String Msg()
    {
        StringBuilder out= new StringBuilder();
        for(int i=0;i<=15;i++)
            out.append(chess[i].transfer()).append(Player.pause);
        return out.toString();
    }
}
