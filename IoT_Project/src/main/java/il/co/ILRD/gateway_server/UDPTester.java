package il.co.ILRD.gateway_server;

import java.io.*;

        import java.net.InetSocketAddress;
        import java.nio.ByteBuffer;
        import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;

public class UDPTester implements Runnable {

    private String byteBufferToString(ByteBuffer buffer) {
        if (null == buffer || !buffer.hasRemaining()) {
            return null;
        }

        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }
    public void run () {
        try {
            // Create a DatagramChannel object.
            DatagramChannel datagramChannel = DatagramChannel.open();

            // Connect the datagramChannel to the server.
            InetSocketAddress serverAddress = new InetSocketAddress("localhost", 7777);
            datagramChannel.connect(serverAddress);

            // Create a ByteBuffer object to store the data you want to send.
            ByteBuffer byteBuffer = ByteBuffer.wrap("registerCompany@Lev-Ron".getBytes());
            // ByteBuffer byteBuffer = ByteBuffer.wrap("update@Lev-Ron@James@19.05.2017@Hello World".getBytes());

            // Write the ByteBuffer object to the datagramChannel.
//            byteBuffer.flip();

            //System.out.println(byteBufferToString(byteBuffer));
            datagramChannel.write(byteBuffer);

            // Create a ByteBuffer object to store the reply message
            ByteBuffer massage = ByteBuffer.allocate(1024);

            // read from the datagramChannel
            datagramChannel.read(massage);
            massage.flip();

            // convert to String
            String reply = byteBufferToString(massage);

            System.out.println(reply);

            datagramChannel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new Thread(new UDPTester()).start();
    }
}