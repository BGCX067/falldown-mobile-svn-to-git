import javax.bluetooth.L2CAPConnection;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;


public class ClientDisplay implements Runnable, CommandListener{
	Client client;
	StringPanel strPanel = new StringPanel();
	StringItem strAffichage = strPanel.getStringItem();
	TextField txtfieldSend = new TextField("to send :","",40,TextField.ANY);
	Form formClient = new Form("Client", new Item[]{txtfieldSend,strAffichage});
	
	Chat chat;
	
	public ClientDisplay(Chat c, Client client)
	{
		formClient.addCommand(c.cmdSend);
		formClient.addCommand(c.cmdExit);
		formClient.setCommandListener(this);
		chat = c;
		this.client = client;
	}
	
	public void run() {
		try {
			while(true)
			{
				if(client != null && client.isConnecte())
				{
					String s = client.receive();
					if(s!=null) strPanel.append(s);
				}
				
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
		public void commandAction(Command c, Displayable d) {
			if (d == formClient)
			{
				if(c==chat.cmdSend)
				{
					if(client.isConnecte())
						client.send(txtfieldSend.getString());
					txtfieldSend.setString("");
				}
				else if (c==chat.cmdExit)
				{
					if(client.isConnecte())
						client.close();
					chat.notifyDestroyed();
				}
			}
		}
		
}
	
