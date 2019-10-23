package lab06_pop.net.server;

import java.util.ArrayList;
import java.util.List;

public class TaskHandlerListManager extends Thread {

	private final List<ServerTaskHandler> taskHandlers = new ArrayList<>();
	private volatile static TaskHandlerListManager instance = null;

	private TaskHandlerListManager() {
	}

	static TaskHandlerListManager getInstance() {
		if (instance == null) {
			synchronized (TaskHandlerListManager.class) {
				if (instance == null) {
					instance = new TaskHandlerListManager();
				}
			}
		}
		return instance;
	}

	synchronized void add(ServerTaskHandler taskHandler) {
		taskHandlers.add(taskHandler);
	}

	synchronized void remove(ServerTaskHandler taskHandler) {
		taskHandlers.remove(taskHandler);
	}

	synchronized void clear() {
		taskHandlers.clear();
	}

	synchronized void joinAll() {
		taskHandlers.forEach(worker -> {
			try {
				worker.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
	}
}
