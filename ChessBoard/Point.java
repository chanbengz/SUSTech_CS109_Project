package ChessBoard;

public class Point//行列均由1为起始下标
{
    int x, y, level;
    boolean show;
    boolean alive;
    public Point(int x, int y, int level, boolean show, boolean alive) {
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
        return x+y*10+level*10;
    }
}
