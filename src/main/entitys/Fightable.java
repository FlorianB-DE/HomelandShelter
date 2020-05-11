package main.entitys;

public interface Fightable {

	abstract float attack();

	public abstract void die();

	abstract void hit(float damage);
}
