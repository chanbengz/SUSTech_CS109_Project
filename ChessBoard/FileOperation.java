package ChessBoard;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.UUID;

public class FileOperation
{
    public static SecretKeySpec GenerateKey(UUID uuid) throws NoSuchAlgorithmException {
        String password=uuid.toString();
        KeyGenerator gen=KeyGenerator.getInstance("AES");
        SecureRandom secRandom=SecureRandom.getInstance("SHA1PRNG");
        secRandom.setSeed(password.getBytes());
        gen.init(128,secRandom);
        SecretKey secretKey=gen.generateKey();
        return new SecretKeySpec(secretKey.getEncoded(), "AES");
    }
    static byte[] Encrypt(String data, UUID uuid) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher=Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE,GenerateKey(uuid));
        RSCode rsCode=new RSCode();
        return Base64.getEncoder().encode(rsCode.Encode(cipher.doFinal(data.getBytes())));
    }
    static String Decrypt(String data, UUID uuid) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, ChessException {
        Cipher cipher=Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE,GenerateKey(uuid));
        RSCode rsCode=new RSCode();
        byte[][] tmp=rsCode.Decode(Base64.getDecoder().decode(data));
        String tips=new String(tmp[1]);
        if(!tips.equals("qwq"))
            JOptionPane.showMessageDialog(null,tips,"Warning",JOptionPane.WARNING_MESSAGE);
        if(tips.contains("Too"))
            throw new ChessException(tips);
        return new String(cipher.doFinal(tmp[0]));
    }
    public static String SaveGame(ChessBoard game) throws IOException {
        StringBuilder data= new StringBuilder(game.uuid.toString()+Player.pause);
        data.append(game.players[0].Msg()).append(game.players[1].Msg());
        data.append(game.turn).append(Player.pause);
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
            fos.write((Encrypt(data.toString(),game.uuid)));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }
        fos.close();
        return dirFile;
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
            fos.write(Encrypt(Alice.UserMsg(),Alice.uuid));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }
        fos.close();
        return dirFile;
    }
    public static String Load(String dir) throws ChessException, IOException {
        Path path=Paths.get(dir);
        String input=Files.readString(path);
        String name;
        if(dir.contains("\\"))
            name=dir.substring(dir.lastIndexOf("\\")+1);
        else
            name=dir.substring(dir.lastIndexOf("/")+1);
        System.out.println(name);
        UUID uuid;
        if(name.contains(".chess"))uuid=UUID.fromString(name.substring(0,name.indexOf(".chess")));
        else if(name.contains(".usr"))uuid=UUID.nameUUIDFromBytes(name.substring(0,name.indexOf(".usr")).getBytes());
        else if(name.contains(".game"))uuid=UUID.fromString(name.substring(0,name.indexOf(".game")));
        else throw new ChessException("Invalid File Format.\nError Code:101");
        try {
            return Decrypt(input,uuid);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }
    public static String GamePause(ChessBoard game) throws IOException {
        StringBuilder data= new StringBuilder(game.uuid.toString()+Player.BigPause);
        data.append(game.players[0].GamingMsg()).append(Player.BigPause);
        data.append(game.players[1].GamingMsg()).append(Player.BigPause);
        data.append(game.turn).append(Player.pause);
        data.append(game.steps).append(Player.pause);
        int n=game.opt_stack.size();
        data.append(n).append(Player.pause);
        for(Operation tmp:game.opt_stack)
            data.append(tmp.transfer()).append(Player.pause);
        String dir="Save/";
        File dictionary=new File(dir);
        if(!dictionary.exists())
            if(!dictionary.mkdir())
                throw new IOException();
        String dirFile=dir+game.uuid.toString()+".game";
        File file=new File(dirFile);
        if(!file.exists())
            if(!file.createNewFile())
                throw new IOException();
        FileOutputStream fos=new FileOutputStream(file);
        try {
            fos.write(Encrypt(data.toString(),game.uuid));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }
        fos.close();
        return dirFile;
    }
    public static void NetWrite(byte[] buf) throws IOException {
        String dirFile="Network.tmp";
        File file=new File(dirFile);
        if(!file.exists())
            if(!file.createNewFile())
                throw new IOException();
        FileOutputStream fos=new FileOutputStream(file);
        fos.write(buf);
        fos.close();
    }
    public static String NetRead(UUID uuid) throws IOException, ChessException {
        Path path=Paths.get("Network.tmp");
        String input=Files.readString(path);
        if(input.equals("qwq"))return null;
        String dirFile="Network.tmp";
        File file=new File(dirFile);
        FileWriter fileWriter=new FileWriter(file);
        fileWriter.write("qwq");
        fileWriter.flush();
        fileWriter.close();
        try {
            return Decrypt(input,uuid);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }
    public static byte[] NetSend(String msg,UUID uuid) throws RuntimeException {
        try {
            return Encrypt(msg,uuid);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }
    public static String NetReceive(UUID uuid) throws IOException, InterruptedException, ChessException {
        int cnt=150;
        while(cnt>=0)
        {
            String msg;
            msg=FileOperation.NetRead(uuid);
            if(msg==null)
                Thread.sleep(1000);
            else
                return msg;
            cnt--;
        }
        throw new ChessException("Connection error: Time out.");
    }
    public static ArrayList<Player> ScanUser(String dir) throws ChessException {
        ArrayList<Player> list=new ArrayList<>();
        File file=new File(dir);
        File[] fs=file.listFiles();
        if(fs==null)return list;
        for(File f:fs)
            if(!f.isDirectory())
            {
                String name=f.getName();
                if(!name.contains(".usr"))continue;
                UUID uuid=UUID.nameUUIDFromBytes(name.substring(0,name.indexOf(".usr")).getBytes());
                try {
                    BufferedReader reader=new BufferedReader(new FileReader(f));
                    String input=reader.readLine();
                    String data=Decrypt(input,uuid);
                    Player tmp=new Player();
                    tmp.Load(data,data.split(Player.pause)[0]);
                    list.add(tmp);
                } catch (IOException | NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException |
                         BadPaddingException | InvalidKeyException e) {
                    throw new RuntimeException(e);
                }
            }
        return list;
    }
}