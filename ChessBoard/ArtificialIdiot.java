package ChessBoard;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

public class ArtificialIdiot
{
    int turn;
    int[][] map=new int[10][10];
    int[] dx={-1,1,0,0},dy={0,0,-1,1};
    boolean Check(int x,int y)
    {
        return 1<=x && x<=8 && 1<=y && y<=4;
    }
    boolean Movable(int x,int y)
    {
        for(int k=0;k<=3;k++)
        {
            int nx=x+dx[k],ny=y+dy[k];
            if(!Check(nx,ny))continue;
            if(map[nx][ny]==9)return true;
        }
        return false;
    }
    public void LoadMap(ChessBoard game)
    {
        turn=game.turn;
        for(int i=1;i<=8;i++)
            for(int j=1;j<=4;j++)
            {
                int who=game.map[i][j].player;
                if(who==-1)map[i][j]=9;
                else if(!game.players[who].pieces.chess[game.map[i][j].index].show)map[i][j]=0;
                else map[i][j]=(int)Math.pow(-1,who)*game.players[who].pieces.chess[game.map[i][j].index].level;
            }
    }
    Operation Attack(int x,int y,int ndx,int ndy,int cost1)
    {
        Operation opt1=null;
        for(int nx=x+ndx,ny=y+ndy,tot=0;Check(nx,ny);nx+=ndx,ny+=ndy)
        {
            if(map[nx][ny]==9)continue;
            if(tot==0){tot++;continue;}
            if(tot==1 && map[nx][ny]*turn<0)
            {
                if(map[nx][ny]<cost1)
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
                if(map[i][j]==9)continue;
                if(map[i][j]*turn<0)
                {
                    int nl=Math.abs(map[i][j]),ndis=Math.abs(x-i)+Math.abs(y-j);
                    if(nl<tl || (nl==tl && ndis<dis))
                    {
                        tl=Math.abs(map[i][j]);
                        tx=i;
                        ty=j;
                        dis=ndis;
                    }
                }
            }
        int nx=x+(tx<x ? -1 : 1),ny=y+(ty<y ? -1 : 1);
        if(map[nx][y]==9)return new Operation(x,y,nx,y);
        if(map[x][ny]==9)return new Operation(x,y,x,ny);
        for(int k=0;k<=3;k++)
        {
            int ax=x+dx[k],ay=y+dy[k];
            if(!Check(ax,ay))continue;
            if(map[ax][ay]==9)return new Operation(x,y,ax,ay);
        }
        return null;
    }
    public Operation Easy()
    {
        turn=-2*turn+1;
        int cost1=10;
        Operation opt=null;
        for(int i=1;i<=8;i++)
            for(int j=1;j<=4;j++)
                if(map[i][j]!=9 && map[i][j]*turn>0)
                {
                    int level=Math.abs(map[i][j]);
                    if(level==7)
                    {
                        for(int k=0;k<=3;k++)
                        {
                            Operation opt2=Attack(i,j,dx[k],dy[k],cost1);
                            if(opt2!=null)
                            {
                                cost1=Math.abs(map[opt2.x2][opt2.y2]);
                                opt=opt2;
                            }
                        }
                    }
                    else
                    {
                        for(int k=0;k<=3;k++)
                        {
                            int nx=i+dx[k],ny=j+dy[k];
                            if(!Check(nx,ny))continue;
                            if(map[nx][ny]!=9 && map[nx][ny]*turn<0)
                            {
                                if(level==6)
                                {
                                    if(Math.abs(map[nx][ny])==1)
                                    {
                                        cost1=Math.abs(map[nx][ny]);
                                        opt=new Operation(i,j,nx,ny);
                                    }
                                }
                                else
                                {
                                    if(level<Math.abs(map[nx][ny]))
                                    {
                                        if(Math.abs(map[nx][ny])<cost1)
                                        {
                                            cost1=Math.abs(map[nx][ny]);
                                            opt=new Operation(i,j,nx,ny);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
        if(cost1!=10)return opt;
        LinkedList<Operation> hide=new LinkedList<>();
        for(int i=1;i<=8;i++)
            for(int j=1;j<=4;j++)
                if(map[i][j]==0)
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
                if(map[i][j]==9)continue;
                if(map[i][j]*turn>0)
                {
                    if(Math.abs(map[i][j])<level && Movable(i,j))
                    {
                        level=Math.abs(map[i][j]);
                        x=i;
                        y=j;
                    }
                }
            }
        opt=Search(x,y);
        turn=(1-turn)/2;
        return opt;
    }
}