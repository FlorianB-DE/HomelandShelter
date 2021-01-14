package main.entities.items;

import main.Constants;
import main.entities.Enemy;

import java.lang.reflect.Method;
import java.util.Scanner;

public class LootTableReader {
	public LootTableReader() {
		try {
			final Scanner reader = new Scanner(getClass().getResourceAsStream(Constants.LOOTTABLE_SOURCE_FILE));
			reader.useDelimiter("\\( |\\) |\n");
			while (reader.hasNext()) {
				String next = reader.next().trim();
				if (!next.startsWith("#") && next.length() != 0) {
					next = next.replaceAll("\\(", "");
					addLootTable(next, reader);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void addLootTable(String classname, Scanner n) {
		final LootTable l = new LootTable();
		try {
			final Class<? extends Enemy> type = Class.forName(classname).asSubclass(Enemy.class);
			final Method m = type.getMethod("setLootTable", LootTable.class);
			m.invoke(null, l);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
