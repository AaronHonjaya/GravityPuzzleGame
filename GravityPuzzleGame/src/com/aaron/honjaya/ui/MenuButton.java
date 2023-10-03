package com.aaron.honjaya.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
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
	
	protected Rectangle bounds;
	protected String str;
	protected final Color myLightGray = new Color(192, 192, 192);
	protected final Color myDarkGray = new Color(160, 160, 160);
	protected Color currColor;
	protected int fontSize;


	
	public MenuButton(double x, double y, int width, int height, String title, int fontSize, GameState state) {
		super(x,y,state);
		this.width = width;
		this.height = height;
		bounds = new Rectangle((int)x, (int)y, width, height);
		str = title;
		this.fontSize = fontSize;
	}

	
	public void tick() {
		if(mouseOver) 
			currColor = myLightGray;
		else if(mousePressed)
			currColor = myDarkGray;
		else
			currColor = Color.white;
	}
	

	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;		
		g.setColor(currColor);
		g2d.fill(bounds);
		
		drawText(g2d);
	}

	private void drawText(Graphics2D g2d) {
		
		// Although slightly altered, original code to center the text was found online. Credit is below: 
		// http://www.java2s.com/Tutorials/Java/Graphics_How_to/Text/Center_a_string_in_a_rectangle.htm
		
		g2d.setColor(Color.BLACK);
		
		Font font = new Font("Arial", Font.BOLD, fontSize);
		g2d.setFont(font);
		FontMetrics fm = g2d.getFontMetrics();
		
		int textX = ((int)bounds.getCenterX() - (fm.stringWidth(str)/ 2) );
		int textY = ((int)bounds.getCenterY() - (fm.getHeight()/ 2) + fm.getAscent());
		
		g2d.drawString(str, textX, textY);
	}
	

	public Rectangle getBounds() {
		return bounds;
	}


	
	
	
	
	


	
	
}
