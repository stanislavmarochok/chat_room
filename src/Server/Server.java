package Server;

import java.io.*;
import javax.swing.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

public class Server extends JFrame{
	private JTextField userInputText;
	private JTextArea chatWindow;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	private ServerSocket serverSocket;
	private Socket connection;
	
	public Server () {
		super ("Серверная часть.");
		userInputText = new JTextField();
		userInputText.setEditable(false);
		userInputText.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						sendMessage(e.getActionCommand());
						userInputText.setText("");
					}
				}
			);		
		add(userInputText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		 add (new JScrollPane(chatWindow));
		 setSize (300, 600);
		 setVisible (true);
	}
}
