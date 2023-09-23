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
import com.aaron.honjaya.objects.Block;
import com.aaron.honjaya.objects.Player;


import gameStates.GameState;
import gameStates.MenuHandler;
import gameStates.PlayingHandler;




public class Game extends Canvas implements Runnable{
	

	//private static final long serialVersionUID = 1L;
	public static final int WIDTH = 800; 
	public static final int HEIGHT = 600; 
	public final String TITLE = "Puzzle Game";

	
	private boolean running = false;
	private Thread thread;
	
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	
	//private Handler handler;
	private PlayingHandler playingHandler;
	private MenuHandler menuHandler;
	
	public void init(){
		
		this.requestFocus();
		
		playingHandler = new PlayingHandler();
		playingHandler.loadLevel(0);
		
		menuHandler = new MenuHandler();
		
		addKeyListener(new KeyInput(this));
		addMouseListener(new MouseInput(this));
	}
	

	public synchronized void start() {
		if(running){
			return;
		}
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
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
			case PLAYING:
				playingHandler.tick();
				break;
			default:
				break;
					
			}
	}
	
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
			case PLAYING:
				playingHandler.render(g);
				break;
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

	public void setPlayingHandler(PlayingHandler playingHandler) {
		this.playingHandler = playingHandler;
	}

	public MenuHandler getMenuHandler() {
		return menuHandler;
	}

	public void setMenuHandler(MenuHandler menuHandler) {
		this.menuHandler = menuHandler;
	}



	


}
