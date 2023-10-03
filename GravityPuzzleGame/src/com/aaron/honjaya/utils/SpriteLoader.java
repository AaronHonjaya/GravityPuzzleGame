package com.aaron.honjaya.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteLoader {

	private BufferedImage image;
	public static final int SPRITE_WIDTH = 25;
	public static final int SPRITE_HEIGHT = 25;
	
	public static final int BUTTON_WIDTH = 200;
	public static final int BUTTON_HEIGHT = 100;

	

	public SpriteLoader() {
		
	}
	
	public SpriteLoader(String path) {
		try {
			image = ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BufferedImage grabSpriteImage(int col, int row) {
		BufferedImage img = image.getSubimage(col * SPRITE_WIDTH, row * SPRITE_WIDTH, SPRITE_WIDTH, SPRITE_HEIGHT);
		return img;
	}
	
	public BufferedImage grabButtonImage(int col, int row) {
		BufferedImage img = image.getSubimage(col * BUTTON_WIDTH, row * BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
		return img;
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public BufferedImage loadImage(String path) {
		BufferedImage temp = null;
		try {
			temp = ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return temp;
	}
	
	
	
}
