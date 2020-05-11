package main.entitys.items.behavior;

public final class Equip extends Behavior{
	private Behavior wielding;
	
	public Equip(Behavior wielding) {
		this.wielding = wielding;
	}
	public Behavior getWielding() {
		return wielding;
	}
	public void setWielding(Behavior wielding) {
		this.wielding = wielding;
	}
	@Override
	public boolean use() {
		return wielding.use();
	}
}
