import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;

import com.sun.glass.events.KeyEvent;

import entities.User;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class KrizicKruzic extends JFrame implements ActionListener {

	private static String user;
	
	public void setUser(String user) {
		KrizicKruzic.user = user;
	}
	
	public static String getUser() {
		return user;
	}
	
	private JButton firstButton;
	private JButton secondButton;
	private JButton thirdButton;
	private JButton fourthButton;
	private JButton fifthButton;
	private JButton sixthButton;
	private JButton seventhButton;
	private JButton eightButton;
	private JButton ninethButton;
	private JMenuItem i2;
	private JMenuItem i1;
	private JMenuItem iPlayer;
	private JMenuItem iStats;
	public boolean isFirstPlayer = true;
	private JMenuBar menuBar;
	private JMenu mnGame;
	private JMenu mnPlayers;
	public JDialog dialog = new JDialog();
	public String firstPlayer = "";
	public String secondPlayer = "";
	public String winner = "";
	public String looser = "";
	public int scoreFirstPlayer = 0;
	public int scoreSecondPlayer = 0;
	JTextField jtfFirstPlayer = new JTextField();
	JTextField jtfSecondPlayer = new JTextField();
	JLabel lblFirstPlayer;
	JLabel lblSecondPlayer;
	JLabel lblFirstPlayerName;
	JLabel lblSecondPlayerName;
	JLabel labelPlayerOne = new JLabel("First player ('X'):  ");
	JLabel labelPlayerTwo = new JLabel("Second player ('O'):  ");
	JLabel labelResult = new JLabel("Score: ");
	JLabel lblFirstPlayerScore;
	JLabel lblBetweenScores;
	JLabel lblSecondPlayerScore;
	JPanel contentPane = new JPanel(new BorderLayout());
	JPanel infoPanel = new JPanel();
	JPanel gamePanel = new JPanel();
	Border emptyBorder = BorderFactory.createEmptyBorder();
	@SuppressWarnings("rawtypes")
	JList jList1 = new JList();
	JTextArea areaMessages = new JTextArea();
	JScrollPane jScrollPane2 = new JScrollPane();
	JScrollPane jScrollPane3 = new JScrollPane();
	JButton btsend = new JButton("SEND");
	JTextField fieldMsg = new JTextField();

	public static KrizicKruzic frame;

	NetworkManager net;
	DefaultListModel<String> dlm;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		// Set the NIMBUS look and feel. if not available -> stay with the default look
		// and feel.
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(KrizicKruzic.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(KrizicKruzic.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(KrizicKruzic.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(KrizicKruzic.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		}
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					frame = new KrizicKruzic();
					frame.setTitle("Križiæ kružiæ");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public KrizicKruzic() {
		net = NetworkManager.getInstance();
		net.setServer(leerIP(), 2014);
		net.setChatInterface(this);
		net.send("NICK " + leerNick());
		dlm = new DefaultListModel<>();
		initComponents();
		setComponentsExtras();

		new Thread(new Runnable() {
			@Override
			public void run() {
				net.listenServer();
			}
		}).start();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				net.send("EXIT");
			}
		});
	}

	/**
	 * Create the frame.
	 */
	@SuppressWarnings("unchecked")
	private void initComponents() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(200, 50, 800, 650);

		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		mnGame = new JMenu("Game");
		menuBar.add(mnGame);
		i1 = new JMenuItem("New game");
		i2 = new JMenuItem("Exit");
		mnGame.add(i1);
		mnGame.add(i2);
		i1.addActionListener(this);
		i2.addActionListener(this);
		menuBar.add(mnGame);

		mnPlayers = new JMenu("Players");
		iPlayer = new JMenuItem("Enter players");
		iStats = new JMenuItem("Statistics");
		iPlayer.addActionListener(this);
		iStats.addActionListener(this);
		mnPlayers.add(iPlayer);
		mnPlayers.add(iStats);
		menuBar.add(mnPlayers);

		contentPane.add(infoPanel, BorderLayout.NORTH);

		lblFirstPlayer = new JLabel("(X) ");
		lblFirstPlayerName = new JLabel("first player");
		lblFirstPlayerScore = new JLabel("   0 ");
		lblBetweenScores = new JLabel(":");
		lblSecondPlayerScore = new JLabel(" 0   ");
		lblSecondPlayerName = new JLabel("second player");
		lblSecondPlayer = new JLabel(" (O)");
		infoPanel.add(lblFirstPlayer);
		infoPanel.add(lblFirstPlayerName);
		infoPanel.add(lblFirstPlayerScore);
		infoPanel.add(lblBetweenScores);
		infoPanel.add(lblSecondPlayerScore);
		infoPanel.add(lblSecondPlayerName);
		infoPanel.add(lblSecondPlayer);
		contentPane.add(gamePanel, null);
		this.getContentPane().add(contentPane);
		gamePanel.setLayout(null);

		firstButton = new JButton("");
		firstButton.setBounds(1, 0, 194, 172);
		firstButton.setFont(new Font("Tahoma", Font.BOLD, 99));
		firstButton.setBackground(Color.white);
		firstButton.setBorder(emptyBorder);
		firstButton.setName("firstButton");
		gamePanel.add(firstButton);
		firstButton.addActionListener(this);

		secondButton = new JButton("");
		secondButton.setBounds(196, 0, 194, 172);
		secondButton.setFont(new Font("Tahoma", Font.BOLD, 99));
		secondButton.setBackground(Color.white);
		secondButton.setBorder(emptyBorder);
		secondButton.setName("secondButton");
		gamePanel.add(secondButton);
		secondButton.addActionListener(this);

		thirdButton = new JButton("");
		thirdButton.setBounds(391, 0, 194, 172);
		thirdButton.setFont(new Font("Tahoma", Font.BOLD, 99));
		thirdButton.setBackground(Color.white);
		thirdButton.setBorder(emptyBorder);
		thirdButton.setName("thirdButton");
		gamePanel.add(thirdButton);
		thirdButton.addActionListener(this);

		fourthButton = new JButton("");
		fourthButton.setBounds(1, 173, 194, 172);
		fourthButton.setFont(new Font("Tahoma", Font.BOLD, 99));
		fourthButton.setBackground(Color.white);
		fourthButton.setBorder(emptyBorder);
		fourthButton.setName("fourthButton");
		gamePanel.add(fourthButton);
		fourthButton.addActionListener(this);

		fifthButton = new JButton("");
		fifthButton.setBounds(196, 173, 194, 172);
		fifthButton.setFont(new Font("Tahoma", Font.BOLD, 99));
		fifthButton.setBackground(Color.white);
		fifthButton.setBorder(emptyBorder);
		fifthButton.setName("fifthButton");
		gamePanel.add(fifthButton);
		fifthButton.addActionListener(this);

		sixthButton = new JButton("");
		sixthButton.setBounds(391, 173, 194, 172);
		sixthButton.setFont(new Font("Tahoma", Font.BOLD, 99));
		sixthButton.setBackground(Color.white);
		sixthButton.setBorder(emptyBorder);
		sixthButton.setName("sixthButton");
		gamePanel.add(sixthButton);
		sixthButton.addActionListener(this);

		seventhButton = new JButton("");
		seventhButton.setBounds(1, 346, 194, 172);
		seventhButton.setFont(new Font("Tahoma", Font.BOLD, 99));
		seventhButton.setBackground(Color.white);
		seventhButton.setBorder(emptyBorder);
		seventhButton.setName("seventhButton");
		gamePanel.add(seventhButton);
		seventhButton.addActionListener(this);

		eightButton = new JButton("");
		eightButton.setBounds(196, 346, 194, 172);
		eightButton.setFont(new Font("Tahoma", Font.BOLD, 99));
		eightButton.setBackground(Color.white);
		eightButton.setBorder(emptyBorder);
		eightButton.setName("eightButton");
		gamePanel.add(eightButton);
		eightButton.addActionListener(this);

		ninethButton = new JButton("");
		ninethButton.setBounds(391, 346, 194, 172);
		ninethButton.setFont(new Font("Tahoma", Font.BOLD, 99));
		ninethButton.setBackground(Color.white);
		ninethButton.setBorder(emptyBorder);
		ninethButton.setName("ninethButton");
		ninethButton.addActionListener(this);
		gamePanel.add(ninethButton);

		jList1.setModel(dlm);
		jList1.setFixedCellHeight(20);
		jScrollPane3.setViewportView(jList1);

		fieldMsg.setBounds(1, 528, 584, 30);
		gamePanel.add(fieldMsg);
		fieldMsg.setColumns(10);

		areaMessages.setBounds(595, 177, 181, 341);
		areaMessages.setEditable(false);
		areaMessages.setColumns(20);
		areaMessages.setLineWrap(true);
		areaMessages.setRows(5);
		areaMessages.setToolTipText("");
		areaMessages.setWrapStyleWord(true);
		jScrollPane2.setViewportView(areaMessages);
		gamePanel.add(areaMessages);

		btsend.setBounds(595, 528, 181, 30);
		gamePanel.add(btsend);

		jList1.setBounds(595, 0, 181, 172);
		gamePanel.add(jList1);

		fieldMsg.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyPressed(java.awt.event.KeyEvent evt) {
				fieldMsgKeyPressed(evt);
			}
		});

		btsend.setName("sendButton");
		btsend.setText("send");
		btsend.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btsendActionPerformed(evt);
			}
		});

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JButton) {
			JButton buttonClicked = (JButton) e.getSource();
			btsendActionPerformed(e);
			switch (buttonClicked.getText()) {
			case "":
				if (isFirstPlayer == true) {
					buttonClicked.setText("X");
					if (checkWinningCombination()) {
						scoreFirstPlayer += 1;
						winner = firstPlayer;
						lblFirstPlayerScore.setText(String.valueOf(scoreFirstPlayer));
						try {
							submitScore(firstPlayer, secondPlayer, winner);
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
					isFirstPlayer = false;
				} else {
					buttonClicked.setText("O");
					if (checkWinningCombination()) {
						scoreSecondPlayer += 1;
						winner = secondPlayer;
						lblSecondPlayerScore.setText(String.valueOf(scoreSecondPlayer));
						try {
							submitScore(firstPlayer, secondPlayer, winner);
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
					isFirstPlayer = true;
				}
				break;
			case "Submit players":
				firstPlayer = jtfFirstPlayer.getText();
				secondPlayer = jtfSecondPlayer.getText();
				lblFirstPlayerName.setText(jtfFirstPlayer.getText());
				lblSecondPlayerName.setText(jtfSecondPlayer.getText());
				lblFirstPlayerScore.setText("0");
				lblSecondPlayerScore.setText("0");
				scoreFirstPlayer = 0;
				scoreSecondPlayer = 0;
				newGame();
				dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
				break;
			}
		} else if (e.getSource() instanceof JMenuItem) {
			JMenuItem item = (JMenuItem) e.getSource();
			switch (item.getText()) {
			case "New game":
				newGame();
				break;
			case "Exit":
				if (dialog.isVisible()) {
					dialog.setVisible(false);
				}
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				break;
			case "Enter players":
				showPlayersDialog();
				break;
			case "Statistics":
				try {
					showStatisticsDialog();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				break;
			}
		}
	}

	public boolean checkWinningCombination() {
		// Row 1
		if ("" != firstButton.getText() && firstButton.getText() == secondButton.getText()
				&& secondButton.getText() == thirdButton.getText()) {
			highlightWinningCombo(firstButton, secondButton, thirdButton);
			return true;
		}
		// Row 2
		if ("" != fourthButton.getText() && fourthButton.getText() == fifthButton.getText()
				&& fifthButton.getText() == sixthButton.getText()) {
			highlightWinningCombo(fourthButton, fifthButton, sixthButton);
			return true;
		}
		// Row 3
		if ("" != seventhButton.getText() && seventhButton.getText() == eightButton.getText()
				&& eightButton.getText() == ninethButton.getText()) {
			highlightWinningCombo(seventhButton, eightButton, ninethButton);
			return true;
		}
		// Column 1
		if ("" != firstButton.getText() && firstButton.getText() == fourthButton.getText()
				&& fourthButton.getText() == seventhButton.getText()) {
			highlightWinningCombo(firstButton, fourthButton, seventhButton);
			return true;
		}
		// Column 2
		if ("" != secondButton.getText() && secondButton.getText() == fifthButton.getText()
				&& fifthButton.getText() == eightButton.getText()) {
			highlightWinningCombo(secondButton, fifthButton, eightButton);
			return true;
		}
		// Column 3
		if ("" != thirdButton.getText() && thirdButton.getText() == sixthButton.getText()
				&& sixthButton.getText() == ninethButton.getText()) {
			highlightWinningCombo(thirdButton, sixthButton, ninethButton);
			return true;
		}
		// Diagonal 1
		if ("" != firstButton.getText() && firstButton.getText() == fifthButton.getText()
				&& fifthButton.getText() == ninethButton.getText()) {
			highlightWinningCombo(firstButton, fifthButton, ninethButton);
			return true;
		}
		// Diagonal 2
		if ("" != thirdButton.getText() && thirdButton.getText() == fifthButton.getText()
				&& fifthButton.getText() == seventhButton.getText()) {
			highlightWinningCombo(thirdButton, fifthButton, seventhButton);
			return true;
		}
		return false;
	}

	public void highlightWinningCombo(JButton first, JButton second, JButton third) {
		first.setBackground(Color.DARK_GRAY);
		second.setBackground(Color.DARK_GRAY);
		third.setBackground(Color.DARK_GRAY);
		int noOfComponents = gamePanel.getComponentCount();
		for (int i = 0; i < noOfComponents; i++) {
			if (gamePanel.getComponent(i) instanceof JButton) {
				gamePanel.getComponent(i).setEnabled(false);
			}
		}
		btsend.setEnabled(true);

	}

	public void newGame() {
		isFirstPlayer = true;
		int noOfComponents = gamePanel.getComponentCount();
		for (int i = 0; i < noOfComponents; i++) {
			gamePanel.getComponent(i).setEnabled(true);
			if (gamePanel.getComponent(i) instanceof JButton) {
				JButton set = (JButton) gamePanel.getComponent(i);
				if(set.getName() != "sendButton") {
				set.setText("");
				set.setBackground(Color.white);
			}
		}

		}
	}

	public void showPlayersDialog() {
		GridBagConstraints gbc = new GridBagConstraints();
		GridBagLayout gbl = new GridBagLayout();
		JButton submitPlayers = new JButton("Submit players");

		dialog = new JDialog((JDialog) null, "Players");
		dialog.setPreferredSize(new Dimension(400, 200));
		dialog.getContentPane().setLayout(gbl);

		gbc.gridx = 0;
		gbc.gridy = 0;
		dialog.getContentPane().add(labelPlayerOne, gbc);

		jtfFirstPlayer.setPreferredSize(new Dimension(150, 30));
		gbc.gridx = 1;
		gbc.gridy = 0;
		dialog.getContentPane().add(jtfFirstPlayer, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		dialog.getContentPane().add(labelPlayerTwo, gbc);

		jtfSecondPlayer.setPreferredSize(new Dimension(150, 30));
		gbc.gridx = 1;
		gbc.gridy = 1;
		dialog.getContentPane().add(jtfSecondPlayer, gbc);

		submitPlayers.setPreferredSize(new Dimension(150, 30));
		gbc.gridx = 1;
		gbc.gridy = 2;
		dialog.getContentPane().add(submitPlayers, gbc);
		submitPlayers.addActionListener(this);

		dialog.pack();
		dialog.setLocationRelativeTo(frame);
		dialog.setVisible(true);
	}

	public void submitScore(String playerOne, String playerTwo, String winner) throws SQLException {
		if (!playerOne.contentEquals("") && !playerTwo.contentEquals("")) {
			Connection conn = connect();
			String select = "SELECT count(*) FROM TicTacToe WHERE Player = ? AND Opponent = ?";
			String insert = "INSERT INTO TicTacToe (Player, Opponent, Winner, Looser) VALUES(?,?,?,?)";
			String updateWinner = "UPDATE TicTacToe SET Winner = Winner + 1 WHERE Player = ? AND Opponent = ?";
			String updateLooser = "UPDATE TicTacToe SET Looser = Looser + 1 WHERE Player = ? AND Opponent = ?";
			PreparedStatement ps = conn.prepareStatement(select);
			ps.setString(1, playerOne);
			ps.setString(2, playerTwo);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getInt(1) == 0) {
					for (int i = 0; i < 2; i++) {
						if (winner == playerOne) {
							switch (i) {
							case 0:
								PreparedStatement psInsert = conn.prepareStatement(insert);
								psInsert.setString(1, playerOne);
								psInsert.setString(2, playerTwo);
								psInsert.setInt(3, 1);
								psInsert.setInt(4, 0);
								psInsert.execute();
								break;
							case 1:
								PreparedStatement psInsertLooser = conn.prepareStatement(insert);
								psInsertLooser.setString(1, playerTwo);
								psInsertLooser.setString(2, playerOne);
								psInsertLooser.setInt(3, 0);
								psInsertLooser.setInt(4, 1);
								psInsertLooser.execute();
								break;
							}
						} else {
							switch (i) {
							case 0:
								PreparedStatement psInsert = conn.prepareStatement(insert);
								psInsert.setString(1, playerTwo);
								psInsert.setString(2, playerOne);
								psInsert.setInt(3, 1);
								psInsert.setInt(4, 0);
								psInsert.execute();
								break;
							case 1:
								PreparedStatement psInsertLooser = conn.prepareStatement(insert);
								psInsertLooser.setString(1, playerOne);
								psInsertLooser.setString(2, playerTwo);
								psInsertLooser.setInt(3, 0);
								psInsertLooser.setInt(4, 1);
								psInsertLooser.execute();
								break;
							}

						}

					}

				} else {
					for (int i = 0; i < 2; i++) {
						switch (i) {
						case 0:
							PreparedStatement psUpdate = conn.prepareStatement(updateWinner);
							psUpdate.setString(1, winner == playerOne ? playerOne : playerTwo);
							psUpdate.setString(2, winner == playerOne ? playerTwo : playerOne);
							psUpdate.executeUpdate();
							break;
						case 1:
							PreparedStatement psUpdateLooser = conn.prepareStatement(updateLooser);
							psUpdateLooser.setString(1, winner == playerOne ? playerTwo : playerOne);
							psUpdateLooser.setString(2, winner == playerOne ? playerOne : playerTwo);
							psUpdateLooser.executeUpdate();
							break;
						}
					}
				}
			}
			conn.close();
		}
	}

	public void showStatisticsDialog() throws SQLException {
		Connection conn = connect();
		Statement ps = conn.createStatement();
		ResultSet rs = ps.executeQuery("select * from tictactoe order by winner desc limit 10");
		ArrayList<ArrayList<String>> table = new ArrayList<>();
		while (rs.next()) {
			ArrayList<String> pl = new ArrayList<>();
			pl.add(rs.getString(1));
			pl.add(rs.getString(2));
			pl.add(String.valueOf(rs.getInt(3)));
			pl.add(String.valueOf(rs.getInt(4)));
			table.add(pl);
		}
		String[] tempTitel = { "Player", "Opponent", "Winners", "Loosers" };
		String[][] tempTable = new String[table.size()][];
		int i = 0;
		for (List<String> next : table) {
			tempTable[i++] = next.toArray(new String[next.size()]);
		}
		GridBagConstraints gbc = new GridBagConstraints();
		GridBagLayout gbl = new GridBagLayout();

		JTable tableStatistics = new JTable(tempTable, tempTitel);
		tableStatistics.setBounds(50, 50, 500, 500);

		dialog = new JDialog((JDialog) null, "Players");
		dialog.setPreferredSize(new Dimension(500, 300));
		dialog.getContentPane().setLayout(gbl);

		gbc.gridx = 0;
		gbc.gridy = 0;
		dialog.getContentPane().add(tableStatistics.getTableHeader());

		gbc.gridx = 0;
		gbc.gridy = 1;
		dialog.getContentPane().add(tableStatistics, gbc);

		dialog.pack();
		dialog.setLocationRelativeTo(frame);
		dialog.setVisible(true);
	}

	public Connection connect() {
		Connection conne = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conne = DriverManager.getConnection("jdbc:mysql://localhost:3306/currencyConverter?useUnicode=true&"
					+ "useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&"
					+ "useSSL=false", "root", "root");
			return conne;
		} catch (Exception e) {
			System.out.println("Konekcija na bazu nije uspostavljena!");
			e.printStackTrace();
		}
		return null;
	}

	private void btsendActionPerformed(java.awt.event.ActionEvent evt) {
		if (evt.getSource() instanceof JButton) {
			JButton buttonClicked = (JButton) evt.getSource();
			if(buttonClicked.getName().contentEquals("sendButton")) {
				// send the message to the server
				net.send(fieldMsg.getText());
				// clean the text field
				fieldMsg.setText("");
			}
			else {
			net.send("xo " + user + " " + buttonClicked.getName());
			}
		}
	}

	private void fieldMsgKeyPressed(java.awt.event.KeyEvent evt) {
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			btsendActionPerformed(null);
		}
	}

	public void addUser(User u) {
		dlm.addElement(u.getNick());
	}

	public void addMessage(String s) {
		areaMessages.append(s + "\n");
	}
	
	public void pressButton(String button) {
		for(int i = 0; i < gamePanel.getComponentCount(); i++) {
			if(gamePanel.getComponent(i).getName() != null) {
			if(gamePanel.getComponent(i).getName().contentEquals(button)) {
				JButton clbutton = (JButton) gamePanel.getComponent(i);
					clbutton.doClick();
					clbutton.setEnabled(false);
					clbutton.setBackground(Color.WHITE);
				}
			}
		}
	}

	public void clearList() {
		dlm.clear();
	}

	private void setComponentsExtras() {
		DefaultCaret caret = (DefaultCaret) areaMessages.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		jList1.setFixedCellHeight(20);
		setLocationRelativeTo(null);
		fieldMsg.requestFocus();
	}

	private String leerIP() {
		return JOptionPane.showInputDialog(null, "Enter the server IP", "127.0.0.1");
	}

	private String leerNick() {
		user = JOptionPane.showInputDialog(null, "Enter your username", "user");
		setUser(user);
		return user;
	}
}
