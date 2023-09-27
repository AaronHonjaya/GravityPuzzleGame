package gameMain;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Window extends JFrame{
	
	
	public Window(int w, int h, String title, Game game) {
		game.setPreferredSize(new Dimension(w, h));
		game.setMaximumSize(new Dimension(w, h));
		game.setMinimumSize(new Dimension(w, h));
		
		this.setTitle(title);
		this.add(game);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	
		
		game.start();
	}
}
