package com.aaron.honjaya.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

import com.aaron.honjaya.framework.GameObject;
import com.aaron.honjaya.framework.ObjectType;

import gameMain.Game;

public class Tile extends GameObject{

	private boolean rendered;
	
	public Tile(double x, double y, ObjectType id) {
		super(x, y, id);
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.white);
		g.drawRect((int)x, (int)y, Game.TILE_SIZE, Game.TILE_SIZE);
		
		
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, Game.TILE_SIZE, Game.TILE_SIZE);
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}



	


}
