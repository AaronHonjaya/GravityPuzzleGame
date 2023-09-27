package com.aaron.honjaya.inputs;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import gameMain.Game;
import gameStates.GameState;
import gameStates.LevelSelectHandler;
import gameStates.MenuHandler;
import gameStates.PlayingHandler;

public class MouseInput extends MouseAdapter{
	
	Game game;	
	private PlayingHandler playingHandler;
	private MenuHandler menuHandler;
	private LevelSelectHandler levelSelectHandler;

	
	public MouseInput(Game game) {
		this.game = game;
		playingHandler = this.game.getPlayingHandler(); 
		menuHandler = this.game.getMenuHandler(); 
		levelSelectHandler = this.game.getLevelSelectHandler(); 
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		switch(GameState.state) {
			case MENU:
				menuHandler.mouseClicked(e);
				break;
			case PLAYING:
				playingHandler.mouseClicked(e);
				break;
			case LEVEL_SELECT:
				levelSelectHandler.mouseClicked(e);
				break;
			default:
				break;
			
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		switch(GameState.state) {
			case MENU:
				menuHandler.mousePressed(e);
				break;
			case PLAYING:
				playingHandler.mousePressed(e);
				break;
			case LEVEL_SELECT:
				levelSelectHandler.mousePressed(e);
				break;
			default:
				break;
			
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		switch(GameState.state) {
			case MENU:
				menuHandler.mouseReleased(e);
				break;
			case PLAYING:
				playingHandler.mouseReleased(e);
				break;
			case LEVEL_SELECT:
				levelSelectHandler.mouseReleased(e);
				break;
			default:
				break;
			
		}
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		switch(GameState.state) {
			case MENU:
				menuHandler.mouseMoved(e);
				break;
			case PLAYING:
				playingHandler.mouseMoved(e);
				break;
			case LEVEL_SELECT:
				levelSelectHandler.mouseMoved(e);
				break;
			default:
				break;
			
		}
	}

}
