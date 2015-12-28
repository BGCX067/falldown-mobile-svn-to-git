//import java.awt.Point;


public class MoveHandler 
{
	Player[] players; 
	LinkedList floors;
	Game game;
	
	public MoveHandler(Game g,Player[] players, LinkedList floors)
	{
		this.players = players;
		this.floors = floors;
		this.game = g;
	}
	
	public void move(Ball b, double dx, double dy)
	{
		moveVertical(b, dy);
		moveHorizontal(b, dx);
	}
	
	private void moveHorizontal(Ball b, double dx)
	{
		if(dx==0)
			return;
		
		double oldX = b.getX();
		b.setX(b.getX()+dx);
		
		if(b.getX()+b.getWidth()>=game.WIDTH) b.setX(game.WIDTH-b.getWidth()-1);
		if(b.getX()<0) b.setX(0);
		
		Floor f = collideWithAFloor(b);
		
		if(f!=null)
		{
			b.setX(oldX);
		}
	}
	
	private void moveVertical(Ball b, double dy)
	{
		
		double oldY = b.getY();
		b.setY(b.getY() + dy);
		
		Floor f = collideWithAFloor(b);
		if(f!=null)
		{
			b.setY(f.getY()+f.getHeight());
		}
		if(b.getY()<0)  b.setY(0);
	}
	
	public void move(Floor f, double dy)
	{
		double newY = f.getY() + dy;
		f.setY(newY);
		
		for(int i = 0; i<players.length; i++)
		{
			if(f.collideWith(players[i].getBall()))
			{
				players[i].getBall().setY(f.getY()+f.getHeight());
			}
		}
	}
	
	private Floor collideWithAFloor(Ball b)
	{
		Iterator it = floors.iterator();
		
		while(it.hasNext())
		{
			Floor f =(Floor) it.next();
			
			if(f.collideWith(b))
			{
				return f;
			}
		}
		return null;
	}
	
}
