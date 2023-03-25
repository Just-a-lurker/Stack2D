package main;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

public class Stack extends Canvas implements Runnable {

	public static final int WIDTH = 250, HEIGHT = 300;
	public static float scale = 2;
	
	Game game;
	KeyInput key;
	JFrame frame;
	Thread thread;
	
	private static BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	//public static int[] pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
	
	public Stack() {
		setPreferredSize(new Dimension((int) (WIDTH * scale), (int) (HEIGHT * scale)));
		frame = new JFrame();
		game = new Game();
		key = new KeyInput();
		addKeyListener(key);
	}
	
	public void start() {
		thread = new Thread(this);
		thread.start();
		game.setSound(new Thread(game));
		game.getSound().start();
	}
	
	@Override
	public void run() {
		double drawInterval = 1000000000/60;
		double deltaTime = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		while(thread!=null)
		{
			currentTime = System.nanoTime();
			deltaTime += (currentTime - lastTime) / drawInterval;
			lastTime = currentTime;
			if(deltaTime >= 1)
			{
				update();
				deltaTime--;
			}
			draw();
		}
		stop();
		
	}
	
	public void stop() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		game.update();
		key.update();
	}
	
	public void draw() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics2D g2 = (Graphics2D) bs.getDrawGraphics();
		game.draw();
		g2.drawImage(img, 0,0,(int) (WIDTH * scale), (int) (HEIGHT * scale),null);
		game.drawText(g2);
		
		g2.dispose();
		bs.show();
	}

}
