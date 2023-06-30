package game;

import java.awt.Color;
import java.awt.Graphics;

public class Killer extends Thread{
	boolean die = false;
	Character character;
	Panel panel;
	int speed = 0;
	int y = 0;
	
	public Killer(Character character,Panel panel,int speed) {
		this.character = character;
		this.panel = panel;
		this.speed = speed;
		y = (panel.maxWorldRow)*panel.tileSize;
	}
	
	@Override
	public void run() {
		super.run();
		while(!die) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			y = y-1;
			if(y-character.worldy+character.screeny <= character.screeny+panel.tileSize) {
				panel.collision = 1;
				break;
			}
		}
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.red);
		int pos = y-character.worldy+character.screeny;
		g.fillRect(0,pos, panel.screen_width, 20);
	}

}
