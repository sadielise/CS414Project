package a4.domain;

public class Street extends Property {
	private int houseCount = 0;
	private int hotelCount = 0;
	private int[] rent;
	private Neighborhood neighborhood;
	private String color;

	public Street(String name, int value, int[] rent, String color) {
		super(name, value, PropertyType.STREET);
		this.color = color;
		this.rent = rent;
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

	public String getColor() {
		return color;
	}

	public void addHouse() {
		if (hotelCount >= 1) {
		} else if (houseCount < 4)
			houseCount++;
		else if (hotelCount == 0) {
			houseCount = 0;
			hotelCount = 1;
		}
	}

	// returns rent based on total house and hotel count
	@Override
	public int getRent(int diceRoll) {
		if (houseCount > 0)
			return rent[houseCount];
		else if (hotelCount > 0)
			return rent[hotelCount * 5];
		else if (houseCount == 0 && hotelCount == 0 && neighborhood.getOwner() != null
				&& neighborhood.getOwner().equals(owner))
			return rent[0] * 2;
		else
			return rent[0];
	}

	// removes house from street
	public void removeHouse() {
		if (houseCount > 0)
			houseCount--;
		else if (hotelCount > 0 && houseCount == 0) {
			hotelCount = 0;
			houseCount = 4;
		}
	}

	// sets n as street's neighborhood
	public void addToNeighborhood(Neighborhood n) {
		neighborhood = n;
	}

	// returns 1 if street is developable, 0 otherwise
	@Override
	public int isDevelopable() {
		if (isMortgaged) {
			return 1;
		} else if (hotelCount == 1) {
			return -1;
		} else if (neighborhood.hasOwner()) {
			if (neighborhood.streetNeedsUnmortgaged()) {
				return -1;
			} else if (houseCount < neighborhood.getMaxNumHouses()) {
				return 1;
			} else {
				return -1;
			}
		} else {
			return -1;
		}
	}

	// returns 1 if street was unMortgaged, 2 if a house was bought, and -1 if
	// developing failed.
	@Override
	public int develop(Bank bank) {
		if (isMortgaged) {
			if (unmortgage(bank) == 1) {
				return 1;
			} else {
				return -1;
			}
		} else if (houseCount < 4 && bank.canRemoveHouse()) {
			if (neighborhood.addHouse(this)) {
				owner.transferMoney(bank, neighborhood.getHouseValue());
				bank.removeHouse();
				return 2;
			} else {
				return -1;
			}
		} else if (houseCount == 4 && bank.canRemoveHotel()) {
			if (neighborhood.addHouse(this)) {
				owner.transferMoney(bank, neighborhood.getHouseValue());
				bank.removeHotel();
				return 2;
			} else {
				return -1;
			}
		} else {
			return -1;
		}
	}

	// sells house or mortgages house if street can be undeveloped, -1 otherwise
	public int undevelop(Bank bank) {
		int undevelopingValue = -1;
		if (isMortgaged) {
			return undevelopingValue;
		} else if (hotelCount == 1 && bank.canAddHotel()) {
			if (neighborhood.removeHouse(this)) {
				undevelopingValue = sellHouse(bank);
				bank.addHotel();
				return undevelopingValue;
			} else {
				return undevelopingValue;
			}
		} else if (houseCount > 0) {
			if (neighborhood.removeHouse(this)) {
				undevelopingValue = sellHouse(bank);
				bank.addHouse();
				return undevelopingValue;
			} else {
				return undevelopingValue;
			}
		} else if (neighborhood.numHousesEqual() && houseCount == 0 && hotelCount == 0) {
			return mortgage(bank);
			
		} else {
			return -1;
		}
	}

	// returns half the value of the street if bank has enough funds, balance of
	// bank otherwise
	private int sellHouse(Bank bank) {
		return bank.transferMoney(owner, neighborhood.getHouseValue() / 2);
	}

	public String toString() {
		return super.toString() + " \nRent: " + getRent(0) + " Number of Houses: " + houseCount + " Number of Hotels: "
				+ hotelCount;
	}
}
