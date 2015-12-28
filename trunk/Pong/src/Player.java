import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.TiledLayer;


public class Player {
	
	public static final int UP = 1;
	public static final int DOWN= 2;
	
	int sens;
	int score;
	Raquette[] raquettes;
	TiledLayer goal;
	
	/**
	 * crée un nouveau joueur
	 * avec un nombre de raquettes = pos.length
	 * la raquette i étant positionnée en x=pos[i][0], y=pos[i][1]
	 * 
	 * 
	 */
	public Player(Raquette[] r, int sens)
	{
		raquettes = r;
		score =0;
		this.sens = sens;
	}
	
	void input(int keyState)
	{
		
		if((keyState & GameCanvas.LEFT_PRESSED) != 0)
		{
			moveLeft();
		}
		else if((keyState & GameCanvas.RIGHT_PRESSED) != 0)
		{
			moveRight();
		}
	}
	
	protected void moveLeft()
	{
		for( int i=0; i<raquettes.length; i++) if(!raquettes[i].left())break;
	}
	
	protected void moveRight()
	{
		for( int i=0; i<raquettes.length; i++) if(!raquettes[i].right())break;
	}
	
	protected void moveLeft(int x)
	{
		for( int i=0; i<raquettes.length; i++) if(!raquettes[i].left(x))break;
	}
	
	protected void moveRight(int x)
	{
		for( int i=0; i<raquettes.length; i++) if(!raquettes[i].right(x))break;
	}
	
	public Raquette[] getRaquettes()
	{
		return raquettes;
	}
	
	public int getScore() { return score; }
	public void marque() { score++;}
	public void setRaquettes(Raquette[] raquettes) {
		this.raquettes = raquettes;
	}
	
	public int getSens()
	{
		return sens;
	}
	
}
