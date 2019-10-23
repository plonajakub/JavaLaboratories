package lab07_pop.monitor;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.table.AbstractTableModel;

import lab07_pop.Info;
import lab07_pop.Ticket;
import lab07_pop.central.CentralServer;
import lab07_pop.central.ICentral;

class Monitor implements IMonitor {

//	private static Integer freeID = 0;
//	private final Integer ID = freeID++;
	private Info[] info;
	private final MonitorMainPanel mmp;

	Monitor(MonitorMainPanel mmp) {
		this.mmp = mmp;
		this.info = new Info[0];
		bindToServer();
	}

	@Override
	public void update(Info[] i) {
		info = i;
		((AbstractTableModel) mmp.getMonitorTable().getModel()).fireTableDataChanged();
	}

	Info[] getInfo() {
		return info;
	}

	private void bindToServer() {
		IMonitor stub = null;
		try {
			stub = (IMonitor) UnicastRemoteObject.exportObject(this, 0);
		} catch (RemoteException e) {
			e.printStackTrace();
			System.exit(CentralServer.REMOTE_ERROR_EXIT_STATUS);
		}
		try {
			Registry registry = LocateRegistry.getRegistry("localhost");
//			registry.rebind("monitor id: " + ID, stub);
			ICentral central = (ICentral) registry.lookup("central");
			central.register(stub);
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
			System.exit(CentralServer.REMOTE_ERROR_EXIT_STATUS);
		}
		System.out.println("Monitor online!");
	}

	// For test purposes
	private void consoleUpdate(Info[] i) {
		System.out.println();
		System.out.println("---Monitor---");
		System.out.println();
		Info catInfo = null;
		for (int j = 0; j != i.length; ++j) {
			catInfo = i[j];
			System.out.println("Category: " + catInfo.category);
			System.out.println("Waiting: " + catInfo.waiting);
			System.out.println("Serving: ");
			if (catInfo.serving.length == 0)
				System.out.println("	--- no entries ---");
			else
				for (Ticket ticket : catInfo.serving) {
					System.out.println("	Ticket No. " + ticket.number);
				}
			System.out.println("------------------------");
			System.out.println();
		}
		System.out.println();
	}
}
