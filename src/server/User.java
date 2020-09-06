package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import static server.Server.ADMIN_PASSWORD;

public class User implements Runnable {

	private String nick;
	private BufferedReader incoming;
	private BufferedWriter outgoing;
	private long loginTime;
	private boolean connected, superUser, heartBeatOn;
	private String IP;
	private long ping;
	private Room room;
	private long lastBeat;

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public long getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(long loginTime) {
		this.loginTime = loginTime;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String IP) {
		this.IP = IP;
	}

	public long getPing() {
		return ping;
	}

	public void setPing(long ping) {
		this.ping = ping;
	}

	public boolean isSuperUser() {
		return superUser;
	}

	public void setSuperUser(boolean superUser) {
		this.superUser = superUser;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnection(boolean connected) {
		this.connected = connected;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public User(String nick) {
		this.nick = nick;
	}

	public User(Socket socket, Room room) throws IOException {
		this.room = room;
		this.loginTime = System.currentTimeMillis();
		this.IP = socket.getInetAddress().getHostAddress();
		this.ping = 0;
		this.superUser = false;
		this.heartBeatOn = true;
		incoming = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		outgoing = new BufferedWriter(new PrintWriter(socket.getOutputStream()));
	}

	@Override
	public void run() {
		String login = receive(); // Wait to receive the customer login message
		if (!login.startsWith("NICK")) { // Check that the login message is correct
			sendToClient("400 Invalid package received"); // Disconnect the client if there is an error
			Log.log("Invalid login package: " + login);
			connected = false;
		} else {
			if (login.split("[ ]")[1].length() >= 12) { // Check that the nickname is not too long
				sendToClient("400 The chosen nickname is too long, enter a nickname of maximum 12 characters"); // Send
																												// an
																												// error
																												// and
																												// disconnect
																												// the
																												// user
				Log.log("A user tried to get in with too long nickname. Nick: " + login.split("[ ]")[1]);
			} else {
				connected = !room.userExists(this); // Connect the client if the user does not exist in the room
			}
		}
		if (connected) { // If everything is correct it connects to the room
			if (Server.playersNum > 2) {
				sendToClient("400 You have been disconnected. Too many players");
				room.leave(this);
			}
			nick = login.split("[ ]")[1]; // We obtain the nickname received from the client
			sendToClient(room.getIn(this)); // connect the user to the room
			sendUsersList(); // We send the list of users of the room
			if (heartBeatOn) {
				lastBeat = System.currentTimeMillis(); // initialize the heart beat to verify that the client maintains
														// the connection
				asyncBeatCheck();
			}
			sendToClient("ROOM " + room.getName()); // send the name of the room
			do { // Loop that will last until the user logs out (EXIT or errors)
				String packages = receive(); // We wait to receive a message from the client instance
				if (packages != null && !packages.isEmpty()) { // If the package is not empty, we will analyze it
					analyzePackage(packages);
				}
			} while (connected); // send the disconnection message to the user
			sendToClient("400 You have been disconnected from the chat"); // The client is no longer connected, we
																			// remove him from the room
			room.leave(this);
		}
	}

	public void analyzePackage(String packageMessage) {
		if (packageMessage.startsWith("EXIT")) { // Chat outgoing request
			connected = false;
		} else if (packageMessage.startsWith("BEAT")) { // Automatic heartbeat message
			String[] p;
			p = packageMessage.split("[ ]");
			if (p.length > 2) {
				sendToClient("500 incorrect syntax");
				Log.log("Invalid package: " + packageMessage);
			} else {
				lastBeat = System.currentTimeMillis(); // We updated the last time we received a heartbeat
				ping = System.currentTimeMillis() - Long.parseLong(p[1]); // We update the user'packageMessage ping
			}
		} else if (packageMessage.startsWith("xo")) {
			String packageContent = packageMessage;
			User opponent = null;
			for (User user : room.getUsers()) {
				if (!user.getNick().contentEquals(nick)) {
					opponent = user;
				}
			}
			if (room.getCountUsers() == 2) {
				System.out.println("pekið kontent = "+packageContent.split(" ")[1]);
				System.out.println("opponent = " + opponent.getNick());
				room.sendPrivateMessage(this, opponent, packageContent);
				Log.log(this.getNick() + " pressed " + packageContent.split(" ")[2]);
			}
		} else if (packageMessage.startsWith("/RESET")) {
			Log.log("Reset request has been sent by " + nick);
		} else { // Received a normal text message
			if (packageMessage.length() < 140) {
				// We spread the message received to all users of the room
				room.spread(nick + ": " + packageMessage);
				Log.log("Received message from " + nick + " in the room " + room.getName() + ". Content: "
						+ packageMessage);
			} else {
				Log.log(" " + nick);
			}
		}
	}

	public void sendUsersList() {
		StringBuilder strb = new StringBuilder();
		strb.append("LIST ");
		for (User usr : room.getUsers()) {
			strb.append(usr.getNick());
			strb.append(" ");
		}
		sendToClient(strb.toString());
	}

	public void sendToClient(String message) {
		try {
			outgoing.write(message + "\n");
			outgoing.flush();
		} catch (IOException ex) {
			Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public String receive() {
		String reciever = "";
		try {
			reciever = incoming.readLine();
		} catch (Exception ex) {
		}
		return reciever;
	}

	private void asyncBeatCheck() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (connected) { // We continue to check that the user is connected as long as the user is
									// actually connected
					try { // wait 1 second
						Thread.sleep(1000);
					} catch (InterruptedException ex) {
					}
					if (System.currentTimeMillis() - lastBeat >= 7000) { // We check that no more than 7 seconds have
																			// passed since the last heartbeat was sent
						sendToClient("400 Disconnected due to inactivity"); // We disconnect the user due to inactivity
						connected = false;
						Log.log(nick + " has been disconnected due to inactivity");
					}
				}
			}
		}).start();
	}

}
