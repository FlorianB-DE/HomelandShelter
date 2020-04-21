package main.entitiys.items;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import utils.exceptions.ItemCreationFailedException;

public class ItemReader {

	public static final String itemSource = "Items.txt";

	public ItemReader() {
		try {
			Scanner reader = new Scanner(new File(getClass().getResource(itemSource).getPath()));
			reader.useDelimiter("\\( |\\) |\n");
			while (reader.hasNext()) {
				try {
					String next = reader.next().trim();
					next = next.replaceAll("\\(", "");
					addItemOfType(next, reader);
				} catch (ItemCreationFailedException e) {
					throw new FileNotFoundException();
				}
			}
		} catch (FileNotFoundException e) {
			System.exit(-1);
		}
	}

	@SuppressWarnings("rawtypes")
	private void addItemOfType(String type, Scanner s) throws ItemCreationFailedException {
		List<Attributes> attributes = new ArrayList<>();
		attributes.add(new Attributes<String>("name", type));
		while (s.hasNext()) {
			String next = s.next().trim();
			if (next.indexOf(String.valueOf(')')) != -1) {
				new ItemBlueprint(attributes);
				break;
			} else {
				String[] split = next.split(": ");
				try {
					if (split[1].indexOf(String.valueOf('.')) != -1)
						attributes.add(new Attributes<Float>(split[0], Float.parseFloat(split[1])));
					else if(split[1].indexOf("\"") != -1)
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
