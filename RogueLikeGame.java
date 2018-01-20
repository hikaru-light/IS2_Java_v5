import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import java.io.*;
import javax.imageio.ImageIO;

class RogueLikeGame {
	public static void main(String[] args) {
		JFrame fr = new JFrame("Dungeon");

		fr.setSize(440, 464);
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fr.getContentPane().setBackground(new Color(0, 0, 0));

		RogueLikeGamePanel panel = new RogueLikeGamePanel();
		panel.setOpaque(false);
		fr.add(panel);

		fr.setResizable(false);
		fr.setVisible(true);
	}
}

class RogueLikeGamePanel extends JPanel implements KeyListener, Runnable {
	char map[][];
	int mapX = 30, mapY = 20;
	String[] mapData = new String[20];

	BufferedImage[][] playerImg = new BufferedImage[4][4];
	BufferedImage[] lampImg = new BufferedImage[8];

	int playerX, playerY;
	int playerDirNo, playerNo;

	double battery = 100;
	int range = 3;

	double time;
	int remain = 100;
	boolean gameover = false;

	Thread th;
	int lampNo = 0;

	RogueLikeGamePanel() {
		try {
			File mapFile = new File("./src/map/data.txt");
			FileReader fr = new FileReader(mapFile);
			BufferedReader br = new BufferedReader(fr);
			String str = br.readLine(); int count = 0;

			while(str!=null) {
			 	mapData[count] = str;
				str = br.readLine();
				count++;
			}
			
			br.close();

			for(int i=0; i<4; i++) {
				for(int j=0; j<4; j++) {
					File playerFile = new File("./src/img/player"+i+j+".png");
					playerImg[i][j] = ImageIO.read(playerFile);
				}
			}

			for(int i=0; i<8; i++) {
				File lampFile = new File("./src/img/lamp"+i+".png");
				lampImg[i] = ImageIO.read(lampFile);
			}
		} catch(IOException e) {
			System.err.println(e.toString());
		}

		map = new char[mapY+10][mapX+10];

		for(int w=0; w<5; w++) {
			for(int x=0; x<mapX+10; x++) {
				map[w][x] = 'W';
				map[(mapY+5)+w][x] = 'W';
			}
			for(int y=0; y<mapY+10; y++) {
				map[y][w] = 'W';
				map[y][(mapX+5)+w] = 'W';
			}
		}

		for(int y=5; y<mapY+5; y++) {
			for(int x=5; x<mapX+5; x++) {
				map[y][x] = mapData[y-5].charAt(x-5);
				if(map[y][x] == 'P') {
					playerSet(x, y);
				}
			}
		}

		th = new Thread(this);
		th.start();

		time = System.currentTimeMillis() * 0.001 + remain;

		addKeyListener(this);
		setFocusable(true);
 	}

 	@Override
 	public void paintComponent(Graphics g) {
		setRange();

		for(int y=-range; y<=range; y++) {
			for(int x=-range; x<=range; x++) {
				int xx = 40*(x+5), yy = 40*(y+5);
				switch(map[playerY+y][playerX+x]) {
					case 'W' : g.setColor(new Color(100, 35, 25));
						   g.fillRect(xx, yy, 26, 10);
						   g.fillRect(xx+32, yy, 8, 10);
						   g.fillRect(xx, yy+15, 10, 10);
						   g.fillRect(xx+16, yy+15, 24, 10);
						   g.fillRect(xx, yy+30, 18, 10);
						   g.fillRect(xx+24, yy+30, 16, 10);
						   break;

					case ' ' : g.setColor(new Color(40, 30, 25));
						   g.fillRect(xx, yy, 40, 40);
						   break;
					case 'P' : g.setColor(new Color(40, 30, 25));
						   g.fillRect(xx, yy, 40, 40);
						   break;

					case 'L' : g.setColor(new Color(40, 30, 25));
						   g.fillRect(xx, yy, 40, 40);
						   g.drawImage(lampImg[lampNo], xx+10, yy, this);
						   break;
				}
			}
		}

		playerDraw(g);

		displayTime(g);

		battery -= 0.3;
 	}

	@Override
	public void run() {
		while (th != null) {
			if(lampNo>6) {
				lampNo = 0;
			} else {
				lampNo++;
			}

	          	repaint();
			sleep(250);
		}
	}

	void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			System.err.println(e.toString());
		}
	}

	void playerSet(int x, int y) {
		playerX = x;
		playerY = y;
	}

	void playerDraw(Graphics g) {
		if(playerNo>3) {
			playerNo = 0;
		}

		g.drawImage(playerImg[playerDirNo][playerNo], 206, 201, this);
	}

	void playerMove(int dir) {
		int dx = 0, dy = 0;

		switch(dir) {
			case 0: dy = 1;
				break;
			case 1: dy = -1;
				break;
			case 2: dx = -1;
				break;
			case 3: dx = 1;
				break;
		}

		if(dx == 0 && dy == 0) {
			return;
		}

		if(map[playerY+dy][playerX+dx] == 'W') {
			return;
		}


		if(map[playerY+dy][playerX+dx] == 'L') {
			map[playerY+dy][playerX+dx] = ' ';
			battery = 100; range = 3;
		}

		playerX += dx;
		playerY += dy;
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		int dir = -1;

		switch(key) {
			case KeyEvent.VK_LEFT: dir = 2;
					       playerDirNo = 2; playerNo++;
					       break;
			case KeyEvent.VK_RIGHT: dir = 3;
					        playerDirNo = 3; playerNo++;
					        break;
			case KeyEvent.VK_UP: dir = 1;
					     playerDirNo = 1; playerNo++;
					     break;
			case KeyEvent.VK_DOWN: dir = 0;
					       playerDirNo = 0; playerNo++;
					       break;
		}

		if(dir >= 0) {
			playerMove(dir);
		}

		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	void displayTime(Graphics g) {
		if(gameover) {
			g.setFont(new Font("TimeRoman", Font.BOLD, 18));
			g.setColor(Color.red);
			g.drawString("GAME OVER", 55, 50);
			setFocusable(false);
		} else {
			int dt = (int)(time - System.currentTimeMillis() * 0.001);
			g.setFont(new Font("TimeRoman", Font.BOLD, 18));
			g.setColor(Color.orange);
			g.drawString("TIME " + dt, 55, 50);

			if(dt==0) {
				gameover = true;
			}
		}
	}

	void setRange() {
		if(battery < 50) {
			range = 2;
		}

		if(battery <= 0) {
			range = 1;
		}
	}
}
