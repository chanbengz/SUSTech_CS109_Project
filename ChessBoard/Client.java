package ChessBoard;
public class Client
{
    private static final String DEFAULT_HOST="127.0.0.1";
    private static final int DEFAULT_PORT=28088;
    private static ClientHandle clientHandle;
    public static void start() {
        start(DEFAULT_HOST, DEFAULT_PORT);
    }
    public static synchronized void start(String ip, int port)
    {
        System.out.println(ip+":"+port);
        stop();
        clientHandle=new ClientHandle(ip, port);
        new Thread(clientHandle,"DarkChessClient").start();
    }
    public static void stop()
    {
        if(clientHandle!=null)
            clientHandle.stop();
    }
    public static void sendMsg(byte[] msg) throws Exception
    {
        clientHandle.sendMsg(msg);
    }
}