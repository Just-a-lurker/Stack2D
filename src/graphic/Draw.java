package graphic;

import java.awt.Color;

import main.Game;
import main.Stack;

public class Draw {

	private static int width = Stack.WIDTH, height = Stack.HEIGHT;
	
	private Game game;
	
	private int[] pixels = new int[width*height];
	
	public Draw(Game game) {
		this.game = game;
	}
	
	public int[] getPixels() {
		return pixels;
	}

	public void setPixels(int[] pixels) {
		this.pixels = pixels;
	}

	public void drawBackGround() {
		for(int i=0;i<pixels.length;i++) {
			if(!game.isGameOver()) pixels[i] = 0xfff4f4f4;
			else pixels[i] = 0x0;
		}
	}
	
	public void drawObject(Object o, int xPos, int yPoa) {
		if(xPos< -o.getWidth() || yPoa< -o.getHeight() || xPos >=width || yPoa>= height) return;

		for(int y=0;y<o.getHeight();y++) {
			
			int y1 = y + yPoa;
			if(y1 >= height || y1< 0) continue;
			
			for(int x=0;x<o.getWidth();x++) {
				int x1 = x + xPos;
				if(x1 >= width || x1< 0) continue;
				
				int color = o.getPixels()[x+y*o.getWidth()];
				if(color == 0xffff00ff) continue;
				pixels[x1 + y1 * width] = color;
			}
		}
			
	}
}
