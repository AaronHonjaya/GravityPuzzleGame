package level;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.util.*;

import com.aaron.honjaya.framework.*;
import com.aaron.honjaya.objects.Tile;
import com.aaron.honjaya.objects.Flag;
import com.aaron.honjaya.objects.Player;
import com.aaron.honjaya.utils.Constants;
import com.aaron.honjaya.utils.SpriteLoader;

import gameMain.Game;
import gameStates.GameState;
import gameStates.PlayingHandler;

public class LevelManager{
	
	
	
	private HashMap<UUID, GameObject> levelObjects;
	
	private PlayingHandler playingHandler;
	
	//two lists for levels and tutorial levels. 
	private ArrayList<BufferedImage> levels = new ArrayList<>();
	private ArrayList<BufferedImage> tutorialLevels = new ArrayList<>();
	
	
	private int numPlayers; 
	private int numPlayersFinished;
	private boolean levelFinished;
	private boolean[][] solidTile;
	private int currLevel;
	private int currTutorialLevel;
	private String[] tutorialInstructions;
	
	public static final int NUM_TUTORIAL_LEVELS = 4;
	public static final int NUM_LEVELS = 2;
	public static final int NUM_COLUMNS = 10;
	private static final int LEVEL_SHEET_WIDTH = Game.WIDTH_IN_TILES;
	private static final int LEVEL_SHEET_HEIGHT = Game.HEIGHT_IN_TILES;


	private GameState state = GameState.PLAYING;



	public LevelManager(PlayingHandler playingHandler) {
		levelObjects = new HashMap<>();
		currLevel = 0;
		currTutorialLevel = 0;
		this.playingHandler = playingHandler;
		loadLevelImages();
		initTutorialInstructions();
	}
	
	
	private void initTutorialInstructions() {
		tutorialInstructions = new String[NUM_TUTORIAL_LEVELS];
		tutorialInstructions[0] = "Use arrow keys to move and reach the flag";
		tutorialInstructions[1] = "Blue's gravity is in a different direction. Use W,D, and space to move";
		tutorialInstructions[2] = "Red's flag is too high to jump and reach. Can Blue help him somehow?";
		tutorialInstructions[3] = "Tutorial Completed! Press 'ESC' to go back to the Menu.";

	}


	private void loadLevelImages() {
		SpriteLoader sl = new SpriteLoader();
		BufferedImage image = sl.loadImage(Constants.LEVEL_SHEET);
		int row = 0;
		
		//stores subimages from sprite sheet into arrays. 
		while(levels.size() < NUM_LEVELS) {
			for(int col = 0; col < NUM_COLUMNS && col < NUM_LEVELS; col++) {
				levels.add(image.getSubimage(col*LEVEL_SHEET_WIDTH, 
						row*LEVEL_SHEET_HEIGHT, LEVEL_SHEET_WIDTH, LEVEL_SHEET_HEIGHT));
			}
			row++;
		}
		row = 0;
		image = sl.loadImage(Constants.TUTORIAL_LEVEL_SHEET);
		for(int col = 0; col < NUM_TUTORIAL_LEVELS; col++) {
			tutorialLevels.add(image.getSubimage(col*LEVEL_SHEET_WIDTH, 
					row*LEVEL_SHEET_HEIGHT, LEVEL_SHEET_WIDTH, LEVEL_SHEET_HEIGHT));
		}
	}


	public void render(Graphics g) {
		for(UUID objectId : levelObjects.keySet()) {
			levelObjects.get(objectId).render(g);
		}
		if(this.state == GameState.TUTORIAL) {
			drawTxt((Graphics2D) g);
		}
	}

	private void drawTxt(Graphics2D g2d) {
		String str = tutorialInstructions[currTutorialLevel];
		g2d.setColor(Color.green);
		Font font = new Font("Arial", Font.BOLD, 20);
		g2d.setFont(font);
		FontMetrics fm = g2d.getFontMetrics();
		
		int textX = ((Game.WIDTH - fm.stringWidth(str))/ 2 );
		int textY = (100 - (fm.getHeight()/ 2) + fm.getAscent());
		
		g2d.drawString(str, textX, textY);
	}


	public void loadNextLevel() {
		if(this.state == GameState.PLAYING)
			currLevel++;
		else if (this.state == GameState.TUTORIAL)
			currTutorialLevel++;
		loadLevel();
	}

	
	
	
	//adapted from https://youtu.be/1TFDOT1HiBo?si=oKgnLm7hqEjlBA3z
	//Modified heavily by Aaron Honjaya - University of Washington. 
	public void loadLevel() {
		clearLevel();
		numPlayers = 0;
		numPlayersFinished = 0;
		
		BufferedImage image = null;
		levelFinished = false;
		solidTile = new boolean[Game.HEIGHT_IN_TILES][Game.WIDTH_IN_TILES];
		
		//try catch is for testing. Will be changed later with a "you have finished!" screen
		//this level will not be able to be completed so that the currLevel never goes beyond the numLevels. 
		try {
			if(this.state == GameState.PLAYING) {
				image = levels.get(currLevel);
			}
			else if(this.state == GameState.TUTORIAL) {
				System.out.println("loading tutoral level: " + currTutorialLevel);
				image = tutorialLevels.get(currTutorialLevel);
			}
				
		}catch(Exception e) {
			System.out.println("Error: Level Not Found");
			GameState.state = GameState.MENU;
			return;
		}
		
		
		for(int x = 0; x < Game.WIDTH_IN_TILES; x++) {
			for(int y = 0; y < Game.HEIGHT_IN_TILES; y++) {
				
				//level is created based on the RGB values of each specific pixel. 
				Color c = new Color(image.getRGB(x, y));
				int red = c.getRed();
				int green = c.getGreen();
				int blue = c.getBlue();
				
				
				if(red == 255 && green == 255 && blue == 255) {
					solidTile[y][x] = true;
					Tile temp = new Tile(x*Game.TILE_SIZE, y*Game.TILE_SIZE, ObjectType.BLOCK);
					levelObjects.put(temp.getID(), temp);
				}
				else if(red == 255 && green == 0 && blue == 0) {
					Player temp = new Player(x*Game.TILE_SIZE, y*Game.TILE_SIZE, ObjectType.PLAYER_D);
					playingHandler.addPlayer(temp);
					numPlayers++;
				}
				else if(red == 0 && green == 0 && blue == 255) {
					Player temp = new Player(x*Game.TILE_SIZE, y*Game.TILE_SIZE, ObjectType.PLAYER_R);
					playingHandler.addPlayer(temp);
					numPlayers++;
				}
				else if(red == 0 && green == 148 && blue == 255) {
					playingHandler.addObject(new Flag(x*Game.TILE_SIZE, y*Game.TILE_SIZE, ObjectType.FLAG_R));
				}
				else if(red == 255 && green == 106 && blue == 0) {
					playingHandler.addObject(new Flag(x*Game.TILE_SIZE, y*Game.TILE_SIZE, ObjectType.FLAG_D));
				}
			}
		}
	}
	
	
	public void clearLevel() {
		levelObjects.clear();
		playingHandler.removeAll();
	}
	
	public boolean[][] getSolidTile() {
		return solidTile;
	}
	
	public int getNumPlayersFinished() {
		return numPlayersFinished;
	}


	public boolean isLevelFinished() {
		//don't want to have level finished when there are zero players. 
		return (numPlayers == numPlayersFinished && numPlayers != 0);
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
	
	public GameState getState() {
		return state;
	}


	public void setState(GameState state) {
		this.state = state;
	}


	public int getCurrTutorialLevel() {
		return currTutorialLevel;
	}


	public void setCurrTutorialLevel(int currTutorialLevel) {
		this.currTutorialLevel = currTutorialLevel;
	}
}
