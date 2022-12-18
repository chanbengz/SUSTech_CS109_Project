package ChessBoard;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class Player
{
    public ChessPieces pieces=new ChessPieces();
    public int score=0;
    public String id;
    public int rating=1500;
    public int isAI;
    UUID uuid;
    String passwd;
    public ArrayList<String> history=new ArrayList<>();
    public static String pause="_=_~";
    public static String BigPause="_~=_";
    private static String byte2Hex(byte[] bytes)
    {
        StringBuilder stringBuffer=new StringBuilder();
        String tmp;
        for (byte aByte : bytes)
        {
            tmp=Integer.toHexString(aByte & 0xFF);
            if(tmp.length()==1)
                stringBuffer.append("0");
            stringBuffer.append(tmp);
        }
        return stringBuffer.toString();
    }
    public Player()
    {
        id="Player";
        this.isAI=0;
        uuid=UUID.nameUUIDFromBytes(id.getBytes());
        String passwd="default";
        try {
            MessageDigest messageDigest=MessageDigest.getInstance("SHA-256");
            messageDigest.update((uuid.toString()+passwd).getBytes());
            this.passwd=byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    public Player(String userid,int isAI)
    {
        id=userid;
        this.isAI=isAI;
        uuid=UUID.nameUUIDFromBytes(id.getBytes());
        String passwd="default";
        try {
            MessageDigest messageDigest=MessageDigest.getInstance("SHA-256");
            messageDigest.update((uuid.toString()+passwd).getBytes());
            this.passwd=byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    public Player(String userid,int isAI,String passwd)
    {
        id=userid;
        this.isAI=isAI;
        uuid=UUID.nameUUIDFromBytes(id.getBytes());
        try {
            MessageDigest messageDigest=MessageDigest.getInstance("SHA-256");
            messageDigest.update((uuid.toString()+passwd).getBytes());
            this.passwd=byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean login(String passwd)
    {
        MessageDigest messageDigest;
        try {
            messageDigest=MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        messageDigest.update((uuid.toString()+passwd).getBytes());
        return this.passwd.equals(byte2Hex(messageDigest.digest()));
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
        this.passwd=user.passwd;
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
        int isAI=Integer.parseInt(data[1]);
        if(isAI!=0 && isAI!=1 && isAI!=2 && isAI!=3 && isAI!=4 && isAI!=5)throw new ChessException("Invalid player's type.\nError Code:202");//check isAI tag
        int rat=Integer.parseInt(data[2]);
        if(rat<0 || rat>10000)throw new ChessException("Wrong rating.\nError Code:203");
        passwd=data[3];
        int n=Integer.parseInt(data[4]);
        if(n+5!=data.length)throw new ChessException("Wrong history size.\nError Code:204");
        for(int i=1;i<=n;i++)
            if(!data[i+4].matches("([0-9a-fA-F]{8}(-[0-9a-fA-F]{4}){3}-[0-9a-fA-F]{12}?)"))
                throw new ChessException("Wrong history UUID.\nError Code:205");
    }
    public String UserMsg()
    {
        StringBuilder out=new StringBuilder(id+pause+isAI+pause+rating+pause+passwd+pause);
        out.append(history.size()).append(pause);
        for(String game:history)
            out.append(game).append(pause);
        return out.toString();
    }
    public void NetLoad(String input)
    {
        String[] data=input.split(pause);
        id=data[0];
        uuid=UUID.nameUUIDFromBytes(id.getBytes());
        isAI=Integer.parseInt(data[1]);
        rating=Integer.parseInt(data[2]);
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
        uuid=UUID.nameUUIDFromBytes(id.getBytes());
        isAI=Integer.parseInt(data[1]);
        rating=Integer.parseInt(data[2]);
        passwd=data[3];
        int n=Integer.parseInt(data[4]);
        history.clear();
        if(n>0)history.addAll(Arrays.asList(data).subList(5, n + 5));
    }
    public String GamingMsg()
    {
        StringBuilder out=new StringBuilder(id+pause+isAI+pause+rating+pause+score+pause+pieces.Msg());
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
        isAI=Integer.parseInt(data[1]);
        rating=Integer.parseInt(data[2]);
        score=Integer.parseInt(data[3]);
        for(int i=0;i<=15;i++)
            pieces.chess[i]=new Point(Integer.parseInt(data[4+i]));
        history.clear();
        int n=Integer.parseInt(data[20]);
        history.addAll(Arrays.asList(data).subList(21, n + 21));
    }
}