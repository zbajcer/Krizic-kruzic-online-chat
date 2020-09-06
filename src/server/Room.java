package server;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class Room {
	
    private ArrayList<User> users;
    private String name;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public int getCountUsers() {
        return users.size();
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void updateListUsers() {
        for (User usr : users) {
            usr.sendUsersList();
        }
    }
    
    public Room(String name) {
        this.name = name;
        this.users = new ArrayList<>();
    }
    
    public Room(String name, String pw) {
        this.name = name;
        this.users = new ArrayList<>();
    }
    
    public String getIn(User u) {
        //Check that the user does not exist in the room
        if (!userExists(u)) {
            //Check that the user is not banned from the room
                //Connect the user
                u.setConnection(true);
                //add the user to the list of users of the room
                users.add(u);
                //send the updated list of users to all users in the room
                updateListUsers();
                Log.log(u.getNick() + " entered the room " + this.name);
                //send the name of the room to the user
                u.sendToClient("ROOM " + this.name);
                return "200 OK";

        } else {
            //disconnect the user
            u.setConnection(false);
            return "400 The user is already in the room";
        }
    }
    
    public void spread(String message) {
    	for(User u: this.getUsers()) {
    		u.sendToClient(message);
    	}
    }
    
    public void leave(User u) {
        //If the user exists, we leave the room
        if (userExists(u)) {
            //We remove it from the list of users of the room
            users.remove(u);
            //send the updated list of users to all users in the room
            updateListUsers();
            Log.log(u.getNick() + " has left the room " + this.name);
            Server.playersNum--;
        }
    }
    
    public boolean userExists(User u) {
        for (User usr : users) {
            if (usr.getNick().equalsIgnoreCase(u.getNick())) {
                return true;
            }
        }
        return false;
    }
    
    public User getUser(String nick) {
        for (User user : users) {
            if (user.getNick().equalsIgnoreCase(nick)) {
                return user;
            }
        }
        return null;
    }
    
    public void sendPrivateMessage(User from, User to, String message) {
    	if(message.startsWith("xo")) {
            to.sendToClient(message);
    	} else {
        //Show the message to the sender
        from.sendToClient("(Private)" + from.getNick() + ": " + message);
        //Show the message to the recipient
        to.sendToClient("(Private)" + to.getNick() + ": " + message);
    }
    	}
    
}
