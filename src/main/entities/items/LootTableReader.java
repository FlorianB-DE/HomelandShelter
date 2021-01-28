package main.entities.items;

import main.Constants;

import java.util.Scanner;

public class LootTableReader {
    public LootTableReader() {
        try {
            final Scanner reader = new Scanner(getClass().getResourceAsStream(Constants.LOOT_TABLE_SOURCE_FILE));
            reader.useDelimiter("\\( |\\) |\n");
            while (reader.hasNext()) {
                String next = reader.next().trim();
                if (!next.startsWith("#") && next.length() != 0) {
                    next = next.replaceAll("\\(", "");
                    createLootTable(next, reader);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createLootTable(String className, Scanner n) {
        final LootTable l = new LootTable(className);
        while (n.hasNext()) {
            String next = n.next().trim();
            if (next.startsWith(String.valueOf(')'))) return;
            else if (!next.startsWith(String.valueOf('#')) && next.length() != 0 && next.contains(String.valueOf(':'))) {
                final String[] split = next.split(":");
                final int itemIndex = ItemBlueprint.items.indexOf(new ItemBlueprint(split[0].trim()));
                float chance = 50F;
                try {
                    chance = Float.parseFloat(split[1].trim());
                } catch (Exception ignore) {
                }
                l.addBlueprint(ItemBlueprint.items.get(itemIndex), chance);
            }
        }
    }
}
