package lab06_pop.net.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import lab06_pop.gui.panels.ServerPanel;
import lab06_pop.tasks.NoAvailableTaskException;
import lab06_pop.tasks.TaskManager;

public class ServerTaskHandler extends Thread {

	private final ServerPanel serverPanel;
	private final Socket client;
	private BufferedReader buffRead = null;
	private PrintWriter prntWrite = null;

	public enum RequestResult {
		SUCCESS, CATEGORY_SKIPPED, NO_TASK_AVAILABLE
	}

	ServerTaskHandler(Socket socket, ServerPanel serverPanel) {
		this.serverPanel = serverPanel;
		this.client = socket;
	}

	@Override
	public void run() {
		processRequest();
	}

	private void processRequest() {
		try {
			buffRead = new BufferedReader(new InputStreamReader(client.getInputStream()));
			prntWrite = new PrintWriter(client.getOutputStream());
			String dataIn = buffRead.readLine();
			List<String> splittedData = Arrays.asList(dataIn.split(";"));
			if (splittedData.get(0).equals("GET")) {
				processGET(splittedData);
			} else if (splittedData.get(0).equals("POST")) {
				processPOST(splittedData);
			}
			stopSelf();
		} catch (IOException e) {
			e.printStackTrace();
			stopSelf();
		}
	}

	private void processGET(List<String> splittedData) {
		TaskManager taskManager = TaskManager.getInstance();
		String taskInfo;
		try {
			taskInfo = taskManager.getNewTask(splittedData.get(1));
			prntWrite.println(RequestResult.SUCCESS.name());
			prntWrite.flush();
			prntWrite.println(taskInfo);
			prntWrite.flush();
			((AbstractTableModel) serverPanel.getTaskTable().getModel()).fireTableDataChanged();
		} catch (NoAvailableTaskException e) {
			try {
				taskInfo = taskManager.getNewTask(null);
				prntWrite.println(RequestResult.CATEGORY_SKIPPED.name());
				prntWrite.flush();
				prntWrite.println(taskInfo);
				prntWrite.flush();
				((AbstractTableModel) serverPanel.getTaskTable().getModel()).fireTableDataChanged();
			} catch (NoAvailableTaskException e1) {
				prntWrite.println(RequestResult.NO_TASK_AVAILABLE.name());
				prntWrite.flush();
			}
		}
	}

	private void processPOST(List<String> splittedData) {
		TaskManager.getInstance().reportTaskResult(splittedData.get(1), splittedData.get(2));
		prntWrite.println(RequestResult.SUCCESS.name());
		prntWrite.flush();
		((AbstractTableModel) serverPanel.getTaskTable().getModel()).fireTableDataChanged();
	}

	private void stopSelf() {
		try {
			if (buffRead != null) {
				buffRead.close();
			}
			if (prntWrite != null) {
				prntWrite.close();
			}
			if (client != null && !client.isClosed()) {
				client.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		interrupt();
	}
}
