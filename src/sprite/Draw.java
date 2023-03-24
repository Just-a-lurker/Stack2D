package sprite;

import java.awt.Color;

import main.Stack;

public class Draw {

	private static int width = Stack.WIDTH, height = Stack.HEIGHT;
	
	private int[] pixels = new int[width*height];
	
	
	
	public int[] getPixels() {
		return pixels;
	}

	public void setPixels(int[] pixels) {
		this.pixels = pixels;
	}

	public void drawBackGround() {
		for(int i=0;i<pixels.length;i++) {
			pixels[i] = 0xfff4f4f4;
		}
	}
	
	public void drawSprite(Sprite s, int xp, int yp) {
		if(xp< -s.getWidth() || yp< -s.getHeight() || xp >=width || yp>= height) return;

		for(int y=0;y<s.getHeight();y++) {
			
			int y1 = y + yp;
			if(y1 >= height || y1< 0) continue;
			
			for(int x=0;x<s.getWidth();x++) {
				int x1 = x + xp;
				if(x1 >= width || x1< 0) continue;
				
				int color = s.getPixels()[x+y*s.getWidth()];
				if(color == 0xffff00ff) continue;
				pixels[x1 + y1 * width] = color;
			}
		}
			
	}
}
