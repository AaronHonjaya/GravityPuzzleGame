package gameStates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.*;

import com.aaron.honjaya.framework.GameObject;
import com.aaron.honjaya.framework.ObjectType;
import com.aaron.honjaya.ui.Button;
import com.aaron.honjaya.ui.MenuButton;

import gameMain.Game;

//credit: //youtu.be/zGrLCHROa4s?si=ZuTVxIi-uoDQMvUC. 
// Slightly Modified by Aaron Honjaya - University of Washington


public class MenuHandler implements Handler{

	
	protected ArrayList<Button> buttons;
	
	
	public MenuHandler() {
		buttons = new ArrayList<>();
		loadButtons();
	}
	
	private void loadButtons() {
		int buttonWidth = 200, buttonHeight = 75;
		buttons.add(new MenuButton(Game.WIDTH/2	- buttonWidth/2, 100, buttonWidth, buttonHeight, "PLAY", 36, GameState.PLAYING));
		buttons.add(new MenuButton(Game.WIDTH/2	- buttonWidth/2, 200, buttonWidth, buttonHeight, "LEVELS", 36, GameState.LEVEL_SELECT));
		buttons.add(new MenuButton(Game.WIDTH/2	- buttonWidth/2, 300, buttonWidth, buttonHeight, "TUTORIAL", 36, GameState.TUTORIAL));
		buttons.add(new MenuButton(Game.WIDTH/2	- buttonWidth/2, 400, buttonWidth, buttonHeight, "EXIT", 36, GameState.EXIT));

	}

	
	public void tick() {
		for(Button button : buttons) {
			button.tick();
		}
	}

	@Override
	public void render(Graphics g) {
		for(Button button : buttons) {
			button.render(g);
		}
	}

	
	protected boolean mouseIsIn(MouseEvent e, Button button) {
		return button.getBounds().contains(e.getX(), e.getY());
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		for(Button menuButton : buttons) {
			if(mouseIsIn(e, menuButton)) {
				menuButton.setMousePressed(true);
				break;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for(Button menuButton : buttons) {
			if(mouseIsIn(e, menuButton) && menuButton.isMousePressed()) {
				menuButton.applyGameState();
				break;
			}
		}
		
		resetButtons();
		
	}
	

	@Override
	public void mouseMoved(MouseEvent e) {
		for(Button menuButton : buttons) {
			menuButton.setMouseOver(false);
		}
		
		for(Button menuButton : buttons) {
			if(mouseIsIn(e, menuButton)) {
				menuButton.setMouseOver(true);
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_ENTER) {
			GameState.state = GameState.PLAYING;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}


	protected void resetButtons() {
		for(Button menuButton : buttons) {
			menuButton.resetBools();
		}
	}
	
}
