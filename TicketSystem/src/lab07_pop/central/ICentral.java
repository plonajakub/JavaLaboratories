package lab07_pop.central;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import lab07_pop.Ticket;
import lab07_pop.monitor.IMonitor;

public interface ICentral extends Remote {

	public Ticket getTicket(String category) throws RemoteException;

	public boolean register(IMonitor m) throws RemoteException;

	public Ticket ticketQuery(Ticket ticketServed) throws RemoteException;

	public List<String> getCategories() throws RemoteException;
}
