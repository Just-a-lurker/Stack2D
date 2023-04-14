package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class Stack extends JPanel implements Runnable {

	private static final long serialVersionUID = 4642014212666320833L;
	public static final int WIDTH = 250, HEIGHT = 300;
	public static float scale = 2;
	public static boolean darkMode = false;
	public static int fps = 0;
	
	Game game;
	KeyInput key;
	Thread thread;
	

	public KeyInput getKey() {
		return key;
	}

	public Stack() {
		setPreferredSize(new Dimension((int) (WIDTH * scale), (int) (HEIGHT * scale)));
		game = new Game(this);
		key = new KeyInput();
		this.addKeyListener(key);
		this.setFocusable(true);
		this.setDoubleBuffered(true);
	}
	
	public void start() {
		game.setSound(new Thread(game));
		game.getSound().start();
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run() {
		double drawInterval = 1000000000/90;
		double deltaTime = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount = 0;
		while(thread!=null)
		{
			currentTime = System.nanoTime();
			deltaTime += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			lastTime = currentTime;
			
			if(deltaTime >= 1)
			{
				update();
				repaint();
				deltaTime--;
				drawCount++;
			}
			if(timer > 1000000000) {
				fps = drawCount;
				drawCount = 0;
				timer = 0;
			}
			
		}
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void update() {
		if(game.isGameOver() || darkMode) setBackground(Color.black);
		else {
			if(!darkMode)
			setBackground(Color.white);
		}
		game.update();
		key.update();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		game.draw(g2);
		game.drawText(g2);
		
		g2.dispose();
	}

}
