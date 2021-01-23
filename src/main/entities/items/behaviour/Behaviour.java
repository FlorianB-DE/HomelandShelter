package main.entities.items.behaviour;

import main.ui.GameBoard;
import main.entities.Player;
import main.entities.items.Item;

public abstract class Behaviour {

	private final Item owner;

	public Behaviour(Item owner) {
		this.owner = owner;
	}

	public abstract void use();

	public Item getOwner() {
		return owner;
	}
	
	protected Player getMainChar() {
		return GameBoard.getCurrentInstance().getPlayer();
	}
}
