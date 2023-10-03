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
	protected boolean left, up, right, down;
	
	public abstract Rectangle getBounds();
	public abstract Rectangle getBoundsBottom();

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

	public void updateXPos() {
		x += velX;
	}

	public void updateYPos() {
		y += velY;
	}
}
