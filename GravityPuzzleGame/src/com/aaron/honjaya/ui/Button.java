package com.aaron.honjaya.ui;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.aaron.honjaya.utils.Constants;

import gameStates.GameState;

public abstract class Button {
	
	protected double x, y;
	protected GameState state;
	protected boolean mouseOver;
	protected boolean mousePressed;
	protected static int width;
	protected static int height;
	
	public Button() {};
	
	public Button(double x, double y, GameState state) {
		this.x = x;
		this.y = y;
		this.state = state;
	}
	
	public abstract void tick();
	public abstract void render(Graphics g);
	
	public void applyGameState() {
		GameState.state = state;
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void resetBools() {
		mouseOver = false;
		mousePressed = false;
	}


	public boolean isMouseOver() {
		return mouseOver;
	}

	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}

	public boolean isMousePressed() {
		return mousePressed;
	}

	public void setMousePressed(boolean mousePressed) {
		this.mousePressed = mousePressed;
	}



	public static int getWidth() {
		return width;
	}

	public static int getHeight() {
		return height;
	}
	

	public abstract Rectangle getBounds();

}
