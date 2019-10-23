package lab07_pop.exceptions;

public class TicketNotFinalizedException extends Exception {

	public TicketNotFinalizedException() {
		super();
	}
	
	public TicketNotFinalizedException(String message) {
		super(message);
	}
}
