package il.co.ILRD.gateway_server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GatewayServerMain {

    private static String pnpDirPath = "/home/chen/git/chen.lev-ron/IoT_Project/IoT_Project/src/main/java/il/co/ILRD/plug_and_play/API_Updates";
    private static int TCPPort = 8888;
    private static int UDPPort = 7777;

//    private static String mySQLurl = "jdbc:mysql://localhost:3306";
//    private static String mySQLusername = "root";
//    private static String mySQLpassword = "chen1234";
    private static String mongoURL = "localhost:27017";


    @BeforeEach
    void setUp() {
    }

    @Test
//    void runServer() {
//        new GatewayServer(8080,7777).start();
//    }

    public static void main(String[] args) {
//        String test = "registerCompany@Electra";
        new GatewayServer(
                TCPPort,
                UDPPort,
                pnpDirPath,
                mongoURL).start();
    }
}