package com.ks.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import com.ks.myexceptions.NetExceptions;

public class TcpNet {
	
	private Socket client=null;
	private DataInputStream dataInputStream=null;
	private DataOutputStream dataOutputStream=null;
	private boolean isConnecting=false;
	private static TcpNet instance=null;
	private TcpNet(){};
	public static TcpNet getInstance()
	{
		if(instance==null)
		{
			synchronized (TcpNet.class) 
			{
				if(instance==null)
				{
					instance=new TcpNet();
				}
					
			}
		}
		return instance;
		
	}
	public boolean connect2Server(SocketAddress address,int timeOut) throws NetExceptions
	{
		client=new Socket();
		try{
			client.setReuseAddress(true);
			client.connect(address,timeOut);
			dataInputStream=new DataInputStream(client.getInputStream());
			dataOutputStream=new DataOutputStream(client.getOutputStream());
		}
		catch(IOException e)
		{
			throw new NetExceptions(e.getMessage());
		}
		if(!client.isClosed() && client.isConnected())
		{
			isConnecting=true;
		}
		return isConnecting;
	}
	
	
	public DataInputStream getInputStream()
	{
		return this.dataInputStream;
	}
	
	
	public DataOutputStream getOutputStream()
	{
		return this.dataOutputStream;	
	}

	public boolean isConnecting()
	{
		return this.isConnecting;
	}
}
