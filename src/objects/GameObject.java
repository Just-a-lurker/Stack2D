package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Random;

import main.Game;
import main.Stack;

public class GameObject{
	private float x,y, newY;
	private int width, height;
	Object object;
	Game game;
	private boolean moving, animate = false,powerUp = false;
	private double speedX = 1 , maxSpeedX = 3;
	
	Random rand = new Random();

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
	
	
	
	public boolean isPowerUp() {
		return powerUp;
	}



	public void setPowerUp(boolean powerUp) {
		this.powerUp = powerUp;
	}



	public int getHeight() {
		return height;
	}



	public void setHeight(int height) {
		this.height = height;
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
			 
			 if(game.getStack().getKey().keyDown(KeyEvent.VK_SPACE)) {
				 this.moving = false;
				 int prevWidth = game.getObjects().get(game.getObjects().size() - 2).width;
				 int newWidth = (int) ((prevWidth - Math.abs(Stack.WIDTH / 2 - (x + width / 2))));
				 if(game.getObjects().get(game.getObjects().size() - 2).isPowerUp() && newWidth > 0) return;
				 if(newWidth <= 0 ) {
					 if(game.getLives() == 0) game.setGameOver(true);
					 game.setLives(game.getLives() - 1);
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
			boolean hasAnimated = true;
			if(y<newY) {
				y++;
				hasAnimated = false;
			}
			if(x + width / 2 < Stack.WIDTH/2) {
				x++;
				hasAnimated = false;
			}
			else if(x + width/2>Stack.WIDTH/2) {
				x--;
				hasAnimated = false;
			}
			if(hasAnimated) animate = false;
		}
	}

public void draw(Graphics2D g2) {
	if(powerUp == true) 
	{
		if(game.isGameOver()) {
			g2.setColor(Color.black);
		}
		else g2.setColor(Color.green);
	}
	else if(!Stack.darkMode || game.isGameOver()) {
		g2.setColor(Color.black);
	}
	
	else g2.setColor(Color.gray);
	
	
	g2.fillRect((int) (x * Stack.scale),(int) (y * Stack.scale),(int) (width *Stack.scale),(int) (height*Stack.scale));
}
}
