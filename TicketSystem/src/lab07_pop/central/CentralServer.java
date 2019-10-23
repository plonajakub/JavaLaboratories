package lab07_pop.central;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;

import lab07_pop.Info;
import lab07_pop.Ticket;
import lab07_pop.monitor.IMonitor;

public class CentralServer implements ICentral {

	public static final int IO_ERROR_EXIT_STATUS = -1;
	public static final int REMOTE_ERROR_EXIT_STATUS = 1;

	private final List<String> categories = new ArrayList<>();
	private final Map<Integer, Ticket> tickets = new TreeMap<>();
	private final List<IMonitor> monitors = new LinkedList<>();

	private CentralServer() {
		readCategoriesFromConfigFile();
	}

	public static void main(String[] args) {
		CentralServer cs = new CentralServer();
		ICentral stub = null;
		try {
			stub = (ICentral) UnicastRemoteObject.exportObject(cs, 0);
		} catch (RemoteException e) {
			e.printStackTrace();
			System.exit(REMOTE_ERROR_EXIT_STATUS);
		}
		try {
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.rebind("central", stub);
		} catch (RemoteException e) {
			e.printStackTrace();
			System.exit(REMOTE_ERROR_EXIT_STATUS);
		}
		System.out.println("Server is running!");
		System.out.println();
	}

	@Override
	public Ticket getTicket(String category) {
		Ticket ticket = new Ticket();
		if (!categories.contains(category)) {
			System.out.println("Terminal called non-existing category, ticket will not be created!");
			return null;
		}
		ticket.category = category;
		ticket.status = 'i';
		synchronized (this) {
			Integer ticketID = IDGenerator.getNext(tickets.keySet());
			ticket.number = ticketID + 1;
			tickets.put(ticketID, ticket);
			updateMonitors();
		}
		System.out.println("Ticket No. " + ticket.number + " created and returned to the terminal");
		return ticket;
	}

	@Override
	public boolean register(IMonitor m) {
		synchronized (this) {
			monitors.add(m);
		}
		System.out.println("New monitor registered!");
		return true;
	}

	@Override
	public Ticket ticketQuery(Ticket ticketServed) {
		Ticket iTicket = null;
		synchronized (this) {
			if (ticketServed == null) {
				for (Ticket ticket : tickets.values()) {
					if (ticket.status == 'i') {
						iTicket = ticket;
						ticket.status = 'c';
						System.out.println("Customer with ticket No. " + iTicket.number + " under service...");
						break;
					}
				}
				updateMonitors();
			} else {
				tickets.get(ticketServed.number - 1).status = 's';
				System.out.println("Service of customer with ticket No. " + ticketServed.number + " ended!");
			}
		}
		return iTicket;
	}

	@Override
	public List<String> getCategories() {
		return categories;
	}

	private void readCategoriesFromConfigFile() {
		URL inURL = getClass().getResource("config.txt");
		String path = inURL.getPath().replaceAll("%20", " ");
		File configFile = new File(path);
		Scanner sc = null;
		try {
			sc = new Scanner(configFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(IO_ERROR_EXIT_STATUS);
		}
		String header = sc.nextLine();
		if (!header.matches("---Categories---")) {
			System.err.println("Wrong config file format, server will be stoped...");
			System.exit(IO_ERROR_EXIT_STATUS);
		}
		while (sc.hasNextLine()) {
			String category = sc.nextLine();
			categories.add(category);
		}
		sc.close();
	}

	private void updateMonitors() {
		List<IMonitor> offlineMonitors = new LinkedList<>();
		monitors.forEach(monitor -> {
			try {
				monitor.update(createMoniotorInfo());
			} catch (RemoteException e) {
				offlineMonitors.add(monitor);
			}
		});
		System.out.println("Monitors have been updated!");
		if (!offlineMonitors.isEmpty()) {
			offlineMonitors.forEach(offMon -> monitors.remove(offMon));
			System.out.println(offlineMonitors.size() + " offline monitors deleted!");
		}

	}

	private Info[] createMoniotorInfo() {
		Map<String, List<Ticket>> servingTickets = new LinkedHashMap<>();
		Map<String, Integer> nWaitingTickets = new LinkedHashMap<>();
		for (String category : categories) {
			servingTickets.put(category, new LinkedList<>());
			nWaitingTickets.put(category, 0);
		}
		for (Ticket ticket : tickets.values()) {
			if (ticket.status == 'i') {
				nWaitingTickets.put(ticket.category, nWaitingTickets.get(ticket.category) + 1);
			} else if (ticket.status == 'c') {
				servingTickets.get(ticket.category).add(ticket);
			}
		}
		Info[] info = new Info[categories.size()];
		Info catInfo = null;
		Iterator<Entry<String, List<Ticket>>> servingTicketsIt = servingTickets.entrySet().iterator();
		Iterator<Entry<String, Integer>> nWaitingTicketsIt = nWaitingTickets.entrySet().iterator();
		for (int i = 0; i != categories.size(); ++i) {
			catInfo = new Info();
			Entry<String, List<Ticket>> catTickets = servingTicketsIt.next();
			catInfo.category = catTickets.getKey();
			catInfo.serving = new Ticket[catTickets.getValue().size()];
			if (!catTickets.getValue().isEmpty()) {
				catInfo.serving = catTickets.getValue().toArray(catInfo.serving);
			}
			catInfo.waiting = nWaitingTicketsIt.next().getValue();
			info[i] = catInfo;
		}
		return info;
	}
}
