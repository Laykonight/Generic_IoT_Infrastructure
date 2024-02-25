package il.co.ILRD.gateway_server;

import com.mongodb.util.JSON;
import com.sun.media.sound.InvalidDataException;
import org.json.JSONObject;

import java.io.*;

        import java.net.InetSocketAddress;
        import java.nio.ByteBuffer;
        import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class TCPTester implements Runnable {
    private static JSONObject registerCompany = new JSONObject()
            .put("start_line", new JSONObject()
                    .put("method", "POST")
                    .put("url", "registerCompany")
                    .put("version", "1.1")
                )
            .put("headers", new JSONObject()
                    .put("content_type", "application/json")
                    .put("Accept", "application/json")
                )
            .put("body", new JSONObject()
                    .put("company_name", "The_Company")
                );
            /*
            request{
                start_line{
                    "method" : "POST",
                    "url" : "registerCompany",
                    "version" : "1.1"}
                headers{
                    "content_type" : "application/json",
                    "Accept" : "application/json"}
                body{
                    "company_name" : "The_Company"}
            }
            */

    private static JSONObject registerProduct = new JSONObject()
            .put("start_line", new JSONObject()
                    .put("method", "POST")
                    .put("url", "registerProduct")
                    .put("version", "1.1")
            )
            .put("headers", new JSONObject()
                    .put("content_type", "application/json")
                    .put("Accept", "application/json")
            )
            .put("body", new JSONObject()
                    .put("company_name", "The_Company")
                    .put("product_name", "test_Product")
            );
            /*
            request{
                start_line{
                    "method" : "POST",
                    "url" : "registerProduct",
                    "version" : "1.1"}
                headers{
                    "content_type" : "application/json",
                    "Accept" : "application/json"}
                body{
                    "company_name" : "The_Company",
                    "product_name" : "test_Product",}
            }
            */

    private static JSONObject registerIoTDevice = new JSONObject()
            .put("start_line", new JSONObject()
                    .put("method", "POST")
                    .put("url", "registerIoT")
                    .put("version", "1.1")
            )
            .put("headers", new JSONObject()
                    .put("content_type", "application/json")
                    .put("Accept", "application/json")
            )
            .put("body", new JSONObject()
                    .put("company_name", "The_Company")
                    .put("product_name", "test_Product")
                    .put("IoT_Serial_num", "xyz-552233")
                    .put("end_user_name", "Random name")
                    .put("end_user_email", "RandomName@gmail.com")
            );

            /*
            request{
                start_line{
                    "method" : "POST",
                    "url" : "registerIoT",
                    "version" : "1.1"}
                headers{
                    "content_type" : "application/json",
                    "Accept" : "application/json"}
                body{
                    "company_name" : "The_Company",
                    "product_name" : "test_Product",
                    "IoT_Serial_num" : "xyz-552233",
                    "end_user_name" : "Random name",
                    "end_user_email" : "RandomName@gmail.com",}
            }
            */

    private static JSONObject update = new JSONObject()
            .put("start_line", new JSONObject()
                    .put("method", "POST")
                    .put("url", "update")
                    .put("version", "1.1")
            )
            .put("headers", new JSONObject()
                    .put("content_type", "application/json")
                    .put("Accept", "application/json")
            )
            .put("body", new JSONObject()
                    .put("company_name", "The_Company")
                    .put("product_name", "test_Product")
                    .put("IoT_Serial_num", "xyz-552233")
                    .put("update", new JSONObject()
                            .put("update_name", "the update")
                            .put("update_type", "daily")
                    )
            );

            /*
            request{
                start_line{
                    "method" : "POST",
                    "url" : "update",
                    "version" : "1.1"}
                headers{
                    "content_type" : "application/json",
                    "Accept" : "application/json"}
                body{
                    "company_name" : "The_Company",
                    "product_name" : "test_Product",
                    "IoT_Serial_num" : "xyz-552233",
                    update{
                        "update_name" : "update",
                        "update_type" : "daily"}
                }
            }
            */

    private String byteBufferToString(ByteBuffer buffer){
        if (null == buffer || !buffer.hasRemaining()){
            return null;
        }

        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private ByteBuffer JSONToByteBuffer(JSONObject HTTPRequest) {
        return ByteBuffer.wrap(HTTPRequest.toString().getBytes());
    }

    private JSONObject ByteBufferToJSON(ByteBuffer HTTPRequest) throws InvalidDataException {
        return new JSONObject(byteBufferToString(HTTPRequest));
    }

    public void run() {
        try {
            // Create a SocketChannel object.
            SocketChannel socketChannel = SocketChannel.open();

            // Connect the SocketChannel to the server.
            InetSocketAddress serverAddress = new InetSocketAddress("localhost", 8888);
            socketChannel.connect(serverAddress);

                // tests for DataBase <<<<<<<<<<<<<<<

                ByteBuffer byteBuffer = JSONToByteBuffer(registerCompany);
//                ByteBuffer byteBuffer = JSONToByteBuffer(registerProduct);
//                ByteBuffer byteBuffer = JSONToByteBuffer(registerIoTDevice);
//                ByteBuffer byteBuffer = JSONToByteBuffer(update);

                socketChannel.write(byteBuffer);

                ByteBuffer massage = ByteBuffer.allocate(2084);
                socketChannel.read(massage);

                massage.flip();
                JSONObject reply = ByteBufferToJSON(massage);

                System.out.println(reply);

            socketChannel.close();
            } catch(IOException e){
                throw new RuntimeException(e);
            }
    }

//    private JSONObject createRequest(JSONObject form){
//        JSONObject request = new JSONObject().
//    }


    public static void main(String[] args) throws IOException {
        new Thread(new TCPTester()).start();
    }
}
