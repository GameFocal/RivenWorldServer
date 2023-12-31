package lowentry.ue4.libs.pyronet.lowentry.pyronet.udp;


import lowentry.ue4.classes.sockets.SocketServer;
import lowentry.ue4.classes.sockets.SocketServerClientHandler;
import lowentry.ue4.library.LowEntry;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroException;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroSelector;
import lowentry.ue4.libs.pyronet.lowentry.pyronet.udp.event.PyroServerUdpListener;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;


public class PyroServerUdp {
    protected final DatagramChannel channel;
    private final Thread networkThread;
    private final PyroServerUdpListener listener;


    public PyroServerUdp(InetSocketAddress end, PyroServerUdpListener listener) throws Exception {
        this.networkThread = Thread.currentThread();
        this.listener = listener;

        this.channel = DatagramChannel.open();
        this.channel.configureBlocking(false);
        this.channel.bind(end);
    }

    public PyroServerUdp(int port, PyroServerUdpListener listener) throws Exception {
        this(new InetSocketAddress(port), listener);
    }

    public PyroServerUdp(boolean acceptExternalConnections, int port, PyroServerUdpListener listener) throws Exception {
        this(new InetSocketAddress((acceptExternalConnections ? null : InetAddress.getLoopbackAddress()), port), listener);
    }


    public void shutdown() {
        try {
            this.channel.close();
        } catch (Exception e) {
        }
    }


    public final boolean isNetworkThread() {
        if (PyroSelector.DO_NOT_CHECK_NETWORK_THREAD) {
            return true;
        }
        return networkThread == Thread.currentThread();
    }

    public final Thread networkThread() {
        return networkThread;
    }

    public final void checkThread() {
        if (PyroSelector.DO_NOT_CHECK_NETWORK_THREAD) {
            return;
        }
        if (!isNetworkThread()) {
            throw new PyroException("call from outside the network-thread, you must schedule tasks");
        }
    }


    /**
     * Will block until the send has been completed.<br>
     * <br>
     * - Thread-safe.<br>
     * - The buffer can be cleared and reused.
     */
    public void write(ByteBuffer buffer, SocketAddress client) {
        if (buffer == null) {
            return;
        }
        int pos = buffer.position();
        try {
            channel.send(buffer, client);
        } catch (Exception e) {
            //e.printStackTrace();
        }
        buffer.position(pos);
    }


    /**
     * Keeps reading and firing the listener events until no more data can be read.
     */
    public void listen(SocketServer server, ByteBuffer buffer) {
        SocketAddress client = receive(buffer);
        while (client != null) {

//            ByteBuffer b = ByteBuffer.wrap(buffer.array());
            int type = buffer.getInt();
            int id = buffer.getInt();

            if (type == 0) {

                System.out.println("UDP INIT");

                // UDP Init
                SocketServerClientHandler handler = server.getHandlerFromClientID(id);
                if (handler != null) {

                    System.out.println("UDP Handler Synced.");

                    server.addUdpClient(id, handler);
//                    handler.getSocketClient().setLastUdpsocketAddress(client);
                    handler.getSocketClient().syncClientId(id, client);

                    System.out.println("UDP Synced...");

                    ByteBuffer send = ByteBuffer.allocate(8);
                    send.putInt(0);
                    send.putInt(id);

                    System.out.println("Sent UDP");
                    handler.getSocketClient().sendUnreliableMessage(send.array());

                    return;
                } else {
                    System.err.println("Invalid Handler from ID: " + id);
                    return;
                }
            } else {
                buffer.position(0);
                listener.receivedDataUdp(id, client, buffer);
                client = receive(buffer);
            }
        }
    }

    /**
     * Returns null on fail.
     */
    private SocketAddress receive(ByteBuffer buffer) {
        try {
            buffer.clear();
            SocketAddress client = channel.receive(buffer);
            if (client == null) {
                return null;
            }
            buffer.flip();
            return client;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Retrieves the local port.<br>
     * <br>
     * Returns 0 if it is not valid.
     */
    public int getLocalPort() {
        int port = channel.socket().getLocalPort();
        if (port <= 0) {
            return 0;
        }
        return port;
    }


    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + getAddressText() + "]";
    }

    @SuppressWarnings("resource")
    public final String getAddressText() {
        if (!channel.isOpen()) {
            return "closed";
        }

        DatagramSocket sockaddr = channel.socket();
        if (sockaddr == null) {
            return "connecting";
        }
        InetAddress inetaddr = sockaddr.getLocalAddress();
        if (inetaddr == null) {
            return "connecting";
        }
        return inetaddr.getHostAddress() + ":" + sockaddr.getLocalPort();
    }
}
