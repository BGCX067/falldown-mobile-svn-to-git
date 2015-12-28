import java.util.Random;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.Layer;
import javax.microedition.lcdui.game.TiledLayer;


public class CPU extends Player implements Runnable {

	
	Player adv;
	Random gen;
	int hasard = 3;
	PongCanvas pc;
	long delay = 50;
	long lastMove=0;
	
	Balle simuBalle;
	
	int targetX;
	Raquette targetRaquette;
	
	public CPU(Raquette[] r, int sens,PongCanvas pc, Player adv) {
		super(r,sens);
		this.adv = adv;
		this.pc = pc;
		gen = new Random();
	}
	
	public void init()
	{
		
		sort(raquettes);
		simuBalle = new Balle(pc.terrain.imBalle,pc);
		simuBalle.max_speed=1;
		simuBalle.min_speed=1;
		simuBalle.impulsion=0;
		targetRaquette = getConcernedRaquette();
		evalueTargetX();
		Thread t = new Thread(this);
		t.start();
	}
	
	void input(int keyState) {	
		
		/*if(targetRaquette.getX()+targetRaquette.getWidth()<targetX)
		{
			moveRight();
		}
		else if (targetRaquette.getX()>targetX)
		{
			moveLeft();
		}*/
		
		// bouger uniquement si le delai est écoulé afin d'éviter un CPU trop rapide
		if(System.currentTimeMillis() - lastMove > delay)
		{			
			lastMove = System.currentTimeMillis();
		}
		else
		{
			return;
		}
		
		
		/*if(sens == DOWN && pc.balle.getY()>220/2)
		{
			//balle dans la partie bas gauche
			if((pc.balle.getX()<172/2-15 && pc.balle.getX()>30) || pc.balle.getX()>220-30-pc.balle.Xcenter())
			{
				if(raquettes[1].getX()+raquettes[1].getWidth()-10<pc.balle.Xcenter())
				{
					moveRight(raquettes[1].getX()+raquettes[1].getWidth()-pc.balle.Xcenter());
				}
				else if (raquettes[1].getX()+raquettes[1].getWidth()>pc.balle.Xcenter())
				{
					moveLeft(raquettes[1].getX()-raquettes[1].getWidth()-pc.balle.Xcenter());
				}
			}
			//			balle dans la partie bas centre
			else if((pc.balle.getX()>172/2-15)&& (pc.balle.getX()<172/2+15))
			{
				if(raquettes[1].getX()+raquettes[1].getWidth()<pc.balle.Xcenter())
				{
					moveRight(pc.balle.Xcenter()-raquettes[1].getX()-raquettes[1].getWidth());
				}
				else if (raquettes[1].getX()>pc.balle.Xcenter())
				{
					moveLeft(raquettes[1].getX()-pc.balle.Xcenter());
				}
			}
			//			balle dans la partie bas droite
			else
			{
				if(raquettes[1].getX()<pc.balle.Xcenter())
				{
					moveRight(pc.balle.Xcenter()-raquettes[1].getX());
				}
				else if (raquettes[1].getX()+raquettes[1].getWidth()>pc.balle.Xcenter())
				{
					moveLeft(raquettes[1].getX()+raquettes[1].getWidth()-pc.balle.Xcenter());
				}
			}
		}
		else
		{*/
			if(raquettes[0].getX()+raquettes[0].getWidth()<pc.balle.Xcenter())
			{
				moveRight(pc.balle.Xcenter()-raquettes[0].getX()+raquettes[0].getWidth());
			}
			else if(raquettes[0].getX()>pc.balle.Xcenter())
			{
				moveLeft(raquettes[0].getX()-pc.balle.Xcenter());
			}
		//}
	}
	
	public void setAdversaire(Player adv) {
		this.adv = adv;
	}
	
	/**
	 * déterminer la raquette concernée par la prochaine action de l'ia
	 * (la plus proche "temporellement de la balle")
	 * celle vers qui la balle se dirige
	 */
	private Raquette getConcernedRaquette()
	{
			if (pc.balle.getY()>raquettes[0].getY())
			{
				return raquettes[0];
			}
			else if (pc.balle.getY()<raquettes[1].getY())
			{
				return raquettes[1];
			}
			else
			{
				if (Math.sin(pc.balle.getAngle())>0.0)
				{
					return raquettes[0];
				}
				else
				{
					return raquettes[1];
				}
			}
	}
	/**
	 * 
	 * POST:
	 * 		r[0].y > r[1].y
	 */
	private void sort(Raquette[] r)
	{
		if(r[1].getY() > r[0].getY())
		{
			Raquette temp = r[1];
			r[1]=r[0];
			r[0]=temp;
		}
	}
	
	/**
	 * détermine a quel endroit (x) la balle croisera l'axe y de la raquette r
	 */
	private int getXofIntersect(Raquette r)
	{
			int c=0;
			System.out.println("marche ?");
			while(targetRaquette.getY() != simuBalle.getY())
			{
				simuBalle.move();
				c++;
				if(c%30==0)
				{
						Thread.yield();
						targetRaquette = getConcernedRaquette();
				}
				System.out.println(""+targetRaquette.getY()+" "+simuBalle.getY());
			}
			return simuBalle.Xcenter();
	}
	
	
	
	private void positionneSimuBalle()
	{
		simuBalle.setPosition(pc.balle.getX(), pc.balle.getY());
		simuBalle.setAngle(pc.balle.getAngle());
		simuBalle.setSpeed(1);
	}
	/**
	 * targetX contiendra au retour la méthode la position à laquelle le CPU doit se placer
	 * avec sa targetRaquette
	 */
	private void evalueTargetX()
	{
		positionneSimuBalle();
		targetX = getXofIntersect(targetRaquette);
		System.out.println(""+targetX);
	}

	public void run() 
	{
		while(true)
		{
			targetRaquette = getConcernedRaquette();
			if(simuBalle.getAngle() != pc.balle.getAngle())
			{
				evalueTargetX();
			}

			try
			{
				Thread.sleep(10);
			}
			catch (InterruptedException e){}
		}
	}
}
