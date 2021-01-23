package main.entities;

public interface Fightable {

	float attack();

	void die();
	
	void heal(float amount);

	void hit(float damage);

	void trueHit(float damage);
}
