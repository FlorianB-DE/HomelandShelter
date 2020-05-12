package main.entitys.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import main.Constants;
import utils.exceptions.ItemCreationFailedException;

public final class ItemReader {

	public ItemReader() {
		try {
			/*
			 * Scanner reader = new Scanner(new
			 * File(getClass().getResource(Constants.ITEM_SOURCE_FILE).getPath()));
			 */
			/*
			 * DOES NOT WORK FOR EXPORTING TO JAR if having trouble with reading .txt enable
			 * this line and disable the next
			 */
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
		List<Attributes<?>> attributes = new ArrayList<>();
		attributes.add(new Attributes<String>("name", type));
		while (s.hasNext()) {
			String next = s.next().trim();
			if (next.indexOf(String.valueOf(')')) != -1) {
				new ItemBlueprint(attributes);
				break;
			} else if (!next.startsWith("#") && next.length() != 0) {
				String[] split = next.split(": ");
				try {
					if (split[1].indexOf(String.valueOf('.')) != -1)
						attributes.add(new Attributes<Float>(split[0], Float.parseFloat(split[1])));
					else if (split[1].indexOf("\"") != -1)
						attributes.add(new Attributes<String>(split[0], split[1].replaceAll("\"", "")));
					else
						attributes.add(new Attributes<Integer>(split[0], Integer.parseInt(split[1])));
				} catch (NumberFormatException e) {
					throw new ItemCreationFailedException();
				}
			}
		}
	}
}
