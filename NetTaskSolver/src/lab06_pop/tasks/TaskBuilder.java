package lab06_pop.tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import lab06_pop.tasks.Task.Category;
import lab06_pop.tasks.Task.Priority;
import lab06_pop.tasks.Task.Type;

public class TaskBuilder {

	private final List<Integer> occupiedIDs = new LinkedList<>();
	private Type innerType;
	private final List<String> innerArguments = new ArrayList<>();
	private Priority innerPriority;
	private Category innerCategory;

	public TaskBuilder occupiedIDs(Collection<Integer> occupiedIDs) {
		this.occupiedIDs.addAll(occupiedIDs);
		return this;
	}

	public TaskBuilder type(Type type) {
		this.innerType = type;
		return this;
	}

	public TaskBuilder arguments(List<String> arguments) {
		this.innerArguments.addAll(arguments);
		return this;
	}

	public TaskBuilder priority(Priority priority) {
		this.innerPriority = priority;
		return this;
	}

	public TaskBuilder category(Category category) {
		this.innerCategory = category;
		return this;
	}

	public Task createTask() {
		return new Task(occupiedIDs, innerType, innerArguments, innerPriority, innerCategory);
	}
}