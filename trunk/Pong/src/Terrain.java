import java.io.IOException;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.TiledLayer;

/**
 * 
 * @author lherbin
 * est appelé a changé de rôle.
 * contiendra les données utiles pour batir un terrain
 */
public class Terrain 
{
	public static int STANDARD = 1;
	
	Image imMurs;
	Image imGoal;
	Image imRaquettePl1;
	Image imRaquettePl2;
	Image imBalle;
	
	private int type;
	
	int[] raquetteP1;
	int[] raquetteP2;
	
	PongCanvas pongCanvas;
	
	public Terrain(int type, PongCanvas pc)
	{
		pongCanvas = pc;
		this.type = type;
		loadImages();
	}

	
	public void build()
	{
		setMurs();
		setGoals();
		setRaquettes();
		setBalle();
		repositionne();
	}
	
	
	public void setBalle()
	{
		pongCanvas.balle = new Balle(imBalle,pongCanvas);
		pongCanvas.balle.setPosition(176-imBalle.getWidth()/2, 220-imBalle.getHeight()/2);
		pongCanvas.balle.setSpeed(4);
	}
	
	/**
	 * construit un terrain de base  
	 */
	
	public void setMurs()
	{
		if(type==STANDARD)
		{
		// mise en place des murs du terrain
		pongCanvas.murs = new TiledLayer(16, 20,imMurs, 11,11);
		pongCanvas.murs.fillCells(0,0,5,1,1);
		//terrain.fillCells(5,0,6,1,2);
		pongCanvas.murs.fillCells(11,0,5,1,1);
		pongCanvas.murs.fillCells(0, 0, 1, 20, 1);
		pongCanvas.murs.fillCells(15, 0, 1, 20, 1);;
		pongCanvas.murs.fillCells(0,19,5,1,1);
		//terrain.fillCells(5,19,6,1,3);
		pongCanvas.murs.fillCells(11,19,5,1,1);
		}
	}
	
	/**
	 * 
	 * @return 	result[0] est le goal du p1
	 * 		 	result[1] est le goal du p2
	 */
	public void setGoals()
	{
		if (type==STANDARD)
		{
			pongCanvas.p1.goal = new TiledLayer(6,1,imGoal,11,11);
			pongCanvas.p1.goal.fillCells(0,0,6,1,1);
			pongCanvas.p1.goal.setPosition(55, 0);
			
			pongCanvas.p2.goal= new TiledLayer(6,1,imGoal,11,11);
			pongCanvas.p2.goal.fillCells(0,0,6,1,2);
			pongCanvas.p2.goal.setPosition(55, 209);
		}
	}
	
	public void setRaquettes()
	{
		if(type == STANDARD)
		{
		// creation du player 1
		Raquette[] raquette = new Raquette[2];
		//p1 = new CPU(raquette,Player.UP,balle,p2);
		raquette[0] = new Raquette(imRaquettePl1, pongCanvas, pongCanvas.p1);
		//raquette[0].setPosition((176-imRaquettePl1.getWidth())/2, 180);
		raquette[0].setSpeed(5);
		raquette[1] = new Raquette(imRaquettePl1, pongCanvas, pongCanvas.p1);
		//raquette[1].setPosition((176-imRaquettePl1.getWidth())/2, 70);
		raquette[1].setSpeed(5);
		pongCanvas.p1.setRaquettes(raquette);
		
		// creation du player 2
		raquette = new Raquette[2];
		raquette[0] = new Raquette(imRaquettePl2, pongCanvas, pongCanvas.p2);
		//raquette[0].setPosition((176-imRaquettePl2.getWidth())/2, 40);
		raquette[0].setSpeed(5);
		raquette[1] = new Raquette(imRaquettePl2, pongCanvas, pongCanvas.p2);
		//raquette[1].setPosition((176-imRaquettePl2.getWidth())/2, 150);
		raquette[1].setSpeed(5);
		pongCanvas.p2.setRaquettes(raquette);
		}
	}
	
	
	
	public void repositionne()
	{
		if(type==STANDARD)
		{
			Raquette[] r = pongCanvas.p1.getRaquettes();
			r[0].setPosition((176-imRaquettePl1.getWidth())/2, 190);;
			r[1].setPosition((176-imRaquettePl1.getWidth())/2, 80);
			
			r = pongCanvas.p2.getRaquettes();
			r[0].setPosition((176-imRaquettePl2.getWidth())/2, 30);
			r[1].setPosition((176-imRaquettePl2.getWidth())/2, 140);
			
			pongCanvas.balle.setPosition((176-pongCanvas.balle.getWidth())/2, (220-pongCanvas.balle.getHeight())/2);
			pongCanvas.balle.setRandAngle();
		}
	}
	
	private void loadImages()
	{
		try{
			imRaquettePl1 = Image.createImage("/raquettePl1.png");
			imRaquettePl2 = Image.createImage("/raquettePl2.png");
			imMurs = Image.createImage("/terrain.png");
			imBalle = Image.createImage("/balle.png");
			imGoal = Image.createImage("/goal.png");
		}
		catch(IOException e)
		{
			System.out.println("pas bon : "+ e.getMessage());
		}
	}
	


}
