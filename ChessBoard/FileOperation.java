package ChessBoard;
import java.io.*;
public class FileOperation
{
    public static String SaveGame(ChessBoard game) throws IOException {
        String dir="Save/";
        dir+=game.uuid.toString();
        dir+=".chess";
        File file=new File(dir);
        if(!file.exists())
            if(!file.createNewFile())
                throw new IOException();
        FileOutputStream fos=new FileOutputStream(file);
        fos.write((game.uuid.toString()+Player.pause).getBytes());
        for(int i=0;i<=1;i++)
            fos.write(game.players[i].Msg().getBytes());
        int n=game.opt_stack.size();
        fos.write((n+Player.pause).getBytes());
        for(Operation tmp:game.opt_stack)
            fos.write((tmp.transfer()+Player.pause).getBytes());
        fos.close();
        return dir;
    }
    public static String SaveUser(Player Alice) throws IOException {
        String dir="User/";
        File dictionary=new File(dir);
        if(!dictionary.exists())
            if(!dictionary.mkdir())
                throw new IOException();
        dir+=Alice.id+".usr";
        File file=new File(dir);
        if(!file.exists())
            if(!file.createNewFile())
                throw new IOException();
        FileOutputStream fos=new FileOutputStream(file);
        fos.write(Alice.UserMsg().getBytes());
        fos.close();
        return dir;
    }
    public static String Load(String dir) throws IOException {
        File file=new File(dir);
        FileInputStream fis=new FileInputStream(file);
        byte[] buf=new byte[1000];
        StringBuilder input=new StringBuilder();
        while(fis.read(buf,0, buf.length)!=-1)
            input.append(new String(buf));
        fis.close();
        return input.toString();
    }
}