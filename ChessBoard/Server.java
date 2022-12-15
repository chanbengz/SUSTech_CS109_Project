package ChessBoard;
import java.io.IOException;
public class Server
{
    private static final int DEFAULT_PORT=28088;
    private static ServerHandle serverHandle;
    public static void start() {start(DEFAULT_PORT);}
    public static synchronized void start(int port)
    {
        stop();
        serverHandle=new ServerHandle(port);
    }
    public static void stop()
    {
        if(serverHandle!=null)
            serverHandle.stop();
    }
    public static void sendMsg(byte[] msg) throws IOException
    {
        serverHandle.sendMsg(msg);
    }
}