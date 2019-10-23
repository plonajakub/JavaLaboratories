package lab07_pop.exceptions;

public class InvalidCategoryException extends Exception {
	
	public InvalidCategoryException() {
		super();
	}
	
	public InvalidCategoryException(String message) {
		super(message);
	}
}
