package ChessBoard;

import java.util.*;

public class ChessBoard
{
    //本机生成
    static class Pair
    {
        int player,index;
        public Pair(int player, int index)
        {
            this.player=player;
            this.index=index;
        }
    }
    Pair[][] map=new Pair[10][10];
    static int turn;
    Player[] players=new Player[2];
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
        this.players[0].pieces.init(RedStart);
        this.players[1].pieces.init(BlackStart);
    }
    int Move(Point piece,Point dest)
    {
        if(piece.x==dest.x && piece.y==dest.y)
        {
            piece.show=true;
            return 0;
        }
        return dest.level;
    }
    void InitialMap(int player)
    {
        for(int i=0;i<=15;i++)
            map[this.players[player].pieces.chess[i].x][this.players[player].pieces.chess[i].y]=new Pair(player, i);
    }
    public void Show()
    {
        System.out.printf("Turn: %d\n",turn);
        System.out.printf("Score: %d : %d\n",this.players[0].score,players[1].score);
        for(int i=1;i<=8;i++,System.out.println())
            for(int j=1;j<=4;j++)
            {
                int who=map[i][j].player;
                if(who==-1)System.out.printf("%2d ",-1);
                else
                {
                    int value=(int)Math.pow(-1,who)*this.players[who].pieces.chess[map[i][j].index].level;
                    System.out.printf("%2d ",value);
                }
            }
        System.out.println("-----------------------");
        for(int i=1;i<=8;i++,System.out.println())
            for(int j=1;j<=4;j++)
            {
                int who=map[i][j].player;
                if(who==-1)System.out.printf("%2d ",-1);
                else
                {
                    if(this.players[who].pieces.chess[map[i][j].index].show)
                    {
                        int value=(int)Math.pow(-1,who)*this.players[who].pieces.chess[map[i][j].index].level;
                        System.out.printf("%2d ",value);
                    }
                    else System.out.printf("%2d ",0);
                }
            }
    }
    void Input(Point[] fun)
    {
        Scanner input=new Scanner(System.in);
        System.out.println("Input: src dest");
        int x1=input.nextInt(),y1=input.nextInt(),x2=input.nextInt(),y2=input.nextInt();
        fun[0]=this.players[map[x1][y1].player].pieces.chess[map[x1][y1].index];
        if(map[x2][y2].player==-1)
            fun[1]=new Point(x2,y2,0,true,true);
        else
            fun[1]=this.players[map[x2][y2].player].pieces.chess[map[x2][y2].index];
    }
    void Scoring(int player,int cost)
    {
        Player tmp=this.players[player];
        switch(cost)
        {
            case 1-> tmp.score+=30;
            case 2-> tmp.score+=10;
            case 3, 4, 5, 7 -> tmp.score+=5;
            case 6-> tmp.score+=1;
        }
        this.players[player]=tmp;
    }
    public void Play()
    {
        for(int i=0;i<=1;i++)
            players[i]=new Player();
        CreatePieces();
        for(int i=0;i<=1;i++)
            InitialMap(i);
        turn=0;
        while(true)
        {
            Show();
            if(Math.max(this.players[0].score,this.players[1].score)>=60)break;
            Point[] fun=new Point[2];
            for(int i=0;i<=1;i++)
                fun[i]=new Point(0,0,0,false,false);
            Input(fun);
            if(fun[0].level==7)
            {
                if(fun[0]==fun[1])
                {
                    this.players[turn].pieces.chess[map[fun[0].x][fun[0].y].index].show=true;
                }
                else
                {
                    map[fun[1].x][fun[1].y].player=-1;
                    int cost=fun[1].level;
                    Scoring(turn,cost);
                }
            }
            else
            {
                int cost=Move(fun[0],fun[1]);
                int TmpPlayer=map[fun[0].x][fun[0].y].player;
                int TmpIndex=map[fun[0].x][fun[0].y].index;
                map[fun[0].x][fun[0].y].player=-1;
                map[fun[1].x][fun[1].y].player=TmpPlayer;
                map[fun[1].x][fun[1].y].index=TmpIndex;
                this.players[turn].pieces.chess[map[fun[1].x][fun[1].y].index]=fun[0];
                Scoring(turn,cost);
            }
            turn^=1;
            //SavePoint();
        }
    }
    //文件读写
    //网络读写
}