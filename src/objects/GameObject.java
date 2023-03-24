package objects;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;


import main.Game;
import main.KeyInput;
import main.Stack;
import sprite.Sprite;

public class GameObject implements Runnable{
	private float x,y, newY;
	private int width, height;
	Sprite sprite;
	Game game;
	Thread sound;
	private AudioClip place;
	private boolean moving, animate = false;
	private double speedX = 1 , maxSpeedX = 3;
	
	Random rand = new Random();

	@SuppressWarnings("deprecation")
	public GameObject(float x, float y, Sprite sprite, boolean moving, Game game) {
		this.x = x;
		this.y = y;
		this.newY = y;
		this.width = sprite.getWidth();
		this.height = sprite.getHeight();
		this.moving = moving;
		this.sprite = sprite;
		this.game = game;
		sound = new Thread();
		try {
			place = Applet.newAudioClip(new URL("file","","data/place.wav"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		this.speedX = rand.nextDouble() * (maxSpeedX - 1) + 1;
		if(rand.nextInt(2) == 0) this.speedX *= -1;
	}
	
	
	
	public float getX() {
		return x;
	}



	public void setX(float x) {
		this.x = x;
	}



	public float getY() {
		return y;
	}



	public void setY(float y) {
		this.y = y;
	}



	public boolean isMoving() {
		return moving;
	}



	public void setMoving(boolean moving) {
		this.moving = moving;
	}



	public boolean isAnimate() {
		return animate;
	}



	public void setAnimate(boolean animate) {
		this.animate = animate;
	}



	public int getWidth() {
		return width;
	}



	public void setWidth(int width) {
		this.sprite = new Sprite(width, height, 0);
		this.width = width;
	}
	
	
	public void update() {
		if(moving) {
			 x+=speedX;
			 if(x + width >= Stack.WIDTH) {
				 x = Stack.WIDTH - width;
				 speedX *=-1;
			 }
			 if(x<0) {
				 x=0;
				 speedX *= -1;
			 }
			 
			 if(KeyInput.keyDown(KeyEvent.VK_SPACE)) {
				 this.moving = false;
				 place.play();
				 int prevWidth = Game.objects.get(Game.objects.size() - 2).width;
				 int newWidth = (int) ((prevWidth - Math.abs(Stack.WIDTH / 2 - (x + width / 2))));
				 if(newWidth < 0) {
					 Game.gameOver = true;
					 return;
				 }
				 setWidth(newWidth);
			 }
		}
		if(Game.animate) {
			this.animate = true;
			newY += height + 1;
			x = Math.round(x);
		}
		if(animate) {
			boolean hasAnimated = false;
			if(y<newY) {
				y++;
				hasAnimated = true;
			}
			if(x + width / 2 < Stack.WIDTH/2) {
				x++;
				hasAnimated = true;
			}
			else if(x + width/2>Stack.WIDTH/2) {
				x--;
				hasAnimated = true;
			}
			if(!hasAnimated) animate = false;
		}
	}

public void draw() {
	game.getDraw().drawSprite(sprite, (int) x, (int ) y);
}



@Override
public void run() {
	while(sound != null) {
		
	}
	
}
}
