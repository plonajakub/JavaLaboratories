package lab06_pop.tasks;

import java.util.ArrayList;
import java.util.List;

public class Task {

	private final int ID;
	private Type type;
	private final List<String> arguments = new ArrayList<>();
	private Priority priority;
	private Category category;

	private State state;
	private String result;
	
	public enum Type {
		GCD, FAST_POWER, BINOMIAL_THEOREM;
		
		@Override
		public String toString() {
			if (this.equals(GCD)) {
				return "Greatest common divisor";
			} else if (this.equals(FAST_POWER)){
				return "Fast power";
			} else {
				return "Binomial theorem";
			}
		}
	}

	public enum Priority {
		HIGH(3), NORMAL(2), LOW(1);
		
		private int priority;
		
		Priority(int priority) {
			this.priority = priority;
		}
		
		public boolean isHigherThan(Priority other) {
			return priority > other.priority;
		}
		
		@Override
		public String toString() {
			if (this.equals(HIGH)) {
				return "high";
			} else if (this.equals(NORMAL)){
				return "normal";
			} else {
				return "low";
			}
		}
	}

	public enum Category {
		COMPLEX, SIMPLE;
		
		@Override
		public String toString() {
			if (this.equals(COMPLEX)) {
				return "complex";
			} else {
				return "simple";
			}
		}
	}
	
	public enum State {
		CREATED, IN_PROGRESS, FINISHED;
		
		@Override
		public String toString() {
			if (this.equals(CREATED)) {
				return "created";
			} else if (this.equals(IN_PROGRESS)) {
				return "in progress";
			} else {
				return "finished";
			}
		}
	}

	public Task(List<Integer> occupiedIDs) {
		this.ID = IDGenerator.getNext(occupiedIDs);
	}

	public Task(List<Integer> occupiedIDs, Type type, List<String> arguments, Priority priority, Category cathegory) {
		this.ID = IDGenerator.getNext(occupiedIDs);
		this.type = type;
		this.arguments.addAll(arguments);
		this.priority = priority;
		this.category = cathegory;
	}

	public int getID() {
		return ID;
	}

	public Type getType() {
		return type;
	}

	public List<String> getArguments() {
		return arguments;
	}

	public Priority getPriority() {
		return priority;
	}

	public Category getCategory() {
		return category;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return ID + ";" + type.name() + ";" + arguments;
	}
}
