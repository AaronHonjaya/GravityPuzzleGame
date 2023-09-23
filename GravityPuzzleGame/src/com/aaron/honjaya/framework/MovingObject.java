package com.aaron.honjaya.framework;

import java.awt.Rectangle;

public abstract class MovingObject extends GameObject{
	
	public MovingObject() {
		
	}
	public MovingObject(double x, double y, ObjectType type) {
		super(x, y, type);
	}
	protected double velX, velY;
	protected boolean jumping = false;
	protected boolean falling = true;
	
	public abstract void update();
	public abstract Rectangle getBounds();
	public abstract Rectangle getBoundsTop();
	public abstract Rectangle getBoundsRight();
	public abstract Rectangle getBoundsLeft();
	
	public void setVelX(double velX) {
		this.velX = velX;
	}
	public void setVelY(double velY) {
		this.velY = velY;
	}
	public double getVelX() {
		return this.velX; 
	}
	public double getVelY() {
		return this.velY;
	}
	
	public boolean isJumping() {
		return jumping;
	}

	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}

	public boolean isFalling() {
		return falling;
	}

	public void setFalling(boolean falling) {
		this.falling = falling;
	}

}
