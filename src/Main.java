package src;
import ChessBoard.*;
import GUI.MainFrame;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main
{
    static void test0()
    {
        Player Alice=new Player("Celina",0);
        String dir;
        try {
            dir=FileOperation.SaveUser(Alice);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(dir);
    }
    static void test10()
    {
        Scanner input=new Scanner(System.in);
        String dir=input.next();
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
        String name;
        if(dir.contains("\\"))
            name=dir.substring(dir.lastIndexOf("\\")+1);
        else
            name=dir.substring(dir.lastIndexOf("/")+1);
        Bob.Load(user,name.substring(0,name.length()-4));
        Bob.Show();
    }
    static void test1()
    {
        ChessBoard game=new ChessBoard();
        Player pop=new Player("pop",0);
        try {
            game.NetworkInit("127.0.0.1",12345,1,pop);
        } catch (ChessException e) {
            throw new RuntimeException(e);
        }
        System.out.println(game.Play());
    }
    static void test2()
    {
        ChessBoard game=new ChessBoard();
        Player push=new Player("push",0);
        try {
            game.NetworkInit("10.24.86.56",12345,0,push);
        } catch (ChessException e) {
            throw new RuntimeException(e);
        }
        System.out.println(game.Play());
    }
    static void test3()
    {
        Player Tim=new Player("Tim",1);
        Player AI=new Player("AI",1);
        ChessBoard Game=new ChessBoard();
        Game.Init(Tim,AI);
        String dir=Game.Play();
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
        String name;
        if(dir.contains("\\"))
            name=dir.substring(dir.lastIndexOf("\\")+1);
        else
            name=dir.substring(dir.lastIndexOf("/")+1);
        try {
            Replay.LoadReplay(data,name.substring(0,name.length()-6));
        } catch (ChessException e) {
            throw new RuntimeException(e);
        }
        try {
            Replay.Replay();
        } catch (ChessException e) {
            throw new RuntimeException(e);
        }
    }
    static void test4()
    {
        ChessBoard Game=new ChessBoard();
        String dir,data,name;
        Scanner input=new Scanner(System.in);
        dir=input.next();
        try {
            data=FileOperation.Load(dir);
        } catch (ChessException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(dir.contains("\\"))
            name=dir.substring(dir.lastIndexOf("\\")+1);
        else
            name=dir.substring(dir.lastIndexOf("/")+1);
        try {
            Game.GameContinue(data,name.substring(0,name.length()-5));
        } catch (ChessException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        System.out.println("Save at"+Game.Play());
    }
    static void test5()
    {
        Player AI1=new Player("Easy",1);
        Player AI2=new Player("Beginner",4);
        ChessBoard Game=new ChessBoard();
        Game.Init(AI1,AI2);
        String dir=Game.Play();
        System.out.println(dir);
    }
    static void test6(){
        SwingUtilities.invokeLater(()->{
            JFrame mainFrame = new MainFrame("DarkChess");
            System.out.println(mainFrame);
        });
    }
    static void test7()
    {
        String passwd;
        Scanner input=new Scanner(System.in);
        passwd=input.next();
        Player Bob=new Player("Alice",0,passwd);
        passwd=input.next();
        System.out.println(Bob.login(passwd));
    }
    static void test8()
    {
        Player Alice=new Player("Alice",1);
        Player Bob=new Player("Bob",0);
        String dir;
        try {
            dir=FileOperation.SaveUser(Alice);
            System.out.println(dir);
            dir=FileOperation.SaveUser(Bob);
            System.out.println(dir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ArrayList<Player> list;
        try {
            list = FileOperation.ScanUser("User/");
        } catch (ChessException e) {
            throw new RuntimeException(e);
        }
        for(Player tmp:list)
            System.out.println(tmp.UserMsg());
    }
    static void test9()
    {
        RSCode rsCode=new RSCode();
        byte[] c={-19, 16, -119, -3, 75, -56, 79, 24, -85, 6, -35, -70, 108, 104, 120, -108, 88, -13, -114, -27, -10, 8, 57, 15, -67, 63, -113, -22, 101, 22, -17, 69, -68, 50, 20, -10, 89, 99, 0, 9, -28, -51, -116, 109, 122, 100, -96, 43, 83, -11, 1, -39, -101, -112, 74, -41, -84, 110, 54, 21, -76, 36, 49, -110, -86, 50, 83, 111, 67, 69, -59, -122, -103, 90, -85, -94, 111, -48, 49, 75, -79, -89, 31, -88, -11, -39, -118, -45, 14, -96, -1, 53, 33, -46, -126, -107};
        byte[] s= rsCode.Encode(c);
        System.out.println(Arrays.toString(s));
        byte[] a={-90, -75, 58, 121, -84, 32, -103, 78, -47, 59, 114, -121, 76, 70, -65, 92, -48, 106, -97, -32, -19, 16, -119, -3, 75, -56, 79, 24, -85, 6, -35, -70, 108, 104, 120, -108, 88, -13, -114, -27, -10, 8, 57, 15, -67, 63, -113, -22, 101, 22, -17, 69, -68, 50, 20, -10, 89, 99, 0, 9, -28, -51, -116, 109, 122, 100, -96, 43, 83, -11, 1, -39, -101, -112, 74, -41, -84, 110, 54, 21, -76, 36, 49, -110, -86, 50, 83, 111, 67, 69, -59, -122, -103, 90, -85, -94, 111, -48, 49, 75, -79, -89, 31, -88, -11, -39, -118, -45, 14, -96, -1, 53, 33, -46, -126, -107};
        byte[] b;
        try {
            b = rsCode.Decode(a)[0];
        } catch (ChessException e) {
            throw new RuntimeException(e);
        }
        System.out.println(Arrays.toString(b));
        try {
            b=rsCode.Decode(s)[0];
        } catch (ChessException e) {
            throw new RuntimeException(e);
        }
        System.out.println(Arrays.toString(b));
    }
    public static void main(String[] args)
    {
        Scanner input=new Scanner(System.in);
        int ack=0;
        while(ack==0)
        {
            System.out.print("input flag:");
            int flag=input.nextInt();
            switch (flag)
            {
                case 0 -> test0();
                case 1 -> test1();
                case 2 -> test2();
                case 3 -> test3();
                case 4 -> test4();
                case 5 -> test5();
                case 6 -> test6();
                case 7 -> test7();
                case 8 -> test8();
                case 9 -> test9();
                case 10 -> test10();
                default -> ack=1;
            }
        }
    }
}