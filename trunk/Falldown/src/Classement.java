import java.util.Vector;


public class Classement {

		private Vector joueurs;
		private Vector scores;
		
		public Classement()
		{
			joueurs = new Vector(5);
			scores = new Vector(5);
		}
		
		// ajoute un joueur dans le classement en conservant le classment tri�
		public void add(String playerName, int score)
		{
			if(joueurs.contains(playerName)) // si le joueur est d�j� dans le classement on ajoute le nouveau score au score pr�c�dent
			{
				int i = joueurs.indexOf(playerName);
				int s = score + ((Integer) scores.elementAt(i)).intValue();
				scores.setElementAt(new Integer(s), i);
				
				Object tmp;
		
				
				// remetre le classement dans le bon ordre via multiples swap
				for(;i>0 &&  s>((Integer)scores.elementAt(i-1)).intValue() ; i--) 
				{
					tmp = joueurs.elementAt(i-1);
					joueurs.setElementAt(joueurs.elementAt(i), i-1);
					joueurs.setElementAt(tmp, i);
					
					tmp = scores.elementAt(i-1);
					scores.setElementAt(scores.elementAt(i), i-1);
					scores.setElementAt(tmp, i);
				}
			}
			else
			{
				int i;
				for(i=0; i<scores.size() && ((Integer) scores.elementAt(i)).intValue() > score; i++);
				Object jtmp = playerName;
				Object stmp = new Integer(score);
				Object jtmp2;
				Object stmp2;
				for(;i<scores.size();i++)
				{
					jtmp2 = joueurs.elementAt(i);
					joueurs.setElementAt(jtmp, i);
					jtmp = jtmp2;

					stmp2 = scores.elementAt(i);
					scores.setElementAt(stmp, i);
					stmp = stmp2;
				}
				joueurs.addElement(jtmp);
				scores.addElement(stmp);
			}
		}
		
		public void merge(Classement c)
		{
			for(int i=0; i<c.joueurs.size(); i++)
			{
				add((String)c.joueurs.elementAt(i),((Integer)c.joueurs.elementAt(i)).intValue());
			}
		}
		
		public String toString()
		{
			StringBuffer s = new StringBuffer();
			for(int i=0; i<joueurs.size();i++)
			{
				s.append("\n");
				s.append(i+1);
				s.append(" : ");
				s.append( ((String)joueurs.elementAt(i)) );
				s.append("  ");
				s.append( ((Integer) scores.elementAt(i)).intValue() );
			}
			return s.toString();
		}
		
		public int taille()
		{
			return joueurs.size();
		}
		
		public String joueursAt(int i)
		{
			return (String)joueurs.elementAt(i);
		}
		
		public int scoreAt(int i)
		{
			if(i<scores.size())
				return ((Integer)scores.elementAt(i)).intValue();
			else
				return 0;
		}
		
		public int scoreOf(String j)
		{
			if(joueurs.contains(j))
			{
				return ((Integer)scores.elementAt(joueurs.indexOf(j))).intValue();
			}
			else
			{
				return 0;
			}
		}
}
