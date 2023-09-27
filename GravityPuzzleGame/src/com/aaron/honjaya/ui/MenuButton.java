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

public class MenuButton extends Button{
	
	private int rowIndex, imgIndex;
	private int xOffsetCenter = SpriteLoader.BUTTON_WIDTH / 2;
	private BufferedImage[] images;
	
	

	
	public MenuButton(double x, double y, int rowIndex, GameState state) {
		super(x,y,state);
		this.rowIndex = rowIndex;
		loadImages();
		width = images[0].getWidth();
		height = images[0].getHeight();
	}
	
	private void loadImages() {
		SpriteLoader sl = new SpriteLoader(Constants.BUTTON_SHEET);
		images = new BufferedImage[3];
		for(int i = 0; i < images.length; i++) {
			images[i] = sl.grabButtonImage(i, rowIndex);
		}
		
	}

	
	public void tick() {
		if(mouseOver) {
			imgIndex = 1;
		}
		else if(mousePressed)
			imgIndex = 2;
		else
			imgIndex = 0;
	}
	

	public void render(Graphics g) {
		g.drawImage(images[imgIndex], (int)(x - xOffsetCenter), (int)y, null);		
		Graphics2D g2d = (Graphics2D) g;
		g.setColor(Color.green);
		
		g2d.draw(getBounds());
	}
	
	
	public Rectangle getBounds() {
		return (new Rectangle((int)(x-xOffsetCenter), (int)y, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT));
	}

	
	
	
	


	
	
}
