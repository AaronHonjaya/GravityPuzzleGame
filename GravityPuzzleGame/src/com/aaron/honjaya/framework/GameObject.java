package com.aaron.honjaya.framework;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.*;

import gameMain.Game;

public abstract class GameObject {
	
	protected double x, y;
	protected ObjectType type; 
	protected double width, height;
	
	
	//edges of the window. Not used yet, but maybe in the future.
	protected final double LEFT_BOARDER = 0;
	protected final double RIGHT_BOARDER = Game.WIDTH - this.width;
	protected final double TOP_BOARDER = 0;
	protected final double BOTTOM_BOARDER = Game.HEIGHT - this.height;
	

	//every object has a UUID for identification. 
	protected UUID id;
	
	public GameObject() {};
	
	public GameObject(double x, double y, ObjectType type) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.id = UUID.randomUUID();
	}
	
	//every object ticks and renders. In some objects, tick may not do anything. 
	public abstract void tick();
	public abstract void render(Graphics g);
	
	//every object needs to have a hitbox. 
	public abstract Rectangle getBounds();


	
	

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
	

	


	
	
	

	
	
}
