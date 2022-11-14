package ChessBoard;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class Player
{
    public ChessPieces pieces=new ChessPieces();
    public int score=0;
    public String id;
    public int rating=1500;
    public boolean isAI=false;
    UUID uuid;
    public ArrayList<String> history=new ArrayList<>();
    public static String pause="?!-.";
    public Player()
    {
        id="Player";
        uuid=UUID.nameUUIDFromBytes(id.getBytes());
    }
    public Player(String userid,boolean isAI)
    {
        id=userid;
        this.isAI=isAI;
        uuid=UUID.nameUUIDFromBytes(id.getBytes());
    }
    public Player(Player user)
    {
        this.pieces.init(user.pieces.chess);
        this.score=user.score;
        this.id=user.id;
        this.isAI=user.isAI;
        this.uuid=user.uuid;
        this.rating=user.rating;
        this.history.addAll(user.history);
    }
    public String Msg()
    {
        return id+pause+isAI+pause+rating+pause+pieces.Msg();
    }
    public String UserMsg()
    {
        StringBuilder out= new StringBuilder(id+pause+uuid.toString()+pause+rating+pause);
        out.append(history.size()).append(pause);
        for(String game:history)
            out.append(game).append(pause);
        return out.toString();
    }
    public void Show()
    {
        System.out.printf("id: %s rating: %d\n",id,rating);
        System.out.println("history:");
        for(String game:history)
            System.out.println(game);
        if(history.isEmpty())System.out.println("null");
    }
    boolean FormatCheck(String raw)
    {

    }
    public void Load(String input)
    {
        if(!FormatCheck(input))return;
        String[] data=input.split(pause);
        id=data[0];
        uuid=UUID.fromString(data[1]);
        rating=Integer.parseInt(data[2]);
        int n=Integer.parseInt(data[3]);
        history.clear();
        if(n>0)history.addAll(Arrays.asList(data).subList(4, n + 4));
    }
}