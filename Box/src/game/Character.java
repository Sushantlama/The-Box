package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

public class Character extends Thread{
	int worldx=0;
	int worldy=0;
	int screenx = 0;
	int screeny = 0;
	Panel panel;
	int move = 0;
	int point = 0;
	boolean die = false;
	Image bgImage;
	
	public Character(Panel panel) {
		this.panel = panel;
		bgImage = new ImageIcon(getClass().getResource("/res/images/smirk.png")).getImage();
		screenx = panel.screen_width/2;
		screeny = (panel.rows-10)*panel.tileSize;
		worldx = screenx;
		worldy = (panel.maxWorldRow-6)*panel.tileSize;
	
	}
	@Override
	public void run() {
		super.run();
		while (!die) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(screeny==worldy) {
				panel.levelComplete();
				move = 0;
			}
			
			int Wcol = worldx/panel.tileSize;
			int Wrow = worldy/panel.tileSize;
			
			if (move == 1) {
				if (panel.tiles.get(Wrow).get(Wcol-1).equals("1")) {
					point = 0;
					move = 0;
				}
				else if (panel.tiles.get(Wrow).get(Wcol-1).equals("2")) {
					panel.collision = 1;
					move = 0;
				}
				else if (panel.tiles.get(Wrow).get(Wcol-1).equals("3")) {
					panel.GetPoint(Wrow,Wcol-1);
				}
				else {
					screenx = screenx - panel.tileSize;
					worldx = worldx - panel.tileSize;
					if(point<4) {
						point++;
					}
				}
			}
			else if (move == 2) {	
				if (panel.tiles.get(Wrow).get(Wcol+1).equals("1")) {
					point = 0;
					move = 0;
				
				}
				else if (panel.tiles.get(Wrow).get(Wcol+1).equals("2")) {
					panel.collision = 1;
					move = 0;
				}
				else if (panel.tiles.get(Wrow).get(Wcol+1).equals("3")) {
					panel.GetPoint(Wrow,Wcol+1);
				}
				else {
					screenx = screenx + panel.tileSize;
					worldx = worldx + panel.tileSize;
					if(point<4) {
						point++;
					}
				}
			}
			else if(move == 3) {
				if (Wrow == 1) {
					point = 0;
					move = 0;
				}
				if (panel.tiles.get(Wrow-1).get(Wcol).equals("1")) {
					move = 0;
					point = 0;
				}
				else if (panel.tiles.get(Wrow-1).get(Wcol).equals("2")) {
					panel.collision = 1;
					move = 0;
				}
				else if (panel.tiles.get(Wrow-1).get(Wcol).equals("3")) {
					panel.GetPoint(Wrow-1,Wcol);
				}
				else {
					worldy = worldy - panel.tileSize;
					if(point<4) {
						point++;
					}
				}
			}
			else if(move == 4) {
				if (Wrow == panel.maxWorldRow-2) {
					panel.collision = 1;
					move  = 0;
				}
				if (panel.tiles.get(Wrow+1).get(Wcol).equals("1")) {
					move = 0;
					point = 0;
				}
				
				else if (panel.tiles.get(Wrow+1).get(Wcol).equals("2")) {
					panel.collision = 1;
					move = 0;
				}
				else if (panel.tiles.get(Wrow+1).get(Wcol).equals("3")) {
					panel.GetPoint(Wrow+1,Wcol);
				}
				else {
					worldy = worldy + panel.tileSize;
					if(point<4) {
						point++;
					}
				}
			}
			else {
				continue;
			}
		}
	}
	
	public void draw(Graphics g) {
		drawMap(g);
		drawCharacter(g);
		drawCharacterTail(g);
	}
	
	public void drawCharacter(Graphics g) {
		g.setColor(Color.yellow);
		g.drawImage(bgImage,screenx,screeny,panel.tileSize,panel.tileSize, null);
//		g.fillRect(screenx, screeny, panel.tileSize, panel.tileSize);
	}
	
	public void drawMap(Graphics g) {
		int worldcol = 0;
		int worldrow = 0;
		while (worldcol<panel.maxWorldCol && worldrow <panel.maxWorldRow) {
			int Worldx = worldcol * panel.tileSize;
			int Worldy = worldrow * panel.tileSize;
			
			//screen x coordinate is same as worldx coordinate and world is not moved in x axis
			int mapscreenx = Worldx;
			int mapscreeny = Worldy - worldy + screeny;
			
			if (panel.tiles.get(worldrow).get(worldcol).equals("1")) {
				g.setColor(panel.colors.get(panel.color));
//				g.setColor(Color.decode("#B200ED"));
//				g.setColor(panel.colors.get(panel.color));
//				g.setColor(Color.decode("#B200ED"));
				if(worldrow-1>=0) {
					 String top = panel.tiles.get(worldrow-1).get(worldcol);
					 if(top.equals("0") || top.equals("3") || top.equals("2")) {
							g.fillRect(mapscreenx, mapscreeny, panel.tileSize, panel.tileSize/3);
					}
				}
				if(worldrow+1<=panel.maxWorldRow-1) {
					 String bottom = panel.tiles.get(worldrow+1).get(worldcol);
					 if(bottom.equals("0") || bottom.equals("3") || bottom.equals("2")) {
							g.fillRect(mapscreenx, mapscreeny+panel.tileSize-panel.tileSize/3, panel.tileSize, panel.tileSize/3);
					 }
				}
				if(worldcol-1>=0) {
					 String left = panel.tiles.get(worldrow).get(worldcol-1);
					 if(left.equals("0") || left.equals("3") || left.equals("2")) {
						 	
							g.fillRect(mapscreenx, mapscreeny, panel.tileSize/3, panel.tileSize);
					 }
				}
				if (worldcol+1<=panel.maxWorldCol-1) {
					 String right = panel.tiles.get(worldrow).get(worldcol+1);
					 if(right.equals("0") || right.equals("3") || right.equals("2")) {
							g.fillRect(mapscreenx+panel.tileSize-panel.tileSize/3, mapscreeny, panel.tileSize/3, panel.tileSize);
					 }
				}
			}
			else if(panel.tiles.get(worldrow).get(worldcol).equals("2")) {
				g.setColor(Color.red);
				if(worldrow-1>=0) {
					String top = panel.tiles.get(worldrow-1).get(worldcol);
					 if(top.equals("0") || top.equals("3") || top.equals("1") ) {
						 
							g.fillRect(mapscreenx, mapscreeny, panel.tileSize/5-2, panel.tileSize/3);
							g.fillRect(mapscreenx+panel.tileSize/5, mapscreeny, panel.tileSize/5-2, panel.tileSize/3);
							g.fillRect(mapscreenx+2*panel.tileSize/5, mapscreeny, panel.tileSize/5-2, panel.tileSize/3);
							g.fillRect(mapscreenx+3*panel.tileSize/5, mapscreeny, panel.tileSize/5-2, panel.tileSize/3);
							g.fillRect(mapscreenx+4*panel.tileSize/5, mapscreeny, panel.tileSize/5-2, panel.tileSize/3);
							
					}
				}
				if(worldrow+1<=panel.maxWorldRow-1) {
					 String bottom = panel.tiles.get(worldrow+1).get(worldcol);
					 if(bottom.equals("0") || bottom.equals("3") || bottom.equals("1") ) {
						 	g.fillRect(mapscreenx,  mapscreeny+panel.tileSize-panel.tileSize/3, panel.tileSize/5-2, panel.tileSize/3);
							g.fillRect(mapscreenx+panel.tileSize/5,  mapscreeny+panel.tileSize-panel.tileSize/3, panel.tileSize/5-2, panel.tileSize/3);
							g.fillRect(mapscreenx+2*panel.tileSize/5,  mapscreeny+panel.tileSize-panel.tileSize/3, panel.tileSize/5-2, panel.tileSize/3);
							g.fillRect(mapscreenx+3*panel.tileSize/5,  mapscreeny+panel.tileSize-panel.tileSize/3, panel.tileSize/5-2, panel.tileSize/3);
							g.fillRect(mapscreenx+4*panel.tileSize/5,  mapscreeny+panel.tileSize-panel.tileSize/3, panel.tileSize/5-2, panel.tileSize/3);

					 }
				}
				if(worldcol-1>=0) {
					 String left = panel.tiles.get(worldrow).get(worldcol-1);
					 if(left.equals("0") || left.equals("3") || left.equals("1") ) {
						 g.fillRect(mapscreenx, mapscreeny, panel.tileSize/3, panel.tileSize/5-2);
							g.fillRect(mapscreenx, mapscreeny+panel.tileSize/5, panel.tileSize/3, panel.tileSize/5-2);
							g.fillRect(mapscreenx, mapscreeny+2*panel.tileSize/5, panel.tileSize/3,panel.tileSize/5-2);
							g.fillRect(mapscreenx, mapscreeny+3*panel.tileSize/5, panel.tileSize/3, panel.tileSize/5-2);
							g.fillRect(mapscreenx, mapscreeny+4*panel.tileSize/5, panel.tileSize/3, panel.tileSize/5-2);
					 }
				}
				if (worldcol+1<=panel.maxWorldCol-1) {
					 String right = panel.tiles.get(worldrow).get(worldcol+1);
					 if(right.equals("0") || right.equals("3") || right.equals("1") ) {
						 	g.fillRect(mapscreenx+panel.tileSize-panel.tileSize/3, mapscreeny, panel.tileSize/3, panel.tileSize/5-2);
							g.fillRect(mapscreenx+panel.tileSize-panel.tileSize/3, mapscreeny+panel.tileSize/5, panel.tileSize/3, panel.tileSize/5-2);
							g.fillRect(mapscreenx+panel.tileSize-panel.tileSize/3, mapscreeny+2*panel.tileSize/5, panel.tileSize/3,panel.tileSize/5-2);
							g.fillRect(mapscreenx+panel.tileSize-panel.tileSize/3, mapscreeny+3*panel.tileSize/5, panel.tileSize/3, panel.tileSize/5-2);
							g.fillRect(mapscreenx+panel.tileSize-panel.tileSize/3, mapscreeny+4*panel.tileSize/5, panel.tileSize/3, panel.tileSize/5-2);
							
					 }
				}
			}
			else if (panel.tiles.get(worldrow).get(worldcol).equals("3")) {
				g.setColor(Color.blue);
				g.fillOval(mapscreenx+(panel.tileSize/4), mapscreeny+(panel.tileSize/4), panel.tileSize/2, panel.tileSize/2);
			}
			worldcol++;
			if (worldcol == panel.maxWorldCol) {
				worldcol = 0;
				worldrow++;
			}
		}
	}
	
	public void drawCharacterTail(Graphics g) {
		 if (move == 1) {
			if(point>=1) {
				if(point>=2) {
					g.fillRect(screenx + panel.tileSize , screeny ,
							(point - 2) * panel.tileSize , panel.tileSize / 3-3);
				}
				
				g.fillRect(screenx + panel.tileSize , screeny + (panel.tileSize / 3) ,
						point * panel.tileSize , panel.tileSize / 3);
				if(point>=3) {
					g.fillRect(screenx + panel.tileSize , screeny + 2*(panel.tileSize / 3)+3,
							(point - 3) * panel.tileSize , panel.tileSize / 3-3);	
				}
			}
		}
		else if (move == 2) {
			if(point>=1) {
				if(point>=2) {
					g.fillRect(screenx - (point-2)*panel.tileSize , screeny ,
							(point - 2) * panel.tileSize , panel.tileSize / 3-3);
				}
				
				g.fillRect(screenx - point*panel.tileSize , screeny + (panel.tileSize / 3) ,
						point * panel.tileSize , panel.tileSize / 3);
				if(point>=3) {
					g.fillRect(screenx -( point-3)*panel.tileSize , screeny + 2*(panel.tileSize / 3)+3,
							(point - 3) * panel.tileSize , panel.tileSize / 3-3);	
				}
			}
		}
		else if (move == 3) {
			if(point>=1) {
				if(point>=2) {
					g.fillRect(screenx , screeny + panel.tileSize ,
							 (panel.tileSize / 3)-3,(point-2) * panel.tileSize);
				}
				
				g.fillRect(screenx+panel.tileSize/3, screeny + panel.tileSize ,
						 panel.tileSize / 3,point * panel.tileSize);
				if(point>=3) {
					g.fillRect(screenx+(2*panel.tileSize)/3 + 3, screeny + panel.tileSize ,
							 (panel.tileSize / 3)-3,(point-3) * panel.tileSize);
				}
			}
		}
		else if (move == 4) {
			if(point>=1) {
				if(point>=2) {
					g.fillRect(screenx , screeny - (point -2) * panel.tileSize ,
							 (panel.tileSize / 3)-3,(point-2) * panel.tileSize);
				}
				
				g.fillRect(screenx+panel.tileSize/3, screeny - point * panel.tileSize ,
						 panel.tileSize / 3,point * panel.tileSize);
				if(point>=3) {
					g.fillRect(screenx + (2*panel.tileSize)/3 + 3, screeny - (point -3) * panel.tileSize ,
							 (panel.tileSize / 3)-3,(point-3) * panel.tileSize);
				}			
			}
		}
	}
	
	public void moveUp() {
		move = 3;
	}
	
	public void moveDown() {
		move = 4;
	}
	
	public void moveLeft() {
		move = 1;
	}
	
	public void moveRight() {
		move = 2;
	}
}
