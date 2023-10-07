package com.aaron.honjaya.utils;

import com.aaron.honjaya.framework.ObjectType;
import com.aaron.honjaya.objects.Player;

import java.awt.Rectangle;
import java.util.*;


import gameMain.Game;
import gameStates.PlayingHandler;



public class CollisionFunctions {

	
//adapted from https://youtu.be/PrAmaeQF4f0?si=uvMySN1JfSCGasHo
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
	
	
	
	public static boolean canSetHere(double futureX, double futureY, 
			Player player, boolean[][] solidTile, HashMap<UUID, Player> otherPlayers) {	
		int width = player.getWidth();
		int height = player.getHeight();
	
		if(isSolid(futureX, futureY, solidTile) ||
		   isSolid(futureX+ width, futureY, solidTile) ||
		   isSolid(futureX, futureY + height, solidTile) ||
		   isSolid(futureX + width, futureY + height, solidTile)) 
		{
			return false;
		}
		for(UUID id : otherPlayers.keySet()) {
			if(id != player.getID() && 
					playerIsInPath((int)futureX, (int)futureY, width, height, otherPlayers.get(id)))
			{
				return false;
			}
		}
		return true;
	}
	
	
	
	//adapted from https://youtu.be/PrAmaeQF4f0?si=uvMySN1JfSCGasHo
	public static double getXPosNextToWall(Player player){
		int currTile = Math.round((float)player.getX() / Game.TILE_SIZE);
		
		if(player.getVelX() > 0 || (player.getVelX() == 0 && player.getType()==ObjectType.PLAYER_L)) {
		
			int tileXPos = currTile * Game.TILE_SIZE;
			int xOffset = (int)(Game.TILE_SIZE - player.getWidth());
			return tileXPos + xOffset - 1;
		}else{
			return currTile*Game.TILE_SIZE;
		}
	}
	
	//Credit to https://youtu.be/PrAmaeQF4f0?si=uvMySN1JfSCGasHo
	public static double getYPosNextToFloorOrRoof(Player player){
		int currTile = Math.round((float)player.getY() / Game.TILE_SIZE);
		
		if(player.getVelY() > 0 || (player.getVelY() == 0 && player.getType()==ObjectType.PLAYER_U)) {
			// falling : hitting floor
			int tileYPos = currTile * Game.TILE_SIZE;
			int yOffset = (int)(Game.TILE_SIZE - player.getHeight());
			return tileYPos + yOffset - 1;
		}else {
			// jumping - hitting head
			return currTile*Game.TILE_SIZE;
		}
	}

	
	//adapted from https://youtu.be/PrAmaeQF4f0?si=uvMySN1JfSCGasHo
	private static boolean isSolid(double x, double y, boolean[][] solidTile) {
		double xIndex = x / Game.TILE_SIZE;
		double yIndex = y / Game.TILE_SIZE;
		
		if(xIndex >= Game.WIDTH_IN_TILES || yIndex >= Game.HEIGHT_IN_TILES)
			return false;
		
		return solidTile[(int)yIndex][(int)xIndex];
	}
	
	
	
	
	public static boolean playerIsInPath(int futureX, int futureY, int width, 
			int height, Player otherPlayer) {		
		Rectangle futureHitbox = new Rectangle(futureX, futureY, width, height);
		return futureHitbox.intersects(otherPlayer.getBounds());
	}
	
	
	//adapted from https://youtu.be/Hles6ghAdiY?si=2xrahJR8amXP6RPF
	public static boolean isPlayerOnFloor(Player player, HashMap<UUID, Player> playerList, boolean solidTile[][]) {
		switch(player.getType()) {
			case PLAYER_D:
				if(isSolid(player.getX(), player.getY() + player.getHeight()+1, solidTile)
					|| isSolid(player.getX()+player.getWidth(), player.getY() + player.getHeight()+1, solidTile)) {
					return true;
				}
				break;
			case PLAYER_R:
				if(isSolid(player.getX() + player.getWidth() + 1, player.getY(), solidTile)
						|| isSolid(player.getX()+player.getWidth()+1, player.getY() + player.getHeight(), solidTile)) {
						return true;
					}
				break;
			case PLAYER_L:
				if(isSolid(player.getX() - 1, player.getY(), solidTile)
						|| isSolid(player.getX()-1, player.getY() + player.getHeight(), solidTile)) {
						return true;
					}
				break;
		}
		
		for(UUID id: playerList.keySet()) {
			if(id != player.getID() && player.getBoundsBottom().intersects(playerList.get(id).getBounds())) {
				return true;
			}
			
		}
		return false;
		
	}
	


}
