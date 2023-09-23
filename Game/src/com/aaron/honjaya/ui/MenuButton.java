package com.aaron.honjaya.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import com.aaron.honjaya.framework.GameObject;
import com.aaron.honjaya.framework.ObjectType;
import com.aaron.honjaya.utils.Constants;
import com.aaron.honjaya.utils.SpriteLoader;

import gameStates.GameState;

public class MenuButton {
	
	private double x, y;
	private int rowIndex, imgIndex;
	private int xOffsetCenter = SpriteLoader.BUTTON_WIDTH / 2;
	private GameState state;
	private BufferedImage[] images;
	private boolean mouseOver, mousePressed;
	
	public MenuButton(double x, double y, int rowIndex, GameState state) {
		this.x = x;
		this.y = y;
		this.rowIndex = rowIndex;
		this.state = state;
		loadImages();
	}
	
	private void loadImages() {
		SpriteLoader sl = new SpriteLoader(Constants.buttonSheet);
		images = new BufferedImage[3];
		for(int i = 0; i < images.length; i++) {
			images[i] = sl.grabButtonImage(i, rowIndex);
		}
	}

	
	public void tick() {
		imgIndex = 0;
		if(mouseOver)
			imgIndex = 1;
		if(mousePressed)
			imgIndex = 2;
	}
	

	public void render(Graphics g) {
		g.drawImage(images[imgIndex], (int)(x - xOffsetCenter), (int)y, null);		
		Graphics2D g2d = (Graphics2D) g;
		g.setColor(Color.green);
		
		g2d.draw(getBounds());
	}
	
	public void applyGameState() {
		GameState.state = state;
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

	
	public Rectangle getBounds() {
		return (new Rectangle((int)(x-xOffsetCenter), (int)y, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT));
	}
	
	
	
	


	
	
}
