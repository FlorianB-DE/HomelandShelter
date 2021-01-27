package main.entities.items;

import main.Constants;
import utils.exceptions.ItemCreationFailedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class ItemReader {

	public ItemReader() {
		try {
			final Scanner reader = new Scanner(getClass().getResourceAsStream(Constants.ITEM_SOURCE_FILE));
			reader.useDelimiter("\\( |\\) |\n");
			while (reader.hasNext()) {
				String next = reader.next().trim();
				if (!next.startsWith("#") && next.length() != 0) {
					next = next.replaceAll("\\(", "");
					addItemOfType(next, reader);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ItemCreationFailedException("Something went wrong scanning \"Items.txt\"");
		}
	}

	private void addItemOfType(String type, Scanner s) throws ItemCreationFailedException {
		List<Attribute<?>> attributes = new ArrayList<>();
		attributes.add(new Attribute<>("name", type));
		while (s.hasNext()) {
			String next = s.next().trim();
			if (next.contains(String.valueOf(')'))) {
				new ItemBlueprint(attributes);
				break;
			} else if (!next.startsWith("#") && next.length() != 0) {
				String[] split = next.split(": ");
				try {
					if (split[1].contains(String.valueOf('.')))
						attributes.add(new Attribute<>(split[0], Float.parseFloat(split[1])));
					else if (split[1].contains("\""))
						attributes.add(new Attribute<>(split[0], split[1].replaceAll("\"", "")));
					else
						attributes.add(new Attribute<>(split[0], Integer.parseInt(split[1])));
				} catch (NumberFormatException e) {
					throw new ItemCreationFailedException();
				}
			}
		}
	}
}
