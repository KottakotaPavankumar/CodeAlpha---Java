package Project;

import java.io.*;
import java.util.*;

class Room {
    enum Category { STANDARD, DELUXE, SUITE }

    int roomNumber;
    Category category;
    boolean isAvailable;

    Room(int roomNumber, Category category) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.isAvailable = true;
    }

    @Override
    public String toString() {
        return "Room " + roomNumber + " (" + category + ") - " + (isAvailable ? "Available" : "Booked");
    }
}

class Reservation {
    String guestName;
    int roomNumber;
    String category;
    String paymentStatus;

    Reservation(String guestName, int roomNumber, String category, String paymentStatus) {
        this.guestName = guestName;
        this.roomNumber = roomNumber;
        this.category = category;
        this.paymentStatus = paymentStatus;
    }

    @Override
    public String toString() {
        return guestName + "," + roomNumber + "," + category + "," + paymentStatus;
    }

    public static Reservation fromString(String line) {
        String[] parts = line.split(",");
        return new Reservation(parts[0], Integer.parseInt(parts[1]), parts[2], parts[3]);
    }
}

public class HotelReservationSystem {
    static List<Room> rooms = new ArrayList<>();
    static List<Reservation> reservations = new ArrayList<>();
    static final String FILE_NAME = "reservations.txt";

    public static void main(String[] args) {
        initializeRooms();
        loadReservationsFromFile();

        Scanner s = new Scanner(System.in);
        int choice;

        System.out.println("🏨 Welcome to Hotel Reservation System");

        do {
            System.out.println("\n--- Menu ---");
            System.out.println("1. View Available Rooms");
            System.out.println("2. Book a Room");
            System.out.println("3. Cancel Reservation");
            System.out.println("4. View My Reservations");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            choice = s.nextInt();
            s.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> showAvailableRooms();
                case 2 -> bookRoom(s);
                case 3 -> cancelReservation(s);
                case 4 -> viewMyReservations(s);
                case 5 -> saveReservationsToFile();
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 5);

        System.out.println("✅ Thank you for using our service!");
    }

    static void initializeRooms() {
        for (int i = 101; i <= 105; i++)
            rooms.add(new Room(i, Room.Category.STANDARD));
        for (int i = 201; i <= 203; i++)
            rooms.add(new Room(i, Room.Category.DELUXE));
        for (int i = 301; i <= 302; i++)
            rooms.add(new Room(i, Room.Category.SUITE));
    }

    static void showAvailableRooms() {
        System.out.println("\nAvailable Rooms:");
        for (Room room : rooms) {
            if (room.isAvailable)
                System.out.println(room);
        }
    }

    static void bookRoom(Scanner scanner) {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        showAvailableRooms();

        System.out.print("Enter room number to book: ");
        int roomNumber = scanner.nextInt();
        scanner.nextLine();

        Room selectedRoom = null;
        for (Room room : rooms) {
            if (room.roomNumber == roomNumber && room.isAvailable) {
                selectedRoom = room;
                break;
            }
        }

        if (selectedRoom == null) {
            System.out.println("❌ Room not available.");
            return;
        }

        System.out.println("💳 Simulating payment...");
        String paymentStatus = "PAID";

        selectedRoom.isAvailable = false;
        Reservation reservation = new Reservation(name, roomNumber, selectedRoom.category.toString(), paymentStatus);
        reservations.add(reservation);
        System.out.println("✅ Booking confirmed!");

        saveReservationsToFile();
    }

    static void cancelReservation(Scanner scanner) {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        boolean found = false;
        Iterator<Reservation> it = reservations.iterator();
        while (it.hasNext()) {
            Reservation r = it.next();
            if (r.guestName.equalsIgnoreCase(name)) {
                it.remove();
                for (Room room : rooms) {
                    if (room.roomNumber == r.roomNumber) {
                        room.isAvailable = true;
                        break;
                    }
                }
                System.out.println("❎ Reservation canceled for room " + r.roomNumber);
                found = true;
            }
        }

        if (!found) {
            System.out.println("❌ No reservations found under your name.");
        }

        saveReservationsToFile();
    }

    static void viewMyReservations(Scanner scanner) {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        boolean found = false;
        for (Reservation r : reservations) {
            if (r.guestName.equalsIgnoreCase(name)) {
                System.out.println("🧾 Room " + r.roomNumber + " (" + r.category + "), Payment: " + r.paymentStatus);
                found = true;
            }
        }

        if (!found) {
            System.out.println("ℹ️ No reservations found.");
        }
    }

    static void saveReservationsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Reservation r : reservations) {
                writer.println(r.toString());
            }
        } catch (IOException e) {
            System.out.println("❌ Error saving reservations.");
        }
    }

    static void loadReservationsFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Reservation r = Reservation.fromString(line);
                reservations.add(r);
                for (Room room : rooms) {
                    if (room.roomNumber == r.roomNumber) {
                        room.isAvailable = false;
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Error loading reservations.");
        }
    }
}
