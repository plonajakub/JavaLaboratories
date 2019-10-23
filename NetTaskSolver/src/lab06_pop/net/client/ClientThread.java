package lab06_pop.net.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.swing.JTextArea;

import lab06_pop.gui.panels.ClientPanel;
import lab06_pop.net.server.ServerTaskHandler;
import lab06_pop.tasks.Task;

public class ClientThread extends Thread {

	private final ClientPanel clientPanel;
	private final JTextArea logArea;
	private Socket socket;
	private BufferedReader buffRead;
	private PrintWriter prntWrite;

	private final Random random;

	private String taskDescription;
	private String taskResult;

	private boolean stopRequest;

	private enum ConnectResult {
		SUCCESS, WRONG_PORT, UNKNOWN_IP, SERVER_DOWN, FAILURE
	}

	private enum GetTaskResult {
		SUCCESS, IO_EXCEPTION, EMPTY_SERVER_RESPONSE, FAILURE
	}

	private enum SendResultStatus {
		SUCCESS, IO_EXCEPTION, FAILURE
	}

	public ClientThread(ClientPanel clientPanel) {
		this.clientPanel = clientPanel;
		this.logArea = clientPanel.getLogTextArea();
		this.random = new Random();
		this.taskDescription = null;
		this.stopRequest = false;
	}

	synchronized void requestStop() {
		clientPanel.getClientStateInfoLabel().setText("stopping");
		setClientPanelIfStopping();
		this.stopRequest = true;
	}

	@Override
	public void run() {
		clientPanel.getClientStateInfoLabel().setText("running");
		setClientPanelIfRunning();
		clientLoop();
	}

	private void clientLoop() {
		GetTaskResult getTaskResult;
		SendResultStatus sendResultStatus;
		while (true) {
			logArea.append("Connecting to server (GET)...\n");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			switch (connect()) {
			case FAILURE:
				logArea.append("Unknown connection error occured!\n");
				stopSelf();
				return;
			case SERVER_DOWN:
				logArea.append("Cannot connect to server!\n");
				stopSelf();
				return;
			case SUCCESS:
				logArea.append("Connected to the server!\n");
				break;
			case UNKNOWN_IP:
				logArea.append("Unknown IP!\n");
				stopSelf();
				return;
			case WRONG_PORT:
				logArea.append("Wrong port!\n");
				stopSelf();
				return;
			default:
				return;
			}
			logArea.append("Fetching task decription from server...\n");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			getTaskResult = getTask();
			switch (getTaskResult) {
			case EMPTY_SERVER_RESPONSE:
				logArea.append("No task to perform!\n");
				stopSelf();
				return;
			case FAILURE:
				logArea.append("Unknown error while getting task description!\n");
				stopSelf();
				return;
			case IO_EXCEPTION:
				logArea.append("I/O error while getting task description!\n");
				stopSelf();
				return;
			case SUCCESS:
				logArea.append("Task description successfuly fetched from server!\n");
				break;
			default:
				return;
			}
			disconnect();
			logArea.append("Disconnected from server (GET)!\n");
			logArea.append("Executing the task...\n");
			executeTask();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			logArea.append("Task executed!\n");
			logArea.append("Connecting to server (POST)...\n");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			switch (connect()) {
			case FAILURE:
				logArea.append("Unknown connection error occured!\n");
				stopSelf();
				return;
			case SERVER_DOWN:
				logArea.append("Cannot connect to server!\n");
				stopSelf();
				return;
			case SUCCESS:
				logArea.append("Connected to the server!\n");
				break;
			case UNKNOWN_IP:
				logArea.append("Unknown IP!\n");
				stopSelf();
				return;
			case WRONG_PORT:
				logArea.append("Wrong port!\n");
				stopSelf();
				return;
			default:
				return;
			}
			logArea.append("Sending task result to the server...\n");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			sendResultStatus = sendResult();
			switch (sendResultStatus) {
			case FAILURE:
				logArea.append("Unknown connection error occured!\n");
				stopSelf();
				return;
			case IO_EXCEPTION:
				logArea.append("I/O error while sending task result!\n");
				stopSelf();
				return;
			case SUCCESS:
				logArea.append("Task result sent!\n");
				break;
			default:
				return;
			}
			disconnect();
			logArea.append("Disconnected from server (POST)!\n");
			synchronized (this) {
				if (isInterrupted() || stopRequest) {
					logArea.append("Stopping client due to external request...\n");
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					stopSelf();
					return;
				}
			}
			logArea.append("\n");
		}

	}

	private ConnectResult connect() {
		int port;
		try {
			port = Integer.parseInt(clientPanel.getHostPortTextField().getText());
		} catch (NumberFormatException e) {
			return ConnectResult.WRONG_PORT;
		}
		String ip = clientPanel.getHostAdressTextField().getText();
		try {
			socket = new Socket(ip, port);
		} catch (UnknownHostException e) {
			return ConnectResult.UNKNOWN_IP;
		} catch (IOException e) {
			return ConnectResult.SERVER_DOWN;
		}
		return ConnectResult.SUCCESS;
	}

	private GetTaskResult getTask() {
		try {
			buffRead = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			prntWrite = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			return GetTaskResult.IO_EXCEPTION;
		}
		String request = "GET" + ";" + getRandomCategory();
		prntWrite.println(request);
		prntWrite.flush();
		logArea.append("Request sent: <" + request + ">\n");
		try {
			String requestResult = buffRead.readLine();
			logArea.append("Server answer: \n1) <" + requestResult + ">\n");
			if (ServerTaskHandler.RequestResult.valueOf(requestResult)
					.equals(ServerTaskHandler.RequestResult.NO_TASK_AVAILABLE)) {
				return GetTaskResult.EMPTY_SERVER_RESPONSE;
			}
			taskDescription = buffRead.readLine();
			logArea.append("2) <" + taskDescription + ">\n");
		} catch (IOException e) {
			e.printStackTrace();
			return GetTaskResult.IO_EXCEPTION;
		}
		return GetTaskResult.SUCCESS;
	}

	private void executeTask() {
		List<String> taskInfo = Arrays.asList(taskDescription.split(";"));
		switch (Task.Type.valueOf(taskInfo.get(1))) {
		case BINOMIAL_THEOREM:
			binomialTheorem(taskInfo.get(2));
			break;
		case FAST_POWER:
			fastPower(taskInfo.get(2));
			break;
		case GCD:
			gcd(taskInfo.get(2));
			break;
		default:
			break;
		}
	}

	private SendResultStatus sendResult() {
		try {
			buffRead = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			prntWrite = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			return SendResultStatus.IO_EXCEPTION;
		}
		List<String> taskInfo = Arrays.asList(taskDescription.split(";"));
		String request = "POST" + ";" + taskInfo.get(0) + ";" + taskResult;
		prntWrite.println(request);
		prntWrite.flush();
		logArea.append("Request sent: <" + request + ">\n");
		try {
			String requestResult = buffRead.readLine();
			logArea.append("Server answer: <" + requestResult + ">\n");
		} catch (IOException e) {
			e.printStackTrace();
			return SendResultStatus.IO_EXCEPTION;
		}
		return SendResultStatus.SUCCESS;
	}

	private void disconnect() {
		try {
			if (buffRead != null) {
				buffRead.close();
			}
			if (prntWrite != null) {
				prntWrite.close();
			}
			if (socket != null && !socket.isClosed()) {
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	private void stopSelf() {
		disconnect();
		setClientPanelIfStopped();
		logArea.append("Client stopped!\n\n\n");
		interrupt();
	}

	private String getRandomCategory() {
		switch (random.nextInt(2)) {
		case 0:
			return Task.Category.SIMPLE.name();
		case 1:
			return Task.Category.COMPLEX.name();
		default:
			return "";
		}
	}

	private void binomialTheorem(String arguments) {
		List<String> args = Arrays.asList(arguments.substring(1, arguments.length() - 1).split("\\s*,\\s*"));
		long n = Long.parseLong(args.get(0));
		long k = Long.parseLong(args.get(1));
		long up = 1, down = 1;
		for (long i = n; i >= n - k + 1; --i)
			up *= i;
		for (long i = 2; i <= k; ++i) {
			down *= i;
		}
		this.taskResult = Long.toString(up / down);
	}

	private void fastPower(String arguments) {
		List<String> args = Arrays.asList(arguments.substring(1, arguments.length() - 1).split("\\s*,\\s*"));
		Double base = Double.parseDouble(args.get(0));
		Double exponent = Double.parseDouble(args.get(1));
		this.taskResult = new Double(Math.pow(base, exponent)).toString();
	}

	private void gcd(String arguments) {
		List<String> args = Arrays.asList(arguments.substring(1, arguments.length() - 1).split("\\s*,\\s*"));
		Integer a = Integer.parseInt(args.get(0));
		Integer b = Integer.parseInt(args.get(1));
		while (a != b) {
			if (a > b)
				a -= b;
			else
				b -= a;
		}
		this.taskResult = a.toString();
	}

	void setClientPanelIfStarting() {
		clientPanel.getStartClientButton().setEnabled(false);
		clientPanel.getStopClientButton().setEnabled(false);
		clientPanel.getHostPortTextField().setEditable(false);
		clientPanel.getHostAdressTextField().setEditable(false);
		clientPanel.getClientStateInfoLabel().setText("starting");
	}

	private void setClientPanelIfStopping() {

		clientPanel.getStartClientButton().setEnabled(false);
		clientPanel.getStopClientButton().setEnabled(false);
		clientPanel.getHostPortTextField().setEditable(false);
		clientPanel.getHostAdressTextField().setEditable(false);
		clientPanel.getClientStateInfoLabel().setText("stopping");
	}

	private void setClientPanelIfStopped() {
		clientPanel.getStartClientButton().setEnabled(true);
		clientPanel.getStopClientButton().setEnabled(false);
		clientPanel.getHostPortTextField().setEditable(true);
		clientPanel.getHostAdressTextField().setEditable(true);
		clientPanel.getClientStateInfoLabel().setText("stopped");
	}

	private void setClientPanelIfRunning() {
		clientPanel.getStartClientButton().setEnabled(false);
		clientPanel.getStopClientButton().setEnabled(true);
		clientPanel.getHostPortTextField().setEditable(false);
		clientPanel.getHostAdressTextField().setEditable(false);
		clientPanel.getClientStateInfoLabel().setText("running");
	}
}
