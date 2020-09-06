import entities.User;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class NetworkManager {
    
    private Socket socket;
    private BufferedReader incoming;
    private BufferedWriter outgoing;
    private static NetworkManager instance;
    private KrizicKruzic chatInterface;
    
    public static NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();
        }
        return instance;
    }
    
    
    public void listenServer() {
        initializeHeartBeat();
        try {
            while (true) {
                String packages = recieve();
                if (packages.startsWith("200")) {
                    //Success
                } else if (packages.startsWith("400")) {
                    JOptionPane.showMessageDialog(chatInterface, packages.substring(4), "Disconnected", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                } else if (packages.startsWith("500")) {
                    JOptionPane.showMessageDialog(chatInterface, packages.substring(4), "Error", JOptionPane.WARNING_MESSAGE);
                } else if (packages.startsWith("ROOM")) {
                    String[] p = packages.split("[ ]");
                    chatInterface.setTitle("ROOM " + p[1] + "@" + socket.getInetAddress().getHostAddress());
                } else if (packages.startsWith("LIST")) {
                    chatInterface.clearList();
                    int count = packages.split("[ ]").length;
                    for (int i = 1; i < count; i++) {
                        User u = new User(packages.split("[ ]")[i], null);
                        chatInterface.addUser(u);
                    }
                    //chatInterface.actualizarLista();
                } else if(packages.startsWith("xo")) {
                	String packageParts[] = packages.split(" ");
                	if(!packageParts[1].contentEquals(KrizicKruzic.getUser())) {
                    	chatInterface.pressButton(packageParts[2]);
                	}
                } else if(packages.startsWith("/RESET")) {
                	chatInterface.newGame();
                } else if(!packages.isEmpty()) {
                    chatInterface.addMessage(packages);
                }
            }
        } catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(chatInterface, "The connection to the server has been lost", "Disconnected", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }
    
    public void setServer(String IP, int port) {
        try {
            socket = new Socket(IP, port);
            incoming = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outgoing = new BufferedWriter(new PrintWriter(socket.getOutputStream()));
        } catch (IOException ex) {
            Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void send(String content) {
        if (!content.isEmpty()) {
            try {
                outgoing.write(content + "\n");
                outgoing.flush();
            } catch (IOException ex) {
                Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public String recieve() {
        String s = "";
        try {
            s = incoming.readLine();
        } catch (IOException ex) {
            
        }
        return s;
    }
    
    public void setChatInterface(KrizicKruzic chatInterface) {
        this.chatInterface = chatInterface;
    }

    private void initializeHeartBeat() {
        new Thread(new Runnable() {
                @Override
                public void run() {
                    long heartbeat = System.currentTimeMillis();
                    while (true) {
                        if (System.currentTimeMillis() - heartbeat >= 5000) {
                            send("BEAT " + System.currentTimeMillis());
                            heartbeat = System.currentTimeMillis();              
                        }
                        try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							System.out.println("Sleeping failed!");
						}
                    }
                }
            }).start();
    }
}
