package com.aaron.honjaya.framework;

import com.aaron.honjaya.objects.Player;

public enum ObjectType 
{
	PLAYER_D, //Gravity = down
	PLAYER_L, // gravity = left
	PLAYER_R, // gravity = right
	PLAYER_U, // gravity = up
	BLOCK,
	FLAG_D, //Gravity = down
	FLAG_L, // gravity = left
	FLAG_R, // gravity = right
	FLAG_U, 
	BUTTON;
	
	public static boolean isPlayer(GameObject obj) {
		ObjectType temp = obj.getType();
		if(temp == PLAYER_D
		   ||temp == PLAYER_L
		   ||temp == PLAYER_U
		   ||temp == PLAYER_R) {
			return true;
		}
		return false;
	}
	
	public static boolean isBlock(GameObject obj) {
		return obj.getType() == BLOCK;
	}
	
	public static boolean reachedFlag(GameObject flag, Player player) {
		ObjectType flagType = flag.getType();
		ObjectType playerType = player.getType();
		
		if( (flagType == FLAG_R && playerType == PLAYER_R)
			|| (flagType == FLAG_D && playerType == PLAYER_D)
			|| (flagType == FLAG_U && playerType == PLAYER_U)
			|| (flagType == FLAG_L && playerType == PLAYER_L)){
				return true;
		}
		return false;
	}
	
	public static boolean isFlag(GameObject obj) {
		ObjectType temp = obj.getType();
		if(temp == FLAG_D
		   ||temp == FLAG_L
		   ||temp == FLAG_U
		   ||temp == FLAG_R) {
			return true;
		}
		return false;
	}
	

}
