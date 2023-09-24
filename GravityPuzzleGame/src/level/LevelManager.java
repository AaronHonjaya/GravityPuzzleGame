package level;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.*;

import com.aaron.honjaya.framework.*;
import com.aaron.honjaya.objects.Tile;
import com.aaron.honjaya.objects.Flag;
import com.aaron.honjaya.objects.Player;
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

	
	public LevelManager(PlayingHandler playingHandler) {
		levelObjects = new HashMap<>();
		this.playingHandler = playingHandler;
		
		SpriteLoader sl = new SpriteLoader("/sprite_sheet.png");
		levels.add(sl.loadImage("/testLevel.png"));
		numPlayers = 0;
		numPlayersFinished = 0;
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


	public PlayingHandler getPlayingHandler() {
		return playingHandler;
	}


	public ArrayList<BufferedImage> getLevels() {
		return levels;
	}


	public int getNumPlayersFinished() {
		return numPlayersFinished;
	}


	public void loadLevel(int lvlNum) {
		BufferedImage image = levels.get(lvlNum);
		levelFinished = false;
		solidTile = new boolean[Game.HEIGHT_IN_TILES][Game.WIDTH_IN_TILES];
		
		for(int xx = 0; xx < Game.WIDTH_IN_TILES; xx++) {
			for(int yy = 0; yy < Game.HEIGHT_IN_TILES; yy++) {
				int pixel = image.getRGB(xx + (lvlNum*Game.WIDTH_IN_TILES), yy + (lvlNum*Game.WIDTH_IN_TILES));
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
		System.out.println(numPlayers + ", " + numPlayersFinished);
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
	
}
