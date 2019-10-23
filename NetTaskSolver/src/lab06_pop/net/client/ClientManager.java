package lab06_pop.net.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import lab06_pop.gui.panels.ClientPanel;

public class ClientManager implements ActionListener {

	private final ClientPanel clientPanel;
	private ClientThread client = null;

	public ClientManager(ClientPanel clientPanel) {
		this.clientPanel = clientPanel;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().equals("START_CLIENT")) {
			clientPanel.getClientStateInfoLabel().setText("starting");
			client = new ClientThread(clientPanel);
			client.setClientPanelIfStarting();
			client.start();
		} else if (arg0.getActionCommand().equals("STOP_CLIENT")) {
			client.requestStop();
		}

	}

}
