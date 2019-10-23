package lab05.logic;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JPanel;

import lab05.gui.StateInfo;
import lab05.gui.component_sets.MixerInfoPanel;
import lab05.gui.component_sets.MixerPanel;
import lab05.gui.component_sets.MixerSlotPanel;

public class Mixer implements StateInfo {

	private int resourcesQuantity;
	private int mixerCapacity;

	private List<Cook> cooks;
	private List<Supplier> suppliers;
	private List<Resource> resources;

	private int fetchQueries = 0;
	private int delayedQueries = 0;
	private List<Integer> runningQueriesID = new LinkedList<>();
	private final MixerPanel fullInfoPanel;

	// default Mixer
	public Mixer() {
		this(5);
	}

	public Mixer(int mixerCapacity) {
		this.mixerCapacity = mixerCapacity;
		this.fullInfoPanel = new MixerPanel(mixerCapacity);

		MixerInfoPanel infoPanel = fullInfoPanel.getInfoPanel();
		infoPanel.setActiveQueriesText(fetchQueries + "/" + mixerCapacity);
		infoPanel.setDelayedQueriesText(Integer.toString(delayedQueries));
	}

	public void setMixerConfiguration(List<Resource> resources, List<Supplier> suppliers, List<Cook> cooks) {
		this.resources = resources;
		this.resourcesQuantity = resources.size();
		this.suppliers = suppliers;
		this.cooks = cooks;
	}

	public void startCooking() {
		for (Supplier supplier : suppliers) {
			supplier.start();
		}
		for (Cook cook : cooks) {
			cook.start();
		}
	}

	public String fetchResources(Map<Integer, Integer> requestedResources) {
		MixerInfoPanel infoPanel = fullInfoPanel.getInfoPanel();
		synchronized (this) {
			++fetchQueries;
			infoPanel.setActiveQueriesText(fetchQueries + "/" + mixerCapacity);
			while (fetchQueries > mixerCapacity) {
				--fetchQueries;
				infoPanel.setActiveQueriesText(fetchQueries + "/" + mixerCapacity);
				try {
					System.out.println(Thread.currentThread().getName() + " is wating (to many queries)");
					++delayedQueries;
					infoPanel.setDelayedQueriesText(Integer.toString(delayedQueries));
					wait();
					--delayedQueries;
					infoPanel.setDelayedQueriesText(Integer.toString(delayedQueries));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				++fetchQueries;
				infoPanel.setActiveQueriesText(fetchQueries + "/" + mixerCapacity);
			}
		}
		Integer queryID;
		synchronized (runningQueriesID) {
			queryID = IDGenerator.getNext(runningQueriesID);
			runningQueriesID.add(queryID);
		}
		MixerSlotPanel slotInfo = fullInfoPanel.getSlotPanel(queryID);
		slotInfo.setCurrentSlotUser(Thread.currentThread().getName());
		slotInfo.setStateLabelText("preparing to fetch requested resources...");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}

		System.out.println("Query No. " + queryID + " is being executed");
		StringBuilder fetchingHistory = new StringBuilder();
		Integer fetchedResourcesQuantity;
		int counter = 1;
		for (Entry<Integer, Integer> resourceData : requestedResources.entrySet()) {
			slotInfo.setStateLabelText("fetching resources " + counter++ + "/" + requestedResources.size());
			do {
				try {
					fetchedResourcesQuantity = resources.get(resourceData.getKey())
							.fetchResource(resourceData.getValue());
				} catch (IllegalArgumentException e) {
					slotInfo.setStateLabelText(
							"waiting for delivery (missing: " + resources.get(resourceData.getKey()).getName() + ")");
					System.out.println(Thread.currentThread().getName() + " is waiting for resource: "
							+ resources.get(resourceData.getKey()).getName());
					fetchedResourcesQuantity = 0;
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				if (fetchedResourcesQuantity != 0) {
					System.out.println(Thread.currentThread().getName() + " recived resource after wait time: "
							+ resources.get(resourceData.getKey()).getName());
				}
			} while (fetchedResourcesQuantity == 0);
			fetchingHistory
					.append(resources.get(resourceData.getKey()).getName() + "(" + fetchedResourcesQuantity + ") ");
		}
		synchronized (runningQueriesID) {
			runningQueriesID.remove(queryID);
		}
		slotInfo.setCurrentSlotUser("-");
		slotInfo.setStateLabelText("slot free");
		synchronized (this) {
			--fetchQueries;
			infoPanel.setActiveQueriesText(fetchQueries + "/" + mixerCapacity);
			notify();
		}
		System.out.println("Query No. " + queryID + " executed");
		return fetchingHistory.toString();
	}

	public List<Cook> getCooks() {
		return cooks;
	}

	public List<Supplier> getSuppliers() {
		return suppliers;
	}

	public List<Resource> getResources() {
		return resources;
	}

	public Resource getResource(int index) {
		return resources.get(index);
	}

	public int getMixerCapacity() {
		return mixerCapacity;
	}

	public int getResourceQuantity() {
		return resourcesQuantity;
	}

	@Override
	public JPanel getStatePanel() {
		return fullInfoPanel;
	}
}
