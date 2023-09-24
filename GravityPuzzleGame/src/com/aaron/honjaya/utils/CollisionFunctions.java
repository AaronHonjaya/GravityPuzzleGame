package com.aaron.honjaya.utils;

import com.aaron.honjaya.framework.ObjectType;
import com.aaron.honjaya.objects.Player;

import java.awt.Rectangle;
import java.util.*;


import gameMain.Game;
import gameStates.PlayingHandler;

public class CollisionFunctions {

	

	public static boolean canMove(Player player, boolean[][] solidTile, boolean checkX, boolean checkY) {
		double futureX = player.getX();
		double futureY = player.getY();
		if(checkX)
			futureX += player.getVelX();
		if(checkY)
			futureY += player.getVelY();
		
		if(!isSolid(futureX, futureY, solidTile) &&
		   !isSolid(futureX+player.getWidth(), futureY, solidTile) &&
		   !isSolid(futureX, futureY+player.getHeight(), solidTile) &&
		   !isSolid(futureX+player.getWidth(), futureY+player.getHeight(), solidTile)) 
		{
			return true;
		}
		return false;
	}
	
	public static boolean canSetHere(double futureX, double futureY, double width, double height, boolean[][] solidTile) {	
		if(!isSolid(futureX, futureY, solidTile) &&
		   !isSolid(futureX+ width, futureY, solidTile) &&
		   !isSolid(futureX, futureY + height, solidTile) &&
		   !isSolid(futureX + width, futureY + height, solidTile)) 
		{
			return true;
		}
		return false;
	}
	
	
	public static double getXPosNextToWall(Player player){
		int currTile = Math.round((float)player.getX() / Game.TILE_SIZE);
		
		if(player.getVelX() > 0) {
			// Right collision
		
			int tileXPos = currTile * Game.TILE_SIZE;
			int xOffset = (int)(Game.TILE_SIZE - player.getWidth());
			return tileXPos + xOffset - 1;
		}else {
			// left collision
			return currTile*Game.TILE_SIZE;
			
		}
	}
	
	public static double getYPosNextToFloorOrRoof(Player player){
		int currTile = Math.round((float)player.getY() / Game.TILE_SIZE);
		
		if(player.getVelY() > 0) {
			// falling : hitting floor
			int tileYPos = currTile * Game.TILE_SIZE;
			int yOffset = (int)(Game.TILE_SIZE - player.getHeight());
			return tileYPos + yOffset - 1;
		}else {
			// jumping - hitting head
			return currTile*Game.TILE_SIZE;
			
		}
	}

	
	
	private static boolean isSolid(double x, double y, boolean[][] solidTile) {
		double xIndex = x / Game.TILE_SIZE;
		double yIndex = y / Game.TILE_SIZE;
		
		if(xIndex >= Game.WIDTH_IN_TILES || yIndex >= Game.HEIGHT_IN_TILES)
			return false;
		
		return solidTile[(int)yIndex][(int)xIndex];
	}
	
	public static boolean playerIsInPath(Player player, Player otherPlayer, boolean checkX, boolean checkY) {
		double futureX = player.getX();
		double futureY = player.getY();
		if(checkX)
			futureX += player.getVelX();
		if(checkY)
			futureY += player.getVelY();
		
		Rectangle futureHitbox = new Rectangle((int)futureX, (int)futureY, (int)player.getWidth(), (int)player.getHeight());
		
		return futureHitbox.intersects(otherPlayer.getBounds());
	}
	
	public static boolean isPlayerOnFloor(Player player, HashMap<UUID, Player> players, boolean solidTile[][]) {
		switch(player.getType()) {
			case PLAYER_D:
				if(isSolid(player.getX(), player.getY() + player.getHeight()+1, solidTile)
					|| isSolid(player.getX()+player.getWidth(), player.getY() + player.getHeight()+1, solidTile)) {
					if(PlayingHandler.i % 100 == 0) {
						System.out.println(PlayingHandler.i  + ": tile floor touched");
					}
					return true;
				}
			case PLAYER_R:
				if(isSolid(player.getX() + player.getWidth() + 1, player.getY(), solidTile)
						|| isSolid(player.getX()+player.getWidth()+1, player.getY() + player.getHeight(), solidTile)) {
						if(PlayingHandler.i % 100 == 0) {
							System.out.println(PlayingHandler.i  + ": tile floor touched");
						}
						return true;
					}
		}
		
		for(UUID id: players.keySet()) {
			if(id != player.getID() && player.getBoundsBottom().intersects(players.get(id).getBounds())) {
				if(PlayingHandler.i % 100 == 0) {
					System.out.println(PlayingHandler.i + ": player floor touched");
				}
				return true;
			}
			
		}
		return false;
		
	}
	


}
