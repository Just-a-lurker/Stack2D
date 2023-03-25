package graphic;

public class Object {
	private int width,height;
	private int[] pixels;
	
	
	
	public int[] getPixels() {
		return pixels;
	}


	public void setPixels(int[] pixels) {
		this.pixels = pixels;
	}


	public int getWidth() {
		return width;
	}


	public void setWidth(int width) {
		this.width = width;
	}


	public int getHeight() {
		return height;
	}


	public void setHeight(int height) {
		this.height = height;
	}


	public Object(int width, int height, int color) {
		this.width = width;
		this.height = height;
		pixels = new int[width*height];
		for(int i=0;i<pixels.length;i++) {
			pixels[i] = color;
		}
	}
	
	
}
