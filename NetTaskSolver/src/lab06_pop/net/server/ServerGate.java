package lab06_pop.net.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.table.AbstractTableModel;

import lab06_pop.gui.panels.ServerPanel;
import lab06_pop.tasks.TaskManager;

public class ServerGate extends Thread {

	private final ServerPanel serverPanel;
	private ServerSocket serverSocket = null;

	ServerGate(ServerPanel serverPanel) {
		this.serverPanel = serverPanel;
		setName("ServerGate");
	}

	@Override
	public void run() {
		startServer();
	}

	void prepareServer() throws IOException, NumberFormatException {
		int port = Integer.parseInt(serverPanel.getPortTextField().getText());
		serverSocket = new ServerSocket(port);
	}

	private void startServer() {
		ServerTaskHandler taskHandler;
		while (true) {
			try {
				Socket client = serverSocket.accept();
				taskHandler = new ServerTaskHandler(client, serverPanel);
				taskHandler.start();
				TaskHandlerListManager.getInstance().add(taskHandler);
			} catch (IOException e) {
				System.out.println("ServerGate interrupted!");
				return;
			}
		}
	}

	void stopServer() throws IOException {
		if (serverSocket != null && !serverSocket.isClosed()) {
			serverSocket.close();
		}
		TaskHandlerListManager.getInstance().joinAll();
		TaskHandlerListManager.getInstance().clear();
		TaskManager.getInstance().cancelActiveTasks();
		((AbstractTableModel) serverPanel.getTaskTable().getModel()).fireTableDataChanged();
		interrupt();
	}
}
