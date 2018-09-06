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
	//connect to server
	private void connectToServer () throws IOException{
		showMessage ("\nПытаемся подключиться...\n");
		socket = new Socket (InetAddress.getByName(servverIP), 7777);
		showMessage ("Теперь ты подключен к:" + socket.getInetAddress().getHostName();
	}
	//setting up streams for sending and getting messages
	private void setupStreams () throws IOException {
		outputStream = new ObjectOutputStream(socket.getOutputStream());
		outputStream.flush();
		inputStream = new ObjectInputStream(socket.getInputStream());
		showMessage ("\nПотоки готовы к работе!");
	}
}
