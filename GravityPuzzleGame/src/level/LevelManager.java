package level;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.*;

import com.aaron.honjaya.framework.*;
import com.aaron.honjaya.objects.Tile;
import com.aaron.honjaya.objects.Flag;
import com.aaron.honjaya.objects.Player;
import com.aaron.honjaya.utils.Constants;
import com.aaron.honjaya.utils.SpriteLoader;

import gameMain.Game;
import gameStates.PlayingHandler;

public class LevelManager{
	
	
	
	private HashMap<UUID, GameObject> levelObjects;
	
	private PlayingHandler playingHandler;
	private ArrayList<BufferedImage> levels = new ArrayList<>();
	private int numPlayers; 
	private int numPlayersFinished;
	private boolean levelFinished;
	private boolean[][] solidTile;
	private int currLevel;
	
	public static final int NUM_LEVELS = 2;
	public static final int NUM_COLUMNS = 10;
	private static final int LEVEL_SHEET_WIDTH = Game.WIDTH_IN_TILES;
	private static final int LEVEL_SHEET_HEIGHT = Game.HEIGHT_IN_TILES;


	



	public LevelManager(PlayingHandler playingHandler) {
		levelObjects = new HashMap<>();
		currLevel = 0;
		this.playingHandler = playingHandler;
		loadLevelImages();
	}
	
	
	private void loadLevelImages() {
		SpriteLoader sl = new SpriteLoader(Constants.LEVEL_SHEET);
		BufferedImage image = sl.getImage();
		int row = 0;
		
		while(levels.size() < NUM_LEVELS) {
	
			for(int col = 0; col < NUM_COLUMNS; col++) {
				levels.add(image.getSubimage(col*LEVEL_SHEET_WIDTH, 
						row*LEVEL_SHEET_HEIGHT, LEVEL_SHEET_WIDTH, LEVEL_SHEET_HEIGHT));
			}
			row++;
		}
	}


	public void render(Graphics g) {
		for(UUID objectId : levelObjects.keySet()) {
			levelObjects.get(objectId).render(g);
		}
	}
	
	public boolean[][] getSolidTile() {
		return solidTile;
	}


	public void setSolidTile(boolean[][] solidTile) {
		this.solidTile = solidTile;
	}


	public HashMap<UUID, GameObject> getLevelObjects() {
		return levelObjects;
	}




	public ArrayList<BufferedImage> getLevels() {
		return levels;
	}


	public int getNumPlayersFinished() {
		return numPlayersFinished;
	}


	public void loadLevel() {
		playingHandler.removeAll();
		levelObjects.clear();
		numPlayers = 0;
		numPlayersFinished = 0;
		
		BufferedImage image = null;
		
		try {
			image = levels.get(currLevel);
		}catch(Exception e) {
			currLevel = 0;
			image = levels.get(currLevel);
		}
		levelFinished = false;
		solidTile = new boolean[Game.HEIGHT_IN_TILES][Game.WIDTH_IN_TILES];
		
		for(int xx = 0; xx < Game.WIDTH_IN_TILES; xx++) {
			for(int yy = 0; yy < Game.HEIGHT_IN_TILES; yy++) {
				int pixel = image.getRGB(xx, yy);
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;

				
				if(red == 255 && green == 255 && blue == 255) {
					solidTile[yy][xx] = true;
					Tile temp = new Tile(xx*Game.TILE_SIZE, yy*Game.TILE_SIZE, ObjectType.BLOCK);
					levelObjects.put(temp.getID(), temp);
				}
				else if(red == 255 && green == 0 && blue == 0) {
					Player temp = new Player(xx*Game.TILE_SIZE, yy*Game.TILE_SIZE, ObjectType.PLAYER_D);
					playingHandler.addObject(temp);
					playingHandler.addPlayer(temp);
					numPlayers++;
				}
				else if(red == 0 && green == 0 && blue == 255) {
					Player temp = new Player(xx*Game.TILE_SIZE, yy*Game.TILE_SIZE, ObjectType.PLAYER_R);
					playingHandler.addObject(temp);
					playingHandler.addPlayer(temp);
					numPlayers++;
				}
				else if(red == 0 && green == 148 && blue == 255) {
					playingHandler.addObject(new Flag(xx*Game.TILE_SIZE, yy*Game.TILE_SIZE, ObjectType.FLAG_R));
				}
				else if(red == 255 && green == 106 && blue == 0) {
					playingHandler.addObject(new Flag(xx*Game.TILE_SIZE, yy*Game.TILE_SIZE, ObjectType.FLAG_D));
				}
			}
		}
		//System.out.println(numPlayers + ", " + numPlayersFinished);
	}


	public boolean isLevelFinished() {
		return (numPlayers == numPlayersFinished);
	}

	public int getNumPlayers() {
		return numPlayers;
	}

	public void increaseNumPlayersFinished() {
		this.numPlayersFinished++;
	}
	
	public int getCurrLevel() {
		return currLevel;
	}


	public void setCurrLevel(int currLevel) {
		this.currLevel = currLevel;
	}
	
}
