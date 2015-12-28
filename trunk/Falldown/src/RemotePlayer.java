import java.io.IOException;

import javax.bluetooth.L2CAPConnection;
import javax.bluetooth.RemoteDevice;


public class RemotePlayer extends Player{

	L2CAPConnection connection;
	String name;
	
	public RemotePlayer(L2CAPConnection c) {
		this.connection = c;
	}

	public int getMove() {
		// TODO Auto-generated method stub
		return Ball.STAY;
	}

	public String getName() {
		if(name == null)
		{
			try {
				name = RemoteDevice.getRemoteDevice(connection).getFriendlyName(true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (name == null)
			return "unknown";
		return name;
	}

}
