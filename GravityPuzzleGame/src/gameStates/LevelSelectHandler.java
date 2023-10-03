package gameStates;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.*;
import com.aaron.honjaya.ui.*;

import gameMain.Game;
import level.LevelManager;

public class LevelSelectHandler extends MenuHandler implements Handler {

	private LevelManager levelManager;
	private final String str = "Press 'ESCAPE' to return to the Menu";
	
	public LevelSelectHandler(LevelManager levelManager) {
		buttons = new ArrayList<>();
		this.levelManager = levelManager;
		loadLevelButtons();
	}
	
	private void loadLevelButtons() {
		int row = 1;
		int xOffset = 50; 
		int yOffset = 50;
		int lvlNum = 1;
		String str = "LEVEL " + lvlNum;
		buttons.add(new MenuButton(xOffset, 100, 100, 50, str, 20, GameState.PLAYING));
		lvlNum++;
				
		while(buttons.size() < LevelManager.NUM_LEVELS) {
			str = "LEVEL " + lvlNum;
			
			Button prevButton = buttons.get(lvlNum-2);
			int nextX = (int)prevButton.getX() + Button.getWidth() + xOffset;
			int nextY = (int)prevButton.getY();
			
			if(nextX + Button.getWidth() + xOffset > Game.WIDTH) {
				row++;
				nextX = xOffset;
				nextY = (int)prevButton.getY() + Button.getHeight() + yOffset;
			}
			
			buttons.add(new MenuButton(nextX, nextY, 100, 50, str, 20, GameState.PLAYING));
			lvlNum++;
		}
	}

	@Override
	public void tick() {
		for(Button lvlButton : buttons) {
			lvlButton.tick();
		}
	}

	@Override
	public void render(Graphics g) {
		for(Button lvlButton : buttons) {
			lvlButton.render(g);
		}
		Graphics2D g2d = (Graphics2D) g;
		drawInstructions(g2d);
	}
	
	private void drawInstructions(Graphics2D g2d){
		
		
		//Although slightly altered, original code to center the text was found online. Credit is below: 
		//http://www.java2s.com/Tutorials/Java/Graphics_How_to/Text/Center_a_string_in_a_rectangle.htm
		g2d.setColor(Color.RED);
		Font font = new Font("Arial", Font.BOLD, 40);
		g2d.setFont(font);
		FontMetrics fm = g2d.getFontMetrics();
		
		int textX = ((Game.WIDTH - fm.stringWidth(str)) / 2);
		int textY = ((100 - fm.getHeight()) / 2) + fm.getAscent();
		
		g2d.drawString(str, textX, textY);	
	}
	

	@Override
	public void mouseReleased(MouseEvent e) {
		
		for(int i = 0; i < buttons.size(); i++) {
			Button lvlButton = buttons.get(i);
			if(mouseIsIn(e, lvlButton) && lvlButton.isMousePressed()) {
				levelManager.setCurrLevel(i);
				levelManager.loadLevel();
				lvlButton.applyGameState();
				break;
			}
		}	
		
		resetButtons();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) 
			GameState.state = GameState.MENU;
	}



}
