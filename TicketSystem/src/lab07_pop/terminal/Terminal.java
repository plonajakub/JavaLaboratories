package lab07_pop.terminal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Random;

import lab07_pop.Ticket;
import lab07_pop.central.CentralServer;
import lab07_pop.central.ICentral;
import lab07_pop.exceptions.InvalidCategoryException;

class Terminal implements ActionListener {

	private ICentral central;
	private final TerminalMainPanel tmp;
	
	Terminal() {
		tmp = null;
		connectToCentral();
	}
	
	public static void main(String[] args) {
		Terminal terminal = new Terminal();
		terminal.autoTest();
	}

	Terminal(TerminalMainPanel tmp) {
		this.tmp = tmp;
		connectToCentral();
		prepareMainPanel();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Ticket ticket = null;
		try {
			ticket = this.getTicket((String) tmp.getCategoryComboBox().getSelectedItem());
		} catch (InvalidCategoryException e1) {
			e1.printStackTrace();
		}
		if (ticket == null) {
			tmp.getLastTicketInfoLabel().setText("Check connection with server!");
		}
		tmp.getLastTicketInfoLabel().setText("Ticket No. " + ticket.number);
	}

	private Ticket getTicket(String category) throws InvalidCategoryException {
		Ticket ticket = null;
		try {
			ticket = central.getTicket(category);
			if (ticket == null) {
				throw new InvalidCategoryException();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
		return ticket;
	}

	private void connectToCentral() {
		try {
			Registry registry = LocateRegistry.getRegistry("localhost");
			central = (ICentral) registry.lookup("central");
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
			System.exit(CentralServer.REMOTE_ERROR_EXIT_STATUS);
		}
		System.out.println("Terminal online!");
	}

	private void prepareMainPanel() {
		List<String> categories = null;
		try {
			categories = central.getCategories();
		} catch (RemoteException e) {
			e.printStackTrace();
			System.exit(CentralServer.REMOTE_ERROR_EXIT_STATUS);
		}
		categories.forEach(category -> tmp.getCategoryComboBox().addItem(category));
		tmp.getGetTicketButton().addActionListener(this);
	}

	// For test purposes
	private void autoTest() {
		Random random = new Random();
		System.out.println();
		while (true) {
			Ticket ticket = null;
			System.out.println("Getting new ticket...");
			try {
				ticket = this.getTicket("Category No. " + (1 + random.nextInt(4)));
			} catch (InvalidCategoryException e1) {
				System.out.println("Entered category does not exist");
				continue;
			}
			if (ticket == null) {
				System.out.println("Ticket has not been created: make sure the terminal is connected to the server");
			} else {
				System.out.println("Your ticket:");
				System.out.println(ticket);
				System.out.println();
			}
//			try {
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
	}
}
