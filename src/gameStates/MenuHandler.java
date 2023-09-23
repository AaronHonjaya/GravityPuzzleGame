package gameStates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import com.aaron.honjaya.framework.GameObject;
import com.aaron.honjaya.framework.ObjectType;
import com.aaron.honjaya.ui.MenuButton;

import gameMain.Game;

public class MenuHandler implements Handler{
	
	public LinkedList<MenuButton> buttons = new LinkedList<>();
	private MenuButton tempButton;
	
	
	public MenuHandler() {
		loadButtons();
	}
	
	private void loadButtons() {
		buttons.add(new MenuButton(Game.WIDTH/2	, 0, 0, GameState.PLAYING));
		buttons.add(new MenuButton(Game.WIDTH/2	, 300, 1, GameState.LEVELS));
		buttons.add(new MenuButton(Game.WIDTH/2	, 500, 2, GameState.QUIT));
	}

	
	public void tick() {
		for(int i = 0; i < buttons.size(); i++) {
			tempButton = buttons.get(i);
			tempButton.tick();
		}
	}

	@Override
	public void render(Graphics g) {
		for(int i = 0; i < buttons.size(); i++) {
			tempButton = buttons.get(i);
			tempButton.render(g);
		}
	}

	
	private boolean isIn(MouseEvent e, MenuButton mb) {
		return mb.getBounds().contains(e.getX(), e.getY());
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		for(MenuButton mb : buttons) {
			if(isIn(e, mb)) {
				mb.setMousePressed(true);
			}
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
		
		if(key == KeyEvent.VK_ENTER) {
			GameState.state = GameState.PLAYING;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	
}
