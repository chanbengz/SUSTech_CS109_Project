package ChessBoard;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
public class ServerHandle implements Runnable
{
    private Selector selector;
    private volatile boolean started;
    public SocketChannel sc;
    public ServerHandle(int port)
    {
        try
        {
            selector=Selector.open();
            ServerSocketChannel serverChannel=ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            serverChannel.socket().bind(new InetSocketAddress(port),1024);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            started=true;
            System.out.println("Server starting at：" + port);
        }
        catch(IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }
    public void stop() {started=false;}
    public void run()
    {
        while(started)
        {
            try
            {
                selector.select(1000);
                Set<SelectionKey> keys=selector.selectedKeys();
                Iterator<SelectionKey> it=keys.iterator();
                SelectionKey key;
                while(it.hasNext())
                {
                    key=it.next();
                    it.remove();
                    try {handleInput(key);}
                    catch(Exception e)
                    {
                        if(key!=null)
                        {
                            key.cancel();
                            if(key.channel()!=null)
                                key.channel().close();
                        }
                    }
                }
            }
            catch(Throwable t){t.printStackTrace();}
        }
        if(selector!=null)
            try {selector.close();}
            catch (Exception e) {e.printStackTrace();}
    }
    private void handleInput(SelectionKey key) throws IOException
    {
        if(key.isValid())
        {
            if(key.isAcceptable())
            {
                ServerSocketChannel ssc=(ServerSocketChannel)key.channel();
                sc=ssc.accept();
                sc.configureBlocking(false);
                sc.register(selector, SelectionKey.OP_READ);
            }
            if(key.isReadable())
            {
                sc=(SocketChannel)key.channel();
                ByteBuffer buffer=ByteBuffer.allocate(1024);
                int readBytes=sc.read(buffer);
                if(readBytes>0)
                {
                    buffer.flip();
                    byte[] bytes=new byte[buffer.remaining()];
                    buffer.get(bytes);
                    FileOperation.NetWrite(bytes);
                }
                else if(readBytes<0)
                {
                    key.cancel();
                    sc.close();
                }
            }
        }
    }
    public void doWrite(SocketChannel channel,byte[] msg) throws IOException
    {
        ByteBuffer writeBuffer=ByteBuffer.allocate(msg.length);
        writeBuffer.put(msg);
        writeBuffer.flip();
        channel.write(writeBuffer);
    }
    public void sendMsg(byte[] msg) throws IOException
    {
        doWrite(sc,msg);
    }
}