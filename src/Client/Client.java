package Client;

import Server.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame {
	private JTextField userInputText;
	private JTextArea chatWindow;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	private String message = "";
	private String serverIP;
	private Socket socket;
	
	//constructor
	public Client(String host) {
		super ("Клиентская часть");
		serverIP = host;
		userInputText = new JTextField();
		userInputText.setEditable(false);
		userInputText.addActionListener(
					new ActionListener() {
						@Override
						public void actionPerformed (ActionEvent e) {
							sendMessage(e.getActionCommand());
							userInputText.setText("");
						}
					}
				);
		add(userInputText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		chatWindow.setBackground(Color.LIGHT_GRAY);
		add (new JScrollPane(chatWindow), BorderLayout.CENTER);
		setSize(300, 600);
		setVisible(true);
	}
	//starting of client
	public void startClient () {
		try {
			connectToServer();
			setupStreams();
			whileChatting();
		}
		catch(EOFException e) {
			showMessage ("\nКлиент поравл соединение");
		}
		catch (IOException c)
		{
			c.printStackTrace();
		}finally {
			closeConnection();
		}
	}
}
