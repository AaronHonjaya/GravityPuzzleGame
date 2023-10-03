package gameStates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.*;

import com.aaron.honjaya.framework.GameObject;
import com.aaron.honjaya.framework.ObjectType;
import com.aaron.honjaya.objects.Tile;
import com.aaron.honjaya.objects.Flag;
import com.aaron.honjaya.objects.Player;
import com.aaron.honjaya.utils.CollisionFunctions;
import com.aaron.honjaya.utils.SpriteLoader;

import gameMain.Game;
import level.LevelManager;


public class PlayingHandler implements Handler{
	

	protected boolean levelIsFinished; 
	
	//two hashmaps containing objects and all players. 
	//Need two so that player collision can be checked against objects. 
	//HashMap used for efficiency - quick to delete and find items. 
	protected HashMap<UUID, GameObject> objects = new HashMap<>();
	protected HashMap<UUID, Player> players = new HashMap<>();
	
	protected LevelManager levelManager;
	
	
	public PlayingHandler() {
		//playing handler handles all playing logistics, so it needs the level manager. 
		levelManager = new LevelManager(this);
		levelManager.loadLevel();
	}
	

	public void render(Graphics g) {
		for(UUID id : players.keySet()) {
			players.get(id).render(g);
		}
		for(UUID id : objects.keySet()) {
			GameObject tempObject = objects.get(id);
			tempObject.render(g);
		}
		levelManager.render(g);
	}
	
	
	public void tick() {
		//loop through  player set
		for(UUID playerID : players.keySet() ) {
			Player currPlayer = players.get(playerID);
			
			//check if player is on the floor. If not, setInAir to true. 
			if(!CollisionFunctions.isPlayerOnFloor(currPlayer, players, levelManager.getSolidTile())) {
				currPlayer.setInAir(true);
			}
			//tick the player and update their position
			currPlayer.tick();
			updatePlayerPos(currPlayer);
			
			
			//loop through the objects and check for collisions with the player. 
			for(UUID objectID : objects.keySet()) {
				GameObject tempObject = objects.get(objectID);
				
				//currently only have a flag object. Will be adding more. 
				if(ObjectType.isFlag(tempObject)) {
					checkFlagCollision(currPlayer, (Flag) tempObject);
				}
			}
			
		}
		
		if(levelManager.isLevelFinished()) {
			levelManager.loadNextLevel();
		}
		
		//when it chages from tutorial to playing or playing to tutorial, the level manager
		//needs to load the level.
		if(GameState.state == GameState.TUTORIAL && levelManager.getState() != GameState.TUTORIAL) {
			levelManager.setState(GameState.TUTORIAL);
			levelManager.loadLevel();
		}else if(GameState.state == GameState.PLAYING && levelManager.getState() != GameState.PLAYING) {
			levelManager.setState(GameState.PLAYING);
			levelManager.loadLevel();
		}
	}
	
	
	private void updatePlayerPos(Player player) {
		//player in xPath
		Player tempX = null;
		
		//player in yPath
		Player tempY = null;
		
		//currently have two player instances for x and y because
		//I may add more players in the future. 
		
		boolean playerInHorizontalPath = false;
		boolean playerInVerticalPath = false;

		for(UUID id : players.keySet()) {
			if(player.getID() == id) 
				continue;
			
			
			if(!playerInHorizontalPath)
				tempX = players.get(id);
			if(!playerInVerticalPath)
				tempY = players.get(id);
			
			if(CollisionFunctions.playerIsInPath(player, tempX, true, false)) {
				playerInHorizontalPath = true;
			}
			if(CollisionFunctions.playerIsInPath(player, tempY, false, true)) {
				playerInVerticalPath = true;
			}
		}
		
		
		if(!playerInHorizontalPath) {
			movePlayerHorizontally(player);
		}else {
			//checks if tempX is on top of player. if so player needs to push tempX
			if(tempX.getBoundsBottom().intersects(player.getBounds())){	
				
				//tempX's futureX position is playerX + or - player width
				//Its plus if they are standing on the right side of the player 
				//and minus if they are standing on the left side. 
				double tempFutureX = player.getX()+player.getVelX();
				
				if(player.getX() > tempX.getX()) {
					//tempX is on the left
					tempFutureX -= tempX.getWidth(); 
				}else {
					//tempX is on the right
					tempFutureX += player.getWidth();
				}
				
				
				
				//first checks if we can set tempX to the future position. 
				//This is so that we don't push tempX into a wall. 
				if(CollisionFunctions.canSetHere(tempFutureX, tempX.getY(),  
						tempX.getWidth(), tempX.getHeight(), levelManager.getSolidTile()))
				{
					//if so, we just move the player and set tempX to the futureX. 
					movePlayerHorizontally(player);
					tempX.setX(tempFutureX);
				}
				else
				{
					//this means tempX would be set into a wall. 
					//So we set tempX to the position next to the wall and then set player to the
					//position right next to tempX. 
					tempX.setX(CollisionFunctions.getXPosNextToWall(tempX));
					player.setX(tempX.getX() + tempX.getHeight());
				}					
			}else{
				//if they are not 
				if(player.getVelX() > 0)
					player.setX(tempX.getX() - player.getWidth());
				else
					player.setX(tempX.getX()+player.getWidth());
				
					player.setVelX(0);
					if(player.isInAir() && player.getType() == ObjectType.PLAYER_R) {
						player.setJumping(false);
						player.setInAir(false);
					}
				
			}
		}
		
		//same logic as above but checking vertical direction.
		//I actually wanted to combine these two into one function and call it with 
		//different parameters but wasn't sure how since they check for different directions.
		//If anyone could give me some advice/pointers, please let me know!
		if(!playerInVerticalPath) {
			movePlayerVertically(player);
		}
		else
		{			
			if(tempY.getBoundsBottom().intersects(player.getBounds())){	
				double tempFutureY = player.getY()+player.getVelY()-tempY.getHeight();
				
				if(CollisionFunctions.canSetHere(tempY.getX(), tempFutureY, 
						tempY.getWidth(), tempY.getHeight(), levelManager.getSolidTile()))
				{
					movePlayerVertically(player);
					tempY.setY(tempFutureY);
				}
				else
				{
					tempY.setY(CollisionFunctions.getYPosNextToFloorOrRoof(tempY));
					player.setY(tempY.getY() + tempY.getHeight());
				}
			}else {
				if(player.getVelY() > 0) {
					player.setY(tempY.getY() - player.getHeight());
				}
				else {
					player.setY(tempY.getY() + player.getHeight());
				}
					player.setVelY(0);
					if(player.isInAir() && player.getType() == ObjectType.PLAYER_D) {
						player.setJumping(false);
						player.setInAir(false);
				}
			}
		}
				
	}
	
	private void movePlayerHorizontally(Player player) {
		ObjectType type = player.getType();
		
		//checks if player can move to the designated location. 
		//if so we just move. 
		if(CollisionFunctions.canMove(player, levelManager.getSolidTile(), true, false)) {
			player.updateXPos();
		}
		else {
			//sets player next to the wall. 
			player.setX(CollisionFunctions.getXPosNextToWall(player));	
			
			//changes inAir and Jumping based off of player type. 
			if(type == ObjectType.PLAYER_R) {
				if(player.getVelX() > 0) {
					player.setJumping(false);
					player.setInAir(false);
				}
				player.setVelX(0);

			}
			else if(type == ObjectType.PLAYER_L) {
				if(player.getVelX() < 0) {
					player.setJumping(false);
					player.setInAir(false);
				}
				player.setVelX(0);
			}
		}
	}
	
	
	//same logic as above but vertical. 
	private void movePlayerVertically(Player player) {
		ObjectType type = player.getType();

		if(CollisionFunctions.canMove(player, levelManager.getSolidTile(), false, true)) {
			player.updateYPos();
		}
		else {
			player.setY(CollisionFunctions.getYPosNextToFloorOrRoof(player));
			
			if(type == ObjectType.PLAYER_D) {
				if(player.getVelY() > 0) {
					player.setJumping(false);
					player.setInAir(false);
				}
				player.setVelY(0);
			}
			else if(type == ObjectType.PLAYER_L) {
				if(player.getVelY() < 0) {
					player.setJumping(false);
					player.setInAir(false);
				}
				player.setVelY(0);
			}
		}
		
	}


	
	private void checkFlagCollision(Player player, Flag flag) {
		
		//must make sure player hasn't already reached their flag first. 
		//then checks if flag and player tyupes match. 
		//then checks if they actually intersect. 
		if(!player.hasReachedFlag() && ObjectType.reachedFlag(flag, player) 
				&& player.getBoundsBottom().intersects(flag.getBounds())) {
			levelManager.increaseNumPlayersFinished();
			player.setReachedFlag(true);
		}
			
	}

	public void addObject(GameObject object) {
		objects.put(object.getID(), object);
	}

	public void removeObject(UUID id) {
		objects.remove(id);
	}
	
	public void addPlayer(Player player) {
		players.put(player.getID(), player);
	}

	public void removePlayer(UUID id) {
		players.remove(id);
	}
	
	public void removeAll() {
		this.objects.clear();
		this.players.clear();
	}
	
	public LevelManager getLevelManager() {
		return this.levelManager;
	}
	
	
	
	

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int key = e.getButton();
		if(key == MouseEvent.BUTTON1) {
			System.out.println("test");
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		for(UUID id : players.keySet()) {
			Player tempPlayer = players.get(id);
			if(tempPlayer.getType() == ObjectType.PLAYER_D) {
				switch(key) {
					case KeyEvent.VK_RIGHT:
						tempPlayer.setVelX(5);
						break;
					case KeyEvent.VK_LEFT:
						tempPlayer.setVelX(-5);
						break;
					case KeyEvent.VK_UP:
						//only jump if the player isn't already jumping or in the air. 
						if(!tempPlayer.isJumping() && !tempPlayer.isInAir()) {								
							tempPlayer.setJumping(true);
							tempPlayer.setInAir(true);
							tempPlayer.setVelY(-10);
						}
						break;
				}
			}
			else if(tempPlayer.getType() == ObjectType.PLAYER_R) {
				switch(key) {
					case KeyEvent.VK_A:
						tempPlayer.setVelY(5);
						break;
					case KeyEvent.VK_D:
						tempPlayer.setVelY(-5);
						break;
					case KeyEvent.VK_SPACE:
						//same as above. 
						if(!tempPlayer.isJumping() && !tempPlayer.isInAir()) {
							tempPlayer.setJumping(true);
							tempPlayer.setInAir(true);
							tempPlayer.setVelX(-10);
						}
						break;
				}
			}			
		}
		if(key == KeyEvent.VK_ESCAPE) {
			GameState.state = GameState.MENU;
			
			//if last tutorial level and we esc, then we reset the tutorial level and
			//reload the first level in case they want to replay it. 
			if(levelManager.getCurrTutorialLevel() == levelManager.NUM_TUTORIAL_LEVELS-1) {
				levelManager.setCurrTutorialLevel(0);
				levelManager.clearLevel();
				levelManager.loadLevel();
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		
		for(UUID id : players.keySet()) {
			Player tempPlayer = players.get(id);
			if(tempPlayer.getType() == ObjectType.PLAYER_D) {
				switch(key) {
					case KeyEvent.VK_RIGHT:
						tempPlayer.setVelX(0);
						break;
					case KeyEvent.VK_LEFT:
						tempPlayer.setVelX(0);
						break;
						
				}
			}else if(tempPlayer.getType() == ObjectType.PLAYER_R) {
				switch(key) {
					case KeyEvent.VK_A:
						tempPlayer.setVelY(0);
						break;
					case KeyEvent.VK_D:
						tempPlayer.setVelY(0);
						break;
				}
			}
			
		}
	}







	
	
	
	
}
