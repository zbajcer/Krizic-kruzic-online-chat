import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.json.JSONArray;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

@SuppressWarnings("serial")
public class KrizicKruzic extends JFrame implements ActionListener {

	private JPanel contentPane;
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
	JTextField jtfFirstPlayer = new JTextField();
	JTextField jtfSecondPlayer = new JTextField();
	public static KrizicKruzic frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new KrizicKruzic();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public KrizicKruzic() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 600);

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
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridLayout gl_contentPane = new GridLayout(3, 3);
		gl_contentPane.setHgap(5);
		gl_contentPane.setVgap(5);
		contentPane.setLayout(gl_contentPane);

		firstButton = new JButton("");
		firstButton.setFont(new Font("Tahoma", Font.PLAIN, 99));
		contentPane.add(firstButton);
		firstButton.addActionListener(this);

		secondButton = new JButton("");
		secondButton.setFont(new Font("Tahoma", Font.PLAIN, 99));
		contentPane.add(secondButton);
		secondButton.addActionListener(this);

		thirdButton = new JButton("");
		thirdButton.setFont(new Font("Tahoma", Font.PLAIN, 99));
		contentPane.add(thirdButton);
		thirdButton.addActionListener(this);

		fourthButton = new JButton("");
		fourthButton.setFont(new Font("Tahoma", Font.PLAIN, 99));
		contentPane.add(fourthButton);
		fourthButton.addActionListener(this);

		fifthButton = new JButton("");
		fifthButton.setFont(new Font("Tahoma", Font.PLAIN, 99));
		contentPane.add(fifthButton);
		fifthButton.addActionListener(this);

		sixthButton = new JButton("");
		sixthButton.setFont(new Font("Tahoma", Font.PLAIN, 99));
		contentPane.add(sixthButton);
		sixthButton.addActionListener(this);

		seventhButton = new JButton("");
		seventhButton.setFont(new Font("Tahoma", Font.PLAIN, 99));
		contentPane.add(seventhButton);
		seventhButton.addActionListener(this);

		eightButton = new JButton("");
		eightButton.setFont(new Font("Tahoma", Font.PLAIN, 99));
		contentPane.add(eightButton);
		eightButton.addActionListener(this);

		ninethButton = new JButton("");
		ninethButton.setFont(new Font("Tahoma", Font.PLAIN, 99));
		contentPane.add(ninethButton);
		ninethButton.addActionListener(this);

	}

	public boolean isFirstPlayer = true;
	private JMenuBar menuBar;
	private JMenu mnGame;
	private JMenu mnPlayers;
	public JDialog dialog;
	public String firstPlayer = "";
	public String secondPlayer = "";
	public String winner = "";
	public String looser = "";
	public int scoreFirstPlayer = 0;
	public int scoreSecondPlayer = 0;

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JButton) {
			JButton buttonClicked = (JButton) e.getSource();
			switch (buttonClicked.getText()) {
			case "":
				if (isFirstPlayer == true) {
					buttonClicked.setText("X");
					if (checkWinningCombination()) {
						scoreFirstPlayer += 1;
						winner = firstPlayer;
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
				dialog.setVisible(false);
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
		int noOfComponents = contentPane.getComponentCount();
		for (int i = 0; i < noOfComponents; i++) {
			contentPane.getComponent(i).setEnabled(false);
		}

	}

	public void newGame() {
		isFirstPlayer = true;
		int noOfComponents = contentPane.getComponentCount();
		for (int i = 0; i < noOfComponents; i++) {
			contentPane.getComponent(i).setEnabled(true);
			if (contentPane.getComponent(i) instanceof JButton) {
				JButton set = (JButton) contentPane.getComponent(i);
				set.setText("");
				set.setBackground(null);
			}

		}
	}

	public void showPlayersDialog() {
		GridBagConstraints gbc = new GridBagConstraints();
		GridBagLayout gbl = new GridBagLayout();
		JLabel labelFirstPlayer = new JLabel("First player ('X'):  ");
		JLabel labelSecondPlayer = new JLabel("Second player ('O'):  ");
		JButton submitPlayers = new JButton("Submit players");

		dialog = new JDialog((JDialog) null, "Players");
		dialog.setPreferredSize(new Dimension(400, 200));
		dialog.setLayout(gbl);

		dialog.add(labelFirstPlayer);
		gbc.gridx = 0;
		gbc.gridy = 0;
		dialog.add(labelFirstPlayer, gbc);

		dialog.add(jtfFirstPlayer);
		jtfFirstPlayer.setPreferredSize(new Dimension(150, 30));
		gbc.gridx = 1;
		gbc.gridy = 0;
		dialog.add(jtfFirstPlayer, gbc);

		dialog.add(labelSecondPlayer);
		gbc.gridx = 0;
		gbc.gridy = 1;
		dialog.add(labelSecondPlayer, gbc);

		dialog.add(jtfSecondPlayer);
		jtfSecondPlayer.setPreferredSize(new Dimension(150, 30));
		gbc.gridx = 1;
		gbc.gridy = 1;
		dialog.add(jtfSecondPlayer, gbc);

		dialog.add(submitPlayers);
		submitPlayers.setPreferredSize(new Dimension(150, 30));
		gbc.gridx = 1;
		gbc.gridy = 2;
		dialog.add(submitPlayers, gbc);
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
		ResultSet rs = ps.executeQuery("select * from tictactoe");
		ArrayList<ArrayList<String>> table = new ArrayList<>();
		while (rs.next()) {
			ArrayList<String> pl = new ArrayList<>();
			pl.add(rs.getString(1));
			pl.add(rs.getString(2));
			pl.add(String.valueOf(rs.getInt(3)));
			pl.add(String.valueOf(rs.getInt(4)));
			table.add(pl);
		}
		String[] tempTitel = {"Player","Opponent","Winners","Loosers"};
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
		dialog.setLayout(gbl);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		dialog.add(tableStatistics.getTableHeader());

		gbc.gridx = 0;
		gbc.gridy = 1;
		dialog.add(tableStatistics, gbc);

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

}
