package gameStates;


//GameState idea adapted from: https://youtu.be/2mhDRjnv4WI?si=O3f5yu5dRWzmfDLB
 
public enum GameState {
	MENU,
	PLAYING,
	LEVEL_SELECT,
	TUTORIAL,
	EXIT;	
	
	public static GameState state = MENU;
}
	