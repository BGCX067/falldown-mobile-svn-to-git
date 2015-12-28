import javax.microedition.lcdui.StringItem;


public class StringPanel {
	StringItem strItem = new StringItem("Derniers messages\n","");
	int n = 0;
	String[] strings = new String[5];
	
	public StringPanel()
	{
	}
	
	public void append(String s)
	{
		if(n<strings.length)
		{
			strings[n]=s;
			n++;
		}
		else
		{
			for(int i=1; i<strings.length;i++)
			{
				strings[i-1]=strings[i];
			}
			strings[strings.length-1]=s;
		}
		StringBuffer strBuff = new StringBuffer();
		for(int i=0; i<n;i++)
		{
			strBuff.append(strings[i]);
			strBuff.append("\n");
		}
		strItem.setText(strBuff.toString());
	}
	
	public StringItem getStringItem()
	{
		return strItem;
	}
}
