package lab06_pop.gui.panels;

import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class MainTabbedPanel extends JTabbedPane {

	public MainTabbedPanel() {
		JPanel serverPanel = new ServerPanel();
		this.addTab("Server", serverPanel);
		this.setMnemonicAt(0, KeyEvent.VK_1);

		JPanel clientPanel = new ClientPanel();
		this.addTab("Client", clientPanel);
		setMnemonicAt(1, KeyEvent.VK_2);
	}
}
