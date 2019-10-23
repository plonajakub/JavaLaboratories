package lab07_pop.office;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.JButton;

import lab07_pop.Ticket;
import lab07_pop.central.CentralServer;
import lab07_pop.central.ICentral;
import lab07_pop.exceptions.NoAvailableTicketException;
import lab07_pop.exceptions.TicketNotFinalizedException;

class Office implements ActionListener {

	private static final String GET_CUSTOMER_ACTION_COMMAND = "get_customer";
	private static final String FINALIZE_SERVICE_ACTION_COMMAND = "finalize_service";
	private static final String GET_CUSTOMER_BUTTON_LABEL = "Get customer";
	private static final String FINALIZE_SERVICE_BUTTON_LABEL = "Finalize service";

	private ICentral central;
	private final OfficeMainPanel omp;
	private Ticket currentTicket;
	
	Office() {
		omp = null;
		connectToCentral();
	}

	Office(OfficeMainPanel omp) {
		this.omp = omp;
		connectToCentral();
		prepareMainPanel();
	}
	
	public static void main(String[] args) {
		Office office = new Office();
		office.autoTest();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(GET_CUSTOMER_ACTION_COMMAND)) {
			try {
				currentTicket = this.getCustomer();
			} catch (NoAvailableTicketException e1) {
				omp.getCurrentTicketInfoLabel().setText("Ticket queue is empty!");
				return;
			}
			if (currentTicket == null) {
				omp.getCurrentTicketInfoLabel().setText("Check connection with server!");
			}
			omp.getCurrentTicketInfoLabel().setText("Ticket No. " + currentTicket.number);
			JButton officeButton = omp.getOfficeButton();
			officeButton.setText(FINALIZE_SERVICE_BUTTON_LABEL);
			officeButton.setActionCommand(FINALIZE_SERVICE_ACTION_COMMAND);
		} else if (e.getActionCommand().equals(FINALIZE_SERVICE_ACTION_COMMAND)) {
			try {
				this.finalizeService(currentTicket);
			} catch (TicketNotFinalizedException e1) {
				omp.getCurrentTicketInfoLabel().setText("Ticket finalization error: try again");
				return;
			}
			omp.getCurrentTicketInfoLabel().setText("-");
			JButton officeButton = omp.getOfficeButton();
			officeButton.setText(GET_CUSTOMER_BUTTON_LABEL);
			officeButton.setActionCommand(GET_CUSTOMER_ACTION_COMMAND);
		}

	}

	private Ticket getCustomer() throws NoAvailableTicketException {
		Ticket calledTicket = null;
		try {
			calledTicket = central.ticketQuery(null);
			if (calledTicket == null) {
				throw new NoAvailableTicketException();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
		return calledTicket;
	}

	private void finalizeService(Ticket servedTicket) throws TicketNotFinalizedException {
		try {
			central.ticketQuery(servedTicket);
		} catch (RemoteException e) {
			e.printStackTrace();
			throw new TicketNotFinalizedException();
		}
	}

	private void connectToCentral() {
		try {
			Registry registry = LocateRegistry.getRegistry("localhost");
			central = (ICentral) registry.lookup("central");
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
			System.exit(CentralServer.REMOTE_ERROR_EXIT_STATUS);
		}
		System.out.println("Office online!");
	}

	private void prepareMainPanel() {
		JButton officeButton = omp.getOfficeButton();
		officeButton.setText(GET_CUSTOMER_BUTTON_LABEL);
		officeButton.setActionCommand(GET_CUSTOMER_ACTION_COMMAND);
		officeButton.addActionListener(this);
	}

	// For test purposes
	private void autoTest() {
		System.out.println();
		while (true) {
			System.out.println("Getting new customer...");
			try {
				currentTicket = this.getCustomer();
			} catch (NoAvailableTicketException e) {
				System.out.println("No available tickets!");
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				continue;
			}
//			try {
//				Thread.sleep(2000);
//			} catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
			System.out.println("Customer arrived!");
			System.out.println("Current customer: " + currentTicket);
//			try {
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			System.out.println("Finalizing ticket...");
			while (true) {
				try {
					this.finalizeService(currentTicket);
					break;
				} catch (TicketNotFinalizedException e1) {
					System.out.println("Ticket finalization error, repeating step...");
				}
			}
//			try {
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			System.out.println("Ticket finalized!");
			System.out.println();
		}

	}
}
