package lowentry.ue4.libs.pyronet.lowentry.pyronet.udp;


import lowentry.ue4.classes.sockets.SocketServer;
import lowentry.ue4.library.LowEntry;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroException;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroSelector;
import lowentry.ue4.libs.pyronet.lowentry.pyronet.udp.event.PyroServerUdpListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.ConcurrentLinkedQueue;


public class PyroServerUdp {
    //    protected final DatagramChannel channel;
    private final Thread networkThread;
    private final PyroServerUdpListener listener;
    private final DatagramSocket socket;
    private ConcurrentLinkedQueue<DatagramPacket> stream = new ConcurrentLinkedQueue<>();


    public PyroServerUdp(InetSocketAddress end, PyroServerUdpListener listener) throws Exception {
        this.networkThread = Thread.currentThread();
        this.listener = listener;

        this.socket = new DatagramSocket(5566);

        new Thread(() -> {
            while (true) {
                byte[] b = new byte[PyroSelector.BUFFER_SIZE];

                DatagramPacket packet = new DatagramPacket(b, b.length);
                try {
                    this.socket.receive(packet);
                    this.stream.add(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


//        this.channel = DatagramChannel.open();
//        this.channel.configureBlocking(false);
//        this.channel.bind(new InetSocketAddress(5568));
    }

    public PyroServerUdp(int port, PyroServerUdpListener listener) throws Exception {
        this(new InetSocketAddress(port), listener);
    }

    public PyroServerUdp(boolean acceptExternalConnections, int port, PyroServerUdpListener listener) throws Exception {
        this(new InetSocketAddress((acceptExternalConnections ? null : InetAddress.getLoopbackAddress()), port), listener);
    }


    public void shutdown() {
        try {
//			this.channel.close();
            this.socket.close();
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

        // TODO: Send the data via the packet stored for the client.

        if (buffer == null) {
            return;
        }
        int pos = buffer.position();
        try {

            if (SocketServer.outboundUdpPackets.containsKey(client)) {
                DatagramPacket p = SocketServer.outboundUdpPackets.get(client);
                p.setData(buffer.array());
                p.setLength(buffer.array().length);

                this.socket.send(p);
            } else {
                System.err.println("Failed to send data because we don't have a socket to send them...");
            }

//			channel.send(buffer, client);
        } catch (Exception e) {
            //e.printStackTrace();
        }
        buffer.position(pos);
    }


    /**
     * Keeps reading and firing the listener events until no more data can be read.
     */
    public void listen(ByteBuffer buffer) {
        SocketAddress client = receive(buffer);
        while (client != null) {
            listener.receivedDataUdp(client, buffer);
            client = receive(buffer);
        }
    }

    /**
     * Returns null on fail.
     */
    private SocketAddress receive(ByteBuffer buffer) {
        try {
            buffer.clear();

//            byte[] b = buffer.array();
//
//            DatagramPacket packet = new DatagramPacket(b, b.length);
//            this.socket.receive(packet);

            if (this.stream.size() == 0) {
                return null;
            }

            DatagramPacket packet = this.stream.poll();

            byte[] data = new byte[packet.getLength()];
            for (int i = 0; i < data.length; i++) {
                data[i] = packet.getData()[i];
            }

            buffer.put(data);

            SocketAddress client = packet.getSocketAddress();

            if (!SocketServer.outboundUdpPackets.containsKey(client)) {
                SocketServer.outboundUdpPackets.put(client, packet);
            }

//            SocketAddress client = channel.receive(buffer);
            buffer.flip();
            return client;
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
    }


    /**
     * Retrieves the local port.<br>
     * <br>
     * Returns 0 if it is not valid.
     */
    public int getLocalPort() {
        int port = this.socket.getLocalPort();
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
        if (!this.socket.isBound()) {
            return "closed";
        }

        DatagramSocket sockaddr = this.socket;
        InetAddress inetaddr = sockaddr.getLocalAddress();
        if (inetaddr == null) {
            return "connecting";
        }
        return inetaddr.getHostAddress() + ":" + sockaddr.getLocalPort();
    }
}
