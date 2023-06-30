package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.sound.sampled.AudioSystem;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.plaf.TreeUI;


public class Panel  extends JPanel implements KeyListener,ActionListener{
	
	final int  tileSize = 24;
	final int  screen_width = 720;
	final int  screen_height = 720;
	final int  cols = screen_width/tileSize;
	final int  rows = screen_height/tileSize;
	int key = 1; //1 for key on else off
	
	int maxWorldCol;
	int maxWorldRow;
	int maxWorldX ;
	int maxWorldY;
	
	int state = 0;
	final private static int titleState = 0;
	final private static int gameState = 1;
	final private static int finishedState = 2;
	final private static int deadState = 3;
	final private static int levelState = 4;
	
	
	private static Image bgImage;
	
	
	int command = 1;//1 for up and 2 for down
	
	Map map ;
	int level = 0;
	List<List<String>> tiles;
	Character character;
	Killer killer;
	int point = 0;
	int collision = 0;
	
	Timer collisionTimer;
	MyAudioPlayer myAudioPlayer = new MyAudioPlayer();
	
	List<Color> colors = Arrays.asList(Color.cyan,Color.white,Color.lightGray,Color.orange,Color.green,Color.pink,Color.magenta,Color.gray);
	int color = 0;
	public int getRandomColor(int exclude) {
		Random random = new Random();
		if (exclude ==  -1) {
			return 0;
		}
		while (1>0) {
			int rand = random.nextInt(colors.size());
			if(rand != exclude) {
				return rand;
			}
		}
	}

	public Panel() {
		setBackground(Color.black);
		setPreferredSize(new Dimension(screen_width,screen_height));
		setFocusable(true);
		requestFocus();
		addKeyListener(this);
		Timer timer = new Timer(10, this);
		timer.start();
		bgImage = new ImageIcon(getClass().getResource("/res/images/bg.jpg")).getImage();
		color = getRandomColor(-1);
		collisionTimer = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (collision == 1) {
					playerDied();
					collision = 0;
				}
			}
		});
		
		myAudioPlayer.SetFile("background");
		myAudioPlayer.playMusic();
		myAudioPlayer.loopMuisc();
	
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		
//		g.drawImage(bgImage, 0,0,null);//background image
		switch (state) {
			case titleState: {
				drawTitleScreen(g);
				break;
			}
			case gameState: {
				drawGameScreen(g);
				break;
			}
			case finishedState:{
				drawFinishedScreen(g);
				break;
			}
			case deadState: {
				drawDeadScreen(g);
				break;
			}
			case levelState:{
				drawLevelScreen(g);
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + state);
		}	
	}
	
	
	
	
	private void drawDeadScreen(Graphics g) {
		setBackground(Color.black);
		String text = "You Died";	
		int x = tileSize*12;
		int y = tileSize*8;
		g.setColor(Color.white);
		g.setFont(getFont().deriveFont(32f));
		g.drawString(text,x, y);
	}

	private void drawLevelScreen(Graphics g) {
		setBackground(Color.black);
		String text = "Level  "+level;	
		int x = tileSize*12;
		int y = tileSize*8;
		g.setColor(Color.white);
		g.setFont(getFont().deriveFont(32f));
		g.drawString(text,x, y);
		
		text = "You Scored : "+String.valueOf(point);	
		g.setColor(Color.white);
		g.setFont(getFont().deriveFont(32f));
		g.drawString(text,x-2*tileSize, y+3*tileSize);
	}
	
	private void drawFinishedScreen(Graphics g) {
		setBackground(Color.black);
		String text = "You Won";	
		int x = tileSize*12;
		int y = tileSize*8;
		g.setColor(Color.white);
		g.setFont(getFont().deriveFont(32f));
		g.drawString(text,x, y);
		
		text = "You Scored : "+String.valueOf(point);	
		g.setColor(Color.white);
		g.setFont(getFont().deriveFont(32f));
		g.drawString(text,x-2*tileSize, y+3*tileSize);
	}

	private void drawGameScreen(Graphics g) {
		character.draw(g);
		killer.draw(g);
		drawPoints(g);
	}
	
	public void drawPoints(Graphics g) {
		int scorex = 25*tileSize;
		int scorey = 2*tileSize;
		g.setColor(Color.black);
		g.fillRect(scorex,scorey-tileSize,4*tileSize+10,tileSize+10);
		g.setColor(Color.white);
		g.drawRect(scorex,scorey-tileSize,4*tileSize+10,tileSize+10);
		g.setColor(Color.YELLOW);
		g.setFont(getFont().deriveFont(16f));
		g.drawString("Points : " +String.valueOf(point), scorex+15, scorey);
	}


	private void drawTitleScreen(Graphics g) {
		String text = "THE BOX";	
		int x = tileSize * 11;
		int y = tileSize * 5;
		g.setColor(Color.YELLOW);
		g.setFont(getFont().deriveFont(64f));
		g.drawString(text,x-2*tileSize, y);
		
		text = "START GAME";
		x = x - tileSize;
		y = y + tileSize * 8;
		g.setFont(getFont().deriveFont(24f));
		g.drawString(text,  x+tileSize, y);
		if(command == 1) {
			g.drawString(">", x, y);
		}
		
		text = "QUIT GAME";
		y = y+tileSize*2;
		
		g.drawString(text,  x+tileSize, y);
		if(command == 2) {
			g.drawString(">", x, y);
		}
	}
	

	
	private boolean LoadMap() {
		map = new Map(level);
		tiles = map.GetMap();
		if(tiles == null) {
			return false;
		}
		maxWorldCol = map.maxWorldCol;
		maxWorldRow = map.maxWorldRow;
		maxWorldX = maxWorldCol * tileSize;
		maxWorldY = maxWorldRow *tileSize;
		return true;
	}
	
	private void StartGame() {
		if(!LoadMap()) {
			return;
		}
		color = getRandomColor(color);
		character = new Character(this);
		killer = new Killer(character,this,15);
		
		state = gameState;
		killer.start();
		character.start();
		
		collisionTimer.start();
		repaint();
	}
	
	private void resetGame(){
		map = null;
		tiles = null;
		maxWorldCol = 0;
		maxWorldRow = 0;
		maxWorldX = 0;
		maxWorldY = 0;
		character.die = true;
		killer.die = true;
		collisionTimer.stop();
		point = 0;
		level = 0;
		
	}
	
	public void playerDied() {
		character.die = true;
		killer.die = true;
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		state = deadState;
		repaint();
		new java.util.Timer().schedule( 
		        new java.util.TimerTask() {
		            @Override
		            public void run() {
		            	state = titleState;
		            	resetGame();
						repaint();
		            }
		        }, 
		        1200 
		);
	}
	
	public void playerWon() {
		character.die = true;
		killer.die = true;
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		state = finishedState;
		repaint();
		new java.util.Timer().schedule( 
		        new java.util.TimerTask() {
		            @Override
		            public void run() {
		            	state = titleState;
		            	resetGame();
						repaint();
		            }
		        }, 
		        1200 
		);
	}
	
	public void levelComplete() {
		level++;
		if(level > 1) {
			playerWon();
			return;
		}
		character.die = true;
		killer.die = true;
		state = levelState;
		repaint();
		new java.util.Timer().schedule( 
		        new java.util.TimerTask() {
		            @Override
		            public void run() {
		        		StartGame();
		            }
		        }, 
		        1200 
		);
	}
	
	

	public void GetPoint(int row,int col) {
		tiles.get(row).set(col, "0");//remove point from screen
		point++;
		repaint();
	}


	@Override
	public void keyReleased(KeyEvent e) {
		if(key == 1) {
			
		}
	}


	@Override
	public void keyTyped(KeyEvent e) {
		if(key == 1) {
			
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(key == 1) {
			if (state == titleState) {
				if (keyCode == KeyEvent.VK_UP) {	
					command = 1;
				}
		
				if (keyCode == KeyEvent.VK_DOWN) {
					command = 2;
				}
				if (keyCode == KeyEvent.VK_ENTER) {
					if (command == 1) {
						StartGame();
					}
					else{
						System.exit(0);
					}
				}
				repaint();
			}
			if(state == gameState) {
				if (keyCode == KeyEvent.VK_UP) {
					character.moveUp();
				}
		
				if (keyCode == KeyEvent.VK_DOWN) {
					character.moveDown();	
				}
		
				if (keyCode == KeyEvent.VK_RIGHT) {
					character.moveRight();
				}
				
				if (keyCode == KeyEvent.VK_LEFT) {
					character.moveLeft();
				}
				repaint();	
			}
		}
	}

	//repaints if there is any changes
	@Override
	public void actionPerformed(ActionEvent e) {
		this.repaint();
	}
}
