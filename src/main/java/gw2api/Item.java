package gw2api;

public class Item {
	private final String id;
	private final String name;
	private final int maxOfferUnitPrice;
	private final int minSaleUnitPrice;
	private final int offerAvailability;
	private final int saleAvailability;

	public Item(String id, String name, int maxOfferUnitPrice,
			int minSaleUnitPrice, int offerAvailability, int saleAvailability) {
		super();
		this.id = id;
		this.name = name;
		this.maxOfferUnitPrice = maxOfferUnitPrice;
		this.minSaleUnitPrice = minSaleUnitPrice;
		this.offerAvailability = offerAvailability;
		this.saleAvailability = saleAvailability;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getMaxOfferUnitPrice() {
		return maxOfferUnitPrice;
	}

	public int getMinSaleUnitPrice() {
		return minSaleUnitPrice;
	}

	public int getOfferAvailability() {
		return offerAvailability;
	}

	public int getSaleAvailability() {
		return saleAvailability;
	}

	public double getNetPrice() {
		return minSaleUnitPrice * 0.85;
	}

	public double getMargin() {
		return getNetPrice() - maxOfferUnitPrice;
	}

	public int getMarginPercentage() {
		return ((int) (getMargin() / getNetPrice()) * 100);
	}

}
