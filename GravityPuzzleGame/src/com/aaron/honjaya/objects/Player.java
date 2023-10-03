package com.aaron.honjaya.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.UUID;

import com.aaron.honjaya.framework.GameObject;
import com.aaron.honjaya.framework.MovingObject;
import com.aaron.honjaya.framework.ObjectType;
import com.aaron.honjaya.utils.Constants;
import com.aaron.honjaya.utils.SpriteLoader;

import gameMain.Game;
import gameStates.Handler;
import gameStates.PlayingHandler;



public class Player extends MovingObject{
	


	private BufferedImage player;
	private final double GRAVITY = 0.5;
	private final double maxVel = 10;
	private boolean isRightOrUpModel;
	private SpriteLoader ss;
	private boolean reachedFlag;
	private BufferedImage images[];
	private int imgIndex;
	private double futureX, futureY;
	private boolean inAir;
	
	public boolean isInAir() {
		return inAir;
	}



	public void setInAir(boolean inAir) {
		this.inAir = inAir;
	}



	public Player(double x, double y, ObjectType type) {
		super(x, y, type);
		isRightOrUpModel = true;
		reachedFlag = false;
		id = UUID.randomUUID();
		
		loadImages();
		this.width = images[0].getWidth(); 
		this.height = images[0].getHeight();
	}
	
	@Override 
	public void tick() {
		checkDirection();
	}
	
	
	@Override
	public void render(Graphics g) {
		
		g.drawImage(images[imgIndex], (int)x, (int)y, null);
		
		Graphics2D g2d = (Graphics2D) g;
		g.setColor(Color.green);
		
		//g2d.draw(getBoundsBottom());
		g2d.draw(getBounds());
		
		

	}
	
	@Override
	public void updateXPos() {
		x += velX;
	}
	
	@Override
	public void updateYPos() {
		y += velY;
	}
	
	

	





	public void updateGrav() {
		switch(type) {
			case PLAYER_L: 
				velX -= GRAVITY;
				if(Math.abs(velX) >= maxVel) {
					velX = -maxVel;
				}
				break;
			case PLAYER_U: 
				velY -= GRAVITY;
				if(Math.abs(velY) >= maxVel) {
					velY = maxVel;
				}
				break;
			case PLAYER_R: 
				velX += GRAVITY;
				if(Math.abs(velX) >= maxVel) {
					velX = maxVel;
				}
				break;
			case PLAYER_D: 
				velY += GRAVITY;
				if(Math.abs(velY) >= maxVel) {
					velY = maxVel;
				}
				break;
		}
	}

	
	@Override
	public void setVelX(double velX) {
		super.setVelX(velX);

		if(velX != 0 && (type == ObjectType.PLAYER_D || type == ObjectType.PLAYER_U )) {
			isRightOrUpModel = (velX > 0);
		}
	}
	
	@Override
	public void setVelY(double velY) {
		super.setVelY(velY);
		if(velY != 0 && (type == ObjectType.PLAYER_R || type == ObjectType.PLAYER_L)) {
			isRightOrUpModel = (velY < 0);
		}
	}
	
	public void checkDirection() {
		if(isRightOrUpModel) {
			imgIndex = 0;
		}else{
			imgIndex = 1;
		}
	}

	
	private void loadImages() {
		ss = new SpriteLoader(Constants.SPRITE_SHEET);
		int col = 0;
		switch(type) {
			case PLAYER_D: 
				col = 0;
				break;
			case PLAYER_L: 
				col = 1;
				break;
			case PLAYER_U: 
				col = 2;
				break;
			case PLAYER_R: 
				col = 3;
				break;
		}
		images = new BufferedImage[2];
		for(int i = 0; i < 2; i++) {
			images[i] = ss.grabSpriteImage(col, i);
		}
		imgIndex = 0;
	}
	
	

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, (int)width, (int)height);
	}


	@Override
	public Rectangle getBoundsBottom() {
		switch(type) {
			//case player_D is default
			case PLAYER_L: 
				return new Rectangle((int)((int)x+(width/4)), (int)((int)y+(height/2)), (int)width/2, (int)height/2);
		
			case PLAYER_U: 
				return new Rectangle((int)((int)x+(width/4)), (int)((int)y+(height/2)), (int)width/2, (int)height/2);
			
			case PLAYER_R: 
				return new Rectangle((int)((int)x+(width/2)), (int)((int)y+(height/4)), (int)width/2+2, (int)height/2);

				//return new Rectangle((int)(x+5), (int)(y+height-7), (int)width-10, (int)7);
				
			default: 
				return new Rectangle((int)((int)x+(width/4)), (int)((int)y+(height/2)), (int)width/2, (int)(height/2)+2);
			
		}
		
	}
	public Rectangle getBoundsTop() {
		
		switch(type) {
		//case Player_d is default;		
		case PLAYER_L: 
			return new Rectangle((int)((int)x+(width/4)), (int)((int)y+(height/2)), (int)width/2, (int)height/2);
	
		case PLAYER_U: 
			return new Rectangle((int)((int)x+(width/4)), (int)((int)y+(height/2)), (int)width/2, (int)height/2);
		
		case PLAYER_R: 
			return new Rectangle((int)(x+5), (int)y, (int)width-10, (int)7);

		
		default: 
			return new Rectangle((int) ((int)x+(width/4)), (int)y, (int)width/2, (int)height/2);
		}
	}
	
	public Rectangle getBoundsRight() {
		
		switch(type) {
		//case player_D is default

		case PLAYER_L: 
			return new Rectangle((int)((int)x+(width/4)), (int)((int)y+(height/2)), (int)width/2, (int)height/2);
	
		case PLAYER_U: 
			return new Rectangle((int)((int)x+(width/4)), (int)((int)y+(height/2)), (int)width/2, (int)height/2);
		
		case PLAYER_R: 
			
			return new Rectangle((int)((int)x+(width/2)), (int)((int)y+(height/4))+1, (int)width/2, (int)height/2-2);

	
		default: 

			return new Rectangle((int)(x+width-7), (int)y+5, (int)7, (int)height-10);
		
		}
	}
	
	public Rectangle getBoundsLeft() {
		switch(type) {
		//case player_D is default

			case PLAYER_L: 
				return new Rectangle((int)((int)x+(width/4)), (int)((int)y+(height/2)), (int)width/2, (int)height/2);
		
			case PLAYER_U: 
				return new Rectangle((int)((int)x+(width/4)), (int)((int)y+(height/2)), (int)width/2, (int)height/2);
			
			case PLAYER_R: 
				return new Rectangle((int)x, (int)((int)y+(height/4))+1, (int)width/2, (int)height/2-2);

			default: 
				return new Rectangle((int)x, (int)y+5, (int)7, (int)height-10);
				
		}
	}
	
	



	
	public boolean hasReachedFlag() {
		return reachedFlag;
	}

	public void setReachedFlag(boolean reachedFlag) {
		this.reachedFlag = reachedFlag;
	}

	@Override
	public ObjectType getType() {
		return this.type;
	}





	
	
	
	
	
	
	
}
