import java.util.Random;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Layer;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.game.TiledLayer;


public class Balle extends Sprite {

	int speed;
	double angle; // en radian
	double reste_x;
	double reste_y;
	PongCanvas canvas;
	boolean dansRaquette = false;
	Random r;

	int max_speed = 9;
	int min_speed = 4;
	int impulsion = 2;
	long decelerationTime = 400;
	long lastDeceleration;
	
	public Balle(Image image, PongCanvas canvas) {
		super(image);
		this.canvas = canvas;
		reste_x=reste_y=0;
		r=new Random();
		setRandAngle();
		lastDeceleration=System.currentTimeMillis();
	}
	
	public void move()
	{
		if (System.currentTimeMillis() - lastDeceleration > decelerationTime && speed > min_speed)
		{
			speed--;
			lastDeceleration = System.currentTimeMillis();
		}
		
		for(int i=0; i<speed; i++)
		{
			
			// calcul du déplacement vertical (axe y)
			double dep = Math.sin(angle)+reste_y;
			int dy = (int)Math.floor(dep); 
			setPosition(getX(),getY()+dy);
			
			// verification que la balle ne rencontre pas d'obstacle
			Layer obstacle = canvas.checkCollision(this);
			if (obstacle != null)
			{
				if (obstacle instanceof TiledLayer)
				{
					
					setPosition(getX(),getY()-dy);
					rebond_hor();
				}
				else if (obstacle instanceof Raquette)
				{
					
					
					if (dansRaquette==false)
					{
						if(speed+impulsion > max_speed)
						{
							speed = max_speed;
						}
						else
						{
							speed+=impulsion;;
						}
						
						setPosition(getX(),getY()-dy);
						rebond_raquette_hor( (Raquette) obstacle);
					}
					else
					{
						reste_y= dep-dy;
					}
				}
			}
			else
			{
				if (dansRaquette == true) dansRaquette = false;
				reste_y= dep-dy; // le déplacement est valide et on comptabilise le reste
			}
			
			// calcul du déplacemetn horizontal (axe x)
			dep = Math.cos(angle)+reste_x;
			int dx = (int)Math.floor(dep); 
			setPosition(getX()+dx,getY());
			// verification que la balle ne rencontre pas d'obstacle
			obstacle = canvas.checkCollision(this);
			if (obstacle != null)
			{
				if (obstacle instanceof TiledLayer)
				{
					setPosition(getX()-dx,getY());
					rebond_vert();
				}
				else if (obstacle instanceof Raquette)
				{
					if( !dansRaquette)
					{
						setPosition(getX()-dx,getY());
						rebond_vert();
					}
					else
					{
						reste_x = dep-dx;
					}
						
				}
			}
			else
			{
				reste_x = dep-dx;
			}
		}
	}
	
	public void setSpeed(int s)
	{
		speed = s;
	}
	
	public void rebond_vert()
	{
		setAngle(Math.PI-angle);
	}
	
	public void rebond_hor()
	{
		setAngle(-angle);
	}
	
	public void setAngle(double angle)
	{
		while (angle>Math.PI*2)
		{
			angle-=Math.PI*2;
		}
		while(angle<0)
		{
			angle+=Math.PI*2;
		}
		this.angle=angle;
	}
	
	public void rebond_raquette_hor(Raquette r)
	{
		dansRaquette=true;
		
		int center = (getX()+getWidth()/2);
		int x = r.getX();
		if (center <= x+1)
		{
			setAngle(2.268928028);
		}
		else if(center >= x+2 && center <= x+6)
		{
			setAngle(1.9198621);
		}
		else if (center >= x+7 && center <= x+12)
		{
			setAngle(1.570796);
		}
		else if (center >= x+13 && center <= x+17)
		{
			setAngle(1.22173);
		}
		else setAngle(0.872664626);
		
		if(r.getSens()==Player.UP)
		{
			setAngle(2.0*Math.PI-getAngle());
		}
	}
	
	public double getAngle()
	{
		return angle;
	}
	
	public void setRandAngle()
	{
		double d = r.nextDouble()*Math.PI/2.0-Math.PI/4;
		if(r.nextInt()%2==0)
		{
			setAngle(d+Math.PI/2);
		}
		else
		{
			setAngle(d-Math.PI/2);
		}
		
		
		
		//setAngle(r.nextDouble()*2*Math.PI);
	}
	
	public int Xcenter()
	{
		return getX()+getWidth()/2;
	}
	
	public boolean move(int num)
	{
		int sx = getX();
		int sy = getY();
		
		double depy = Math.sin(angle)+reste_y;
		int dy = (int)Math.floor(depy); 
		setPosition(getX(),getY()+dy);
		
		
		
		double depx = Math.cos(angle)+reste_x;
		int dx = (int)Math.floor(depx); 
		setPosition(getX()+dx,getY());
		
		
		Layer obstacle = canvas.checkCollision(this);
		if (obstacle != null)
		{
			setPosition(sx, sy);
			return false;
		}
		else
		{
			reste_y= depy-dy;
			reste_x= depx-dx;
			return true;
		}
		
	}

}
