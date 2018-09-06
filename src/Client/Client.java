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
		socket = new Socket (InetAddress.getByName(serverIP), 7777);
		showMessage ("Теперь ты подключен к:" + socket.getInetAddress().getHostName());
	}
	//setting up streams for sending and getting messages
	private void setupStreams () throws IOException {
		outputStream = new ObjectOutputStream(socket.getOutputStream());
		outputStream.flush();
		inputStream = new ObjectInputStream(socket.getInputStream());
		showMessage ("\nПотоки готовы к работе!");
	}
	//checking data during chatting
	private void whileChatting() throws IOException {
		readyToType(true);
		do {
			try {
				message = (String) inputStream.readObject();
				showMessage ("\n " + message);
			}
			catch(ClassNotFoundException bla) {
				showMessage ("\nНепонятно!!!");
			}
		}while(!message.equals("СЕРВЕР - *"));
	}
	//closing all the streams and sockets
	private void closeConnection() {
		showMessage("\nЗакрываем соединение...");
		readyToType(false);
		try {
			outputStream.close();
			inputStream.close();
			socket.close();
		}
		catch(IOException c) {
			c.printStackTrace();
		}
	}
	//sending messages on server
	private void sendMessage (String message) {
		try {
			outputStream.writeObject("КЛИЕНТ - " + message);
			outputStream.flush();
			showMessage("\nКЛИЕНТ - " + message);
		}
		catch(IOException c){
			chatWindow.append("\nЧто-то пошло не так во время отправки сообщения...");
		}
	}
	//updating chat window
	private void showMessage (final String msg) {
		SwingUtilities.invokeLater(
				new Runnable() {
					@Override
					public void run() {
						chatWindow.append(msg);
					}
				}
			);
	}
}
