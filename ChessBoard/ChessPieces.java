package ChessBoard;

public class ChessPieces {
    Point[] chess = new Point[16];

    public void init(Point[] A)
    {
        System.arraycopy(A, 0, this.chess, 0, 16);
    }
}
