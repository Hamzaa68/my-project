package coursework;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class RoomBookingSystem {
	// File path for file of room data
	private static final String FILENAME = "C:\\Users\\stepm\\eclipse-workspace\\coursework\\src\\coursework\\rooms (2).txt";
	private ArrayList<Room> rooms;
	
	// Read the room list and load the data from the file
	public RoomBookingSystem() {
		rooms = new ArrayList<>();
		loadRoomsData();
	}
	
	// Method to load data from the file
	private void loadRoomsData() {
		try (FileReader filereader = new FileReader(FILENAME);
			 Scanner scanner = new Scanner(filereader)) {
			// Read each line of the file and analyze room details
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine(); 
				String[] data = line.split(" "); // Line will get split in components
				int roomNum = Integer.parseInt(data[0]); // Room number
				String roomType = data[1]; // Room type (Single, Double, Suite)
				double roomPrice = Double.parseDouble(data[2]); // Room price
				boolean hasBalcony = Boolean.parseBoolean(data[3]); // Availability of balcony
				boolean hasLounge = Boolean.parseBoolean(data[4]); // Availability of lounge
				String reservedBy = data[5].equals("free") ? "free" : data[5]; // Reservation status
				
				// Add the room  object to list
				rooms.add(new Room(roomNum,  roomType, roomPrice, hasBalcony, hasLounge, reservedBy));	
			}
		}catch (FileNotFoundException e) {
			System.out.println("File not found: " + e.getMessage());
		}catch (IOException e) {
			System.out.println("Error reading room data: " + e.getMessage());
			
		}
	}
	// Method to save the present condition of room data to file
	private void saveRoomData() {
		try (PrintWriter writer = new PrintWriter(new File(FILENAME))) {
			for (Room room : rooms) {
				writer.println(room.toString());
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error saving room data: " + e.getMessage());
		}
	}
	//Helper Method to get a valid boolean input (true/false) from the user 
	private boolean getValidBooleanInput(Scanner scanner, String prompt)  {
		while (true) {
			System.out.print(prompt);
			String input = scanner.next().toLowerCase();
			if (input.equals("true") || input.equals("false")) {
				return Boolean.parseBoolean(input);
			} else {
				System.out.println("Invalid input! Please enter 'true' or 'false'.");
				
			}
		}
	}
	
	// Helper Method to authenticate and get a valid room type input from user
	private String getValidRoomType(Scanner scanner) {
		while (true) {
			System.out.print("Enter room type(Single/Double/Suite): ");
			String roomType = scanner.next();
			if (roomType.equalsIgnoreCase("Single") || roomType.equalsIgnoreCase("Double") || roomType.equalsIgnoreCase("Suite")) {
			    return roomType;	
			} else {
				System.out.println("Invalid room type! please enter 'Single', 'Double', or 'Suite'.");
			}
		}
	}
	
	//Helper Method to authenticate and get and get price input within a declared range
	private double getValidPrice(Scanner scanner) {
		double maxPrice = -1;
		while (maxPrice < 0 || maxPrice > 60) {
			System.out.print("Enter maximum price you can afford(0 to 60): ");
			if (scanner.hasNextDouble()) {
				maxPrice = scanner.nextDouble();
				if (maxPrice < 0 || maxPrice > 60) {
					System.out.println("Invalid price! Please enter a valid value between 0 and 60.");
				}			
			} else {
				System.out.println("Invalid input! Please enter a numeric value.");
				scanner.next();
			}
		}
		return maxPrice;
	}
	
	//Helper Method to authenticate and get a valid email address
	private String getValidEmail(Scanner scanner) {
		while (true) {
			System.out.print("Enter your email: ");
			String email = scanner.next();
			if (isValidEmail(email)) {
				return email;
			} else {
				System.out.println("Invalid email format! Please enetr a valid email.");
			}
		}
	}
	
	//Helper Method to authenticate email format using regular expression
	private boolean isValidEmail(String email) {
		String emailRegex = "^[\\w-\\.]+@[\\w-]+\\.[a-z]{2,}$";
		Pattern pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
		return pattern.matcher(email).matches();
	}
	
	// Method to reserve a room based on choice of user
	public void reserveRoom(String roomType, double maxPrice, boolean balconyPref, boolean loungePref, String guestEmail) {
			Scanner scanner = new Scanner(System.in);
			
			// Find exact matches for the room
			ArrayList<Room> exactMatches = new ArrayList<>();
			for (Room room : rooms) {
				if (room.getRoomsType().equalsIgnoreCase(roomType)
						&& room.getRoomPrice() <= maxPrice
						&& (!balconyPref || room.hasBalcony())
						&& (!loungePref || room.hasLounge())
						&& room.getReservedBy().equals("free")) {
					exactMatches.add(room);
				}
			}
			
			// if exact matches found display them
			if (!exactMatches.isEmpty()) {
				System.out.println("Available Rooms (Exact Matches): ");
				for (Room room : exactMatches) {
					System.out.println(room);
				}
				
				System.out.print("Enter the room number you want to rserve or type 'Q' to exit: ");
				String Input = scanner.next();
				
				// Allows the user to quit the booking process
				if (Input.equalsIgnoreCase("q")) {
					System.out.println("Reservation process cancelled.");
					return;
				}
				
				// Process what is the room selection of the user
				try {
					int roomNum = Integer.parseInt(Input);
					for (Room room : exactMatches) {
						if (room.getRoomNum() == roomNum) {
							room.setReservedBy(guestEmail);
							System.out.println("Room " + roomNum + " successfully reseserved for " + guestEmail);
							saveRoomData();
							return;
						}
					}
					System.out.println("Invalid room number. Reservation failed.");	
				} catch (NumberFormatException e) {
					System.out.println("Invalid input. Please enter a valid room number or 'Q'. ");
				}
				return;
			}
			
			// If no exact match is found, suggest user next best matches
			ArrayList<Room> nextBestMatches = new ArrayList<>();
			for (Room room : rooms) {
				int matchScore = 0;
				if (room.getRoomsType().equalsIgnoreCase(roomType)) matchScore++;
				if (room.getRoomPrice() <= maxPrice) matchScore++;
				if (balconyPref == room.hasBalcony()) matchScore++;
				if (loungePref == room.hasLounge()) matchScore++;
				
				if (matchScore >= 3 && room.getReservedBy().equals("free")) {
					nextBestMatches.add(room);
				}
			}
			// Suggest next best matches according to user's preference
			if (!nextBestMatches.isEmpty()) {
				System.out.println("No exact match found. Here are some next best matches:");
				for (Room room : nextBestMatches) {
					System.out.println(room);
				}
				
				System.out.println("Do you want to reserve one of these rooms? (yes/no): ");
				String response = scanner.next();
				
				if (response.equalsIgnoreCase("yes")) {
					System.out.print("Entr the room number you want to reserve: ");
					int roomNum = scanner.nextInt();
					for(Room room : nextBestMatches) {
						if (room.getRoomNum() == roomNum) {
							room.setReservedBy(guestEmail);
							System.out.println("Room " + roomNum + " successfully reserved for " + guestEmail);
							saveRoomData();
							return;
						}
					}
					System.out.println("Invalid room number. Reservation failed.");
				} else {
					System.out.println("No reservation made.");
				}
			 }else	{
				 System.out.println("No suitable rooms availabe.");
			 }
	}	
	//Method to cancel the room reservation
	public void cancelRoom(String guestEmail) {
		ArrayList<Room> reservedRooms = new ArrayList<>();
		for (Room room : rooms) {
			if (room.getReservedBy().equalsIgnoreCase(guestEmail)) {
				reservedRooms.add(room);
			}
		}
		// If no reservation is found for the given email
		if (reservedRooms.isEmpty()) {
			System.out.println("No reservation found for " + guestEmail);
			return;
		}
		// Display all the rooms that are reserved
		System.out.println("Rooms reserved by " + guestEmail + ":");
		for (Room room : reservedRooms) {
			System.out.println(room);
		}
		
		Scanner scanner = new Scanner(System.in);
		System.out.print("Do you want to cancel a specific room (1) or all reservations (2)? Enter 1 or 2:");
		int choice = scanner.nextInt();
		
		if (choice == 1) {
			// Cancel reservation for a room
			System.out.print("Enter the room number to cancel: ");
			int roomNum = scanner.nextInt();
			boolean roomFound = false;
			for (Room room : reservedRooms) {
				if (room.getRoomNum() == roomNum) {
					room.setReservedBy("free");
					System.out.println("Reservation for room" + roomNum + "cancelled.");
					roomFound = true;
					break;
				}
			}
			// If the room number is not found in reservation list
			if (!roomFound) {
				System.out.println("Room number " + roomNum + " is not reserved by " + guestEmail);		
			}
		} else if (choice == 2) {
			for (Room room : reservedRooms) {
				room.setReservedBy("free");
			}
			System.out.println("All reservations for " + guestEmail + " have been cancelled.");
		} else {
			System.out.println("Invalid Choice. No action taken.");
		}
		// Save the updated room data
		saveRoomData();
	}
	
	// Method to view all the rooms that are reserved
	public void viewRoomsResevations() {
		System.out.println("Room\tType\tPrice\tBalcony\tLounge\tReserved By");
		System.out.println("-----\t-----\t-----\t-------\t-------\t-----------");
		
		boolean anyReserved =  false;
		// Iterate through the rooms and display the reserved rooms
		for (Room room : rooms) {
			if (!room.getReservedBy().equals("free")) {
				System.out.println(room);
				anyReserved = true;
			}
		}
		// A text will be displayed if no rooms are reserved
		if (!anyReserved) {
			System.out.println("No rooms are reserved.");
		}
	}
	
	// Main menu interface for user interaction
	public void mainMenu() {
		Scanner scanner = new Scanner(System.in);
		char choice;
		
		// Display the menu until user decides to quit
		do {
			System.out.println("---Room Booking System---");
			System.out.println("1 - Reserve Room");
			System.out.println("2 - Cancel Room");
			System.out.println("3 - View Room Reservation");
			System.out.println("Q - Quit");
			System.out.print("Pick: ");
			choice = scanner.next().charAt(0);
			
			// Execute operation based on users preference
			switch (choice) {
			    case '1' :
			    	String roomType = getValidRoomType(scanner);
			    	double maxPrice = getValidPrice(scanner);
			    	boolean balconyPref = getValidBooleanInput(scanner,"Do you prefer a balcony? (true/false): ");
			    	boolean loungePref = getValidBooleanInput(scanner, "Do you prefer a lounge? (true/false): ");
			    	String email = getValidEmail(scanner);
			    	reserveRoom(roomType, maxPrice, balconyPref, loungePref, email);
			    	break;
			    case '2':
			    	String guestEmail = getValidEmail(scanner);
			    	cancelRoom(guestEmail);
			    	break;
			    case '3' :
			    	 viewRoomsResevations();
			    case 'Q' :
			    	System.out.println("Goodbye!");
			    	break;
			    default: 
			    	System.out.println("Invalid option.");	   
			}
		} while (choice != 'Q');
		scanner.close();
	}
	
	public static void main(String[] args) {
		RoomBookingSystem system = new RoomBookingSystem();
		system.mainMenu();
	}
}		
		
	



