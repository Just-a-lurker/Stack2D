package objects;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import graphic.Object;
import main.Game;
import main.KeyInput;
import main.Stack;

public class GameObject{
	private float x,y, newY;
	private int width, height;
	Object object;
	Game game;
	private boolean moving, animate = false;
	private double speedX = 1 , maxSpeedX = 3;
	
	Random rand = new Random();

	@SuppressWarnings("deprecation")
	public GameObject(float x, float y, Object object, boolean moving, Game game) {
		this.x = x;
		this.y = y;
		this.newY = y;
		this.width = object.getWidth();
		this.height = object.getHeight();
		this.moving = moving;
		this.object = object;
		this.game = game;
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
		this.object = new Object(width, height, 0);
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
				 int prevWidth = Game.objects.get(Game.objects.size() - 2).width;
				 int newWidth = (int) ((prevWidth - Math.abs(Stack.WIDTH / 2 - (x + width / 2))));
				 if(newWidth < 0) {
					 game.setGameOver(true);
					 return;
				 }
				 setWidth(newWidth);
			 }
		}
		if(game.isAnimate()) {
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
	game.getDraw().drawObject(object, (int) x, (int ) y);
}
}
