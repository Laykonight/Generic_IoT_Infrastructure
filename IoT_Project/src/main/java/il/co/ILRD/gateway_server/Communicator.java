package il.co.ILRD.gateway_server;

import java.nio.ByteBuffer;

public interface Communicator {
    ByteBuffer receive();

    void send(ByteBuffer buffer);
}
