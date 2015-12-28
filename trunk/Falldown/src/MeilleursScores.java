import javax.microedition.rms.RecordStore;


public class MeilleursScores 
{
	int[] scores;
	String[] noms;
	String recordStoreName;
	
	public MeilleursScores(String gamePlayName)
	{
		recordStoreName = gamePlayName;
		load();
	}
	
	public void load()
	{
		try
		{
			scores = new int[5];
			noms = new String[5];
			RecordStore rs = RecordStore.openRecordStore(recordStoreName, true);
			int n = rs.getNumRecords();
			int i;
			for(i=0; i<n && i<10; i+=2)
			{
				byte[] b = rs.getRecord(i+1);
				if(b!=null)
				{
					noms[i/2]=new String(b);
					b = rs.getRecord(i+2);
					scores[i/2] = byteArrayToInt(b,0);
				}
			}
			for(;i<10;i+=2)
			{
				
				noms[i/2]="";
				scores[i/2]=0;
			}		
			rs.closeRecordStore();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void save()
	{
		try
		{
			RecordStore rs = RecordStore.openRecordStore(recordStoreName, true);
			int i;
			for(i=0; i<noms.length*2 && i<rs.getNumRecords(); i+=2)
			{
				byte[] b = noms[i/2].getBytes();
				rs.setRecord( i+1,b,0,b.length);
				b = IntToByteArray(scores[i/2]);
				rs.setRecord(i+2,b,0,b.length);
			}
			for(;i<noms.length*2;i+=2)
			{
				byte[] b = noms[i/2].getBytes();
				rs.addRecord(b,0,b.length);
				b = IntToByteArray(scores[i/2]);
				rs.addRecord(b,0,b.length);
			}
			rs.closeRecordStore();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	
    public static int byteArrayToInt(byte[] b, int offset) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (b[i + offset] & 0x000000FF) << shift;
        }
        return value;
    }
    
    public static byte[] IntToByteArray(int x) {
    	byte[] b = new byte[4];
        for(int i=0;i<4;i++)
        {
        	int shift = (4-1-i)*8;
        	b[i] = (byte)(x >> shift);
        }
    	return b;
    }
    
    public boolean isHigh(int score)
    {
    	return score>=scores[scores.length-1];
    }
    
    /*
     * ajoute le nom, score dans les meilleurs scores
     * et renvoie la position de ce r�sultat ex: si premier renvoie 1
     * si pas dans top, renvoie -1
     */
    public int add(String nom, int score)
    {
    	
    	if(scores == null) load();
    	
    	
    	if(scores[4]>score)return -1;
    	
    	int i;
    	int scoreTmp;
    	String nomTmp;
    	
    	scores[scores.length-1]=score;
    	noms[noms.length-1] = nom;
    	
    	
    	for(i=4; i>0 && score>=scores[i-1] ;i--)
    	{
    		scoreTmp = scores[i-1];
    		nomTmp  = noms[i-1];
    		scores[i-1]=scores[i];
    		noms[i-1]=noms[i];
    		noms[i]=nomTmp;
    		scores[i]=scoreTmp;
    	}
    	save();
    	return i+1;
    }
    
    public String toString()
    {

    	StringBuffer s = new StringBuffer();
    	for(int i=0; i<noms.length;i++)
    	{
    		s.append(i+1);
    		s.append(" : ");
    		s.append( noms[i] );
    		s.append("  ");
    		s.append( scores[i] );
    		s.append("\n");
    	}
    	return s.toString();
    }
    
    
}
