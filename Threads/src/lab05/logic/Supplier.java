package lab05.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

import lab05.gui.StateInfo;
import lab05.gui.component_sets.SupplierPanel;

public class Supplier extends Thread implements StateInfo {

	private final Mixer mixer;
	private final Random rand = new Random();
	private final SupplierPanel infoPanel = new SupplierPanel();

	public Supplier(String name, Mixer mixer) {
		super(name);
		this.mixer = mixer;
		this.infoPanel.setNameLabelText(this.getName());
		this.infoPanel.setStateLabelText("preparing to work...");
	}

	public static List<Supplier> getInstances(int quantity, Mixer mixer) {
		List<Supplier> suppliers = new ArrayList<>();
		Integer counter = 1;
		while (suppliers.size() < quantity) {
			suppliers.add(new Supplier("Supplier No. " + (counter++).toString(), mixer));
		}
		return suppliers;
	}

	@Override
	public void run() {
		Resource resource;
		int resourceQuantity;
		while (true) {
			infoPanel.setStateLabelText("buying resources...");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			resource = chooseResource();
			resourceQuantity = 1 + rand.nextInt(resource.getMaxLevel());

			infoPanel.setStateLabelText("delivering " + resource.getName() + " (" + resourceQuantity + ")...");
			resource.addResource(resourceQuantity);
			System.out.println(Thread.currentThread().getName() + " has a break");
			infoPanel.setStateLabelText("resting...");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private Resource chooseResource() {
		while (true) {
			Resource resource = mixer.getResource(rand.nextInt(mixer.getResourceQuantity()));
			if (!resource.isFull() && !resource.isDeliverIncoming()) {
				resource.setDeliverIncoming(true);
				return resource;
			}
		}

	}

	@Override
	public JPanel getStatePanel() {
		return infoPanel;
	}
}
