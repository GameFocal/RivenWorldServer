package lowentry.ue4.libs.pyronet.lowentry.pyronet.udp.event;


import java.nio.ByteBuffer;


public interface PyroClientUdpListener
{
	void receivedDataUdp(ByteBuffer data);
}
