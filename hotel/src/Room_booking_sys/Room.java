package coursework;

public class Room {
	private int roomNum; // Room number
	private String roomType; // The type of room (e.g., Single, Double, Suite)
	private double roomPrice; // The price of room
	private boolean hasBalcony; // Shows weather room has balcony
	private boolean hasLounge; // Shows weather room has lounge
	private String reservedBy; // Person email who reserve the room, "free" if room is not reserved
	
	// a constructor to build up my project
	public Room(int roomNum, String roomType, double roomPrice, boolean hasBalcony, boolean hasLounge, String reservedBy) {
		this.roomNum = roomNum;
		this.roomType = roomType;
		this.roomPrice = roomPrice;
		this.hasBalcony = hasBalcony;
		this.hasLounge = hasLounge;
		this.reservedBy = reservedBy;
	}
	
	//list of getters
	public int getRoomNum() {
		return roomNum;
	}
	
	public String getRoomsType() {
		return roomType;
	}
	
	public double getRoomPrice() {
		return roomPrice;
	}
	
	public boolean hasBalcony() {
		return hasBalcony;
	}
	
	public boolean hasLounge() {
		return hasLounge;
	}
	
	public String getReservedBy() {
		return reservedBy;
	}
	// one setter 
	public void setReservedBy(String reservedBy) {
		this.reservedBy = reservedBy;
	}
	 // to string function for the output
	public String toString() {
		return roomNum + " " + roomType + " " + roomPrice + " " + hasBalcony + " " + hasLounge + " " + reservedBy;
	}
	
}











































