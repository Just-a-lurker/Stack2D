package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyInput implements KeyListener {

	private boolean[] keys = new boolean[100];
	private boolean[] lastKeys = new boolean[100];
	
	public void update() {
		for(int i =0;i< keys.length;i++) {
			lastKeys[i] = keys[i];
		}
	}
	
	public boolean key(int key) {
		return keys[key];
	}
	
	
	public boolean keyDown(int key) {
		return keys[key] && !lastKeys[key];
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

}
