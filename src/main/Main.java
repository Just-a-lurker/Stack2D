package main;


import java.awt.Color;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		Stack s = new Stack();
		s.frame.setResizable(false);
		s.frame.setTitle("Stack");
		s.frame.add(s);
		s.frame.pack();
		s.setBackground(Color.black);
		s.frame.setVisible(true);
		s.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		s.frame.setLocationRelativeTo(null);
		s.start();
	}

}
