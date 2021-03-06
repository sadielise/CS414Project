package a4.domain;

import java.util.ArrayList;
import java.util.List;

public class Board {
	private List<BoardSpace> spaces = new ArrayList<BoardSpace>();
	private ArrayList<Neighborhood> neighborhoods;
	private int jailLocation;

	public Board() {
		createNeighborhoods();
		createBoardBottom();
		createBoardLeft();
		createBoardTop();
		createBoardRight();
	}
	
	public List<BoardSpace> getSpaces() {
		return spaces;
	}

	public void addSpace(BoardSpace spaceToAdd) {
		spaces.add(spaceToAdd);
	}
	
	public int getJailLocation(){
		return jailLocation;
	}
	
	// creates all neighborhoods and adds them to the neighborhoods list
	private void createNeighborhoods(){
		neighborhoods = new ArrayList<Neighborhood>();
		neighborhoods.add(new Neighborhood("Brown", 50));
		neighborhoods.add(new Neighborhood("SkyBlue", 50));
		neighborhoods.add(new Neighborhood("Pink", 100));
		neighborhoods.add(new Neighborhood("Orange", 100));
		neighborhoods.add(new Neighborhood("Red", 150));
		neighborhoods.add(new Neighborhood("Yellow", 150));
		neighborhoods.add(new Neighborhood("Green", 200));
		neighborhoods.add(new Neighborhood("Blue", 200));
	}
	
	// creates bottom row board spaces
	private void createBoardBottom(){
		OpenSpace space1 = (OpenSpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.OPEN);
		space1.setName("Go");
		spaces.add(space1);
		
		PropertySpace space2 = (PropertySpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.PROPERTY);
		space2.setPropertyInfo(PropertyType.STREET, "Mediterranean Avenue", 60, new int[] { 2, 10, 30, 90, 160, 250 }, "Brown");
		this.addToNeighborhood(space2);
		spaces.add(space2);
		
		CommunityChestSpace space3 = (CommunityChestSpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.COMMUNITYCHEST);
		spaces.add(space3);
		
		PropertySpace space4 = (PropertySpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.PROPERTY);
		space4.setPropertyInfo(PropertyType.STREET, "Baltic Avenue", 60, new int[] { 4, 20, 60, 180, 320, 450 }, "Brown");
		this.addToNeighborhood(space4);
		spaces.add(space4);
		
		IncomeTaxSpace space5 = (IncomeTaxSpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.INCOMETAX);
		spaces.add(space5);
		
		PropertySpace space6 = (PropertySpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.PROPERTY);
		space6.setPropertyInfo(PropertyType.RAILROAD, "Reading Railroad", 200, new int[] {}, "");
		spaces.add(space6);
		
		PropertySpace space7 = (PropertySpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.PROPERTY);
		space7.setPropertyInfo(PropertyType.STREET, "Oriental Avenue", 100, new int[] { 6, 30, 90, 270, 400, 550 }, "SkyBlue");
		this.addToNeighborhood(space7);
		spaces.add(space7);
		
		ChanceSpace space8 = (ChanceSpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.CHANCE);
		spaces.add(space8);
		
		PropertySpace space9 = (PropertySpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.PROPERTY);
		space9.setPropertyInfo(PropertyType.STREET, "Vermont Avenue", 100, new int[] { 6, 30, 90, 270, 400, 550 }, "SkyBlue");
		this.addToNeighborhood(space9);
		spaces.add(space9);
		
		PropertySpace space10 = (PropertySpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.PROPERTY);
		space10.setPropertyInfo(PropertyType.STREET, "Connecticut Avenue", 120, new int[] { 8, 40, 100, 300, 450 }, "SkyBlue");
		this.addToNeighborhood(space10);
		spaces.add(space10);
	}

	// creates left row board spaces
	private void createBoardLeft(){
		JailSpace space11 = (JailSpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.JAIL);
		spaces.add(space11);
		jailLocation = spaces.size()-1;
		
		PropertySpace space12 = (PropertySpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.PROPERTY);
		space12.setPropertyInfo(PropertyType.STREET, "St. Charles Place", 140,	new int[] { 10, 50, 150, 450, 625, 750 }, "Pink");
		this.addToNeighborhood(space12);
		spaces.add(space12);
		
		PropertySpace space13 = (PropertySpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.PROPERTY);
		space13.setPropertyInfo(PropertyType.UTILITY, "Electric Company", 150, new int[] {}, "");
		spaces.add(space13);
		
		PropertySpace space14 = (PropertySpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.PROPERTY);
		space14.setPropertyInfo(PropertyType.STREET, "States Avenue", 140, new int[] { 10, 50, 150, 450, 625, 750 }, "Pink");
		spaces.add(space14);
		this.addToNeighborhood(space14);
		
		PropertySpace space15 = (PropertySpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.PROPERTY);
		space15.setPropertyInfo(PropertyType.STREET, "Virginia Avenue", 160, new int[] { 12, 60, 180, 500, 700, 900 }, "Pink");
		spaces.add(space15);
		this.addToNeighborhood(space15);

		PropertySpace space16 = (PropertySpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.PROPERTY);
		space16.setPropertyInfo(PropertyType.RAILROAD, "Pennsylvania Railroad", 200, new int[] {}, "");
		spaces.add(space16);
		
		PropertySpace space17 = (PropertySpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.PROPERTY);
		space17.setPropertyInfo(PropertyType.STREET, "St. James Place", 180, new int[] { 14, 70, 200, 550, 750, 950 }, "Orange");
		spaces.add(space17);
		this.addToNeighborhood(space17);
		
		CommunityChestSpace space18 = (CommunityChestSpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.COMMUNITYCHEST);
		spaces.add(space18);
		
		PropertySpace space19 = (PropertySpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.PROPERTY);
		space19.setPropertyInfo(PropertyType.STREET, "Tennessee Avenue", 180, new int[] { 14, 200, 550, 750, 950 }, "Orange");
		spaces.add(space19);
		this.addToNeighborhood(space19);
		
		PropertySpace space20 = (PropertySpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.PROPERTY);
		space20.setPropertyInfo(PropertyType.STREET, "New York Avenue", 200, new int[] { 16, 80, 220, 600, 800, 1000 }, "Orange");
		this.addToNeighborhood(space20);
		spaces.add(space20);
	}
	
	// creates top row board spaces
	private void createBoardTop(){
		OpenSpace space21 = (OpenSpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.OPEN);
		space21.setName("Free Parking");
		spaces.add(space21);
		
		PropertySpace space22 = (PropertySpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.PROPERTY);
		space22.setPropertyInfo(PropertyType.STREET, "Kentucky Avenue", 220, new int[] { 18, 90, 250, 700, 875, 1050 }, "Red");
		spaces.add(space22);
		this.addToNeighborhood(space22);
		
		ChanceSpace space23 = (ChanceSpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.CHANCE);
		spaces.add(space23);
		
		PropertySpace space24 = (PropertySpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.PROPERTY);
		space24.setPropertyInfo(PropertyType.STREET, "Indiana Avenue", 220, new int[] { 18, 90, 250, 700, 875, 1050 }, "Red");
		spaces.add(space24);
		this.addToNeighborhood(space24);

		PropertySpace space25 = (PropertySpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.PROPERTY);
		space25.setPropertyInfo(PropertyType.STREET, "Illinois Avenue", 240, new int[] { 20, 100, 300, 750, 925, 1100 }, "Red");
		spaces.add(space25);
		this.addToNeighborhood(space25);

		PropertySpace space26 = (PropertySpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.PROPERTY);
		space26.setPropertyInfo(PropertyType.RAILROAD, "B. & O. Railroad", 200, new int[] {}, "");
		spaces.add(space26);

		PropertySpace space27 = (PropertySpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.PROPERTY);
		space27.setPropertyInfo(PropertyType.STREET, "Atlantic Avenue", 260, new int[] { 22, 110, 330, 800, 975, 1150 }, "Yellow");
		spaces.add(space27);
		this.addToNeighborhood(space27);
		
		PropertySpace space28 = (PropertySpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.PROPERTY);	
		space28.setPropertyInfo(PropertyType.STREET, "Ventnor Avenue", 260, new int[] { 22, 110, 330, 800, 975, 1150 }, "Yellow");
		spaces.add(space28);
		this.addToNeighborhood(space28);

		PropertySpace space29 = (PropertySpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.PROPERTY);	
		space29.setPropertyInfo(PropertyType.UTILITY, "Water Works", 150, new int[] {}, "");
		spaces.add(space29);
		
		PropertySpace space30 = (PropertySpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.PROPERTY);	
		space30.setPropertyInfo(PropertyType.STREET, "Marvin Gardens", 280, new int[] { 24, 120, 360, 850, 1025, 1200 }, "Yellow");
		spaces.add(space30);
		this.addToNeighborhood(space30);
	}
	
	// creates right row board spaces
	private void createBoardRight(){
		GoToJailSpace space31 = (GoToJailSpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.GOTOJAIL);
		spaces.add(space31);

		PropertySpace space32 = (PropertySpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.PROPERTY);	
		space32.setPropertyInfo(PropertyType.STREET, "Pacific Avenue", 300, new int[] { 26, 130, 390, 900, 1100, 1275 }, "Green");
		spaces.add(space32);
		this.addToNeighborhood(space32);

		PropertySpace space33 = (PropertySpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.PROPERTY);	
		space33.setPropertyInfo(PropertyType.STREET, "North Carolina Avenue", 300, new int[] { 26, 130, 390, 900, 1100, 1275 }, "Green");
		spaces.add(space33);
		this.addToNeighborhood(space33);

		CommunityChestSpace space34 = (CommunityChestSpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.COMMUNITYCHEST);
		spaces.add(space34);
		
		PropertySpace space35 = (PropertySpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.PROPERTY);		
		space35.setPropertyInfo(PropertyType.STREET, "Pennsylvania Avenue", 320, new int[] { 28, 150, 450, 1000, 1200, 1400 }, "Green");
		spaces.add(space35);
		this.addToNeighborhood(space35);

		PropertySpace space36 = (PropertySpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.PROPERTY);		
		space36.setPropertyInfo(PropertyType.RAILROAD, "Short Line", 200, new int[] {}, "");
		spaces.add(space36);
		
		ChanceSpace space37 = (ChanceSpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.CHANCE);
		spaces.add(space37);
		
		PropertySpace space38 = (PropertySpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.PROPERTY);		
		space38.setPropertyInfo(PropertyType.STREET, "Park Place", 350, new int[] { 35, 175, 500, 1100, 1300 }, "Blue");
		spaces.add(space38);
		this.addToNeighborhood(space38);

		LuxuryTaxSpace space39 = (LuxuryTaxSpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.LUXURYTAX);
		spaces.add(space39);
		
		PropertySpace space40 = (PropertySpace) BoardSpaceFactory.getBoardSpace(BoardSpaceType.PROPERTY);
		space40.setPropertyInfo(PropertyType.STREET, "Boardwalk", 400, new int[] { 50, 200, 600, 1400, 1700, 2000 }, "Blue");
		spaces.add(space40);
		this.addToNeighborhood(space40);
	}

	// adds board space (street) to corresponding neighborhood based on property/neighborhood color
	public void addToNeighborhood(BoardSpace space) {
		Street s = (Street) ((PropertySpace) space).getProperty();
		for (Neighborhood n : neighborhoods) {
			if (n.getColor() == s.getColor()) {
				n.addStreetToNeighborhood(s);
				s.addToNeighborhood(n);
			}
		}
	}
}
