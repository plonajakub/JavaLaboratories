package lab06_pop.tasks;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import lab06_pop.tasks.Task.Category;
import lab06_pop.tasks.Task.Priority;
import lab06_pop.tasks.Task.State;
import lab06_pop.tasks.Task.Type;

public class TaskManager {

	private final Map<Integer, Task> tasks = new TreeMap<>();
	private final Map<Integer, Map<String, String>> sTasks = new TreeMap<>();
	private volatile static TaskManager instance = null;

	private TaskManager() {
		Map<String, String> entry = new HashMap<>();
		entry.put("type", "GCD");
		entry.put("arguments", "1054,2048");
		entry.put("priority", "high");
		entry.put("category", "simple");
		addTask(entry);
		
		entry = new HashMap<>();
		entry.put("type", "FAST_POWER");
		entry.put("arguments", "4,4");
		entry.put("priority", "low");
		entry.put("category", "simple");
		addTask(entry);
		
		entry = new HashMap<>();
		entry.put("type", "BINOMIAL_THEOREM");
		entry.put("arguments", "15,7");
		entry.put("priority", "high");
		entry.put("category", "complex");
		addTask(entry);
		
		entry = new HashMap<>();
		entry.put("type", "GCD");
		entry.put("arguments", "3423,2322");
		entry.put("priority", "normal");
		entry.put("category", "complex");
		addTask(entry);
		
		entry = new HashMap<>();
		entry.put("type", "FAST_POWER");
		entry.put("arguments", "3,7");
		entry.put("priority", "normal");
		entry.put("category", "simple");
		addTask(entry);
		
		entry = new HashMap<>();
		entry.put("type", "BINOMIAL_THEOREM");
		entry.put("arguments", "25,12");
		entry.put("priority", "high");
		entry.put("category", "complex");
		addTask(entry);
		
		entry = new HashMap<>();
		entry.put("type", "GCD");
		entry.put("arguments", "2233,2432");
		entry.put("priority", "normal");
		entry.put("category", "complex");
		addTask(entry);
		
		entry = new HashMap<>();
		entry.put("type", "FAST_POWER");
		entry.put("arguments", "7,6");
		entry.put("priority", "low");
		entry.put("category", "complex");
		addTask(entry);
		
		entry = new HashMap<>();
		entry.put("type", "BINOMIAL_THEOREM");
		entry.put("arguments", "9,3");
		entry.put("priority", "high");
		entry.put("category", "simple");
		addTask(entry);
		
		entry = new HashMap<>();
		entry.put("type", "FAST_POWER");
		entry.put("arguments", "5,3");
		entry.put("priority", "low");
		entry.put("category", "simple");
		addTask(entry);
		
		entry = new HashMap<>();
		entry.put("type", "BINOMIAL_THEOREM");
		entry.put("arguments", "9,6");
		entry.put("priority", "normal");
		entry.put("category", "simple");
		addTask(entry);
	}

	public static TaskManager getInstance() {
		if (instance == null) {
			synchronized (TaskManager.class) {
				if (instance == null) {
					instance = new TaskManager();
				}
			}
		}
		return instance;
	}

	public void addTask(Map<String, String> args) {
		Type taskType = Type.valueOf(args.get("type").toUpperCase());
		List<String> userArguments = Arrays.asList(args.get("arguments").split("\\s*,\\s*"));
		if (userArguments.isEmpty()) {
			throw new IllegalArgumentException("Error: arguments not provided for the new task");
		}
		Priority taskPriority = Priority.valueOf(args.get("priority").toUpperCase());
		Category taskCategory = Category.valueOf(args.get("category").toUpperCase());
		Task task = new TaskBuilder().occupiedIDs(tasks.keySet()).type(taskType).arguments(userArguments)
				.priority(taskPriority).category(taskCategory).createTask();
		task.setState(State.CREATED);
		synchronized (this) {
			tasks.put(task.getID(), task);
			sTasks.put(task.getID(), makeTaskPrintableRepresentation(task));
		}
	}

	public synchronized String getNewTask(String category) throws NoAvailableTaskException {
		Task foundTask = null;
		for (Task task : tasks.values()) {
			if (!task.getState().equals(State.CREATED))
				continue;
			if (category != null && !category.equals("-"))
				if (!task.getCategory().equals(Category.valueOf(category.toUpperCase())))
					continue;
			if (foundTask == null || task.getPriority().isHigherThan(foundTask.getPriority())) {
				foundTask = task;
			}
		}
		if (foundTask == null) {
			throw new NoAvailableTaskException("Error: there is no available task");
		}
		foundTask.setState(State.IN_PROGRESS);
		sTasks.get(foundTask.getID()).put("State", State.IN_PROGRESS.toString());
		return foundTask.toString();
	}

	public void reportTaskResult(String sID, String result) {
		Integer ID = Integer.parseInt(sID);
		if (!tasks.containsKey(ID)) {
			throw new IllegalArgumentException("Error: task not found");
		}
		synchronized (this) {
			Task task = tasks.get(ID);
			task.setResult(result);
			task.setState(State.FINISHED);
			sTasks.get(ID).put("Result", task.getResult());
			sTasks.get(ID).put("State", task.getState().toString());
		}
	}

	public void removeTask(String sID) throws TaskInProgressException {
		Integer ID = Integer.parseInt(sID);
		if (!tasks.containsKey(ID)) {
			throw new IllegalArgumentException("Error: task not found");
		}
		synchronized (this) {
			Task task = tasks.get(ID);
			if (task.getState().equals(State.IN_PROGRESS)) {
				throw new TaskInProgressException("Error: task cannot be deleted becouse is being executed");
			}
			tasks.remove(ID);
			sTasks.remove(ID);
		}
	}

	public synchronized void cancelActiveTasks() {
		tasks.forEach((id, task) -> {
			if (task.getState().equals(State.IN_PROGRESS))
				task.setState(State.CREATED);
		});
		sTasks.forEach((id, sTask) -> {
			if (sTask.get("State").equals(State.IN_PROGRESS.toString()))
				sTask.put("State", Task.State.CREATED.toString());
		});
	}

	private Map<String, String> makeTaskPrintableRepresentation(Task task) {
		Map<String, String> entry = new LinkedHashMap<>();
		entry.put("ID", String.valueOf(task.getID()));
		entry.put("Type", task.getType().toString());
		entry.put("Arguments", task.getArguments().toString());
		entry.put("Priority", task.getPriority().toString());
		entry.put("Category", task.getCategory().toString());
		entry.put("State", task.getState().toString());
		entry.put("Result", task.getResult());
		return entry;
	}

	public synchronized Map<Integer, Map<String, String>> getTasksPrintableRepresentation() {
		return sTasks;
	}
}
