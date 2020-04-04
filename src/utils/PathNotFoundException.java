package utils;

public class PathNotFoundException extends RuntimeException {

	public PathNotFoundException(){
		super("Could not generate Path!");
	}

	public PathNotFoundException(String s){
		super(s);
	}
}
