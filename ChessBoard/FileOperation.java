package ChessBoard;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

public class FileOperation
{
    static String Encrypt(String data, UUID uuid) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String password=uuid.toString();
        KeyGenerator gen=KeyGenerator.getInstance("AES");
        gen.init(128,new SecureRandom(password.getBytes()));
        SecretKey secretKey=gen.generateKey();
        SecretKeySpec key=new SecretKeySpec(secretKey.getEncoded(), "AES");
        Cipher cipher=Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE,key);
        return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
    }
    static String Decrypt(String data, UUID uuid)
    {

    }
    public static String SaveGame(ChessBoard game) throws IOException {
        StringBuilder data= new StringBuilder();
        data.append(game.uuid.toString()).append(Player.pause);
        data.append(game.players[0].Msg()).append(game.players[1].Msg());
        int n=game.opt_stack.size();
        data.append(n).append(Player.pause);
        for(Operation tmp:game.opt_stack)
            data.append(tmp.transfer()).append(Player.pause);
        String dir="Save/";
        File dictionary=new File(dir);
        if(!dictionary.exists())
            if(!dictionary.mkdir())
                throw new IOException();
        String dirFile=dir+game.uuid.toString()+".chess";
        File file=new File(dirFile);
        if(!file.exists())
            if(!file.createNewFile())
                throw new IOException();
        FileOutputStream fos=new FileOutputStream(file);
        try {
            fos.write((Encrypt(data.toString(),game.uuid)).getBytes());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }
        fos.close();
        return dir;
    }
    public static String SaveUser(Player Alice) throws IOException {
        String dir="User/";
        File dictionary=new File(dir);
        if(!dictionary.exists())
            if(!dictionary.mkdir())
                throw new IOException();
        String dirFile=dir+Alice.id+".usr";
        File file=new File(dirFile);
        if(!file.exists())
            if(!file.createNewFile())
                throw new IOException();
        FileOutputStream fos=new FileOutputStream(file);
        try {
            fos.write(Encrypt(Alice.UserMsg(),Alice.uuid).getBytes());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }
        fos.close();
        return dirFile;
    }
    public static String Load(String dir) throws IOException {
        File file=new File(dir);
        FileInputStream fis=new FileInputStream(file);
        byte[] buf=new byte[1000];
        StringBuilder input=new StringBuilder();
        while(fis.read(buf,0, buf.length)!=-1)
            input.append(new String(buf));
        fis.close();
        String name=dir.substring(dir.lastIndexOf("/")+1);
        UUID uuid;
        if(name.contains(".chess"))uuid=UUID.fromString(name.substring(0,name.indexOf(".chess")));
        else uuid=UUID.nameUUIDFromBytes(name.substring(0,name.indexOf(".usr")).getBytes());
        return Decrypt(input.toString(),uuid);
    }
}