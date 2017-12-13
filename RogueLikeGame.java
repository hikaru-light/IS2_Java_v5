import javax.swing.JFrame;
import java.awt.Color;
// import javax.swing.JPanel;

class RogueLikeGame {
	public static void main(String[] args) {
		JFrame fr = new JFrame("Dungeon");
		
		fr.setSize(400, 400);
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fr.getContentPane().setBackground(new Color(0, 0, 0));

		// RogueLikeGamePanel panel = new RogueLikeGamePanel();
		// panel.setOpaque(false);
		// fr.add(panel);
		
		fr.setVisible(true);
	}
}

// class RogueLikeGamePanel extends JPanel {
// 	RogueLikeGamePanel() {
// 	}
// 
// 	@Override
// 	public void paintComponent(Graphics g) {
// 	}
// }

