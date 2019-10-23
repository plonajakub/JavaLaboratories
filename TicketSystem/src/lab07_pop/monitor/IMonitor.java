package lab07_pop.monitor;

import java.rmi.Remote;
import java.rmi.RemoteException;

import lab07_pop.Info;

public interface IMonitor extends Remote {

	public void update(Info[] i) throws RemoteException;
}
