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
	
	
	protected final double LEFT_BOARDER = 0;
	protected final double RIGHT_BOARDER = Game.WIDTH - this.width;
	protected final double TOP_BOARDER = 0;
	protected final double BOTTOM_BOARDER = Game.HEIGHT - this.height;
	

	protected UUID id;
	
	public GameObject() {};
	
	public GameObject(double x, double y, ObjectType type) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.id = UUID.randomUUID();
	}
	
	public abstract void update();
	public abstract void render(Graphics g);
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
