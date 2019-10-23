package lab07_pop;

import java.io.Serializable;

public class Ticket implements Serializable {

	public String category;
	public int number;
	public char status; // 'i' - issued, 'c' - called, 's' - served
	
	// For test purposes
	@Override
	public String toString() {
		return "Ticket [category=" + category + ", number=" + number + ", status=" + status + "]";
	}
}
