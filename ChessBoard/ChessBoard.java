package ChessBoard;
import java.io.IOException;
import java.util.*;
public class ChessBoard
{
    static class Pair
    {
        int player,index;
        public Pair(int player, int index)
        {
            this.player=player;
            this.index=index;
        }
        public Pair(Pair x)
        {
            this.player=x.player;
            this.index=x.index;
        }
    }
    Pair[][] map=new Pair[10][10];
    int turn,steps;
    Player[] players=new Player[2];
    ArrayList<Operation> opt_stack=new ArrayList<>();
    UUID uuid;
    static class Cache
    {
        int turn,steps;
        Pair[][] map=new Pair[10][10];
        ArrayList<Operation> opt_stack=new ArrayList<>();
        Player[] players=new Player[2];
        public Cache(int turn,int steps,Pair[][] map,ArrayList<Operation> opt_stack,Player[] players)
        {
            this.turn=turn;
            this.steps=steps;
            for(int i=1;i<=8;i++)
                for(int j=1;j<=4;j++)
                    this.map[i][j]=new Pair(map[i][j]);
            this.opt_stack.addAll(opt_stack);
            for(int i=0;i<=1;i++)
                this.players[i]=new Player(players[i]);
        }
    }
    ArrayList<Cache> game_stack=new ArrayList<>();
    void SavePoint()
    {
        game_stack.add(new Cache(turn,steps,map,opt_stack,players));
    }
    void LoadPoint()
    {
        Cache page=game_stack.get(game_stack.size()-1);
        turn=page.turn;
        steps=page.steps;
        for(int i=1;i<=8;i++)
            for(int j=1;j<=4;j++)
                map[i][j]=new Pair(page.map[i][j]);
        opt_stack.clear();
        opt_stack.addAll(page.opt_stack);
        for(int i=0;i<=1;i++)
            players[i]=new Player(page.players[i]);
    }
    void CreatePieces()
    {
        LinkedList<Integer> All= new LinkedList<>();
        for(int i=1;i<=32;i++) All.add(i);
        Collections.shuffle(All, new Random());
        Point[] RedStart=new Point[16];
        Point[] BlackStart=new Point[16];
        for(int i=0;i<=15;i++)
        {
            int tmp=All.getLast();
            All.removeLast();
            int y=tmp%4==0 ? 4 : tmp%4;
            int x=(tmp-y)/4+1;
            int level;
            if(i==0)level=1;//General
            else if(i<=2)level=2;//Advisor
            else if(i<=4)level=3;//Minister
            else if(i<=6)level=4;//Chariot
            else if(i<=8)level=5;//Horse
            else if(i<=13)level=6;//Soldier
            else level=7;//Cannon
            RedStart[i]=new Point(x,y,level,false,true);
            tmp=All.getFirst();
            All.removeFirst();
            y=tmp%4==0 ? 4 : tmp%4;
            x=(tmp-y)/4+1;
            BlackStart[i]=new Point(x,y,level,false,true);
        }
        players[0].pieces.init(RedStart);
        players[1].pieces.init(BlackStart);
    }
    void InitialMap()
    {
        for(int player=0;player<=1;player++)
            for(int i=0;i<=15;i++)
                map[players[player].pieces.chess[i].x][players[player].pieces.chess[i].y]=new Pair(player, i);
    }
    public void Show()
    {
        System.out.printf("Turn: %d\n",turn);
        System.out.printf("Steps: %d\n",steps);
        System.out.println("Score:");
        for(int i=0;i<=1;i++)
            System.out.printf("id: %s rating: %d score: %d\n",players[i].id,players[i].rating,players[i].score);
        for(int i=1;i<=8;i++,System.out.println())
            for(int j=1;j<=4;j++)
            {
                int who=map[i][j].player;
                if(who==-1 || !players[who].pieces.chess[map[i][j].index].alive)System.out.printf("%2d ",9);
                else
                {
                    int value=(int)Math.pow(-1,who)*players[who].pieces.chess[map[i][j].index].level;
                    System.out.printf("%2d ",value);
                }
            }
        System.out.println("-----------------------");
        for(int i=1;i<=8;i++,System.out.println())
            for(int j=1;j<=4;j++)
            {
                int who=map[i][j].player;
                if(who==-1 || !players[who].pieces.chess[map[i][j].index].alive)System.out.printf("%2d ",9);
                else
                {
                    if(this.players[who].pieces.chess[map[i][j].index].show)
                    {
                        int value=(int)Math.pow(-1,who)*players[who].pieces.chess[map[i][j].index].level;
                        System.out.printf("%2d ",value);
                    }
                    else System.out.printf("%2d ",0);
                }
            }
    }
    Operation Input()
    {
        Scanner input=new Scanner(System.in);
        System.out.println("Input: src dest");
        int x1=input.nextInt(),y1=input.nextInt(),x2=input.nextInt(),y2=input.nextInt();
        return new Operation(x1,y1,x2,y2);
    }
    void Scoring(int player,int cost)
    {
        Player tmp=players[player];
        switch(cost)
        {
            case 1-> tmp.score+=30;
            case 2-> tmp.score+=10;
            case 3, 4, 5, 7 -> tmp.score+=5;
            case 6-> tmp.score+=1;
        }
    }
    void Click()
    {
        Scanner input=new Scanner(System.in);
        System.out.println("Enter");
        String useless=input.next();
        System.out.println(useless);
    }
    void Go(boolean isReplay)
    {
        InitialMap();
        turn=0;
        steps=0;
        boolean start=false;
        while(true)
        {
            Show();
            if(Math.max(players[0].score,players[1].score)>=60)break;
            Point[] fun=new Point[2];
            Operation opt;
            if(isReplay)
            {
                Click();
                opt=opt_stack.get(steps);
            }
            else
            {
                opt=Input();
                if(opt.check()){LoadPoint();continue;}
                opt_stack.add(opt);
            }
            steps++;
            fun[0]=players[map[opt.x1][opt.y1].player].pieces.chess[map[opt.x1][opt.y1].index];
            if(map[opt.x2][opt.y2].player==-1)
                fun[1]=new Point(opt.x2,opt.y2,0,true,true);
            else
                fun[1]=players[map[opt.x2][opt.y2].player].pieces.chess[map[opt.x2][opt.y2].index];
            if(fun[0]==fun[1])
            {
                fun[0].show=true;
                if(!start)
                {
                    if(map[fun[0].x][fun[0].y].player==1)
                    {
                        ChessPieces tmp=new ChessPieces();
                        for(int i=0;i<=15;i++)
                            tmp.chess[i]=new Point(players[0].pieces.chess[i]);
                        players[0].pieces=players[1].pieces;
                        players[1].pieces=tmp;
                        InitialMap();
                    }
                    start=true;
                }
            }
            else
            {
                if(fun[0].level==7)
                {
                    fun[1].alive=false;
                    map[fun[1].x][fun[1].y].player=-1;
                    int cost=fun[1].level;
                    Scoring(turn,cost);
                }
                else
                {
                    int cost=fun[1].level;
                    int TmpPlayer=map[fun[0].x][fun[0].y].player;
                    int TmpIndex=map[fun[0].x][fun[0].y].index;
                    map[fun[0].x][fun[0].y].player=-1;
                    fun[1].alive=false;
                    map[fun[1].x][fun[1].y].player=TmpPlayer;
                    map[fun[1].x][fun[1].y].index=TmpIndex;
                    fun[0].x=fun[1].x;
                    fun[0].y=fun[1].y;
                    Scoring(turn,cost);
                }
            }
            turn^=1;
            SavePoint();
        }
    }
    public String Play(Player Alice,Player Bob)
    {
        players[0]=Alice;
        players[1]=Bob;
        uuid=UUID.randomUUID();
        CreatePieces();
        Go(false);
        double p=1.0/(1.0+Math.pow(10,1.0*(players[1].rating-players[0].rating)/400));
        int sign=(players[0].score>players[1].score ? 1 : -1);
        players[0].rating+=sign*20*(1-p);
        players[1].rating+=-1*sign*20*(1-p);
        System.out.println("Game over!");
        String guid=uuid.toString();
        System.out.println(guid);
        System.out.println("Rating is updated!");
        for(int i=0;i<=1;i++)
            System.out.printf("id: %s rating: %d score: %d\n",players[i].id,players[i].rating,players[i].score);
        players[0].history.add(guid);
        players[1].history.add(guid);
        String dir;
        try {
            dir=FileOperation.SaveGame(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return dir;
    }
    public void Load(String data)
    {
        int index=0;
        int pos_uuid=data.indexOf(Player.pause,index);
        uuid=UUID.fromString(data.substring(index,pos_uuid));
        index=pos_uuid+Player.pause.length();
        for(int i=0;i<=1;i++)
        {
            int pos_id=data.indexOf(Player.pause,index);
            String id=data.substring(index,pos_id);
            index=pos_id+Player.pause.length();
            players[i]=new Player(id);
            int pos_rat=data.indexOf(Player.pause,index);
            players[i].rating=Integer.parseInt(data.substring(index,pos_rat));
            index=pos_rat+Player.pause.length();
            for(int j=0;j<=15;j++)
            {
                int pos=data.indexOf(Player.pause,index);
                players[i].pieces.chess[j]=new Point(Integer.parseInt(data.substring(index,pos)));
                index=pos+Player.pause.length();
            }
        }
        int pos_size=data.indexOf(Player.pause,index);
        int n=Integer.parseInt(data.substring(index,pos_size));
        index=pos_size+Player.pause.length();
        for(int i=1;i<=n;i++)
        {
            int pos=data.indexOf(Player.pause,index);
            opt_stack.add(new Operation(Integer.parseInt(data.substring(index,pos))));
            index=pos+Player.pause.length();
        }
    }
    public void Replay()
    {
        Go(true);
        System.out.println("Game over!");
        String guid=uuid.toString();
        System.out.println(guid);
        for(int i=0;i<=1;i++)
            System.out.printf("id: %s rating: %d score: %d\n",players[i].id,players[i].rating,players[i].score);
    }
}