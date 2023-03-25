package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import objects.GameObject;
import sprite.Sprite;

public class SaveManager {
	float[] saveInfo =new float [10];
	String path;
	Game game;
	File oF;
	BufferedWriter oBR;
	BufferedReader oR;
	
	public SaveManager(String path, Game game) {
		this.path = path;
		this.game = game;
	}
	
	public void save() {
		
		for(int i=0;i<Game.objects.size()-1;i++) {
			if(Game.objects.get(i) != null)
			{
				saveInfo[i] = Game.objects.get(i).getWidth();
			}
		}
		try {
			oF = new File(path);
			oBR = new BufferedWriter(new FileWriter(oF));
			if(Game.gameOver) {
				return;
			}
			oBR.write(Integer.toString(Game.score) + "\n");
			for(int i=0;i<Game.objects.size()-1;i++) {
				oBR.write(Float.toString(saveInfo[i]) + " ");
			}
			oBR.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
}
	
	public void load() {
		
		try {
			oF = new File(path);
			oR = new BufferedReader(new FileReader(oF));
			String line = oR.readLine(); if(line == null) return;
			Game.score = Integer.parseInt(line)-1; 
			if(Integer.parseInt(line) == 0) return;
			while((line = oR.readLine()).equals(""));
			String str[] = line.split(" "); 
			Game.objects = new ArrayList<GameObject>();
			for(int i=0;i<str.length;i++) {
				Game.objects.add(new GameObject(Stack.WIDTH / 2 - game.objectSprite.getWidth()/2, Stack.HEIGHT - 30*(i+1), new Sprite((int) Float.parseFloat(str[i]), 29, 0), false, game));
				while(Game.objects.get(i).getX() + Game.objects.get(i).getWidth()/2 != Stack.WIDTH/2) {
					if(Game.objects.get(i).getX() + Game.objects.get(i).getWidth()/2 / 2 < Stack.WIDTH/2) {
						Game.objects.get(i).setX(Game.objects.get(i).getX() + 1);
					}
					else if(Game.objects.get(i).getX() + Game.objects.get(i).getWidth()/2>Stack.WIDTH/2) {
						Game.objects.get(i).setX(Game.objects.get(i).getX() - 1);
					}
				}
				
			}
			oR.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
}
	
}
