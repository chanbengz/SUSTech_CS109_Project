package ChessBoard;
import java.util.ArrayList;
public class Player
{
    public ChessPieces pieces=new ChessPieces();
    public int score=0;
    public String id;
    public int rating=1500;
    public ArrayList<String> history=new ArrayList<>();
    public static String pause="?!-.";
    public Player()
    {
        id="Player";
    }
    public Player(String userid)
    {
        id=userid;
    }
    public Player(Player user)
    {
        this.pieces.init(user.pieces.chess);
        this.score=user.score;
        this.id=user.id;
        this.rating=user.rating;
        this.history.addAll(user.history);
    }
    public String Msg()
    {
        String out="";
        out+=id+pause+rating+pause+pieces.Msg();
        return out;
    }
    public String UserMsg()
    {
        StringBuilder out= new StringBuilder(id+pause+rating+pause);
        out.append(history.size()).append(pause);
        for(String game:history)
            out.append(game).append(pause);
        return out.toString();
    }
    public void Load(String input)
    {
        int index=0;
        int pos_id=input.indexOf(pause,index);
        id=input.substring(index,pos_id);
        index=pos_id+pause.length();
        int pos_rat=input.indexOf(pause,index);
        rating=Integer.parseInt(input.substring(index,pos_rat));
        index=pos_rat+pause.length();
        int pos_size=input.indexOf(pause,index);
        int n=Integer.parseInt(input.substring(index,pos_size));
        index=pos_size+pause.length();
        history.clear();
        for(int i=1;i<=n;i++)
        {
            int pos_his=input.indexOf(pause,index);
            history.add(input.substring(index,pos_his));
            index=pos_his+pause.length();
        }
    }
}