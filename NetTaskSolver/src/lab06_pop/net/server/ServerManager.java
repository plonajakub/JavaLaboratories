package lab06_pop.net.server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import lab06_pop.gui.panels.ServerPanel;

public class ServerManager implements ActionListener {

	private final ServerPanel serverPanel;
	private ServerGate serverGate;

	public ServerManager(ServerPanel serverPanel) {
		this.serverPanel = serverPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("START_SERVER")) {
			serverPanel.getServerStateLabelInfo().setText("starting");
			serverGate = new ServerGate(serverPanel);
			try {
				serverGate.prepareServer();
			} catch (NumberFormatException e1) {
				serverPanel.getPortTextField().setText("Wrong port format!");
				return;
			} catch (IOException e1) {
				e1.printStackTrace();
				return;
			}
			serverGate.start();
			serverPanel.getServerStateLabelInfo().setText("running");
			serverPanel.getStartServerButton().setEnabled(false);
			serverPanel.getStopServerButton().setEnabled(true);
			serverPanel.getPortTextField().setEnabled(false);
		} else if (e.getActionCommand().equals("STOP_SERVER")) {
			serverPanel.getServerStateLabelInfo().setText("stopping");
			try {
				serverGate.stopServer();
			} catch (IOException e1) {
				e1.printStackTrace();
				return;
			}
			serverPanel.getServerStateLabelInfo().setText("stopped");
			serverPanel.getStartServerButton().setEnabled(true);
			serverPanel.getStopServerButton().setEnabled(false);
			serverPanel.getPortTextField().setEnabled(true);
		}
	}

}
