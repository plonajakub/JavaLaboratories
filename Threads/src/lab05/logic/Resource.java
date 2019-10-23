package lab05.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

import lab05.gui.StateInfo;
import lab05.gui.component_sets.ResourcePanel;

public class Resource implements StateInfo {

	public static final int DEFAULT_MAX_LEVEL = 100;
	
	private final String name;
	private final int maxLevel;
	private int level;
	private boolean incomingDeliver = false;
	private final ResourcePanel infoPanel = new ResourcePanel();

	public Resource(String name, int initialLevel, int maxLevel) {
		this.name = name;
		this.level = initialLevel;
		this.maxLevel = maxLevel;
		this.infoPanel.setNameLabelText(this.getName());
		this.infoPanel.setLevelLabelText(level + "/" + maxLevel);
		this.infoPanel.setStateLabelText("resource initialized");
		this.infoPanel.setCurrentUser("-");
	}
	
	public static List<Resource> getInstances(int quantity) {
		Random rand = new Random();
		List<Resource> resources = new ArrayList<>();
		int resourceMaxLevel = 1 + rand.nextInt(Resource.DEFAULT_MAX_LEVEL);
		int resourceLevel = resourceMaxLevel + 1;
		while (resourceLevel > resourceMaxLevel) {
			resourceLevel = 1 + rand.nextInt(Resource.DEFAULT_MAX_LEVEL);
		}
		Integer counter = 1;
		while (resources.size() < quantity) {
			resources.add(new Resource("Resource No. " + (counter++).toString(), resourceLevel, resourceMaxLevel));
		}
		return resources;
	}

	public synchronized int addResource(int ammount) {
		infoPanel.setCurrentUser(Thread.currentThread().getName());
		infoPanel.setStateLabelText("resources are being added(" + ammount + ")...");
		System.out.println(Thread.currentThread().getName() + " is adding " +  name + "(" + ammount + ")");
		if (level + ammount > maxLevel) {
			ammount = maxLevel - level;
		}
		level += ammount;
		try {
			Thread.sleep(1000 + ammount * 25);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		infoPanel.setLevelLabelText(level + "/" + maxLevel);
		infoPanel.setStateLabelText("idle");
		infoPanel.setCurrentUser("-");
		System.out.println(Thread.currentThread().getName() + " added " +  name + "(" + ammount + ")");
		setDeliverIncoming(false);
		return ammount;
	}

	public synchronized int fetchResource(int ammount) {
		infoPanel.setCurrentUser(Thread.currentThread().getName());
		infoPanel.setStateLabelText("resources are being fetched(" + ammount + ")...");
		if (ammount > level) {
			infoPanel.setStateLabelText("failed to fetch resources");
			infoPanel.setCurrentUser("-");
			throw new IllegalArgumentException("There is not enought resource to fetch supplied ammount");
		}
		System.out.println(Thread.currentThread().getName() + " is fetching " +  name + "(" + ammount + ")");
		level -= ammount;
		try {
			Thread.sleep(1000 + ammount * 10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		infoPanel.setLevelLabelText(level + "/" + maxLevel);
		infoPanel.setStateLabelText("idle");
		infoPanel.setCurrentUser("-");
		System.out.println(Thread.currentThread().getName() + " fetched " +  name + "(" + ammount + ")");
		return ammount;
	}

	public String getName() {
		return name;
	}
	
	public int getMaxLevel() {
		return maxLevel;
	}
	
	public synchronized boolean isFull() {
		return level == maxLevel;
	}
	
	public synchronized boolean isDeliverIncoming() {
		return incomingDeliver;
	}

	public synchronized void setDeliverIncoming(boolean incomingAddition) {
		this.incomingDeliver = incomingAddition;
	}

	public int getLevel() {
		return level;
	}

	@Override
	public JPanel getStatePanel() {
		return infoPanel;
	}
}