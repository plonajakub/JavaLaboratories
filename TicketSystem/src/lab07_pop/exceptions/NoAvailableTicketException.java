package lab07_pop.exceptions;

public class NoAvailableTicketException extends Exception {

	public NoAvailableTicketException() {
		super();
	}
	
	public NoAvailableTicketException(String message) {
		super(message);
	}
}
