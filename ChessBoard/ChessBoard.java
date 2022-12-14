package ChessBoard;
import GUI.Controller;
import GUI.PieceComponent;

import java.io.IOException;
import java.util.*;
public class ChessBoard
{
    public static class Pair
    {
        public int player,index;
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
    public Pair[][] map=new Pair[10][10];
    int turn,steps;
    public Player[] players=new Player[2];
    ChessPieces[] initPieces=new ChessPieces[2];
    ArrayList<Operation> opt_stack=new ArrayList<>();
    UUID uuid;
    ArrayList<Cache> game_stack=new ArrayList<>();
    public Controller controller;
    public void Init(ChessBoard p)
    {
        this.turn=p.turn;
        this.steps=p.steps;
        for(int i=1;i<=8;i++)
            for(int j=1;j<=4;j++)
                this.map[i][j]=new Pair(p.map[i][j]);
        this.players[0]=new Player(p.players[0]);
        this.players[1]=new Player(p.players[1]);
        /*this.initPieces[0]=new ChessPieces();
        this.initPieces[0].init(p.initPieces[0]);
        this.initPieces[1]=new ChessPieces();
        this.initPieces[1].init(p.initPieces[1]);
        this.opt_stack.clear();
        this.opt_stack.addAll(p.opt_stack);
        this.uuid=UUID.fromString(p.uuid.toString());
        this.game_stack.clear();
        this.game_stack.addAll(p.game_stack);*/
    }
    void SavePoint()
    {
        game_stack.add(new Cache(turn,steps,map,opt_stack,players));
    }
    void LoadPoint()
    {
        game_stack.remove(game_stack.size()-1);
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
        for(int i=0;i<=1;i++)
            initPieces[i]=new ChessPieces();
        initPieces[0].init(RedStart);
        players[0].pieces.init(RedStart);
        initPieces[1].init(BlackStart);
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
                if(who==-1)System.out.printf("%2d ",9);
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
                if(who==-1)System.out.printf("%2d ",9);
                else
                {
                    if(players[who].pieces.chess[map[i][j].index].show)
                    {
                        int value=(int)Math.pow(-1,who)*players[who].pieces.chess[map[i][j].index].level;
                        System.out.printf("%2d ",value);
                    }
                    else System.out.printf("%2d ",0);
                }
            }
    }
    Operation Input() throws ChessException {
        switch(players[turn].isAI)//check isAI tag
        {
            case 0 ->
            {
                Scanner input=new Scanner(System.in);
                System.out.println("Input: src dest");
                int x1=input.nextInt(),y1=input.nextInt(),x2=input.nextInt(),y2=input.nextInt();
                return new Operation(x1,y1,x2,y2);
            }
            case 1 ->
            {
                ArtificialIdiot AI=new ArtificialIdiot();
                AI.LoadMap(new Cache(turn,map,players));
                return AI.Easy();
            }
            case 2 ->
            {
                ArtificialIdiot AI=new ArtificialIdiot();
                AI.LoadMap(this);
                return AI.Normal();
            }
            case 3 ->
            {
                PieceComponent first = controller.getFirst();
                PieceComponent second = controller.getSecond();
                int x1 = first.x, y1 = first.y, x2 = second.x, y2 = second.y;
                return new Operation(x1,y1,x2,y2);
            }
            default -> throw new ChessException("Invalid player's type.\nError Code:202");
        }
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
    boolean Click()
    {
        Scanner input=new Scanner(System.in);
        System.out.println("input 1 to forward, 0 to back");
        int flag=input.nextInt();
        return flag==1;
    }
    void Go_Show(Point funny)
    {
        funny.show=true;
        if(steps==1)
        {
            if(map[funny.x][funny.y].player==1)
            {
                ChessPieces tmp=new ChessPieces();
                for(int i=0;i<=15;i++)
                    tmp.chess[i]=new Point(players[0].pieces.chess[i]);
                players[0].pieces=players[1].pieces;
                players[1].pieces=tmp;
                InitialMap();
            }
        }
        turn^=1;
    }
    void Go_Move(Point fun0,Point fun1) throws ChessException {
        if(map[fun0.x][fun0.y].player!=turn)
            throw new ChessException("Invalid move.\nError Code:305");
        if(fun0.level==7)
        {
            fun1.alive=false;
            int who=map[fun1.x][fun1.y].player;
            map[fun1.x][fun1.y].player=-1;
            Scoring(who^1,fun1.level);
        }
        else
        {
            int cost=fun1.level;
            int TmpPlayer=map[fun0.x][fun0.y].player;
            int TmpIndex=map[fun0.x][fun0.y].index;
            map[fun0.x][fun0.y].player=-1;
            fun1.alive=false;
            map[fun1.x][fun1.y].player=TmpPlayer;
            map[fun1.x][fun1.y].index=TmpIndex;
            fun0.x=fun1.x;
            fun0.y=fun1.y;
            Scoring(turn,cost);
        }
        turn^=1;
    }
    void Go(boolean isReplay) throws ChessException {
        InitialMap();
        SavePoint();
        int loop=0,lastScore=0;
        while(true)
        {
            Show();
            if(Math.max(players[0].score,players[1].score)>=60)break;
            Operation opt;
            if(isReplay)
            {
                boolean flag=Click();
                if(flag)opt=opt_stack.get(steps);
                else
                {
                    if(steps!=0){LoadPoint();Show();continue;}
                    else throw new ChessException("Invalid regret.\nError Code:307");
                }
            }
            else
            {
                opt=Input();
                if(opt.isLoad())
                {
                    if(steps!=0){LoadPoint();Show();continue;}
                    else throw new ChessException("Invalid regret.\nError Code:307");
                }
                if(opt.isSave())
                {
                    String dir;
                    try {
                        dir = FileOperation.GamePause(this);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Game pause file at "+dir);
                    continue;
                }
                if(!opt.isValid())throw new ChessException("Out of range.\nError Code:306");
                opt_stack.add(opt);
            }
            steps++;
            System.out.printf("%d %d %d %d\n",opt.x1,opt.y1,opt.x2,opt.y2);
            Point[] fun=new Point[2];
            fun[0]=players[map[opt.x1][opt.y1].player].pieces.chess[map[opt.x1][opt.y1].index];
            if(map[opt.x2][opt.y2].player==-1)
                fun[1]=new Point(opt.x2,opt.y2,0,true,true);
            else
                fun[1]=players[map[opt.x2][opt.y2].player].pieces.chess[map[opt.x2][opt.y2].index];
            if(fun[0]==fun[1])
                Go_Show(fun[0]);
            else
                Go_Move(fun[0],fun[1]);
            if(players[0].score+players[1].score==lastScore)loop++;
            else {lastScore=players[0].score+players[1].score;loop=0;}
            if(loop>=50)return;
            SavePoint();
        }
    }
    public void Init(Player Alice, Player Bob)
    {
        players[0]=Alice;
        players[1]=Bob;
        uuid=UUID.randomUUID();
        CreatePieces();
        turn=0;
        steps=0;
    }
    public String Play()
    {
        try {
            Go(false);
        } catch (ChessException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        System.out.println("Game over!");
        double p=1.0/(1.0+Math.pow(10,1.0*(players[1].rating-players[0].rating)/400));
        double sign;
        if(players[0].score<60 && players[1].score<60)
        {
            System.out.println("Draw!");
            sign=0.5*(players[0].rating<=players[1].rating ? 1 : -1);
        }
        else if(players[0].score>players[1].score)
        {
            System.out.println(players[0].id+" Win!");
            sign=1;
        }
        else
        {
            System.out.println(players[1].id+" Win!");
            sign=-1;
        }
        String guid=uuid.toString();
        System.out.println(guid);
        players[0].rating+=sign*20*(1-p);
        players[1].rating+=-1*sign*20*(1-p);
        System.out.println("Rating is updated!");
        for(int i=0;i<=1;i++)
            System.out.printf("id: %s rating: %d score: %d\n",players[i].id,players[i].rating,players[i].score);
        players[0].history.add(guid);
        players[1].history.add(guid);
        players[0].pieces=initPieces[0];
        players[1].pieces=initPieces[1];
        String dir;
        try {
            dir=FileOperation.SaveGame(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return dir;
    }
    void FormatCheck(String raw,String name) throws ChessException
    {
        String[] data=raw.split(Player.pause);
        if(!data[0].equals(name))throw new ChessException("Wrong UUID.\nError Code:301");
        for(int i=0;i<=1;i++)
            ChessPieces.FormatCheck(Arrays.asList(data).subList(4+i*19,20+i*19));
        int tmp=Integer.parseInt(data[39]);
        if(tmp!=0 && tmp!=1)throw new ChessException("No sufficient turn.\nError Code:304");
        int n=Integer.parseInt(data[40]);
        System.out.println(n);
        System.out.println(data.length);
        if(n+41!=data.length)throw new ChessException("Wrong options size.\nError Code:307");
    }
    public void LoadReplay(String raw,String name)
    {
        try {
            FormatCheck(raw,name);
        } catch (ChessException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        String[] data=raw.split(Player.pause);
        uuid=UUID.fromString(data[0]);
        for(int i=0;i<=1;i++)
        {
            String id=data[1+i*19];
            String isAI=data[2+i*19];
            players[i]=new Player(id,Integer.parseInt(isAI));
            players[i].rating=Integer.parseInt(data[3+i*19]);
            for(int j=0;j<=15;j++)
                players[i].pieces.chess[j]=new Point(Integer.parseInt(data[4+j+i*19]));
        }
        turn=Integer.parseInt(data[39]);
        int n=Integer.parseInt(data[40]);
        for(int i=1;i<=n;i++)
            opt_stack.add(new Operation(Integer.parseInt(data[40+i])));
        turn=0;
        steps=0;
    }
    public void Replay()
    {
        try {
            Go(true);
        } catch (ChessException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        System.out.println("Game over!");
        String guid=uuid.toString();
        System.out.println(guid);
        for(int i=0;i<=1;i++)
            System.out.printf("id: %s rating: %d score: %d\n",players[i].id,players[i].rating,players[i].score);
    }
    public void GameContinue(String raw,String name) throws ChessException {
        String[] Segment=raw.split(Player.BigPause);
        if(!Segment[0].equals(name))throw new ChessException("Wrong UUID.\nError Code:401");
        uuid=UUID.fromString(name);
        for(int i=0;i<=1;i++)
            players[i]=new Player();
        players[0].LoadGaming(Segment[1]);
        players[1].LoadGaming(Segment[2]);
        String[] data=Segment[3].split(Player.pause);
        turn=Integer.parseInt(data[0]);
        steps=Integer.parseInt(data[1]);
        int n=Integer.parseInt(data[2]);
        for(int i=1;i<=n;i++)
            opt_stack.add(new Operation(Integer.parseInt(data[2+i])));
    }
}