package lowentry.ue4.examples;


import lowentry.ue4.classes.sockets.SocketConnection;
import lowentry.ue4.classes.sockets.SocketConnection.FunctionCallListener;
import lowentry.ue4.classes.sockets.SocketConnectionListener;
import lowentry.ue4.library.LowEntry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ExampleSocket4
{
	public static void main(final String[] args) throws Throwable
	{
		/*
		  This examples tries to overload your server by making up to 5.000 connections and massively sending the packet defined below.
		 */
		byte[] packet = LowEntry.mergeBytes(LowEntry.integerToBytes(301), LowEntry.integerToBytes(5));
		
		
		SocketConnectionListener listener = new SocketConnectionListener()
		{
			@Override
			public void connected(final SocketConnection connection)
			{
				System.out.println("Connected: " + connection);
			}
			
			@Override
			public void disconnected(final SocketConnection connection)
			{
				System.out.println("Disconnected: " + connection);
			}
			
			@Override
			public void receivedUnreliableMessage(SocketConnection connection, byte[] bytes)
			{
			}
			
			@Override
			public void receivedMessage(final SocketConnection connection, final byte[] bytes)
			{
			}
		};
		
		
		List<SocketConnection> connections = new ArrayList<>();
		for(int i = 1; i <= 5000; i++)
		{
			try
			{
				SocketConnection connection = new SocketConnection("localhost", 7780, 7880, listener);
				if(connection.connect())
				{
					connections.add(connection);
				}
				else
				{
					System.out.println("Failed to connect");
				}
			}
			catch(Exception e)
			{
				System.out.println("Failed to connect");
			}
			LowEntry.sleep(10);
			if((i % 50) == 0)
			{
				System.out.println(connections.size() + " connected");
			}
		}
		
		
		FunctionCallListener functionCallListener = new FunctionCallListener()
		{
			@Override
			public void receivedResponse(SocketConnection connection, byte[] bytes)
			{
			}
			@Override
			public void failed(SocketConnection connection)
			{
			}
		};
		
		
		int loops = 0;
		while(connections.size() > 0)
		{
			loops++;
			System.out.println("[" + loops + "] " + connections.size() + " connected");
			Iterator<SocketConnection> iterator = connections.iterator();
			while(iterator.hasNext())
			{
				SocketConnection connection = iterator.next();
				if(connection.isConnected())
				{
					connection.sendFunctionCall(packet, functionCallListener);
					connection.listen(1);
				}
				else
				{
					iterator.remove();
				}
			}
		}
	}
}
