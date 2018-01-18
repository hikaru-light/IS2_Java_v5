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

class RogueLikeGamePanel extends JPanel implements KeyListener {
	char map[][];
	int mapX = 30, mapY = 20;
	String mapData[] = {"PWWWWWW       WWWWW        WWW",
			    " WWWWWWWWWWWW WWWWW WWWWWW WWW",
			    " WWWWWWWWWWWW WWWWW WWWWWW WWW",
			    " WWWWWWWWWWWW WWWWW WWWWWW WWW",
			    " WWWWWWWWWWWW       WWWWWW WWW",
			    " WWWWWWWWWWWW WWWWW WWWWWW WWW",
			    " WWWWWWWWWWWW WWWWW WWWWWW WWW",
			    " WWWW         WWWWW WWWWWW WWW",
			    " WWWW WWWWWWWWWWWWW WWWWWW WWW",
			    " WWWW WWWWWWWWWWWWW        WWW",
			    " WWWW WWWWWWWWWWWWW WWWWWWWWWW",
			    " WWWW WWWWWWWWWWWWW WWWWWWWWWW",
			    "              WWWWW WWWWWWWWWW",
			    "WWWWWWWWWWWWWWWWWWW WWWWWWWWWW",
			    "WWW     LWWWWWWWWWW WWWWWWWWWW",
			    "WWW WWWWWWWWWWWWWWW WWWWWWWWWW",
			    "WWW WWWWWWWWWWWWWWW WWWWWWWWWW",
			    "WWW                 WWWWWWWWWW",
			    "WWW WWWWWWWWWWWWWWWWWWWWWWWWWW",
			    "WWWWWWWWWWWWWWWWWWWWWWWWWWWWWW"};

	BufferedImage[][] playerImg = new BufferedImage[4][4];
	BufferedImage[] lampImg = new BufferedImage[5];

	int playerX, playerY;
	int playerDirNo, playerNo;

	int steps = 1;
	int range = 3;

	RogueLikeGamePanel() {
		try {
			for(int i=0; i<4; i++) {
				for(int j=0; j<4; j++) {
					File playerFile = new File("./src/img/player"+i+j+".png");
					playerImg[i][j] = ImageIO.read(playerFile);
				}
			}

			for(int i=0; i<5; i++) {
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

		addKeyListener(this);
		setFocusable(true);
 	}

 	@Override
 	public void paintComponent(Graphics g) {
		countSteps();

		for(int y=-range; y<=range; y++) {
			for(int x=-range; x<=range; x++) {
				int xx = 40*(x+5), yy = 40*(y+5);
				switch(map[playerY+y][playerX+x]) {
					case 'W' : g.setColor(new Color(100+(int)(Math.random()*20), 35+(int)(Math.random()*5), 25+(int)(Math.random()*5)));
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
						   g.drawImage(lampImg[0], xx+10, yy, this);
						   break;
				}
			}
		}

		playerDraw(g);
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
			steps = 0;
		}

		playerX += dx;
		playerY += dy;

		steps += 1;
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

		// try {
		//         Thread.sleep(30);
		// } catch (InterruptedException e) {
		// 	System.err.println(e.toString());
		// }

		if(dir >= 0) {
			playerMove(dir);
		}

		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	void countSteps() {
		if(steps > 0) {
			range = 3;
		}

		if(steps > 50) {
			range = 2;
		}

		if(steps > 100) {
			range = 1;
		}
	}
}
