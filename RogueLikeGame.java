import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JPanel;
import java.awt.Graphics;

import java.io.*;
import java.awt.image.*;
import javax.imageio.*;

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

class RogueLikeGamePanel extends JPanel {
	char map[][];
	int mapX = 20, mapY = 20;
	String mapData[] = {"   P                ",
			    "  WWWWWWW      WWWW ",
			    "  W            WWWW ",
			    "   WWW              ",
			    " WWWWW             W",
			    "   WW              W",
			    " W  WWW            W",
			    " WW                 ",
			    "                    ",
			    "W                   ",
			    "                    ",
			    "                    ",
			    "                    ",
			    "                    ",
			    "                    ",
			    "                    ",
			    "                    ",
			    "                    ",
			    "                    ",
			    "                   W"};
	BufferedImage playerImg;
	int playerX, playerY;
	
	RogueLikeGamePanel() {
		try {
			File playerFile = new File("./src/img/player.png");
			playerImg = ImageIO.read(playerFile);
		} catch(IOException e) {
			System.err.println(e.toString());
		}

		map = new char[mapY+10][mapX+10];

		for(int w=0; w<5; w++) {
			for(int x=0; x<mapX+10; x++) {
				map[w][x] = 'W';
				map[mapY+5+w][x] = 'W';
			}
			for(int y=0; y<mapY+10; y++) {
				map[y][w] = 'W';
				map[y][mapX+5+w] = 'W';
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

		// setFocusable(true);
 	}
 
 	@Override
 	public void paintComponent(Graphics g) {
		for(int y=-5; y<=5; y++) {
			for(int x=-5; x<=5; x++) {
				int xx = 40*(x+5), yy = 40*(y+5);
				switch(map[playerY+y][playerX+x]) {
					case 'W' : g.setColor(new Color(100, 40, 30));
						   g.fillRect(xx, yy, 26, 10);
						   g.fillRect(xx+32, yy, 8, 10);
						   g.fillRect(xx, yy+15, 10, 10);
						   g.fillRect(xx+16, yy+15, 24, 10);
						   g.fillRect(xx, yy+30, 18, 10);
						   g.fillRect(xx+24, yy+30, 16, 10);
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
		g.drawImage(playerImg, 200, 200, this);
	}

}

