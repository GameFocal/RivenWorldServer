package lowentry.ue4.classes.sockets;


import lowentry.ue4.classes.sockets.SocketConnection.FunctionCallListener;
import lowentry.ue4.classes.sockets.SocketConnection.LatentFunctionCallListener;


public class SocketTasks
{
	public static class ConnectedSocketConnection implements Runnable
	{
		private final SocketConnectionListener listener;
		private final SocketConnection         connection;
		
		public ConnectedSocketConnection(final SocketConnectionListener listener, final SocketConnection connection)
		{
			this.listener = listener;
			this.connection = connection;
		}
		
		@Override
		public void run()
		{
			listener.connected(connection);
		}
	}
	
	
	public static class DisconnectedSocketConnection implements Runnable
	{
		private final SocketConnectionListener listener;
		private final SocketConnection         connection;
		
		public DisconnectedSocketConnection(final SocketConnectionListener listener, final SocketConnection connection)
		{
			this.listener = listener;
			this.connection = connection;
		}
		
		@Override
		public void run()
		{
			listener.disconnected(connection);
		}
	}
	
	
	public static class ReceivedUnreliableMessageSocketConnection implements Runnable
	{
		private final SocketConnectionListener listener;
		private final SocketConnection         connection;
		private final byte[]                   bytes;
		
		public ReceivedUnreliableMessageSocketConnection(final SocketConnectionListener listener, final SocketConnection connection, final byte[] bytes)
		{
			this.listener = listener;
			this.connection = connection;
			this.bytes = bytes;
		}
		
		@Override
		public void run()
		{
			listener.receivedUnreliableMessage(connection, bytes);
		}
	}
	
	
	public static class ReceivedMessageSocketConnection implements Runnable
	{
		private final SocketConnectionListener listener;
		private final SocketConnection         connection;
		private final byte[]                   bytes;
		
		public ReceivedMessageSocketConnection(final SocketConnectionListener listener, final SocketConnection connection, final byte[] bytes)
		{
			this.listener = listener;
			this.connection = connection;
			this.bytes = bytes;
		}
		
		@Override
		public void run()
		{
			listener.receivedMessage(connection, bytes);
		}
	}
	
	
	public static class ConnectedThreadedSocketConnection implements Runnable
	{
		private final ThreadedSocketConnection         thread;
		private final ThreadedSocketConnectionListener listener;
		private final SocketConnection                 connection;
		
		public ConnectedThreadedSocketConnection(final ThreadedSocketConnection thread, final ThreadedSocketConnectionListener listener, final SocketConnection connection)
		{
			this.thread = thread;
			this.listener = listener;
			this.connection = connection;
		}
		
		@Override
		public void run()
		{
			listener.connected(thread, connection);
		}
	}
	
	
	public static class DisconnectedThreadedSocketConnection implements Runnable
	{
		private final ThreadedSocketConnection         thread;
		private final ThreadedSocketConnectionListener listener;
		private final SocketConnection                 connection;
		
		public DisconnectedThreadedSocketConnection(final ThreadedSocketConnection thread, final ThreadedSocketConnectionListener listener, final SocketConnection connection)
		{
			this.thread = thread;
			this.listener = listener;
			this.connection = connection;
		}
		
		@Override
		public void run()
		{
			listener.disconnected(thread, connection);
		}
	}
	
	
	public static class ReceivedUnreliableMessageThreadedSocketConnection implements Runnable
	{
		private final ThreadedSocketConnection         thread;
		private final ThreadedSocketConnectionListener listener;
		private final SocketConnection                 connection;
		private final byte[]                           bytes;
		
		public ReceivedUnreliableMessageThreadedSocketConnection(final ThreadedSocketConnection thread, final ThreadedSocketConnectionListener listener, final SocketConnection connection, final byte[] bytes)
		{
			this.thread = thread;
			this.listener = listener;
			this.connection = connection;
			this.bytes = bytes;
		}
		
		@Override
		public void run()
		{
			listener.receivedUnreliableMessage(thread, connection, bytes);
		}
	}
	
	
	public static class ReceivedMessageThreadedSocketConnection implements Runnable
	{
		private final ThreadedSocketConnection         thread;
		private final ThreadedSocketConnectionListener listener;
		private final SocketConnection                 connection;
		private final byte[]                           bytes;
		
		public ReceivedMessageThreadedSocketConnection(final ThreadedSocketConnection thread, final ThreadedSocketConnectionListener listener, final SocketConnection connection, final byte[] bytes)
		{
			this.thread = thread;
			this.listener = listener;
			this.connection = connection;
			this.bytes = bytes;
		}
		
		@Override
		public void run()
		{
			listener.receivedMessage(thread, connection, bytes);
		}
	}
	
	
	public static class ConnectedThreadedSimpleSocketConnection implements Runnable
	{
		private final ThreadedSimpleSocketConnection         thread;
		private final ThreadedSimpleSocketConnectionListener listener;
		private final SimpleSocketConnection                 connection;
		
		public ConnectedThreadedSimpleSocketConnection(final ThreadedSimpleSocketConnection thread, final ThreadedSimpleSocketConnectionListener listener, final SimpleSocketConnection connection)
		{
			this.thread = thread;
			this.listener = listener;
			this.connection = connection;
		}
		
		@Override
		public void run()
		{
			listener.connected(thread, connection);
		}
	}
	
	
	public static class DisconnectedThreadedSimpleSocketConnection implements Runnable
	{
		private final ThreadedSimpleSocketConnection         thread;
		private final ThreadedSimpleSocketConnectionListener listener;
		private final SimpleSocketConnection                 connection;
		
		public DisconnectedThreadedSimpleSocketConnection(final ThreadedSimpleSocketConnection thread, final ThreadedSimpleSocketConnectionListener listener, final SimpleSocketConnection connection)
		{
			this.thread = thread;
			this.listener = listener;
			this.connection = connection;
		}
		
		@Override
		public void run()
		{
			listener.disconnected(thread, connection);
		}
	}
	
	
	public static class ReceivedPacketThreadedSimpleSocketConnection implements Runnable
	{
		private final ThreadedSimpleSocketConnection         thread;
		private final ThreadedSimpleSocketConnectionListener listener;
		private final SimpleSocketConnection                 connection;
		private final byte[]                                 bytes;
		
		public ReceivedPacketThreadedSimpleSocketConnection(final ThreadedSimpleSocketConnection thread, final ThreadedSimpleSocketConnectionListener listener, final SimpleSocketConnection connection, final byte[] bytes)
		{
			this.thread = thread;
			this.listener = listener;
			this.connection = connection;
			this.bytes = bytes;
		}
		
		@Override
		public void run()
		{
			listener.receivedPacket(thread, connection, bytes);
		}
	}
	
	
	public static class ConnectedThreadedAsyncSocketConnection implements Runnable
	{
		private final ThreadedAsyncSocketConnection         thread;
		private final ThreadedAsyncSocketConnectionListener listener;
		private final SocketConnection                      connection;
		
		public ConnectedThreadedAsyncSocketConnection(final ThreadedAsyncSocketConnection thread, final ThreadedAsyncSocketConnectionListener listener, final SocketConnection connection)
		{
			this.thread = thread;
			this.listener = listener;
			this.connection = connection;
		}
		
		@Override
		public void run()
		{
			listener.connected(thread, connection);
		}
	}
	
	
	public static class DisconnectedThreadedAsyncSocketConnection implements Runnable
	{
		private final ThreadedAsyncSocketConnection         thread;
		private final ThreadedAsyncSocketConnectionListener listener;
		private final SocketConnection                      connection;
		
		public DisconnectedThreadedAsyncSocketConnection(final ThreadedAsyncSocketConnection thread, final ThreadedAsyncSocketConnectionListener listener, final SocketConnection connection)
		{
			this.thread = thread;
			this.listener = listener;
			this.connection = connection;
		}
		
		@Override
		public void run()
		{
			listener.disconnected(thread, connection);
		}
	}
	
	
	public static class ReceivedUnreliableMessageThreadedAsyncSocketConnection implements Runnable
	{
		private final ThreadedAsyncSocketConnection         thread;
		private final ThreadedAsyncSocketConnectionListener listener;
		private final SocketConnection                      connection;
		private final byte[]                                bytes;
		
		public ReceivedUnreliableMessageThreadedAsyncSocketConnection(final ThreadedAsyncSocketConnection thread, final ThreadedAsyncSocketConnectionListener listener, final SocketConnection connection, final byte[] bytes)
		{
			this.thread = thread;
			this.listener = listener;
			this.connection = connection;
			this.bytes = bytes;
		}
		
		@Override
		public void run()
		{
			listener.receivedUnreliableMessage(thread, connection, bytes);
		}
	}
	
	
	public static class ReceivedMessageThreadedAsyncSocketConnection implements Runnable
	{
		private final ThreadedAsyncSocketConnection         thread;
		private final ThreadedAsyncSocketConnectionListener listener;
		private final SocketConnection                      connection;
		private final byte[]                                bytes;
		
		public ReceivedMessageThreadedAsyncSocketConnection(final ThreadedAsyncSocketConnection thread, final ThreadedAsyncSocketConnectionListener listener, final SocketConnection connection, final byte[] bytes)
		{
			this.thread = thread;
			this.listener = listener;
			this.connection = connection;
			this.bytes = bytes;
		}
		
		@Override
		public void run()
		{
			listener.receivedMessage(thread, connection, bytes);
		}
	}
	
	
	public static class ConnectedThreadedAsyncSimpleSocketConnection implements Runnable
	{
		private final ThreadedAsyncSimpleSocketConnection         thread;
		private final ThreadedAsyncSimpleSocketConnectionListener listener;
		private final SimpleSocketConnection                      connection;
		
		public ConnectedThreadedAsyncSimpleSocketConnection(final ThreadedAsyncSimpleSocketConnection thread, final ThreadedAsyncSimpleSocketConnectionListener listener, final SimpleSocketConnection connection)
		{
			this.thread = thread;
			this.listener = listener;
			this.connection = connection;
		}
		
		@Override
		public void run()
		{
			listener.connected(thread, connection);
		}
	}
	
	
	public static class DisconnectedThreadedAsyncSimpleSocketConnection implements Runnable
	{
		private final ThreadedAsyncSimpleSocketConnection         thread;
		private final ThreadedAsyncSimpleSocketConnectionListener listener;
		private final SimpleSocketConnection                      connection;
		
		public DisconnectedThreadedAsyncSimpleSocketConnection(final ThreadedAsyncSimpleSocketConnection thread, final ThreadedAsyncSimpleSocketConnectionListener listener, final SimpleSocketConnection connection)
		{
			this.thread = thread;
			this.listener = listener;
			this.connection = connection;
		}
		
		@Override
		public void run()
		{
			listener.disconnected(thread, connection);
		}
	}
	
	
	public static class ReceivedPacketThreadedAsyncSimpleSocketConnection implements Runnable
	{
		private final ThreadedAsyncSimpleSocketConnection         thread;
		private final ThreadedAsyncSimpleSocketConnectionListener listener;
		private final SimpleSocketConnection                      connection;
		private final byte[]                                      bytes;
		
		public ReceivedPacketThreadedAsyncSimpleSocketConnection(final ThreadedAsyncSimpleSocketConnection thread, final ThreadedAsyncSimpleSocketConnectionListener listener, final SimpleSocketConnection connection, final byte[] bytes)
		{
			this.thread = thread;
			this.listener = listener;
			this.connection = connection;
			this.bytes = bytes;
		}
		
		@Override
		public void run()
		{
			listener.receivedPacket(thread, connection, bytes);
		}
	}
	
	
	public static class FailedFunctionCallListener implements Runnable
	{
		private final FunctionCallListener listener;
		private final SocketConnection     connection;
		
		public FailedFunctionCallListener(final FunctionCallListener listener, final SocketConnection connection)
		{
			this.listener = listener;
			this.connection = connection;
		}
		
		@Override
		public void run()
		{
			listener.failed(connection);
		}
	}
	
	
	public static class ReceivedResponseFunctionCallListener implements Runnable
	{
		private final FunctionCallListener listener;
		private final SocketConnection     connection;
		private final byte[]               bytes;
		
		public ReceivedResponseFunctionCallListener(final FunctionCallListener listener, final SocketConnection connection, final byte[] bytes)
		{
			this.listener = listener;
			this.connection = connection;
			this.bytes = bytes;
		}
		
		@Override
		public void run()
		{
			listener.receivedResponse(connection, bytes);
		}
	}
	
	
	public static class FailedLatentFunctionCallListener implements Runnable
	{
		private final LatentFunctionCallListener listener;
		private final SocketConnection           connection;
		
		public FailedLatentFunctionCallListener(final LatentFunctionCallListener listener, final SocketConnection connection)
		{
			this.listener = listener;
			this.connection = connection;
		}
		
		@Override
		public void run()
		{
			listener.failed(connection);
		}
	}
	
	
	public static class CanceledLatentFunctionCallListener implements Runnable
	{
		private final LatentFunctionCallListener listener;
		private final SocketConnection           connection;
		
		public CanceledLatentFunctionCallListener(final LatentFunctionCallListener listener, final SocketConnection connection)
		{
			this.listener = listener;
			this.connection = connection;
		}
		
		@Override
		public void run()
		{
			listener.canceled(connection);
		}
	}
	
	
	public static class ReceivedResponseLatentFunctionCallListener implements Runnable
	{
		private final LatentFunctionCallListener listener;
		private final SocketConnection           connection;
		private final byte[]                     bytes;
		
		public ReceivedResponseLatentFunctionCallListener(final LatentFunctionCallListener listener, final SocketConnection connection, final byte[] bytes)
		{
			this.listener = listener;
			this.connection = connection;
			this.bytes = bytes;
		}
		
		@Override
		public void run()
		{
			listener.receivedResponse(connection, bytes);
		}
	}
}
