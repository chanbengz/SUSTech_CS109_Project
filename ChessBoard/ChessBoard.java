package ChessBoard;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

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
    ChessPieces Red=new ChessPieces();
    ChessPieces Black=new ChessPieces();
    //本机生成
    void CreatePieces()
    {
        LinkedList<Integer> All= new LinkedList<>();
        for(int i=1;i<=32;i++) All.add(i);
        Collections.shuffle(All, new Random());
        Point[] RedStart=new Point[16];
        for(int i=0;i<=15;i++)
        {
            int tmp=All.getLast();
            All.removeLast();
            int y=tmp%4==0 ? 4 : tmp%4;
            int x=(tmp-y)/4+1;
            RedStart[i]=new Point(x,y,false,true);
        }
        Red.init(RedStart);
        Point[] BlackStart=new Point[16];
        for(int i=0;i<=15;i++)
        {
            int tmp=All.getLast();
            All.removeLast();
            int y=tmp%4==0 ? 4 : tmp%4;
            int x=(tmp-y)/4+1;
            BlackStart[i]=new Point(x,y,false,true);
        }
        Black.init(BlackStart);
    }
    //文件读写
    //网络读写
}