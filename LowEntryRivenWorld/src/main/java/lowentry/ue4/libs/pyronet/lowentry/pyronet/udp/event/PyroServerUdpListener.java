package lowentry.ue4.libs.pyronet.lowentry.pyronet.udp.event;


import java.net.SocketAddress;
import java.nio.ByteBuffer;


public interface PyroServerUdpListener
{
	void receivedDataUdp(Integer cliendId, SocketAddress client, ByteBuffer data);
}
