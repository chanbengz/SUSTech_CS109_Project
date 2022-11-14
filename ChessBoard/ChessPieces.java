package ChessBoard;
import java.util.List;

public class ChessPieces
{
    Point[] chess=new Point[16];
    public ChessPieces()
    {
        for(int i=0;i<=15;i++)
            this.chess[i]=new Point();
    }
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
    public static void FormatCheck(List<String> raw) throws ChessException {
        for(String tmp:raw)
            new Point(Integer.parseInt(tmp)).check();
    }
}