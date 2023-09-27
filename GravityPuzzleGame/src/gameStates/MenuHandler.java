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

public class MenuHandler implements Handler{
	private static final int PLAY = 1;
	private static final int LEVELS = 2;
	private static final int EXIT = 3;

	
	protected ArrayList<Button> buttons = new ArrayList<>();
	
	
	public MenuHandler() {
		loadButtons();
	}
	
	private void loadButtons() {
		buttons.add(new MenuButton(Game.WIDTH/2	, 100, 0, GameState.PLAYING));
		buttons.add(new MenuButton(Game.WIDTH/2	, 250, 1, GameState.LEVEL_SELECT));
		buttons.add(new MenuButton(Game.WIDTH/2	, 400, 2, GameState.EXIT));
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

	
	protected boolean isIn(MouseEvent e, Button button) {
		return button.getBounds().contains(e.getX(), e.getY());
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		for(Button menuButton : buttons) {
			if(isIn(e, menuButton)) {
				menuButton.setMousePressed(true);
				break;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for(Button menuButton : buttons) {
			if(isIn(e, menuButton) && menuButton.isMousePressed()) {
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
			if(isIn(e, menuButton)) {
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
