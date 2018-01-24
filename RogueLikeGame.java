import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import java.io.*;
import javax.imageio.ImageIO;

import java.util.*;

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
	int mapNo;
	int mapX = 50, mapY = 20;
	String[] mapData = new String[20];

	BufferedImage[][] playerImg = new BufferedImage[4][4];
	BufferedImage[] lampImg = new BufferedImage[8];
	BufferedImage stairsImg;

	int playerX, playerY;
	int playerDirNo, playerNo;
	int lampNo = 0;

	double battery = 100;
	int range = 3;

	double time;
	int remain = 100;
	int dt;
	ArrayList<Integer> scoreList = new ArrayList<Integer>();
	boolean newRecord = false;

	boolean gameOver = false;
	boolean gameCleared = false;

	Thread th1;
	int No;

	RogueLikeGamePanel() {
		mapNo = (int)(Math.random()*5);
		try {
			File mapFile = new File("./src/map/data"+mapNo+".txt");
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

			File stairsFile = new File("./src/img/stairs0.png");
			stairsImg = ImageIO.read(stairsFile);
		} catch(IOException e) {
			System.err.println(e.toString());
		}

		map = new char[mapY+10][mapX+10];

		for(int w=0; w<5; w++) {
			for(int x=0; x<mapX+10; x++) {
				map[w][x] = 'B';
				map[(mapY+5)+w][x] = 'B';
			}
			for(int y=0; y<mapY+10; y++) {
				map[y][w] = 'B';
				map[y][(mapX+5)+w] = 'B';
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

		th1 = new Thread(this);
		th1.start();

		time = System.currentTimeMillis() * 0.001 + remain;

		addKeyListener(this);
		setFocusable(true);
 	}

 	@Override
 	public void paintComponent(Graphics g) {
		if(gameOver) {
			range = 5;
		} else {
			setRange();
		}

		for(int y=-range; y<=range; y++) {
			for(int x=-range; x<=range; x++) {
				int xx = 40*(x+5), yy = 40*(y+5);
				switch(map[playerY+y][playerX+x]) {
					case 'B' : g.setColor(new Color(100, 35, 25));
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

					case 'S' : g.drawImage(stairsImg, xx, yy, this);
						   break;
				}
			}
		}

    // I can make this method more efficient.
		if(gameCleared) {
			if(th1!=null){
				scoreRecord(dt);
				Mv th2 = new Mv();
				th2.start();
			}

			th1 = null;

			setFocusable(false);

			g.setFont(new Font("TimeRoman", Font.BOLD, 30));
			g.setColor(new Color(0, 191, 255));
			g.drawString("STAGE COMPLETED", 70-No, 235);

			for(int i=0; i<5; i++) {
				g.setFont(new Font("TimeRoman", Font.BOLD, 18));
				g.setColor(Color.orange);
				g.drawString(i+1+".            "+scoreList.get(i), 903-No, 200+(i*20));
			}

			if(newRecord) {
				g.setColor(Color.red);
				g.drawString("NEW RECORD", 900-No, 150);
			}
		} else {
			displayTime(g);
			playerDraw(g);
			battery -= 0.3;
		}
 	}

	@Override
	public void run() {
		while (th1 != null) {
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
			// System.err.println(e.toString());
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
			case 0 : dy = 1;
				break;
			case 1 : dy = -1;
				break;
			case 2 : dx = -1;
				break;
			case 3 : dx = 1;
				break;
		}

		if(dx == 0 && dy == 0) {
			return;
		}

		switch(map[playerY+dy][playerX+dx]) {
			case 'B' : return;
			case 'L' : map[playerY+dy][playerX+dx] = ' ';
				   battery= 100; range = 3;
				   break;
			case 'S' : gameCleared = true;
				   break;
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
			case KeyEvent.VK_LEFT : dir = 2;
					       playerDirNo = 2; playerNo++;
					       break;
			case KeyEvent.VK_RIGHT : dir = 3;
					        playerDirNo = 3; playerNo++;
					        break;
			case KeyEvent.VK_UP : dir = 1;
					     playerDirNo = 1; playerNo++;
					     break;
			case KeyEvent.VK_DOWN : dir = 0;
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
		if(gameOver) {
			g.setFont(new Font("TimeRoman", Font.BOLD, 40));
			g.setColor(Color.red);
			g.drawString("GAME OVER", 93, 235);

			th1.interrupt(); repaint();
			setFocusable(false);
		} else {
			dt = (int)(time - System.currentTimeMillis() * 0.001);
			g.setFont(new Font("TimeRoman", Font.BOLD, 18));
			g.setColor(Color.orange);
			g.drawString("TIME : " + dt, 55, 50);

			if(dt==0) {
				gameOver = true;
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

  // I can make this method more efficient.
	void scoreRecord(int dt) {
		try{
  		File recordFile = new File("./src/record/data.txt");
			FileReader fr = new FileReader(recordFile);
			FileWriter fw = new FileWriter(recordFile, true);
			BufferedReader br = new BufferedReader(fr);
			String str = br.readLine();

			while(str!=null) {
				int num = Integer.parseInt(str);
				scoreList.add(num);
				str = br.readLine();
			}

			int max = scoreList.get(0);
			for(int i=1; i<scoreList.size(); i++) {
				int num = scoreList.get(i);
				if(num>max) {
					max = num;
				}
			}

			if(dt>max) {
				newRecord = true;
			}

			String sp = System.lineSeparator();
			fw.write(dt+sp);

			scoreList.add(dt);
			Collections.sort(scoreList);
			Collections.reverse(scoreList);

			br.close(); fw.close();
		}catch(IOException e){
  		System.err.println(e.toString());
		}
	}

  // I can make this method more efficient.
	class Mv extends Thread {
		@Override
		public void run() {
			sleep(3000);

			for(int i=0; i<740; i++) {
				No++;
				repaint();
				sleep(5);
			}
		}

		void sleep(int time) {
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				// System.err.println(e.toString());
			}
		}
	}
}
