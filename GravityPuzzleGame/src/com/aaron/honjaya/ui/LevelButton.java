package com.aaron.honjaya.ui;

import java.awt.*;


import gameStates.GameState;

public class LevelButton extends Button{
	public int lvlNum;
	
	private Rectangle bounds;
	
	private String str;
	private final Color myLightGray = new Color(192, 192, 192);
	private final Color myDarkGray = new Color(160, 160, 160);
	private Color currColor;

	
	public LevelButton(double x, double y, int lvlNum) {
		super(x,y, GameState.PLAYING);
		this.lvlNum = lvlNum;
		this.width = 100;
		this.height = 50;
		this.str = "LEVEL " + lvlNum;
		bounds = new Rectangle((int)x, (int)y, width, height);
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
		
		g2d.draw(bounds);
		g2d.fill(bounds);
		
		drawLvlText(g2d);
		
		
	}

	private void drawLvlText(Graphics2D g2d) {
		
		// Although slightly altered, original code to center the text was found online. Credit is below: 
		// http://www.java2s.com/Tutorials/Java/Graphics_How_to/Text/Center_a_string_in_a_rectangle.htm
		
		g2d.setColor(Color.BLACK);
		
		Font font = new Font("Arial", Font.BOLD, 20);
		g2d.setFont(font);
		FontMetrics fm = g2d.getFontMetrics();
		
		int textX = ((int)x + (width - fm.stringWidth(str)) / 2);
		int textY = ((int)y + (height - fm.getHeight()) / 2) + fm.getAscent();
		
		g2d.drawString(str, textX, textY);
	}
	
	
	@Override
	public Rectangle getBounds() {
		return bounds;
	}
	
	
	
}
