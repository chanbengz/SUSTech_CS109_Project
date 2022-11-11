package src;

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
        for(int i=0;i<=1;i++)
            Advisor[i]=A[i+1];
        Minister=new Point[2];
        for(int i=0;i<=1;i++)
            Minister[i]=A[i+3];
        Chariot=new Point[2];
        for(int i=0;i<=1;i++)
            Chariot[i]=A[i+5];
        Horse=new Point[2];
        for(int i=0;i<=1;i++)
            Horse[i]=A[i+7];
        Soldier=new Point[5];
        for(int i=0;i<=4;i++)
            Soldier[i]=A[i+9];
        Cannon=new Point[2];
        for(int i=0;i<=1;i++)
            Cannon[i]=A[i+14];
    }
}
public class ChessBoard
{

}