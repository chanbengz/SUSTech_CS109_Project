package ChessBoard;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
public class ClientHandle implements Runnable
{
    private final String host;
    private final int port;
    private Selector selector;
    private SocketChannel socketChannel;
    private volatile boolean started;
    public ClientHandle(String ip,int port)
    {
        this.host=ip;
        this.port=port;
        try
        {
            selector=Selector.open();
            socketChannel=SocketChannel.open();
            socketChannel.configureBlocking(false);
            started=true;
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
        try {doConnect();}
        catch(IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        while(started)
        {
            try
            {
                selector.select(1000);
                Set<SelectionKey> keys=selector.selectedKeys();
                Iterator<SelectionKey> it=keys.iterator();
                SelectionKey key;
                while(it.hasNext()){
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
            catch(Exception e)
            {
                e.printStackTrace();
                System.exit(1);
            }
        }
        if(selector!=null)
            try {selector.close();}
            catch (Exception e) {e.printStackTrace();}
    }
    private void handleInput(SelectionKey key) throws IOException
    {
        if(key.isValid())
        {
            SocketChannel sc=(SocketChannel) key.channel();
            if(key.isConnectable())
            {
                if(!sc.finishConnect())
                    System.exit(1);
            }
            if(key.isReadable())
            {
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
    private void doWrite(SocketChannel channel,byte[] msg) throws IOException
    {
        ByteBuffer writeBuffer=ByteBuffer.allocate(msg.length);
        writeBuffer.put(msg);
        writeBuffer.flip();
        channel.write(writeBuffer);
    }
    private void doConnect() throws IOException
    {
        if(socketChannel.connect(new InetSocketAddress(host,port)))System.out.println("New connection.");
        else socketChannel.register(selector, SelectionKey.OP_CONNECT);
    }
    public void sendMsg(byte[] msg) throws Exception
    {
        socketChannel.register(selector, SelectionKey.OP_READ);
        doWrite(socketChannel, msg);
    }
}