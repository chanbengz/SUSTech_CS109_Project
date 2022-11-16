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
    public static String pause="_=_~";
    public static String BigPause="_~=_";
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
    public void Show()
    {
        System.out.printf("id: %s rating: %d\n",id,rating);
        System.out.println("history:");
        for(String game:history)
            System.out.println(game);
        if(history.isEmpty())System.out.println("null");
    }
    void FormatCheck(String raw,String name) throws ChessException {
        String[] data=raw.split(pause);
        if(!data[0].equals(name))throw new ChessException("Wrong username.\nError Code:201");
        if(!data[1].equals(UUID.nameUUIDFromBytes(name.getBytes()).toString()))throw new ChessException("Wrong user UUID.\nError Code:202");
        int rat=Integer.parseInt(data[2]);
        if(rat<0 || rat>10000)throw new ChessException("Wrong rating.\nError Code:203");
        int n=Integer.parseInt(data[3]);
        if(n+4!=data.length)throw new ChessException("Wrong history size.\nError Code:204");
        for(int i=1;i<=n;i++)
            if(!data[i+3].matches("([0-9a-fA-F]{8}(-[0-9a-fA-F]{4}){3}-[0-9a-fA-F]{12}?)"))
                throw new ChessException("Wrong history UUID.\nError Code:205");
    }
    public String UserMsg()
    {
        StringBuilder out= new StringBuilder(id+pause+uuid.toString()+pause+rating+pause);
        out.append(history.size()).append(pause);
        for(String game:history)
            out.append(game).append(pause);
        return out.toString();
    }
    public void Load(String input,String name)
    {
        try {
            FormatCheck(input,name);
        } catch (ChessException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        String[] data=input.split(pause);
        id=data[0];
        uuid=UUID.fromString(data[1]);
        rating=Integer.parseInt(data[2]);
        int n=Integer.parseInt(data[3]);
        history.clear();
        if(n>0)history.addAll(Arrays.asList(data).subList(4, n + 4));
    }
    public String GamingMsg()
    {
        StringBuilder out= new StringBuilder(id+pause+isAI+pause+rating+pause+score+pause+pieces.Msg());
        out.append(history.size()).append(pause);
        for(String game:history)
            out.append(game).append(pause);
        return out.toString();
    }
    public void LoadGaming(String input)
    {
        String[] data=input.split(pause);
        id=data[0];
        uuid=UUID.nameUUIDFromBytes(id.getBytes());
        isAI=data[1].equals("true");
        rating=Integer.parseInt(data[2]);
        score=Integer.parseInt(data[3]);
        for(int i=0;i<=15;i++)
            pieces.chess[i]=new Point(Integer.parseInt(data[4+i]));
        history.clear();
        int n=Integer.parseInt(data[20]);
        history.addAll(Arrays.asList(data).subList(21, n + 21));
    }
}