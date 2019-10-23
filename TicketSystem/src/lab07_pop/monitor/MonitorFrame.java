package lab07_pop.monitor;

import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

class MonitorFrame extends JFrame {

	static final int WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 10 * 3;
	static final int HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 10 * 3;

	private final JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MonitorFrame frame = new MonitorFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	private MonitorFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Monitor");
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		setResizable(true);

		contentPane = new MonitorMainPanel();
		setContentPane(contentPane);
	}

}
