import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JPanel;
import java.awt.Graphics;

class RogueLikeGame {
	public static void main(String[] args) {
		JFrame fr = new JFrame("Dungeon");
		
		fr.setSize(400, 400);
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fr.getContentPane().setBackground(new Color(0, 0, 0));

		RogueLikeGamePanel panel = new RogueLikeGamePanel();
		panel.setOpaque(false);
		fr.add(panel);
		
		fr.setVisible(true);
	}
}

class RogueLikeGamePanel extends JPanel {
	char map[][];
	int mapX = 8, mapY = 8;
	String mapData[] = {"        ",
			    "   WWWW ",
			    "   WWWW ",
			    "   WWW  ",
			    " WWWWW W",
			    "   WW  W",
			    " W  WWWW",
			    " WW     "};
	
	RogueLikeGamePanel() {
		map = new char[mapY+2][mapX+2];

		for(int x=0; x<mapX+2; x++) {
			map[0][x] = 'W';
			map[mapY+1][x] = 'W';
		}
		for(int y=0; y<mapY+2; y++) {
			map[y][0] = 'W';
			map[y][mapY+1] = 'W';
		}
		for(int y=1; y<=mapY; y++) {
			for(int x=1; x<mapX+1; x++) {
				map[y][x] = mapData[y-1].charAt(x-1);
			}
		}
 	}
 
 	@Override
 	public void paintComponent(Graphics g) {
		for(int y=1; y<=mapY; y++) {
			for(int x=1; x<=mapX; x++) {
				int xx = 40*x+20, yy = 40*y+20;
				switch(map[y][x]) {
					case 'W' : g.setColor(Color.white);
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
 	}
   }

