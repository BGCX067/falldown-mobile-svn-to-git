import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Layer;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.game.TiledLayer;


public class Raquette extends Sprite
{
	
	
	PongCanvas canvas;
	int speed;
	
	int sens;
	Player player;
	
	public Raquette(Image im, PongCanvas canvas, Player p) {
		super(im);
		this.canvas = canvas; 
		this.player = p;
	}
	
	public boolean left()
	{
		return left(speed);
	}
	
	public boolean left(int speed)
	{
		Layer c;
		if(speed>this.speed)
			speed = this.speed;
		for (int i=0; i<speed;i++)
		{
			setPosition(getX()-1, getY());
			c=canvas.checkCollision(this);
			if(c!=null)
			{
				setPosition(getX()+1, getY());
				return false;
				/*if(c instanceof TiledLayer)
				{
					setPosition(getX()+1, getY());
					break;
				}
				else if(c instanceof Balle)
				{
					Balle b = (Balle)c;
					b.setPosition(b.getX()-1, b.getY());
				}*/
				
			}
		}	
		return true;
	}
	
	public boolean right()
	{
		return right(speed);
	}
	
	public boolean right(int speed)
	{
		Layer c;
		if(speed>this.speed)
			speed = this.speed;
		for (int i=0; i<speed;i++)
		{
			setPosition(getX()+1, getY());
			c=canvas.checkCollision(this);
			if(c!=null)
			{
				setPosition(getX()-1, getY());
				return false;
			}
		}
		return true;
		
	}
	
	public void setSpeed(int v){speed=v;}
	
	public int getSpeed(){return speed;}
	
	public int getSens(){return player.getSens();}
	
	public int Xcenter(){return getX()+getWidth()/2;}
	
}
