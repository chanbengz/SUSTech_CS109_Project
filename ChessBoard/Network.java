package ChessBoard;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Network
{
    ServerSocket server;
    Socket app;
    public void ServerSetup(int port) throws IOException
    {
        server=new ServerSocket(port);
        System.out.printf("Server has created. Listening at %d...\n",port);
        app=server.accept();
    }
    public String Receive() throws IOException
    {
        DataInputStream in=new DataInputStream(app.getInputStream());
        byte[] buf=new byte[1000];
        StringBuilder input=new StringBuilder();
        while(in.read(buf,0,buf.length)!=1)
            input.append(new String(buf));
        in.close();
        return input.toString();
    }
    public void Send(String data) throws IOException
    {
        DataOutputStream out=new DataOutputStream(app.getOutputStream());
        out.write(data.getBytes());
        out.close();
    }
    public void ClientSetup(String ip,int port) throws IOException
    {
        System.out.printf("Connecting to %s at %d\n",ip,port);
        app=new Socket(ip,port);
    }
    public void ServerShutdown() throws IOException
    {
        app.close();
        server.close();
    }
    public void ClientShutdown() throws IOException
    {
        app.close();
    }
}