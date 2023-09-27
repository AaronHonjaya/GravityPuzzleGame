package gameStates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
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
	

	private BufferedImage spriteSheet = null;
	private BufferedImage image = null; 
	private boolean levelIsFinished; 
	private HashMap<UUID, GameObject> allObjects = new HashMap<>();
	private HashMap<UUID, Player> players = new HashMap<>();
	private LevelManager levelManager;
	
	
	
	public PlayingHandler() {
		levelManager = new LevelManager(this);
		levelManager.loadLevel();
	}
	
	public void render(Graphics g) {
		for(UUID id : allObjects.keySet()) {
			GameObject tempObject = allObjects.get(id);
			tempObject.render(g);
			levelManager.render(g);
		}
	}
	
	private void movePlayerHorizontally(Player player) {
		ObjectType type = player.getType();
		if(CollisionFunctions.canMove(player, levelManager.getSolidTile(), true, false)) {
			player.updateXPos();
			if(player.isInAir() && (type == ObjectType.PLAYER_R || type == ObjectType.PLAYER_L)) {
				player.updateGrav();
			}
		}
		else {
			player.setX(CollisionFunctions.getXPosNextToWall(player));			
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
	
	private void movePlayerVertically(Player player) {
		ObjectType type = player.getType();

		if(CollisionFunctions.canMove(player, levelManager.getSolidTile(), false, true)) {
			player.updateYPos();
			if(player.isInAir() && (type == ObjectType.PLAYER_D || type == ObjectType.PLAYER_U)) {
				player.updateGrav();
			}
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


	private void updatePlayerPos(Player player) {
		Player tempX = null;
		Player tempY = null;
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

			if(player.getVelX() > 0) {
				player.setX(tempX.getX() - player.getWidth());
				player.setVelX(0);
				if(player.isInAir() && player.getType() == ObjectType.PLAYER_R) {
					player.setJumping(false);
					player.setInAir(false);
				}
			}else {	
				double tempFutureX = player.getX()+player.getVelX()-tempX.getWidth();
				
				if(CollisionFunctions.canSetHere(tempFutureX, tempX.getY(),  
						tempX.getWidth(), tempX.getHeight(), levelManager.getSolidTile()))
				{
					movePlayerHorizontally(player);
					tempX.setX(tempFutureX);
				}
				else
				{
					tempX.setX(CollisionFunctions.getXPosNextToWall(tempX));
					player.setX(tempX.getX() + tempX.getHeight());
				}
				
				
					
			}
		}
		
		if(!playerInVerticalPath) {
			movePlayerVertically(player);
		}
		else
		{
			if(player.getVelY() > 0) {
				player.setY(tempY.getY() - player.getHeight());
				player.setVelY(0);
				if(player.isInAir() && player.getType() == ObjectType.PLAYER_D) {
					player.setJumping(false);
					player.setInAir(false);
				}
				
			}else {	
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
			
			}
		}
				
	}
	

	public void tick() {	
		for(UUID playerID : players.keySet() ) {
			Player currPlayer = players.get(playerID);
			
			if(!CollisionFunctions.isPlayerOnFloor(currPlayer, players, levelManager.getSolidTile())) {

				currPlayer.setInAir(true);
			}
			updatePlayerPos(currPlayer);
		
		
			
			for(UUID objectID : allObjects.keySet()) {
				if(objectID == playerID) {
					continue;
				}					
				GameObject tempObject = allObjects.get(objectID);
				if(ObjectType.isFlag(tempObject)) {
					checkFlagCollision(currPlayer, (Flag) tempObject);
				}
				
			}
			
		}
		
		if(levelManager.isLevelFinished()) {
			levelManager.setCurrLevel(levelManager.getCurrLevel()+1);
			levelManager.loadLevel();
		}
	}

	
	private void checkFlagCollision(Player player, Flag flag) {
		if(!player.hasReachedFlag() && ObjectType.reachedFlag(flag, player) && player.getBoundsBottom().intersects(flag.getBounds())) {
			levelManager.increaseNumPlayersFinished();
			player.setReachedFlag(true);
		}
			
	}

	
	private void checkPlayerCollision(Player player, Player otherPlayer) {
		switch(player.getType()) {
			case PLAYER_D:
				switch(otherPlayer.getType()) {
					case PLAYER_R:
						if(player.getBoundsBottom().intersects(otherPlayer.getBoundsTop())) {
							player.setY(otherPlayer.getY() - player.getHeight());
							player.setVelY(otherPlayer.getVelY());
							player.setFalling(false);
							player.setJumping(false);
							System.out.println("Red:  x = " + player.getX() + " | y = " + (player.getY()+player.getHeight()));
							System.out.println(" | velX = " + player.getVelX() + " | velY = " + player.getVelY());
							System.out.println("Blue:  x = " + otherPlayer.getX() + " | y = " + otherPlayer.getY());
							System.out.println(" | velX = " + otherPlayer.getVelX() + " | velY = " + otherPlayer.getVelY());
							System.out.println();
						}
						if(player.getBoundsRight().intersects(otherPlayer.getBoundsLeft())) {
							player.setVelX(0);
							player.setX(otherPlayer.getX() - player.getWidth());
							System.out.println("test r");
						}
						if(player.getBoundsTop().intersects(otherPlayer.getBoundsBottom())) {
							if(player.isJumping())
								player.setY(otherPlayer.getY() + player.getHeight());
							System.out.println("test T");

						}
						if(otherPlayer.getVelX() == 0 && player.getBoundsLeft().intersects(otherPlayer.getBoundsRight())) {
							player.setX(otherPlayer.getX()+player.getWidth());
						}
						break;
					case PLAYER_L:
						break;
					case PLAYER_U:
						break;
					default:
						break;		
				} 
				break;
			case PLAYER_R:
				switch(otherPlayer.getType()) {
					case PLAYER_D:
						if(player.getBoundsRight().intersects(otherPlayer.getBoundsLeft())) {
							player.setX(otherPlayer.getX() - player.getWidth());
							player.setVelX(otherPlayer.getVelX());
							player.setFalling(false);
							player.setJumping(false);
						}
						if(player.getBoundsBottom().intersects(otherPlayer.getBoundsTop())) {
							player.setY(otherPlayer.getY() - player.getHeight());
						}
						if(player.getBoundsLeft().intersects(otherPlayer.getBoundsRight())) {
							if(player.isJumping())
								player.setX(otherPlayer.getX()+ player.getWidth());
						}
						if(otherPlayer.getVelY() == 0 && player.getBoundsTop().intersects(otherPlayer.getBoundsBottom())) {
							player.setY(otherPlayer.getY() + player.getHeight());
						}
						break;
					case PLAYER_L:
						break;
					case PLAYER_U:
						break;
					default:
						break;		
			} 
				
		}
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
		
		for(UUID objectID : allObjects.keySet()) {
			GameObject obj = allObjects.get(objectID);
			if(ObjectType.isPlayer(obj)) {
				Player tempPlayer = players.get(objectID);
				if(obj.getType() == ObjectType.PLAYER_D) {
					switch(key) {
						case KeyEvent.VK_RIGHT:
							tempPlayer.setVelX(5);
							break;
						case KeyEvent.VK_LEFT:
							tempPlayer.setVelX(-5);
							break;
						case KeyEvent.VK_UP:
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
							if(!tempPlayer.isJumping() && !tempPlayer.isInAir()) {
								tempPlayer.setJumping(true);
								tempPlayer.setInAir(true);
								tempPlayer.setVelX(-10);
							}
							break;
					}
				}
			}
			
		}
		if(key == KeyEvent.VK_ESCAPE) {
			GameState.state = GameState.MENU;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		
		for(UUID objectID : allObjects.keySet()) {
			GameObject obj = allObjects.get(objectID);
			if(ObjectType.isPlayer(obj)) {
				
				Player tempPlayer = players.get(objectID);
				
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
			
		}//end of for;
		
		
	}






	public void addObject(GameObject object) {
		allObjects.put(object.getID(), object);
	}

	public void removeObject(UUID id) {
		allObjects.remove(id);
	}
	
	public void addPlayer(Player player) {
		players.put(player.getID(), player);
	}

	public void removePlayer(UUID id) {
		players.remove(id);
	}
	
	public void removeAll() {
		this.allObjects.clear();
		this.players.clear();
	}
	
	public LevelManager getLevelManager() {
		return this.levelManager;
	}
	
	
	
	
}
