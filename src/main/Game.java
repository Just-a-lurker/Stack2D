package main;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import objects.GameObject;
import sprite.Draw;
import sprite.Sprite;


public class Game {
	
	Draw draw;
	SaveManager save;
	Random rand = new Random();
	
	public static List<GameObject> objects;
	boolean spawnNew = false;
	public static boolean animate, animating,gameOver;
	
	public Sprite objectSprite;
	
	GameConfig config = new GameConfig();
	public static int bestScore = 0, score;
	
	
	public Draw getDraw() {
		return draw;
	}

	public Game() {
		save = new SaveManager("save.txt",this);
		config.loadConfig("config.xml");
		start();
		save.load();
		draw = new Draw();
	}
	
	public void start() {
		objectSprite = new Sprite(100, 29, 0);
		objects = new ArrayList<GameObject>();
		objects.add(new GameObject(Stack.WIDTH / 2 - objectSprite.getWidth()/2, Stack.HEIGHT - 30, objectSprite, false, this));
		objects.add(new GameObject(Stack.WIDTH / 2 - objectSprite.getWidth()/2, Stack.HEIGHT - 30*2, objectSprite, false, this));
		objects.add(new GameObject(Stack.WIDTH / 2 - objectSprite.getWidth()/2, Stack.HEIGHT - 30*3, objectSprite, false, this));
		spawnNew = true;
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
		
		if(spawnNew) {
			if(!animating) {
				score++;
				objectSprite = new Sprite(objects.get(objects.size()-1).getWidth(), 29, 0);
				objects.add(new GameObject(rand.nextInt(Stack.WIDTH) - objectSprite.getWidth(), Stack.HEIGHT - 30*4, objectSprite, true, this));
				spawnNew = false;
				save.save();
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
		}
		else {
			if(!objects.get(objects.size() - 1).isMoving()) {
				spawnNew = true;
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
		g2.drawString(s, Stack.WIDTH * Stack.scale / 2 - w, 80);
		String best = "Best score : " + Game.bestScore;
		int bw = g2.getFontMetrics().stringWidth(best)/2;
		g2.drawString(best, Stack.WIDTH * Stack.scale / 2 - bw, 100);
	}
	
	
}
