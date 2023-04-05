package graphic;

public class Object {
	private int width,height;

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
	}
	
	
}
