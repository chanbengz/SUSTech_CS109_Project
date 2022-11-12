package src;
import ChessBoard.*;

import java.io.IOException;

public class Main
{
    public static void main(String[] args)
    {
        Player Alice=new Player("Alice");
        String dir;
        try {
            dir=FileOperation.SaveUser(Alice);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Player Bob=new Player();
        String user;
        try {
            user = FileOperation.Load(dir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Bob.Load(user);
        String userid="Tim";
        Player Tim=new Player(userid);
        ChessBoard Game=new ChessBoard();
        dir=Game.Play(Bob,Tim);
        ChessBoard Replay=new ChessBoard();
        String data;
        try {
            data=FileOperation.Load(dir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Replay.Load(data);
        Replay.Replay();
    }
}