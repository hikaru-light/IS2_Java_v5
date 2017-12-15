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
		
		fr.setResizable(false);
		fr.setVisible(true);
	}
}

class RogueLikeGamePanel extends JPanel {
	char map[][];
	int mapX = 20, mapY = 20;
	String mapData[] = {"                    ",
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
	
	RogueLikeGamePanel() {
		map = new char[2*(mapY+10)][2*(mapX+10)];

		for(int w=0; w<5; w++) {
			for(int x=0; x<mapX+10; x++) {
				map[w][x] = 'W';
				map[mapY+6+w][x] = 'W';
			}
			for(int y=0; y<mapY+10; y++) {
				map[y][w] = 'W';
				map[y][mapY+6+w] = 'W';
			}
		}

		for(int y=5; y<mapY+5; y++) {
			for(int x=5; x<mapX+5; x++) {
				map[y][x] = mapData[y-5].charAt(x-5);
			}
		}
 	}
 
 	@Override
 	public void paintComponent(Graphics g) {
		for(int y=0; y<mapY+2; y++) {
			for(int x=0; x<mapX+2; x++) {
				int xx = 40*x, yy = 40*y;
				switch(map[y][x]) {
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
 	}
   }

