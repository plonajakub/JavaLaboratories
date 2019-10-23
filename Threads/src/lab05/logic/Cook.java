package lab05.logic;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JPanel;

import lab05.gui.StateInfo;
import lab05.gui.component_sets.CookPanel;

public class Cook extends Thread implements StateInfo {

	private final Mixer mixer;
	private final Random rand = new Random();	
	private final CookPanel infoPanel = new CookPanel();

	public Cook(String name, Mixer mixer) {
		super(name);
		this.mixer = mixer;
		this.infoPanel.setNameLabelText(this.getName());
		this.infoPanel.setStateLabelText("preparing to work...");
	}

	public static List<Cook> getInstances(int quantity, Mixer mixer) {
		List<Cook> cooks = new ArrayList<>();
		Integer counter = 1;
		while (cooks.size() < quantity) {
			cooks.add(new Cook("Cook No. " + (counter++).toString(), mixer));
		}
		return cooks;
	}

	@Override
	public void run() {
		Map<Integer, Integer> neededResouces;
		String fetchLog;
		while (true) {
			infoPanel.setStateLabelText("preparing resources list...");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			neededResouces = getNeededResourcesList();
			
			infoPanel.setStateLabelText("fetching resources (" + neededResouces.size() + ")...");
			fetchLog = mixer.fetchResources(neededResouces);
			System.out.println(getName() + " collected resources: " + fetchLog);
			
			System.out.println(getName() + " is preparing food");
			infoPanel.setStateLabelText("preparing food...");
			try {
				Thread.sleep(50000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private Map<Integer, Integer> getNeededResourcesList() {
		Map<Integer, Integer> neededResources = new LinkedHashMap<>();
		int neededResoucesQuantity = rand.nextInt(mixer.getResourceQuantity() + 1);
		Integer resourceID;
		for (int i = 0; i != neededResoucesQuantity; ++i) {
			resourceID = rand.nextInt(mixer.getResourceQuantity());
			while (neededResources.keySet().contains(resourceID)) {
				resourceID = rand.nextInt(mixer.getResourceQuantity());
			}
			neededResources.put(resourceID, 1 + rand.nextInt(mixer.getResource(resourceID).getMaxLevel()));
		}
		return neededResources;
	}

	@Override
	public JPanel getStatePanel() {
		return infoPanel;
	}
}
