package a4.domain;

public class Street extends Property {
	int houseCount = 0;
	int hotelCount = 0;
	Neighborhood neighborhood;
	String color;

	public Street(String name, int value, String color) {
		super(name, value);
		this.color = color;
	}

	public int getHouseCount() {
		return houseCount;
	}

	public void setHouseCount(int houseCount) {
		this.houseCount = houseCount;
	}

	public int getHotelCount() {
		return hotelCount;
	}

	public void setHotelCount(int hotelCount) {
		this.hotelCount = hotelCount;
	}

	public Neighborhood getNeighborhood() {
		return neighborhood;
	}

	public void addHouse() {
		if (houseCount < 4)
			houseCount++;
		else if (hotelCount == 0) {
			houseCount = 0;
			hotelCount = 1;
		}
	}

	public void removeHouse() {
		if (houseCount > 0)
			houseCount--;
		else if (hotelCount > 0 && houseCount == 0) {
			hotelCount = 0;
			houseCount = 4;
		}
	}

	public String getColor() {
		return color;
	}

	public void setColor(String newColor) {
		this.color = newColor;
	}

	public void addToNeighborhood(Neighborhood n) {
		neighborhood = n;
	}

	public int getRent() {
		int rent = value;
		if (0 == houseCount && 0 == hotelCount && owner == neighborhood.belongsTo()) {
			rent = rent * 2;
		}
		return value;
		// TODO: this needs unit tests still
	}
}
