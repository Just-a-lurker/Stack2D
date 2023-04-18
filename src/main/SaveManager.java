package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import objects.GameObject;
import objects.Object;

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
		
		for(int i=0;i<game.getObjects().size()-1;i++) {
			if(game.objects.get(i) != null)
			{
				saveInfo[i] = game.objects.get(i).getWidth();
			}
		}
		try {
			oF = new File(path);
			oBR = new BufferedWriter(new FileWriter(oF));
			if(game.isGameOver()) {
				return;
			}
			oBR.write(Integer.toString(game.getScore()) + "\n");
			for(int i=0;i<game.getObjects().size()-1;i++) {
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
			game.setScore(Integer.parseInt(line)-1);
			if(Integer.parseInt(line) == 0) return;
			while((line = oR.readLine()).equals(""));
			String str[] = line.split(" "); 
			game.setObjects(new ArrayList<GameObject>());
			for(int i=0;i<str.length;i++) {
				game.getObjects().add(new GameObject(Stack.WIDTH / 2 - game.object.getWidth()/2, Stack.HEIGHT - 30*(i+1), new Object((int) Float.parseFloat(str[i]), 29), false, game));
				while(game.getObjects().get(i).getX() +game.getObjects().get(i).getWidth()/2 != Stack.WIDTH/2) {
					if(game.getObjects().get(i).getX() + game.getObjects().get(i).getWidth()/2 / 2 < Stack.WIDTH/2) {
						game.getObjects().get(i).setX(game.getObjects().get(i).getX() + 1);
					}
					else if(game.getObjects().get(i).getX() + game.getObjects().get(i).getWidth()/2>Stack.WIDTH/2) {
						game.getObjects().get(i).setX(game.getObjects().get(i).getX() - 1);
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
