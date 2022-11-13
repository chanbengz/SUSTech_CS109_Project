package ChessBoard;
public class Operation
{
    int x1,y1,x2,y2;
    public Operation(int x1,int y1,int x2,int y2)
    {
        this.x1=x1;
        this.y1=y1;
        this.x2=x2;
        this.y2=y2;
    }
    public Operation(int status)
    {
        this.x1=status%10;
        status/=10;
        this.y1=status%10;
        status/=10;
        this.x2=status%10;
        status/=10;
        this.y2=status;
    }
    public int transfer()
    {
        return x1+y1*10+x2*100+y2*1000;
    }
    public boolean isLoad()
    {
        return x1==0 && y1==0 && x2==0 && y2==0;
    }
}