package ChessBoard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

public class ArtificialIdiot//change isAI tag
{
    int[] dx={-1,1,0,0},dy={0,0,-1,1};
    static class State
    {
        int turn;
        int[][] map=new int[10][10];
        public State(Cache game)
        {
            this.turn=game.turn;
            for(int i=1;i<=8;i++)
                for(int j=1;j<=4;j++)
                {
                    int who=game.map[i][j].player;
                    if(who==-1)this.map[i][j]=9;
                    else if(!game.players[who].pieces.chess[game.map[i][j].index].show)this.map[i][j]=0;
                    else this.map[i][j]=(int)Math.pow(-1,who)*game.players[who].pieces.chess[game.map[i][j].index].level;
                }
        }
    }
    State init;
    ChessBoard background;
    boolean Check(int x,int y)
    {
        return 1<=x && x<=8 && 1<=y && y<=4;
    }
    public void LoadMap(Cache game)
    {
        init=new State(game);
    }
    public void LoadMap(ChessBoard board)
    {
        background=new ChessBoard();
        background.Init(board);
    }
    boolean Movable(int x,int y)
    {
        for(int k=0;k<=3;k++)
        {
            int nx=x+dx[k],ny=y+dy[k];
            if(!Check(nx,ny))continue;
            if(init.map[nx][ny]==9)return true;
        }
        return false;
    }
    Operation Attack(int x,int y,int ndx,int ndy,int cost1)
    {
        Operation opt1=null;
        for(int nx=x+ndx,ny=y+ndy,tot=0;Check(nx,ny);nx+=ndx,ny+=ndy)
        {
            if(init.map[nx][ny]==9)continue;
            if(tot==0){tot++;continue;}
            if(tot==1 && init.map[nx][ny]*init.turn<0)
            {
                if(init.map[nx][ny]<cost1)
                    opt1=new Operation(x,y,nx,ny);
                break;
            }
        }
        return opt1;
    }
    Operation Search(int x,int y)
    {
        int tx=0,ty=0,tl=10,dis=100;
        for(int i=1;i<=8;i++)
            for(int j=1;j<=4;j++)
            {
                if(init.map[i][j]==9)continue;
                if(init.map[i][j]*init.turn<0)
                {
                    int nl=Math.abs(init.map[i][j]),ndis=Math.abs(x-i)+Math.abs(y-j);
                    if(nl<tl || (nl==tl && ndis<dis))
                    {
                        tl=Math.abs(init.map[i][j]);
                        tx=i;
                        ty=j;
                        dis=ndis;
                    }
                }
            }
        int nx=x+(tx<x ? -1 : 1),ny=y+(ty<y ? -1 : 1);
        if(init.map[nx][y]==9)return new Operation(x,y,nx,y);
        if(init.map[x][ny]==9)return new Operation(x,y,x,ny);
        for(int k=0;k<=3;k++)
        {
            int ax=x+dx[k],ay=y+dy[k];
            if(!Check(ax,ay))continue;
            if(init.map[ax][ay]==9)return new Operation(x,y,ax,ay);
        }
        return null;
    }
    public Operation Easy()
    {
        init.turn=-2*init.turn+1;
        int cost1=10;
        Operation opt=null;
        for(int i=1;i<=8;i++)
            for(int j=1;j<=4;j++)
                if(init.map[i][j]!=9 && init.map[i][j]*init.turn>0)
                {
                    int level=Math.abs(init.map[i][j]);
                    if(level==7)
                    {
                        for(int k=0;k<=3;k++)
                        {
                            Operation opt2=Attack(i,j,dx[k],dy[k],cost1);
                            if(opt2!=null)
                            {
                                cost1=Math.abs(init.map[opt2.x2][opt2.y2]);
                                opt=opt2;
                            }
                        }
                    }
                    else
                    {
                        for(int k=0;k<=3;k++)
                        {
                            int nx=i+dx[k],ny=j+dy[k],nl=Math.abs(init.map[nx][ny]);
                            if(!Check(nx,ny) || init.map[nx][ny]==9 || init.map[nx][ny]*init.turn>=0)continue;
                            if((level==6 && nl==1) || (level<nl && nl<cost1 && (level!=1 || nl<=5)))
                            {
                                cost1=nl;
                                opt=new Operation(i,j,nx,ny);
                            }
                        }
                    }
                }
        if(cost1!=10)return opt;
        LinkedList<Operation> hide=new LinkedList<>();
        for(int i=1;i<=8;i++)
            for(int j=1;j<=4;j++)
                if(init.map[i][j]==0)
                    hide.add(new Operation(i,j,i,j));
        if(!hide.isEmpty())
        {
            Collections.shuffle(hide, new Random());
            return hide.getFirst();
        }
        int level=10,x=0,y=0;
        for(int i=1;i<=8;i++)
            for(int j=1;j<=4;j++)
            {
                if(init.map[i][j]==9)continue;
                if(init.map[i][j]*init.turn>0)
                {
                    if(Math.abs(init.map[i][j])<level && Movable(i,j))
                    {
                        level=Math.abs(init.map[i][j]);
                        x=i;
                        y=j;
                    }
                }
            }
        opt=Search(x,y);
        init.turn=(1-init.turn)/2;
        return opt;
    }
    static class Value
    {
        int score;
        Operation opt;
        public Value(int score,Operation opt)
        {
            this.score=score;
            this.opt=opt;
        }
    }
    static class Pig
    {
        int x,y;
        public Pig(int x,int y)
        {
            this.x=x;
            this.y=y;
        }
    }
    public Value Alpha_Beta(ChessBoard now,int alpha,int beta)
    {
        if(Math.max(now.players[0].score,now.players[1].score)>=60)
        {
            int score=(now.players[0].score>now.players[1].score ? 1 : -1)*(background.turn==0 ? 1 : -1)*90;
            Operation opt=new Operation(-1,-1,-1,-1);
            return new Value(score,opt);
        }
        Value ans;
        ArrayList<Pig> buf=new ArrayList<>();
        State game=new State(new Cache(now.turn,now.map, now.players));
        for(int i=1;i<=8;i++)
            for(int j=1;j<=4;j++)
            {
                if(game.map[i][j]*now.turn<0 || game.map[i][j]==9)continue;
                buf.add(new Pig(i,j));
            }
        Collections.shuffle(buf, new Random());
        if(now.turn==background.turn)
        {
            for(Pig tmp:buf)
            {
                int level=Math.abs(game.map[tmp.x][tmp.y]);
                if(level==0)
                {
                    ChessBoard next=new ChessBoard();
                    next.Init(now);
                    next.Go_Show(next.players[next.map[tmp.x][tmp.y].player].pieces.chess[next.map[tmp.x][tmp.y].index]);
                    if(alpha<0)
                    {
                        alpha=0;
                        ans=new Value(alpha,new Operation(tmp.x,tmp.y,tmp.x,tmp.y));
                    }
                }
                else
                {
                    int cost1=10;
                    Operation opt;
                    for(int k=0;k<=3;k++)
                    {
                        int nx=tmp.x+dx[k],ny=tmp.y+dy[k],nl=Math.abs(game.map[nx][ny]);
                        if(!Check(nx,ny) || game.map[nx][ny]==9 || game.map[nx][ny]*game.turn>=0)continue;
                        if((level==6 && nl==1) || (level<nl && nl<cost1 && (level!=1 || nl<=5)))
                        {
                            cost1=nl;
                            opt=new Operation(tmp.x,tmp.y,nx,ny);
                        }
                    }
                    if(cost1!=10)
                    {

                    }
                }
                if(alpha>=beta)break;
            }
            return ans;
        }
        else
        {
            return ans;
        }
    }
    public Operation Normal()
    {
        return Alpha_Beta(background,-100,100).opt;
    }
}