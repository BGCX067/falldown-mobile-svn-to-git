
public class LocalGame extends Game
{

	public LocalGame(GamePlay gp, LocalPlayer p)
	{
		this.players = new Player[]{p};
		this.gamePlay = gp;
		init();
	}
	
	void checkOver() 
	{
		for (int i=0; i<players.length; i++)
		{
			if(players[i].getBall().getY()>HEIGHT)
				over=true;
		}
	}



}