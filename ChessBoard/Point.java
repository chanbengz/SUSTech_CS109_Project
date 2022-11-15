package ChessBoard;
public class Point//行列均由1为起始下标
{
    int x, y, level;
    boolean show;
    boolean alive;
    public Point()
    {
        this.x=0;
        this.y=0;
        this.level=0;
        this.show=false;
        this.alive=false;
    }
    public Point(int x, int y, int level, boolean show, boolean alive)
    {
        this.x = x;
        this.y = y;
        this.level = level;
        this.show = show;
        this.alive = alive;
    }
    public Point(Point p)
    {
        this.x=p.x;
        this.y=p.y;
        this.level=p.level;
        this.show=p.show;
        this.alive=p.alive;
    }
    public Point(int status)
    {
        this.x=status%10;
        status/=10;
        this.y=status%10;
        status/=10;
        this.level=status;
        this.show=false;
        this.alive=true;
    }
    public int transfer()
    {
        return x+y*10+level*100;
    }
    public void check() throws ChessException
    {
        if(x<1 || x>8 || y<1 || y>4)throw new ChessException("Wrong coordinate.\nError Code:302");
        if(level<1 || level>7)throw new ChessException("Invalid level.\nError Code:303");
    }
}