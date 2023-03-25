package main;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import graphic.Draw;
import graphic.Object;
import objects.GameObject;


public class Game implements Runnable{
	
	Draw draw;
	SaveManager save;
	Random rand = new Random();
	private Thread sound;
	
	private AudioClip place;
	
	public static List<GameObject> objects;
	boolean spawnAnother = false;
	private boolean animate, animating,gameOver;
	
	public Object objectSprite;
	
	GameConfig config = new GameConfig();
	public static int bestScore = 0, score;
	
	
	public Draw getDraw() {
		return draw;
	}
	
	

	public Thread getSound() {
		return sound;
	}



	public void setSound(Thread sound) {
		this.sound = sound;
	}



	public boolean isAnimate() {
		return animate;
	}



	public void setAnimate(boolean animate) {
		this.animate = animate;
	}



	public boolean isAnimating() {
		return animating;
	}



	public void setAnimating(boolean animating) {
		this.animating = animating;
	}



	public boolean isGameOver() {
		return gameOver;
	}



	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}



	public Game() {
		try {
			place = Applet.newAudioClip(new URL("file","","data/place.wav"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		save = new SaveManager("save.txt",this);
		config.loadConfig("config.xml");
		start();
		save.load();
		draw = new Draw(this);
	}
	
	public void start() {
		objectSprite = new Object(100, 29, 0);
		objects = new ArrayList<GameObject>();
		objects.add(new GameObject(Stack.WIDTH / 2 - objectSprite.getWidth()/2, Stack.HEIGHT - 30, objectSprite, false, this));
		objects.add(new GameObject(Stack.WIDTH / 2 - objectSprite.getWidth()/2, Stack.HEIGHT - 30*2, objectSprite, false, this));
		objects.add(new GameObject(Stack.WIDTH / 2 - objectSprite.getWidth()/2, Stack.HEIGHT - 30*3, objectSprite, false, this));
		spawnAnother = true;
		gameOver = false;
		animate = false;
		animating = false;
		
		if(score > bestScore) {
			bestScore = score;
			config.saveConfig("best", bestScore);
		}
		score = -1;
	}
	
	public void update() {
		if(gameOver) {
			if(KeyInput.key(KeyEvent.VK_R)) {
				start();
			}
			return;
		}
		
		if(KeyInput.keyDown(KeyEvent.VK_R)) start();
		
		for(int i=0;i< objects.size();i++) {
			objects.get(i).update();
			if(objects.get(i).getY() + Stack.HEIGHT >= Stack.HEIGHT * Stack.scale) objects.remove(i);
		}
		animate = false;
		
		if(spawnAnother) {
			if(!animating) {
				score++;
				objectSprite = new Object(objects.get(objects.size()-1).getWidth(), 29, 0);
				objects.add(new GameObject(rand.nextInt(Stack.WIDTH) - objectSprite.getWidth(), Stack.HEIGHT - 30*4, objectSprite, true, this));
				spawnAnother = false;
			}
			else {
				boolean endOfAnim = true;
				for(int i=0;i<objects.size();i++) {
					if(objects.get(i).isAnimate()) {
						endOfAnim = false;
					}
				}
				if(endOfAnim) animating = false;
			}
			save.save();
		}
		else {
			if(!objects.get(objects.size() - 1).isMoving()) {
				spawnAnother = true;
				animate = true;
				animating = true;
			}
		}
	}
	
	public void draw() {
		draw.drawBackGround();
		
		for(int i=0;i< objects.size();i++) {
			objects.get(i).draw();
		}
		
		for(int i = 0;i<Stack.pixels.length;i++) {
			Stack.pixels[i] = draw.getPixels()[i];
		}
	}
	
	public void drawText(Graphics2D g2) {
			g2.setFont(new Font("Arial",0,20));
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			String s = "Score : " + score;
			int w = g2.getFontMetrics().stringWidth(s)/2;
			String best = "Best score : " + Game.bestScore;
			int bw = g2.getFontMetrics().stringWidth(best)/2;
			String go = "Game Over";
			int gow = g2.getFontMetrics().stringWidth(go)/2;
			String r = "Press r to restart";
			int rw = g2.getFontMetrics().stringWidth(r)/2;
		if(!gameOver) {
			g2.drawString(s, Stack.WIDTH * Stack.scale / 2 - w, 80);
		}
		else {
			g2.setColor(Color.white);
			g2.drawString(go, Stack.WIDTH * Stack.scale / 2 - gow, Stack.HEIGHT*Stack.scale / 2 - 20);
			g2.drawString(s, Stack.WIDTH * Stack.scale / 2 - w, Stack.HEIGHT*Stack.scale / 2);
			g2.drawString(best, Stack.WIDTH * Stack.scale / 2 - bw, Stack.HEIGHT*Stack.scale / 2 + 20);
			g2.drawString(r, Stack.WIDTH * Stack.scale / 2 - rw, Stack.HEIGHT*Stack.scale / 2 + 40);
		}
		
	}

	@Override
	public void run() {
		while(sound!=null) {
			if(KeyInput.keyDown(KeyEvent.VK_SPACE) && !gameOver && !animating) place.play();
//			try {
//				Thread.sleep(0);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		Thread.yield();
			
		}
		}
}
