package gameStates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.*;

import com.aaron.honjaya.framework.GameObject;
import com.aaron.honjaya.framework.ObjectType;
import com.aaron.honjaya.objects.Block;
import com.aaron.honjaya.objects.Flag;
import com.aaron.honjaya.objects.Player;
import com.aaron.honjaya.utils.SpriteLoader;


public class PlayingHandler implements Handler{
	

	private BufferedImage spriteSheet = null;
	private ArrayList<BufferedImage> levels = new ArrayList<>();
	private BufferedImage image = null;
	private int numPlayers; 
	private int numPlayersFinished; 
	private boolean levelIsFinished; 
	

	
	public HashMap<UUID, GameObject> allObjects = new HashMap<>();
	public HashMap<UUID, Player> players = new HashMap<>();
	
	
	public PlayingHandler() {
		SpriteLoader sl = new SpriteLoader("/sprite_sheet.png");
		levels.add(sl.loadImage("/testLevel.png"));
		numPlayers = 0;
		numPlayersFinished = 0;
	}
	
	public void render(Graphics g) {
		for(UUID id : allObjects.keySet()) {
			GameObject tempObject = allObjects.get(id);
			tempObject.render(g);
			
		}
	}

	public void tick() {	
		
		for(UUID playerID : players.keySet() ) {
			Player currPlayer = players.get(playerID);
			currPlayer.tick();
			//Hello how are you;
			
				for(UUID objectID : allObjects.keySet()) {
					if(objectID == playerID) {
						continue;
					}
					
					GameObject tempObject = allObjects.get(objectID);
					if(!ObjectType.isPlayer(tempObject))
						tempObject.tick();
					
					
					if(ObjectType.isBlock(tempObject)){
						checkBlockCollision(currPlayer, tempObject);
					}
					else if(tempObject.getID() != currPlayer.getID() && players.containsKey(tempObject.getID())){
						checkPlayerCollision(currPlayer, players.get(tempObject.getID()));
					}
					else if(ObjectType.isFlag(tempObject)) {
						checkFlagCollision(currPlayer, (Flag) tempObject);
					}
					
				}

		
			
			
		}
		if(!levelIsFinished && numPlayers == numPlayersFinished) {
			System.out.println("test");
			numPlayers = 0;
			numPlayersFinished = 0;
			levelIsFinished = true;
			allObjects.clear();
			players.clear();
			loadLevel(0);
		}
	}
	
	
	private void checkBlockCollision(Player player, GameObject obj) {
			switch(player.getType()) {
				case PLAYER_D: 
					if(player.getBounds().intersects(obj.getBounds())) {
						player.setY(obj.getY() - player.getHeight());
						player.setVelY(0);
						player.setFalling(false);
						player.setJumping(false);
					}else{
						player.setFalling(true);
					}
					if(player.getBoundsTop().intersects(obj.getBounds())) { 
						player.setY(obj.getY() + player.getHeight());
						player.setVelY(0);
						System.out.println("red top touched");
					}
					if(player.getBoundsRight().intersects(obj.getBounds())) {
						player.setX(obj.getX() - player.getWidth());
					}
					if(player.getBoundsLeft().intersects(obj.getBounds())) {
						player.setX(obj.getX() + player.getWidth());
					}
					break;
				case PLAYER_L: 
					
					break;
				case PLAYER_U: 
					
					break;
				case PLAYER_R: 
					if(player.getBoundsRight().intersects(obj.getBounds())) {
						player.setX(obj.getX() - player.getWidth());
						player.setVelX(0);
						player.setFalling(false);
						player.setJumping(false);
						//System.out.println("test B");

					}else{
						player.setFalling(true);
					}
					if(player.getBoundsLeft().intersects(obj.getBounds())) {
						player.setX(obj.getX() + player.getWidth());
						player.setVelX(0);
						//System.out.println("test L");
					}
					if(player.getBounds().intersects(obj.getBounds())) {
						player.setY(obj.getY() - player.getHeight());
						
					}
					if(player.getBoundsTop().intersects(obj.getBounds())) {
						player.setY(obj.getY() + player.getHeight());
						//System.out.println("test T");
						
					}
					break;
			}
	}		
	
	private void checkFlagCollision(Player player, Flag flag) {
		if(!player.hasReachedFlag() && ObjectType.reachedFlag(flag, player) && player.getBounds().intersects(flag.getBounds())) {
			numPlayersFinished++;
			player.setReachedFlag(true);
		}
			
	}

	
	private void checkPlayerCollision(Player player, Player otherPlayer) {
		switch(player.getType()) {
			case PLAYER_D:
				switch(otherPlayer.getType()) {
					case PLAYER_R:
						if(player.getBounds().intersects(otherPlayer.getBoundsTop())) {
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
						if(player.getBoundsTop().intersects(otherPlayer.getBounds())) {
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
						if(player.getBounds().intersects(otherPlayer.getBoundsTop())) {
							player.setY(otherPlayer.getY() - player.getHeight());
						}
						if(player.getBoundsLeft().intersects(otherPlayer.getBoundsRight())) {
							if(player.isJumping())
								player.setX(otherPlayer.getX()+ player.getWidth());
						}
						if(otherPlayer.getVelY() == 0 && player.getBoundsTop().intersects(otherPlayer.getBounds())) {
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
	
	
	


	
	
	public void loadLevel(int lvlNum) {
		levelIsFinished = false;
		BufferedImage image = levels.get(lvlNum);
		int w = image.getWidth();
		int h = image.getHeight();
		System.out.println(w + " " + h);
		
		for(int xx = 0; xx < w; xx++) {
			for(int yy = 0; yy < h; yy++) {
				int pixel = image.getRGB(xx, yy);
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;

				if(red == 255 && green == 255 && blue == 255) {
					addObject(new Block(xx*32, yy*32, ObjectType.BLOCK));
				}
				else if(red == 255 && green == 0 && blue == 0) {
					Player temp = new Player(xx*32, yy*32, ObjectType.PLAYER_D, this);
					addObject(temp);
					addPlayer(temp);
					numPlayers++;
				}
				else if(red == 0 && green == 0 && blue == 255) {
					Player temp = new Player(xx*32, yy*32, ObjectType.PLAYER_R, this);
					addObject(temp);
					addPlayer(temp);
					numPlayers++;
				}
				else if(red == 0 && green == 148 && blue == 255) {
					addObject(new Flag(xx*32, yy*32, ObjectType.FLAG_R, this));
				}
				else if(red == 255 && green == 106 && blue == 0) {
					addObject(new Flag(xx*32, yy*32, ObjectType.FLAG_D, this));
				}
			}
		}
		System.out.println(numPlayers + ", " + numPlayersFinished);
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
			GameObject temp = allObjects.get(objectID);
			if(temp.getType() == ObjectType.PLAYER_D) {
				switch(key) {
					case KeyEvent.VK_RIGHT:
						temp.setVelX(5);
						break;
					case KeyEvent.VK_LEFT:
						temp.setVelX(-5);
						break;
					case KeyEvent.VK_UP:
						System.out.println("jumped");
						if(!temp.isJumping()) {
							temp.setJumping(true);
							temp.setVelY(-10);
						}
						break;
				}
			}
			else if(temp.getType() == ObjectType.PLAYER_R) {
				switch(key) {
					case KeyEvent.VK_A:
						temp.setVelY(5);
						break;
					case KeyEvent.VK_D:
						temp.setVelY(-5);
						break;
					case KeyEvent.VK_SPACE:
						System.out.println("jumped");
						if(!temp.isJumping()) {
							temp.setJumping(true);
							temp.setVelX(-10);
						}
						break;
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
			GameObject temp = allObjects.get(objectID);
			
			if(temp.getType() == ObjectType.PLAYER_D) {
				switch(key) {
					case KeyEvent.VK_RIGHT:
						temp.setVelX(0);
						break;
					case KeyEvent.VK_LEFT:
						temp.setVelX(0);
						break;
				}
			}else if(temp.getType() == ObjectType.PLAYER_R) {
				switch(key) {
					case KeyEvent.VK_A:
						temp.setVelY(0);
						break;
					case KeyEvent.VK_D:
						temp.setVelY(0);
						break;
				}
			}
		}
		
		
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
	
	
	
}
