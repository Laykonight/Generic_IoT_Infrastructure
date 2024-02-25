//  reviewer        Daniel
//  date            02-Oct-23
//  Developer       Chen
//  Status          need to correct

package il.co.ILRD.gateway_server;

import com.sun.media.sound.InvalidDataException;
import il.co.ILRD.command_factory.Command;
import il.co.ILRD.command_factory.Factory;
import il.co.ILRD.DB_CRUD.MongoManagerCRUD;
import il.co.ILRD.plug_and_play.PlugAndPlay;
import il.co.ILRD.thread_pool.ThreadPool;
import org.json.JSONObject;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.security.InvalidKeyException;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;


public class GatewayServer {
    private final MultiProtocolServer multiProtocolServer; //inplace
    private final RequestHandler requestHandler; // inplace
    private final PlugAndPlay PnP; // out source
    private final Factory<String, JSONObject> factory; // out source
//    private final DB_ManagerCRUD DBManagerCRUD;
//    private final AdminCRUD adminCRUD;
    private final int TCPPort;
    private final int UDPPort;
//    private String pnpDirPath;


    // User set number of thread in thread pool
    public GatewayServer(int TCPPort, int UDPPort, String pnpDirPath, int numOfThreads,
                         String URL) {
        this(TCPPort, UDPPort, pnpDirPath, URL);
        this.requestHandler.threadPool.setRealNumOfThreads(numOfThreads);
    }

    public GatewayServer(int TCPPort, int UDPPort, String pnpDirPath,
                         String URL) {
        this.multiProtocolServer = new MultiProtocolServer();
        this.factory = new Factory<>();
        this.requestHandler = new RequestHandler(this.factory, URL);
        this.TCPPort = TCPPort;
        this.UDPPort = UDPPort;

        this.PnP = new PlugAndPlay(
                Callable.class.getSimpleName(),
                pnpDirPath,
                "call",
                this.factory);

        this.requestHandler.addDefaultCommands();
        System.out.println("server is online");
    }

    // todo add TCPPort and UDPPort Methods

    public void start() {
        try {
            this.multiProtocolServer.addTCPConnection(this.TCPPort);
            this.multiProtocolServer.addUDPConnection(this.UDPPort);
            this.multiProtocolServer.start();

        } catch (IOException | /*SQLException |*/ ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("couldn't run server.... " + e.getMessage());
        }
        new Thread(this.PnP).start();
    }

    public void stop() {
        this.multiProtocolServer.stop();
        this.PnP.stop();
    }


    private void handle(ByteBuffer buffer, Communicator communicator) {
        requestHandler.handle(buffer, communicator);
    }

    /*============================================================================================================*/
    /*================================================ Interfaces ================================================*/
    private interface Communicator {            // Communicator
        ByteBuffer receive();

        void send(ByteBuffer buffer) throws IOException;
    }

    //===============================================================================================================
    //============================================ RequestHandler ===================================================
    //===============================================================================================================
    private class RequestHandler {
        private ThreadPool threadPool;
        private Factory<String, JSONObject> factory;
        private MongoManagerCRUD mongoManager;

        private RequestHandler(Factory<String, JSONObject> factory, String URL) { // System set number of thread in thread pool
            this.threadPool = new ThreadPool();
            this.factory = factory;
            this.mongoManager = new MongoManagerCRUD(URL);
        }

        private void handle(ByteBuffer buffer, Communicator communicator) {
            if (null == buffer || !buffer.hasRemaining()/*todo check if needed hasRemaining*/) {
                return;
            }
            threadPool.submit(createRunnable(buffer, communicator), ThreadPool.Priority.DEFAULT);
        }

        private String[] splitter(String str) {
            return str.split("@");
        }

        /*====================================================================================================*/
        /*================================================ API Commands ======================================*/
        // todo hierarchy check
        // key = 'registerCompany' -> value = '@companyName'
        // key = 'registerProduct' -> value = '@companyName@productName'
        // key = 'registerIoT' -> value = '@companyName@productName@IoTSerial'
        // key = 'update' -> value = '@companyName@productName@IoTSerial@update'
        private void addDefaultCommands() {
            /*
            request{
                start_line{
                    "method" : "POST",
                    "url" : "Companies",
                    "version" : "1.1"}
                headers{
                    "content_type" : "application/json",
                    "content_length" : "200",
                    "Accept" : "application/json"}
                body{
                    "company_name" : "The_Company",
                    "company_address" : "Planet_Earth",
                    "contact_name" : "Chuck Norris",
                    "contact_phone" : "0-000-000-1",
                    "contact_email" : "Norris_mail.com",
                    "service_fee" : "500"}
            }
            */
            Function<JSONObject, Command> registerCompany = (registerCompanyBody) -> (mongoManager ) -> {

//                JSONObject body = registerCompanyBody.getJSONObject("body");

                mongoManager.registerCompanyCRUD(registerCompanyBody);
            };
            factory.add("registerCompany", registerCompany);

            //-------------------------------------------------------
            /*
            request{
                start_line{
                    "method" : "POST",
                    "URL" : "Products",
                    "version" : "1.1"}
                headers{
                    "content_type" : "application/json",
                    "content_length" : "200",
                    "Accept" : "application/json"}
                body{
                    "company_name" : "The_Company",
                    "product_name" : "Product A"}
            }
            */
            Function<JSONObject, Command> registerProduct = (registerProductBody) -> (mongoManager) -> {

                JSONObject body = registerProductBody.getJSONObject("body");

                if (mongoManager.isDBExist(registerProductBody.getString("company_name"))){
                    mongoManager.registerProductCRUD(body);
                } else {
                    throw new InvalidDataException("Company don't exist");
                }
            };
            factory.add("registerProduct", registerProduct);

            //-------------------------------------------------------
            /*
            request{
                start_line{
                    "method" : "POST",
                    "URL" : "IoT",
                    "version" : "1.1"}
                headers{
                    "content_type" : "application/json",
                    "content_length" : "200",
                    "Accept" : "application/json"}
                body{
                    "company_name" : "The_Company",
                    "product_name" : "Product A"
                    "IoT_Serial_num" : "xyz-552233"
                    "end_user_name" : "Random name"
                    "end_user_email" : "RandomName@gmail.com"}
            }
            */
            Function<JSONObject, Command> registerIoT = (registerIoTRequest) -> (mongoManager) -> {

                JSONObject body = registerIoTRequest.getJSONObject("body");

                if (mongoManager.isDBExist(body.getString("company_name"))){
                    if (mongoManager.isCollectionExist(
                            body.getString("company_name"),
                            body.getString("product_name") + "_users")){
                        mongoManager.registerIoTCRUD(body);
                    } else {
                        throw new InvalidDataException("Product don't exist");
                    }
                } else {
                    throw new InvalidDataException("Company don't exist");
                }
            };
            factory.add("registerIoT", registerIoT);

            //-------------------------------------------------------
            /*
            request{
                start_line{
                    "method" : "POST",
                    "URL" : "update",
                    "version" : "1.1"}
                headers{
                    "content_type" : "application/json",
                    "content_length" : "200",
                    "Accept" : "application/json"}
                body{
                    "company_name" : "The_Company",
                    "product_name" : "Product A"
                    "IoT_Serial_num" : "xyz-552233"
                    update{
                        "update_name" : "the update"
                        "update_type" : "daily"}
                    }
            }
            */
            Function<JSONObject, Command> update = (registerUpdateRequest) -> (mongoManager) -> {

                JSONObject body = registerUpdateRequest.getJSONObject("body");

                if (mongoManager.isDBExist(body.getString("company_name"))){
                    if (mongoManager.isCollectionExist(
                            body.getString("company_name"),
                            body.getString("product_name") + "_updates")){
                        mongoManager.registerUpdateCRUD(body);
                    } else {
                        throw new InvalidDataException("Product don't exist");
                    }
                } else {
                    throw new InvalidDataException("Company don't exist");
                }
            };
            factory.add("update", update);
        }

        private Entry<String, JSONObject> requestParse(JSONObject request) {
            return new AbstractMap.SimpleEntry<>(request
                    .getJSONObject("start_line").getString("url"), request.getJSONObject("body"));
        }

        // converting buffer to String
        private String byteBufferToString(ByteBuffer buffer) throws InvalidDataException {
            if (null == buffer || !buffer.hasRemaining()) {
                throw new InvalidDataException("ByteBuffer Error");
            }
            return new String(buffer.array(), 0, buffer.remaining()).trim();
        }

        private ByteBuffer convertToByteBuffer(JSONObject HTTPRequest) {
            return ByteBuffer.wrap(HTTPRequest.toString().getBytes());
        }

        private JSONObject convertToJSON(ByteBuffer HTTPRequest) throws InvalidDataException {
            return new JSONObject(byteBufferToString(HTTPRequest));
        }

        private JSONObject createResponse(JSONObject request){
            request.getJSONObject("start_line").remove("method");
            request.getJSONObject("start_line").put("status_code", "200");

            request.getJSONObject("headers").remove("Accept");

            return request;
        }
        private JSONObject createErrorResponse(JSONObject request, String error){
            request.getJSONObject("start_line").remove("method");
            request.getJSONObject("start_line").put("status_code", "404");
            request.getJSONObject("body").put("error", error);

            request.getJSONObject("headers").remove("Accept");

            return request;
        }

        // creating Runnable object
        private Runnable createRunnable(ByteBuffer buffer, Communicator communicator) {
            return () -> {
                JSONObject request = null;
                try {
                    request = convertToJSON(buffer);

                    Entry<String, JSONObject> entry = requestParse(request);

                    factory.create(entry.getKey(), entry.getValue()).exec(mongoManager);

                    communicator.send(convertToByteBuffer(createResponse(request)));

                } catch (InvalidKeyException | IOException e) {
                    try {
                        communicator.send(convertToByteBuffer(createErrorResponse(request, e.getMessage())));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            };
        }
    }

    /*==============================================================================================================*/
    /*============================================= MultiProtocolServer ============================================*/
    /*==============================================================================================================*/
    private class MultiProtocolServer {
        private final CommunicationManager communicationManger;
        private final MessageManager messageManager;

        public MultiProtocolServer() {
            this.communicationManger = new CommunicationManager();
            messageManager = new MessageManager();
        }

        public void addTCPConnection(int TCPtPort) throws IOException {
            this.communicationManger.addTCPConnection(TCPtPort);
        }

        public void addUDPConnection(int UDPPort) throws IOException {
            this.communicationManger.addUDPConnection(UDPPort);
        }

        public void stop() {
            this.communicationManger.stop();
        }

        public void start() throws IOException, ClassNotFoundException {
            this.communicationManger.start();
        }

        /*=================================================================================================*/
        /*===================================== Massage Handlers ==========================================*/
        /*=================================================================================================*/

        private class MessageManager {
            public void handle(Communicator communicator) throws IOException, ClassNotFoundException {
                ByteBuffer byteBuffer = communicator.receive();

                String test = requestHandler.byteBufferToString(byteBuffer);
                System.out.println(test);

//                System.out.println("\nMessageManager.handle");// todo remove
                if (null == byteBuffer) {
                    return;
                }

                GatewayServer.this.handle(byteBuffer, communicator);
            }
        }

        /*=================================================================================================*/
        /*===================================== Communication Manager =====================================*/
        /*=================================================================================================*/

        private class CommunicationManager { // todo add option to get address -> localhost
            private final Selector selector;
            private AtomicBoolean isRunning;
            private final ByteBuffer msgBuffer;
            private final SelectorRunner selectorRunner;


            public CommunicationManager() {
                try {
                    this.selector = Selector.open();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                this.isRunning = new AtomicBoolean(true);
                this.msgBuffer = ByteBuffer.allocate(1024);
                this.selectorRunner = new SelectorRunner();
            }

            public void addTCPConnection(int TCPPort) throws IOException {
                ServerSocketChannel tcpServerSocket = ServerSocketChannel.open();
                tcpServerSocket.configureBlocking(false);
                // create non-clocking 'ServerSocketChannel' listening to incoming TCP connections.

                tcpServerSocket.bind(new InetSocketAddress("localhost", TCPPort));
                // sets up the server to listen to specified port and host name.

                tcpServerSocket.register(selector, SelectionKey.OP_ACCEPT);
                // registers to the selector monitored for the 'OP_ACCEPT' event.
            }

            public void addUDPConnection(int UDPPort) throws IOException {
                DatagramChannel udpServerSocket = DatagramChannel.open();
                udpServerSocket.configureBlocking(false);
                // create non-clocking 'DatagramChannel' used for UDP communication.

                udpServerSocket.bind(new InetSocketAddress("localhost", UDPPort));
                // sets up the server to listen to specified port and host name.

                udpServerSocket.register(selector, SelectionKey.OP_READ);
                // registers to the selector monitored for the 'OP_READ' event.
            }


            public void start() {
                new Thread(this.selectorRunner).start();
            }

            public void stop() {
                this.isRunning.set(false);
            }

            /*================================ Selector Runner =========================================*/

            private class SelectorRunner implements Runnable {
                private final TCPRegister tcpRegister;

                public SelectorRunner() {
                    this.tcpRegister = new TCPRegister();
                }

                @Override
                public void run() {
                    Set<SelectionKey> selectedKeys = null;
                    while (isRunning.get()) {
                        try {
                            selector.select();

                            selectedKeys = selector.selectedKeys();
                            Iterator<SelectionKey> iterator = selectedKeys.iterator();

                            while (iterator.hasNext()) {
                                SelectionKey currentKey = iterator.next();

                                if (currentKey.isAcceptable()) { // new TCP connection.
                                    this.tcpRegister.TCPAccept((ServerSocketChannel) currentKey.channel());
                                    //retrieves the 'ServerSocketChannel' from the current key and
                                    // send it to TCPAccept() to attached 'Communicator'

                                } else if (currentKey.isReadable()) { // there is data to be read from a channel.
                                    SelectableChannel channel = currentKey.channel();
                                    // retrieves the 'Channel' from the current key

                                    if (channel instanceof SocketChannel) { // the channel used for TCP communication.
                                        messageManager.handle((TCPCommunicator) currentKey.attachment());
                                        // retrieves the attached 'Communicator' and send it to 'messageManager.handle()'

                                    } else { // the channel used for UDP communication.
                                        messageManager.handle(new UDPCommunicator((DatagramChannel) channel));
                                        // create new 'Communicator' from the 'datagramChannel' and
                                        // send it to 'messageManager.handle()'.
                                    }
                                }
                                iterator.remove();
                            }
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    assert selectedKeys != null;
                    selectedKeys.clear();
                }
            }

            /*======================================== TCP register ====================================*/
            private class TCPRegister {
                public void TCPAccept(ServerSocketChannel serverSocketChannel) throws IOException {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    //create new non-blocking socket channel for TCP connection

                    SelectionKey key = socketChannel.register(selector, SelectionKey.OP_READ);
                    // register the socket to the selector for 'OP_READ' events

                    key.attach(new TCPCommunicator(socketChannel));
                    // attach to the event key new 'TCPCommunicator'

                }
            }

            /*========================================== TCPCommunicator =================================*/

            private class TCPCommunicator implements Communicator {
                private final SocketChannel clientSocketChannel;

                public TCPCommunicator(SocketChannel clientSocketChannel) {
                    this.clientSocketChannel = clientSocketChannel;
                }

                @Override
                public ByteBuffer receive() {
                    try {
                        // create 'ByteBuffer' and read from TCP socket channel into it
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        int bytesRead = clientSocketChannel.read(buffer);

                        if (bytesRead == -1) { // can't read
                            clientSocketChannel.close();
                            return null;
                        }

                        return buffer;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void send(ByteBuffer buffer) throws IOException {
                    try {
                        if (!clientSocketChannel.isOpen() ||
                                !clientSocketChannel.isConnected()) {
                            System.out.println("SocketChannel is not open or connected!");
                            return;
                        }
                        //  sets the limit of the 'ByteBuffer' to its array length.
                        //  This ensures that all data in the buffer will be sent.
                        buffer.limit(buffer.array().length);

                        while (buffer.hasRemaining()) {
                            // writes the data from the 'ByteBuffer' to the TCP socket channel
                            clientSocketChannel.write(buffer);
                        }

                        System.out.println("Sending massage to " + clientSocketChannel);
                    } catch (IOException e) {
                        throw new IOException("write error: channel is not yet connected " + e.getMessage());
                    }
                }
            }

            /*========================================== UDPCommunicator =================================*/
            private class UDPCommunicator implements Communicator {

                private final DatagramChannel clientDatagramChannel;
                private SocketAddress clientAddress;

                public UDPCommunicator(DatagramChannel clientDatagramChannel) {
                    this.clientDatagramChannel = clientDatagramChannel;
                }

                @Override
                public ByteBuffer receive() {
                    try {
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        //  create 'ByteBuffer' and read from UDP socket channel into it
                        this.clientAddress = clientDatagramChannel.receive(buffer);

                        return buffer;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void send(ByteBuffer buffer) {
                    try {
                        // sending the 'ByteBuffer' to 'clientAddress'
                        this.clientDatagramChannel.send(buffer, clientAddress);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

}

