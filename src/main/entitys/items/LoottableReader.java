package main.entitys.items;

import java.lang.reflect.Method;
import java.util.Scanner;

import main.Constants;
import main.entitys.Enemy;

public class LoottableReader {
	public LoottableReader() {
		try {
			final Scanner reader = new Scanner(getClass().getResourceAsStream(Constants.LOOTTABLE_SOURCE_FILE));
			reader.useDelimiter("\\( |\\) |\n");
			while (reader.hasNext()) {
				String next = reader.next().trim();
				if (!next.startsWith("#") && next.length() != 0) {
					next = next.replaceAll("\\(", "");
					addLoottable(next, reader);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void addLoottable(String classname, Scanner n) {
		final Loottable l = new Loottable();
		try {
			final Class<? extends Enemy> type = Class.forName(classname).asSubclass(Enemy.class);
			final Method m = type.getMethod("setLoottable", Loottable.class);
			m.invoke(null, l);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
