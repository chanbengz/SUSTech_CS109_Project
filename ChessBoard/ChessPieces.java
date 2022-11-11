package ChessBoard;

public class ChessPieces {
    Point[] chess = new Point[16];

    public void init(Point[] A)
    {
        for(int i=0;i<=15;i++)
            this.chess[i]=new Point(0,0,0,false,false);
        System.arraycopy(A, 0, this.chess, 0, 16);
    }
}
