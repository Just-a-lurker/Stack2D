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
import java.sql.Date;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import objects.GameObject;
import objects.Object;


@SuppressWarnings("removal")
public class Game implements Runnable{
	
	SaveManager save;
	Random rand = new Random();
	Stack stack;
	Thread sound;
	AudioClip place;
	List<GameObject> objects;
	Object object;
	SQLManager sqlM;
	Date date;
	
	private boolean animate, animating,gameOver,endGame = false,spawnAnother = false;
	private int bestScore = 0, score;
	
	
	public void setDate(Date date) {
		this.date = date;
	}



	public int getBestScore() {
		return bestScore;
	}



	public void setBestScore(int bestScore) {
		this.bestScore = bestScore;
	}



	public int getScore() {
		return score;
	}



	public void setScore(int score) {
		this.score = score;
	}



	public List<GameObject> getObjects() {
		return objects;
	}



	public void setObjects(List<GameObject> objects) {
		this.objects = objects;
	}



	public Stack getStack() {
		return stack;
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



	public Game(Stack stack) {
		this.stack = stack;
		try {
			place = Applet.newAudioClip(new URL("file","","data/place.wav"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		sqlM = new SQLManager("config.txt", this);
		place.play();
		place.stop();
		save = new SaveManager("save.txt",this);
		start();
		save.load();
	}
	
	public void start() {
		object = new Object(100, 29);
		objects = new LinkedList<GameObject>();
		objects.add(new GameObject(Stack.WIDTH / 2 - object.getWidth()/2, Stack.HEIGHT - 30, object, false, this));
		objects.add(new GameObject(Stack.WIDTH / 2 - object.getWidth()/2, Stack.HEIGHT - 30*2, object, false, this));
		objects.add(new GameObject(Stack.WIDTH / 2 - object.getWidth()/2, Stack.HEIGHT - 30*3, object, false, this));
		spawnAnother = true;
		gameOver = false;
		animate = false;
		animating = false;
		score = -1;
		try {
			sqlM.loadSQL();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		endGame = false;
	}
	
	public void update() {
		if(gameOver) {
			if(!endGame) {
				if(score > bestScore) {
					bestScore = score;
					try {
						sqlM.saveSQL();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				save.save();
				endGame = true;
			}
			if(stack.getKey().keyDown(KeyEvent.VK_R)) {
				start();
			}
			return;
		}
		
		if(stack.getKey().keyDown(KeyEvent.VK_D)) Stack.darkMode = !Stack.darkMode;
		
		if(stack.getKey().keyDown(KeyEvent.VK_R)) start();
		
		for(int i=0;i< objects.size();i++) {
			objects.get(i).update();
			if(objects.get(i).getY() + Stack.HEIGHT >= Stack.HEIGHT * Stack.scale) objects.remove(i);
		}
		animate = false;
		
		if(spawnAnother) {
			if(!animating) {
				if(!gameOver) score++;
				object = new Object(objects.get(objects.size()-1).getWidth(), 29);
				objects.add(new GameObject(rand.nextInt(Stack.WIDTH) - object.getWidth(), Stack.HEIGHT - 30*4, object, true, this));
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
	
	public void draw(Graphics2D g2) {
		for(int i=0;i< objects.size();i++) {
			objects.get(i).draw(g2);
		}
	
	}
	
	public void drawText(Graphics2D g2) {
			if(!Stack.darkMode || gameOver) {
				g2.setColor(Color.black);
			}
			else g2.setColor(Color.gray);
			g2.setFont(new Font("Arial",0,20));
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			String s = "Score : " + score;
			int w = g2.getFontMetrics().stringWidth(s)/2;
			String best = "Best score : " + bestScore + " since " + date;
			int bw = g2.getFontMetrics().stringWidth(best)/2;
			String newBest = "New Best score : " + score;
			int nbw = g2.getFontMetrics().stringWidth(newBest)/2;
			String go = "Game Over";
			int gow = g2.getFontMetrics().stringWidth(go)/2;
			String r = "Press r to restart";
			int rw = g2.getFontMetrics().stringWidth(r)/2;
		if(!gameOver) {
			g2.drawString(Integer.toString(Stack.fps), 1, 18);
			g2.drawString(s, Stack.WIDTH * Stack.scale / 2 - w, 80);
		}
		else {
			g2.setColor(Color.white);
			g2.drawString(go, Stack.WIDTH * Stack.scale / 2 - gow, Stack.HEIGHT*Stack.scale / 2 - 20);
			g2.drawString(r, Stack.WIDTH * Stack.scale / 2 - rw, Stack.HEIGHT*Stack.scale / 2 + 40);
			if(score < bestScore) {
				g2.drawString(s, Stack.WIDTH * Stack.scale / 2 - w, Stack.HEIGHT*Stack.scale / 2);
				g2.drawString(best, Stack.WIDTH * Stack.scale / 2 - bw, Stack.HEIGHT*Stack.scale / 2 + 20);
			}
			else {
				g2.drawString(newBest, Stack.WIDTH * Stack.scale / 2 - nbw, Stack.HEIGHT*Stack.scale / 2);
			}
		}
		
	}

	@Override
	public void run() {
		while(sound!=null) {
			if(stack.getKey().keyDown(KeyEvent.VK_SPACE) && !gameOver && !animating) place.play();
			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			sound.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		}
}
