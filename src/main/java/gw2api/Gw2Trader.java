package gw2api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import au.com.bytecode.opencsv.CSVParser;

public class Gw2Trader {

	private void doIt() {
		// Gw2Api gw2Api = new Gw2Api();
		List<Item> items = new ArrayList<>();
		CSVParser parser = new CSVParser();
		try {
			URL url = new URL(
					"http://www.gw2spidy.com/api/v0.9/csv/all-items/all");
			try (BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream()))) {
				in.readLine();
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					String[] strings = parser.parseLine(inputLine);
					Item item = new Item(strings[0], strings[1],
							Integer.parseInt(strings[8]),
							Integer.parseInt(strings[9]),
							Integer.parseInt(strings[10]),
							Integer.parseInt(strings[11]));
					if (item.getOfferAvailability() > 10000
							&& item.getSaleAvailability() > 10000
							&& item.getMargin() > 1) {
						items.add(item);
						System.out.print('.');
					}
				}
				System.out.println();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out
				.println("id, name, gw2-spidy, max. kaufgebot, min. verkaufsangebot, gewinn");
		Collections.sort(items, new Comparator<Item>() {
			@Override
			public int compare(Item o1, Item o2) {
				double m1 = o1.getMargin();
				double m2 = o2.getMargin();
				if (m2 > m1) {
					return 1;
				} else if (m2 == m1) {
					return 0;
				} else {
					return -1;
				}
			}
		});

		for (Item item : items) {
			System.out.print(item.getId() + ", " + item.getName() + ", ");
			System.out.print("http://www.gw2spidy.com/item/" + item.getId()
					+ ", ");
			// JSONObject itemDetails = (JSONObject) gw2Api.getItemDetails(
			// item.getId(), "de");
			// System.out.print(itemDetails.get("name") + ", ");
			System.out.println(item.getMaxOfferUnitPrice() + ", "
					+ item.getMinSaleUnitPrice() + ", " + item.getMargin());
		}

	}

	public static void main(String[] args) {
		new Gw2Trader().doIt();
	}

}
