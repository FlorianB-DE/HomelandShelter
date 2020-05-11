package main.entitys;

public interface Fightable {

	abstract float attack();

	abstract void die();

	abstract void hit(float damage);
}
