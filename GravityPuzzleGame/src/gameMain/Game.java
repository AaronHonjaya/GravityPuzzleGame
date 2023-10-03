package gameMain;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JFrame;

import com.aaron.honjaya.framework.ObjectType;
import com.aaron.honjaya.inputs.KeyInput;
import com.aaron.honjaya.inputs.MouseInput;
import com.aaron.honjaya.objects.Tile;
import com.aaron.honjaya.objects.Player;


import gameStates.GameState;
import gameStates.LevelSelectHandler;
import gameStates.MenuHandler;
import gameStates.PlayingHandler;




public class Game extends Canvas implements Runnable{
	

	public static final int TILE_SIZE = 32;
	
	public static final int WIDTH_IN_TILES = 25;
	public static final int HEIGHT_IN_TILES = 18;
	
	public static final int WIDTH = TILE_SIZE * WIDTH_IN_TILES; 
	public static final int HEIGHT = TILE_SIZE * HEIGHT_IN_TILES; 
	
	
	public final String TITLE = "Puzzle Game";

	
	private boolean running = false;
	private Thread thread;
	
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	
	private PlayingHandler playingHandler;
	private MenuHandler menuHandler;
	private LevelSelectHandler levelSelectHandler;
	
	public void init(){
		
		this.requestFocus();
				
		playingHandler = new PlayingHandler();
		
		menuHandler = new MenuHandler();
		levelSelectHandler = new LevelSelectHandler(playingHandler.getLevelManager());
		
		addKeyListener(new KeyInput(this));
		MouseInput mouseInput = new MouseInput(this);
		addMouseListener(mouseInput);
		addMouseMotionListener(mouseInput);
	}
	

	//credit to https://youtu.be/Zh7YiiEuJFw?si=cKaJM3PAIy-QLsTz
	public synchronized void start() {
		if(running){
			return;
		}
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	//credit to https://youtu.be/Zh7YiiEuJFw?si=cKaJM3PAIy-QLsTz
	private synchronized void stop() {
		if(!running){
			return;
		}
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(1);
		
	}
	
	//credit to https://youtu.be/Zh7YiiEuJFw?si=cKaJM3PAIy-QLsTz
	public void run() {
		init();
		long lastTime = System.nanoTime();
		final double numTicks = 60.0;
		double ns = 1e9 / numTicks;
		double delta = 0;
		int updates = 0;
		int frames = 0;
		long timer = System.currentTimeMillis();
		
		
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime)/ns;
			lastTime = now;
			
			if(delta >= 1) {
				tick();
				render();
				updates++;
				delta--;
			}
			frames++;
			if(System.currentTimeMillis()-timer > 1000) {
				timer += 1000;
				//System.out.println(updates + " ticks, fps " + frames);
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}
	
	
	
	private void tick() {
		switch(GameState.state) {
			case MENU:
				menuHandler.tick();
			case TUTORIAL:
			case PLAYING:
				playingHandler.tick();
				break;
			case LEVEL_SELECT: 
				levelSelectHandler.tick();
				break;
			case EXIT:
				System.exit(1);
			default:
				break;
					
			}
	}
	
	
	//adapted from https://youtu.be/Nn8LH6T3xuc?si=L0l2EqtFLivnAUeS
	private void render(){
		BufferStrategy bs = this.getBufferStrategy();
			
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
				
		Graphics g = bs.getDrawGraphics();
		
		//////
				
		g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		
		switch(GameState.state) {
			case MENU:
				menuHandler.render(g);
				break;
			case TUTORIAL:
			case PLAYING:
				playingHandler.render(g);
				break;
			case LEVEL_SELECT: 
				levelSelectHandler.render(g);
			default:
				break;
				
		}
		
		//////
		g.dispose();
		bs.show();
			
	}
		
	
	
	

	
	public static void main(String[] args) {
		Game game = new Game();
		new Window(WIDTH, HEIGHT, "PUZZLE GAME", new Game());
	}


	
	public PlayingHandler getPlayingHandler() {
		return playingHandler;
	}

	public MenuHandler getMenuHandler() {
		return menuHandler;
	}

	public LevelSelectHandler getLevelSelectHandler() {
		return levelSelectHandler;
	}



	


}
