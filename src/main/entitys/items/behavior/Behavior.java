package main.entitys.items.behavior;

import main.UI.Gameboard;
import main.entitys.Player;
import main.entitys.items.Item;

public abstract class Behavior {

	private final Item owner;

	public Behavior(Item owner) {
		this.owner = owner;
	}

	public abstract void use();

	public Item getOwner() {
		return owner;
	}
	
	protected Player getMainChar() {
		return Gameboard.getCurrentInstance().getPlayer();
	}
}
