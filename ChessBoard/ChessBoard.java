package ChessBoard;

class ChessPieces
{
    Point General;//将
    Point[] Advisor;//士
    Point[] Minister;//象
    Point[] Chariot;//车
    Point[] Horse;//马
    Point[] Soldier;//卒
    Point[] Cannon;//炮
    public void init(Point[] A)
    {
        General=new Point(A[0]);
        Advisor=new Point[2];
        System.arraycopy(A, 1, Advisor, 0, 2);
        Minister=new Point[2];
        System.arraycopy(A, 3, Minister, 0, 2);
        Chariot=new Point[2];
        System.arraycopy(A, 5, Chariot, 0, 2);
        Horse=new Point[2];
        System.arraycopy(A, 7, Horse, 0, 2);
        Soldier=new Point[5];
        System.arraycopy(A, 9, Soldier, 0, 5);
        Cannon=new Point[2];
        System.arraycopy(A, 14, Cannon, 0, 2);
    }
}
public class ChessBoard
{

}