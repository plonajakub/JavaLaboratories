package lab06_pop.gui;

import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import lab06_pop.gui.panels.MainTabbedPanel;

public class MainFrame extends JFrame {

	public static final int WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 5 * 3;
	public static final int HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 5 * 3;

	private JTabbedPane contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
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
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("NetTaskSolver");
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		setResizable(true);

		contentPane = new MainTabbedPanel();
		setContentPane(contentPane);
	}
}
