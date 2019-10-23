package lab05.gui.component_sets;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class MixerPanel extends JPanel {

	private static final long serialVersionUID = -3125663632811623972L;
	
	private final MixerInfoPanel infoPanel;
	private final List<MixerSlotPanel> slotPanels = new ArrayList<>();
	
	public MixerPanel(int mixerCapacity) {
		this.infoPanel = new MixerInfoPanel();
		for (int i = 0; i != mixerCapacity; ++i) {
			slotPanels.add(new MixerSlotPanel(i));
		}
		
		this.setLayout(new GridLayout(mixerCapacity + 1, 1));
		this.setBorder(BorderFactory.createTitledBorder("Mixer"));
		prepareComponenets();
	}

	private void prepareComponenets() {
		this.add(infoPanel);
		
		for (JPanel slotPanel : slotPanels) {
			this.add(slotPanel);
		}
	}
	
	public MixerInfoPanel getInfoPanel() {
		return infoPanel;
	}

	public MixerSlotPanel getSlotPanel(int index) {
		return slotPanels.get(index);
	}
}
