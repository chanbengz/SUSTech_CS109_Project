package src;
import ChessBoard.*;
import java.io.IOException;
import java.util.Scanner;

public class Main
{
    static void test0()
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
    static void test1()
    {
        Network net=new Network();
        Scanner input=new Scanner(System.in);
        System.out.print("Port: ");
        int port=input.nextInt();
        try {
            net.ServerSetup(port);
            net.Send("Server");
            String data=net.Receive();
            System.out.println(data);
            net.ServerShutdown();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    static void test2()
    {
        Network net=new Network();
        Scanner input=new Scanner(System.in);
        System.out.println("IP Port");
        String ip=input.next();
        int port=input.nextInt();
        try {
            net.ClientSetup(ip,port);
            net.Send("Client");
            String data=net.Receive();
            System.out.println(data);
            net.ClientShutdown();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args)
    {
        Scanner input=new Scanner(System.in);
        int flag=input.nextInt();
        switch (flag)
        {
            case 0 -> test0();
            case 1 -> test1();
            case 2 -> test2();
        }
    }
}