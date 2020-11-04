package main.entitys;

public interface Fightable {

	abstract float attack();

	abstract void die();
	
	abstract void heal(float amount);

	abstract void hit(float damage);

	abstract void trueHit(float damage);
}
