package lab07_pop.terminal;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

class TerminalFrame extends JFrame {

	static final int WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 20 * 3;
	static final int HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 10 * 2;

	private final JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TerminalFrame frame = new TerminalFrame();
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
	private TerminalFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Terminal");
		setSize(new Dimension(WIDTH, HEIGHT));
		setLocationRelativeTo(null);
		setResizable(true);

		contentPane = new TerminalMainPanel();
		setContentPane(contentPane);
	}

}
