package com.aaron.honjaya.inputs;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import gameMain.Game;
import gameStates.GameState;
import gameStates.LevelSelectHandler;
import gameStates.MenuHandler;
import gameStates.PlayingHandler;

//adapted from https://youtu.be/2mhDRjnv4WI?si=VXWiE--mdG4DdKVy

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
	
	
	//Depending on GameState, mouseInputs differ. 
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		switch(GameState.state) {
			case MENU:
				menuHandler.mouseClicked(e);
				break;
			case TUTORIAL:
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
			case TUTORIAL:
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
			case TUTORIAL:
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
			case TUTORIAL:
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
