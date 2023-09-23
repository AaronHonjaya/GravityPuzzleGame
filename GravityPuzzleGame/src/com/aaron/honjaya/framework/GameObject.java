package com.aaron.honjaya.framework;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.*;

import gameMain.Game;

public abstract class GameObject {
	
	protected double x, y;
	protected ObjectType type; 
	protected double velX, velY;
	protected double width, height;
	
	
	protected final double LEFT_BOARDER = 0;
	protected final double RIGHT_BOARDER = Game.WIDTH - this.width;
	protected final double TOP_BOARDER = 0;
	protected final double BOTTOM_BOARDER = Game.HEIGHT - this.height;
	
	protected boolean jumping = false;
	protected boolean falling = true;
	protected UUID id;
	
	public GameObject() {};
	
	public GameObject(double x, double y, ObjectType type) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.id = UUID.randomUUID();
	}
	
	
	public abstract void tick();
	public abstract void render(Graphics g);

	
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

	
	

	public UUID getID() {
		return this.id;
	}
	
	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public ObjectType getType() {
		return this.type;
	}
	
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	
	public void setX(double x) {
		this.x = x;
		
	}
	public void setY(double y) {
		this.y = y;
	}
	
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
	
	public abstract Rectangle getBounds();
	public abstract Rectangle getBoundsTop();
	public abstract Rectangle getBoundsRight();
	public abstract Rectangle getBoundsLeft();
	
	

	
	
}
