package com.aaron.honjaya.objects;

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

import gameStates.Handler;
import gameStates.PlayingHandler;

public class Flag extends GameObject{
	

	private SpriteLoader ss;
	private BufferedImage flag;
	private Handler handler;

	public Flag(double x, double y, ObjectType id, PlayingHandler handler) {
		super(x, y, id);
		
		this.handler = handler;
		ss = new SpriteLoader(Constants.spriteSheet);
		switch(id) {
			case FLAG_D: 
				flag = ss.grabSpriteImage(0, 3);
				break;
			case FLAG_L: 
				flag = ss.grabSpriteImage(1, 3);
				break;
			case FLAG_U: 
				flag = ss.grabSpriteImage(2, 3);
				break;
			case FLAG_R: 
				flag = ss.grabSpriteImage(3, 3);
				break;
		}
	}

	@Override
	public void tick() {
		
	}

	@Override
	public void render(Graphics g) {
		
		g.drawImage(flag, (int)x, (int)y, null);
		
		Graphics2D g2d = (Graphics2D) g;
		g.setColor(Color.green);
		
		g2d.draw(getBounds());
		
		
	}

	@Override
	public Rectangle getBounds() {
		switch(type) {				
			case FLAG_R:
				return new Rectangle((int)x+12, (int)y+5, 20, 20);
			
			default: 
				return new Rectangle((int)x+7, (int)y+12, 20, 20);
				
		}
		
	}



	@Override
	public Rectangle getBoundsTop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rectangle getBoundsRight() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rectangle getBoundsLeft() {
		// TODO Auto-generated method stub
		return null;
	}


}
