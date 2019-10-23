package lab05.gui;

import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import lab05.logic.Cook;
import lab05.logic.Mixer;
import lab05.logic.Resource;
import lab05.logic.Supplier;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 512682020804718039L;
	
	public static final int WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 4 * 3;
	public static final int HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 4 * 3;
	public static final int MIXER_CAPACITY = 20;
	public static final int RESOURCE_QUANTITY = 60;
	public static final int SUPPLIER_QUANTITY = 50;
	public static final int COOK_QUANTITY = 30;
	
	private JPanel contentPane;
	private Mixer mixer;
	

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
		setTitle("Threads");
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		setResizable(true);
	
		prepareWorkers();
		
		contentPane = new MainPanel(mixer);
		setContentPane(new JScrollPane(contentPane));
		mixer.startCooking();
	}

	private void prepareWorkers() {
		mixer = new Mixer(MIXER_CAPACITY);
		mixer.setMixerConfiguration(Resource.getInstances(RESOURCE_QUANTITY),
				Supplier.getInstances(SUPPLIER_QUANTITY, mixer), Cook.getInstances(COOK_QUANTITY, mixer));	
	}

}
