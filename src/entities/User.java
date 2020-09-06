package entities;

public class User {
    
    private String nick;
    private Room room;
    
    public User(String nick, Room room) {
        this.nick = nick;
        this.room = room;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
    
    

}
