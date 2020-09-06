package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	public static final int PORT = 2014;
	public static ArrayList<Room> listRooms;
	public static final String ADMIN_PASSWORD = "admin";
	public static boolean saveLogs = true;
	public static int playersNum = 0;

	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		if (saveLogs)
			System.out.println("Logging enabled");
		else
			System.out.println("Logging disabled");
		ServerSocket ss = new ServerSocket(PORT); // create the socket to listen for incoming connections
		Log.log("Server initialized on port " + PORT);

		listRooms = new ArrayList<>(); // initialize the list of rooms, create the "Main" room, which is the default
										// room and add it to the list of rooms
		Room room = new Room("Principal");
		addRoom(room);

		while (true) { // Infinite loop that waits for connection requests and creates independent instances for each one
				Socket socket = ss.accept();
				Log.log("Established connection with " + socket.getInetAddress().getHostAddress());
				playersNum++;
				new Thread(new User(socket, room)).start(); // create a new instance passing it the socket and the main room
		}
	}

	public static void addRoom(Room s) {
		if (getRoom(s.getName()) == null) {
			listRooms.add(s);
			Log.log("Room has been created: " + s.getName());
		}
	}

	public static Room getRoom(String name) {
		for (Room s : listRooms) {
			if (s.getName().equalsIgnoreCase(name)) {
				return s;
			}
		}
		return null;
	}

}
