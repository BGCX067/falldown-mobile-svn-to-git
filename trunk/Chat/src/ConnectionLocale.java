import java.io.IOException;

import javax.bluetooth.L2CAPConnection;


public class ConnectionLocale implements L2CAPConnection {
	private ConnectionLocale connection;
	private boolean ready = false;
	byte[] buffer;
	
	public int getReceiveMTU() throws IOException {
		return 512;
	}

	public int getTransmitMTU() throws IOException {
		// TODO Auto-generated method stub
		return 512;
	}

	public boolean ready() throws IOException {
		return ready;
	}

	public int receive(byte[] b) throws IOException {
		while(!ready)
		{
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		ready=false;
		for(int i=0; i<buffer.length && i<b.length; i++)
		{
			b[i]=buffer[i];
		}
		return buffer.length;
	}

	public void send(byte[] b) throws IOException {
		// TODO Auto-generated method stub
		connection.setBuffer(b);
	}

	public ConnectionLocale getConnection() {
		return connection;
	}

	public void setConnection(ConnectionLocale connection) {
		this.connection = connection;
	}

	public void close() throws IOException {
		
	}
	
	public void setBuffer(byte[] b)
	{
		buffer = new byte[b.length];
		for(int i=0; i<b.length; i++)
		{
			buffer[i]=b[i];
		}
		ready = true;
	}
	

}
