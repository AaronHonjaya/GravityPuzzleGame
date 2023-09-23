package com.aaron.honjaya.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.UUID;

import com.aaron.honjaya.framework.GameObject;
import com.aaron.honjaya.framework.ObjectType;
import com.aaron.honjaya.utils.Constants;
import com.aaron.honjaya.utils.SpriteLoader;

import gameMain.Game;
import gameStates.Handler;
import gameStates.PlayingHandler;



public class Player extends GameObject{
	


	private BufferedImage player;
	private final double GRAVITY = 0.1;
	private final double maxVel = 5;
	private boolean isRightOrUpModel;
	private SpriteLoader ss;
	private boolean reachedFlag;
	private BufferedImage images[];
	private int imgIndex;
	

	
	private PlayingHandler handler;

	
	public Player(double x, double y, ObjectType type, PlayingHandler handler) {
		super(x, y, type);
		isRightOrUpModel = true;
		this.width = 32; 
		this.height = 32;
		this.handler = handler;
		reachedFlag = false;
		id = UUID.randomUUID();
		loadImages();
	}
	


	private void loadImages() {
		ss = new SpriteLoader(Constants.spriteSheet);
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
	public void render(Graphics g) {
		g.drawImage(images[imgIndex], (int)x, (int)y, null);
		
		
		Graphics2D g2d = (Graphics2D) g;
		g.setColor(Color.green);
		
		g2d.draw(getBounds());
		g2d.draw(getBoundsRight());
		g2d.draw(getBoundsLeft());
		g2d.draw(getBoundsTop());
		

	}
	
	@Override
	public void tick() {
		x += velX;
		y += velY;
		if(falling || jumping) {
			updateGrav();
		}
		//Collision(objects);
		checkDirection();
		
	}
	
	private void Collision(LinkedList<GameObject> objects) {
		for(int i = 0; i < objects.size(); i++) {
			
			GameObject temp = objects.get(i);
			
			if(temp.getType() == ObjectType.BLOCK) {
				//checkBlockCollision(temp);
			}
			else if(temp.getType() == ObjectType.PLAYER_D) {
				checkPlayerDCollision(temp);
			}
			else if(temp.getType() == ObjectType.PLAYER_R) {
				checkPlayerRCollision(temp);
			}
			else if( !reachedFlag) {
				if((temp.getType() == ObjectType.FLAG_R && this.type == ObjectType.PLAYER_R)
					|| (temp.getType() == ObjectType.FLAG_D && this.type == ObjectType.PLAYER_D)
					|| (temp.getType() == ObjectType.FLAG_U && this.type == ObjectType.PLAYER_U)
					|| (temp.getType() == ObjectType.FLAG_L && this.type == ObjectType.PLAYER_L)) {
						if(getBounds().intersects(temp.getBounds())) {
							reachedFlag = true;
							//handler.increaseNumPlayersFinished();
						}
				}
			}
		}
	}
	




	private void checkPlayerDCollision(GameObject pD) {
		
		if(type == ObjectType.PLAYER_L) {
			
			
			
		}else if(type == ObjectType.PLAYER_U) {
			
			
			
		}else if(type == ObjectType.PLAYER_R) {
			
			if(getBounds().intersects(pD.getBoundsTop())) {
				y = pD.getY() - height;
				
			}
			if(getBoundsRight().intersects(pD.getBoundsLeft())) {
				x = pD.getX()-width;
				velX = pD.getVelX();
				falling = false;
				jumping = false;
			}
			if(getBoundsLeft().intersects(pD.getBoundsRight())) {
				x = pD.getX()+width+5;
				
			}
			if(pD.getVelY() == 0 && getBoundsTop().intersects(pD.getBounds())) {
				y = pD.getY()+height;
			}
			
		}
	}
	private void checkPlayerRCollision(GameObject pR) {
		if(type == ObjectType.PLAYER_D) {
			if(getBounds().intersects(pR.getBoundsTop())) {
				y = pR.getY() - height;
				velY = pR.getVelY();
				falling = false;
				jumping = false;
			}
			if(getBoundsRight().intersects(pR.getBoundsLeft())) {
				velX = 0;
				x = pR.getX()-width;
			}
			if(getBoundsTop().intersects(pR.getBounds())) {
				y = pR.getY()+height;
			}
			if(pR.getVelX() == 0 && getBoundsLeft().intersects(pR.getBoundsRight())) {
				x = pR.getX()+width;
			}
			
		}else if(type == ObjectType.PLAYER_L) {
			
			
			
		}else if(type == ObjectType.PLAYER_U) {
			
			
		}
	}

	
	private void checkBlockCollision(GameObject block) {
		switch(type) {
			case PLAYER_D: 
				if(getBounds().intersects(block.getBounds())) {
					y = block.getY() - height;
					velY = 0;
					falling = false;
					jumping = false;
				}else if(type == ObjectType.PLAYER_D){
					falling = true;
				}
				if(getBoundsTop().intersects(block.getBounds())) { 
					y = block.getY() + height;
					velY = 0;
				}
				if(getBoundsRight().intersects(block.getBounds())) {
					x = block.getX()-width;
				}
				if(getBoundsLeft().intersects(block.getBounds())) {
					x = block.getX()+width;
				}
				break;
			case PLAYER_L: 
				
				break;
			case PLAYER_U: 
				
				break;
			case PLAYER_R: 
				if(getBoundsRight().intersects(block.getBounds())) {
					x = block.getX() - width;
					velX = 0;
					falling = false;
					jumping = false;
				}else if(type == ObjectType.PLAYER_R) {
					falling = true;
				}	
				
				if(getBoundsLeft().intersects(block.getBounds())) {
					x = block.getX() + width;
					velX = 0;
					
				}
				if(getBounds().intersects(block.getBounds())) {
					y = block.getY()-height;
					break;
				}

				if(getBoundsTop().intersects(block.getBounds())) {
					y = block.getY()+height;					
				}
				
				
		}
			
		//end of switch
		
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

	
	


	@Override
	public Rectangle getBounds() {
		switch(type) {
			//case player_D is default
			case PLAYER_L: 
				return new Rectangle((int)((int)x+(width/4)), (int)((int)y+(height/2)), (int)width/2, (int)height/2);
		
			case PLAYER_U: 
				return new Rectangle((int)((int)x+(width/4)), (int)((int)y+(height/2)), (int)width/2, (int)height/2);
			
			case PLAYER_R: 
				return new Rectangle((int)(x+5), (int)(y+height-7), (int)width-10, (int)7);
				
			default: 
				return new Rectangle((int)((int)x+(width/4)), (int)((int)y+(height/2)), (int)width/2, (int)(height/2));
			
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
			
			return new Rectangle((int)((int)x+(width/2)), (int)((int)y+(height/4)), (int)width/2, (int)height/2);

	
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
				return new Rectangle((int)x, (int)((int)y+(height/4)), (int)width/2, (int)height/2);

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
