package ChessBoard;

import java.util.ArrayList;
import java.util.Arrays;

public class RSCode
{
    private static final int BlockSize=255;
    private static final int CheckSize=10;
    private static final int Payload=235;
    private final int[] Trans=new int[BlockSize+1];
    private final int[] index=new int[BlockSize+1];
    private final int[] Ploy=new int[BlockSize-Payload+1];
    public RSCode()
    {
        generateGF();
        generatePoly();
    }
    public void generateGF()
    {
        int mask=1;
        Trans[8]=0;
        index[Trans[8]]=8;
        int[] tmp={1,0,1,1,1,0,0,0};
        for(int i=0;i<8;i++)
        {
            Trans[i]=mask;
            index[Trans[i]]=i;
            if(tmp[i]==1)
                Trans[8]^=mask;
            mask<<=1;
        }
        mask>>=1;
        for(int i=9;i<BlockSize;i++)
        {
            if(Trans[i-1]>=mask)
                Trans[i]=Trans[8]^((Trans[i-1]^mask)<<1);
            else
                Trans[i]=Trans[i-1]<<1;
            index[Trans[i]]=i;
        }
        index[0]=-1;
    }
    public void generatePoly()
    {
        Ploy[0]=2;
        Ploy[1]=1;
        for(int i=2;i<=BlockSize-Payload;i++)
        {
            Ploy[i]=1;
            for(int j=i-1;j>0;j--)
            {
                if(Ploy[j]!=0)
                    Ploy[j]=Ploy[j-1]^Trans[(index[Ploy[j]]+i)%BlockSize];
                else
                    Ploy[j]=Ploy[j-1];
            }
            Ploy[0]=Trans[(index[Ploy[0]]+i)%BlockSize];
        }
        for(int i=0;i<=BlockSize-Payload;i++)
            Ploy[i]=index[Ploy[i]];
    }
    public byte[] Encode(byte[] input)
    {
        System.out.println("rawE: "+ Arrays.toString(input));
        ArrayList<int[]> data=new ArrayList<>();
        int num=(input.length+Payload-1)/Payload;
        for(int i=0;i<num;i++)
        {
            int[] tmp;
            if(i<(num-1))
            {
                tmp=new int[Payload];
                for(int j=0;j<Payload;j++)
                    tmp[j]=input[j+i*Payload]&0xff;
            }
            else
            {
                tmp=new int[input.length-i*Payload];
                for(int j=0;j<input.length-i*Payload;j++)
                    tmp[j]=input[j+i*Payload]&0xff;
            }
            data.add(tmp);
        }
        byte[] ans=new byte[(num-1)*BlockSize+(BlockSize-Payload+input.length%Payload)];
        int tail=0;
        for(int[] tmp:data)
        {
            int[] tpm=EncodeBlock(tmp);
            for(int j=tail;j<tail+tpm.length;j++)
                ans[j]=(byte)tpm[j-tail];
            tail+=tpm.length;
        }
        return ans;
    }
    public int[] EncodeBlock(int[] input)
    {
        int[] data=new int[Payload];
        int[] CheckCode=new int[BlockSize-Payload];
        System.arraycopy(input, 0, data, 0, input.length);
        int back;
        for(int i=0;i<BlockSize-Payload;i++)
            CheckCode[i]=0;
        for(int i=Payload-1;i>=0;i--)
        {
            back=index[data[i]^CheckCode[BlockSize-Payload-1]];
            if(back!=-1)
            {
                for(int j=BlockSize-Payload-1;j>0;j--)
                {
                    if(Ploy[j]!=-1)
                        CheckCode[j]=CheckCode[j-1]^Trans[(Ploy[j]+back)%BlockSize];
                    else
                        CheckCode[j]=CheckCode[j-1];
                }
                CheckCode[0]=Trans[(Ploy[0]+back)%BlockSize];
            }
            else
            {
                for(int j=BlockSize-Payload-1;j>0;j--)
                    CheckCode[j]=CheckCode[j-1];
                CheckCode[0]=0;
            }
        }
        int[] ans=new int[BlockSize-Payload+input.length];
        System.arraycopy(CheckCode, 0, ans, 0, BlockSize - Payload);
        System.arraycopy(data, 0, ans, 20, input.length);
        return ans;
    }
    public byte[] Decode(byte[] input)
    {
        System.out.println("rawD: "+ Arrays.toString(input));
        ArrayList<int[]> data=new ArrayList<>();
        int num=(input.length+BlockSize-1)/BlockSize;
        for(int i=0;i<num;i++)
        {
            int[] tmp;
            if(i<(num-1))
            {
                tmp=new int[BlockSize];
                for(int j=0;j<BlockSize;j++)
                    tmp[j]=input[j+i*BlockSize]&0xff;
            }
            else
            {
                tmp=new int[input.length-i*BlockSize];
                for(int j=0;j<input.length-i*BlockSize;j++)
                    tmp[j]=input[j+i*BlockSize]&0xff;
            }
            data.add(tmp);
        }
        byte[] ans=new byte[(num-1)*Payload+input.length%BlockSize-(BlockSize-Payload)];
        int tail=0;
        for(int[] tmp:data)
        {
            int[] tpm=DecodeBlock(tmp);
            for(int i=tail;i<tail+tpm.length;i++)
                ans[i]=(byte)tpm[i-tail];
            tail+=tpm.length;
        }
        return ans;
    }
    public int[] DecodeBlock(int[] input)
    {
        int[] recd=new int[BlockSize];
        System.arraycopy(input, 0, recd, 0, input.length);

        int[][] elp=new int[BlockSize-Payload+2][BlockSize-Payload];
        int[] d=new int[BlockSize-Payload+2];
        int[] l=new int[BlockSize-Payload+2];
        int[] lu=new int[BlockSize-Payload+2];
        int[] s=new int[BlockSize-Payload+1];
        int[] z=new int[CheckSize+1];
        int[] err=new int[BlockSize];

        for(int i=0;i<BlockSize;i++)
            if(recd[i]==-1)
                recd[i]=0;
            else
                recd[i]=index[recd[i]];
        boolean flag=false;
        for(int i=1; i<=BlockSize-Payload; i++)
        {
            s[i]=0;
            for(int j=0;j<BlockSize;j++)
                if(recd[j]!=-1)
                    s[i]^=Trans[(recd[j]+i*j)%BlockSize];
            flag|=s[i]!=0;
            s[i]=index[s[i]];
        }
        System.out.println("isError: " + flag);
        if(flag)
        {
            d[0]=0;d[1]=s[1];
            elp[0][0]=0;elp[1][0]=1;
            for(int i=1;i<BlockSize-Payload;i++)
            {
                elp[0][i]=-1;
                elp[1][i]=0;
            }
            l[0]=0;l[1]=0;
            lu[0]=-1;lu[1]=0;
            int u=0;
            do
            {
                u++;
                if(d[u]==-1)
                {
                    l[u+1]=l[u];
                    for(int i=0;i<=l[u];i++)
                    {
                        elp[u+1][i]=elp[u][i];
                        elp[u][i]=index[elp[u][i]];
                    }
                }
                else
                {
                    int q=u-1;
                    while((d[q]==-1) && (q>0)) q--;
                    if(q>0)
                    {
                        int j=q;
                        do
                        {
                            j--;
                            if((d[j]!=-1) && (lu[q]<lu[j])) q=j;
                        }while(j>0);
                    }
                    l[u+1]=Math.max(l[u],l[q]+u-q);
                    for(int i=0;i<BlockSize-Payload;i++)
                        elp[u+1][i]=0;
                    for(int i=0;i<=l[q];i++)
                        if(elp[q][i]!=-1)
                            elp[u+1][i+u-q]=Trans[(d[u]+BlockSize-d[q]+elp[q][i])%BlockSize];
                    for(int i=0;i<=l[u];i++)
                    {
                        elp[u+1][i]^=elp[u][i];
                        elp[u][i]=index[elp[u][i]];
                    }
                }
                lu[u+1]=u-l[u+1];
                if(u<BlockSize-Payload)
                {
                    if(s[u+1]!=-1)
                        d[u+1]=Trans[s[u+1]];
                    else
                        d[u+1]=0;
                    for(int i=1;i<=l[u+1];i++)
                        if((s[u+1-i]!=-1) && (elp[u+1][i]!=0))
                            d[u+1]^=Trans[(s[u+1-i]+index[elp[u+1][i]])%BlockSize];
                    d[u+1]=index[d[u+1]];
                }
            }while((u<BlockSize-Payload) && (l[u+1]<=CheckSize));
            u++;
            System.out.println("Number of Error: " + l[u]);
            if(l[u]<=CheckSize)
            {
                for(int i=0;i<=l[u];i++)
                    elp[u][i]=index[elp[u][i]];
                int[] reg=new int[CheckSize+1];
                int[] loc=new int[CheckSize];
                int[] root=new int[CheckSize];
                if(l[u]>=0)System.arraycopy(elp[u],1,reg,1,l[u]);
                int count=0;
                for(int i=1;i<=BlockSize;i++)
                {
                    int q=1;
                    for(int j=1;j<=l[u];j++)
                        if(reg[j]!=-1)
                        {
                            reg[j]=(reg[j]+j)%BlockSize;
                            q^=Trans[reg[j]];
                        }
                    if(q==0)
                    {
                        root[count]=i;
                        loc[count]=BlockSize-i;
                        System.out.println("Error at: "+loc[count]);
                        count++;
                    }
                }
                if(count==l[u])
                {
                    for(int i=1;i<=l[u];i++)
                    {
                        if((s[i]!=-1) && elp[u][i]!=-1)
                            z[i]=Trans[s[i]]^Trans[elp[u][i]];
                        else if((s[i]!=-1) && (elp[u][i]==-1))
                            z[i]=Trans[s[i]];
                        else if((s[i]==-1) && (elp[u][i]!=-1))
                            z[i]=Trans[elp[u][i]] ;
                        else
                            z[i] = 0;
                        for(int j=1;j<i;j++)
                            if((s[j]!=-1) && (elp[u][i-j]!=-1))
                                z[i]^=Trans[(elp[u][i-j]+s[j])%BlockSize];
                        z[i]=index[z[i]];
                    }
                    for(int i=0;i<BlockSize;i++)
                    {
                        err[i]=0;
                        if(recd[i]!=-1)
                            recd[i]=Trans[recd[i]];
                        else
                            recd[i]=0;
                    }
                    for(int i=0;i<l[u];i++)
                    {
                        err[loc[i]]=1;
                        for(int j=1;j<=l[u];j++)
                            if(z[j]!=-1)
                                err[loc[i]]^=Trans[(z[j]+j*root[i])%BlockSize];
                        if(err[loc[i]]!=0)
                        {
                            err[loc[i]]=index[err[loc[i]]];
                            int q=0;
                            for(int j=0;j<l[u];j++)
                                if(j!=i)
                                    q+=index[1^Trans[(loc[j]+root[i])%BlockSize]];
                            q=q%BlockSize;
                            err[loc[i]]=Trans[(err[loc[i]]-q+BlockSize)%BlockSize];
                            recd[loc[i]]^=err[loc[i]];
                        }
                    }
                }
                else Restore(recd);
            }
            else Restore(recd);
        }
        else Restore(recd);
        int[] ans=new int[input.length-(BlockSize-Payload)];
        System.arraycopy(recd, 20, ans, 0, input.length - (BlockSize - Payload));
        return ans;
    }
    private void Restore(int[] recd)
    {
        for(int i=0;i<BlockSize;i++)
            if(recd[i]!=-1)
                recd[i]=Trans[recd[i]];
            else
                recd[i]=0;
    }
}