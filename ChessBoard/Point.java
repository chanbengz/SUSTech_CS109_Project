package ChessBoard;

public class Point
{
    int x, y;
    boolean show;
    boolean alive;

    public Point(int x, int y, boolean show, boolean alive) {
        this.x = x;
        this.y = y;
        this.show = show;
        this.alive = alive;
    }

    public Point(Point point) {
        this.x = point.x;
        this.y = point.y;
        this.show = point.show;
        this.alive = point.alive;
    }
}
