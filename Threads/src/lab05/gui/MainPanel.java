package lab05.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import lab05.logic.Cook;
import lab05.logic.Mixer;
import lab05.logic.Resource;
import lab05.logic.Supplier;

public class MainPanel extends JPanel {

	private static final long serialVersionUID = -5263021146464721955L;

	private final JPanel mixerPanel;
	private final List<JPanel> resourcePanels = new LinkedList<>();
	private final List<JPanel> cookPanels = new LinkedList<>();
	private final List<JPanel> supplierPanels = new LinkedList<>();

	MainPanel(Mixer mixer) {
		this.mixerPanel = mixer.getStatePanel();
		for (Resource resource : mixer.getResources()) {
			resourcePanels.add(resource.getStatePanel());
		}
		for (Cook cook : mixer.getCooks()) {
			cookPanels.add(cook.getStatePanel());
		}
		for (Supplier supplier : mixer.getSuppliers()) {
			supplierPanels.add(supplier.getStatePanel());
		}

		this.setLayout(new GridLayout(1, 4));
		this.setBackground(Color.gray);
		prepareComponents();
	}

	private void prepareComponents() {

		JPanel resourcePanel = new JPanel(new GridLayout(resourcePanels.size(), 1));
		resourcePanel.setBorder(BorderFactory.createTitledBorder("Resources"));
		for (JPanel rPanel : resourcePanels) {
			resourcePanel.add(rPanel);
		}
		this.add(resourcePanel);

		JPanel cookPanel = new JPanel(new GridLayout(cookPanels.size(), 1));
		cookPanel.setBorder(BorderFactory.createTitledBorder("Cooks"));
		for (JPanel cPanel : cookPanels) {
			cookPanel.add(cPanel);
		}
		this.add(cookPanel);

		JPanel supplierPanel = new JPanel(new GridLayout(supplierPanels.size(), 1));
		supplierPanel.setBorder(BorderFactory.createTitledBorder("Suppliers"));
		for (JPanel sPanel : supplierPanels) {
			supplierPanel.add(sPanel);
		}
		this.add(supplierPanel);

		this.add(mixerPanel);

	}

}
