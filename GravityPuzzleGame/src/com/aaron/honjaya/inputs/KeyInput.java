package com.aaron.honjaya.inputs;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.aaron.honjaya.objects.Player;

import gameMain.Game;
import gameStates.GameState;
import gameStates.Handler;
import gameStates.MenuHandler;
import gameStates.PlayingHandler;

public class KeyInput extends KeyAdapter {
	
	Game game;	
	private PlayingHandler playingHandler;
	private MenuHandler menuHandler;

	
	public KeyInput(Game game) {
		this.game = game;
		playingHandler = this.game.getPlayingHandler(); 
		menuHandler = this.game.getMenuHandler(); 
	}
	
	public void keyPressed(KeyEvent e) {
		switch(GameState.state) {
			case MENU:
				menuHandler.keyPressed(e);
				break;
			case PLAYING:
				playingHandler.keyPressed(e);
				break;
			default:
				break;
		
		}
	}
	
	
	public void keyReleased(KeyEvent e) {
		switch(GameState.state) {
			case MENU:
				menuHandler.keyReleased(e);
				break;
			case PLAYING:
				playingHandler.keyReleased(e);
				break;
			default:
				break;
	
		}
	}

}
