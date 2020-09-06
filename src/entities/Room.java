package entities;

import java.util.ArrayList;

public class Room {
    
    private final ArrayList<User> users;
    private String name;
    
    public Room(String name) {
        users = new ArrayList<>();
        this.name = name;
    }

    public ArrayList<User> getusers() {
        return users;
    }

    public void addUser(User usr) {
        users.add(usr);
    }
    
    public void removeUser(User usr) {
        users.remove(usr);
    }
    
    public int getNumberOfUsers() {
        return users.size();
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

}
