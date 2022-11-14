package src;
import ChessBoard.*;
import java.io.IOException;
import java.util.Scanner;

public class Main
{
    static void test0()
    {
        Player Alice=new Player("Alice",false);
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
        } catch (ChessException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String name=dir.substring(dir.lastIndexOf("/")+1);
        Bob.Load(user,name.substring(0,name.length()-4));
        Bob.Show();
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
    static void test3()
    {
        Player Tim=new Player("Tim",false);
        Player AI=new Player("AI",true);
        ChessBoard Game=new ChessBoard();
        String dir=Game.Play(Tim,AI);
        ChessBoard Replay=new ChessBoard();
        String data;
        try {
            data=FileOperation.Load(dir);
        } catch (ChessException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Replay.Load(data);
        Replay.Replay();
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
            case 3 -> test3();
        }
    }
}