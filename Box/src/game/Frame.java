package game;

import javax.swing.JFrame;

public class Frame extends JFrame{

	
	
	public Frame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setTitle("The Box");
		Panel gamePanel = new Panel();
		add(gamePanel);
		pack();
		setVisible(true);
//		System.out.println(this.getWidth()+" "+this.getHeight());
		
	}
}