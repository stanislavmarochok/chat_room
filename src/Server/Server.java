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
	//setting up and starting the server's part of the programm
	public void startServer()
	{
		try	{
			serverSocket = new ServerSocket (7777, 100);
			while (true)
			{
				try {
					waitForConnection();
					setupStreams();
					whileChatting ();
				}
				catch (EOFException eofException) {
					showMessage ("\nСервер оборвал соединение!!!\n");
				}finally {
					closeConnection ();
				}
			}
		}
		catch(IOException ioException)
		{
			ioException.printStackTrace();
		}
	}
	//waiting for connection and printing all the info about connecting
	private void waitForConnection () throws IOException {
		showMessage("\nОжидание подключения клиентов...\n");
		connection = serverSocket.accept();
		showMessage("\nСоединен с \n" + connection.getInetAddress().getHostName());
	}
	//setting up all the streams for sending and getting data
	private void setUpStreams () throws IOException{
		outputStream = new ObjectOutputStream (connection.getOutputStream());
		outputStream.flush();
		inputStream = new ObjectInputStream (connection.getInputStream());
		showMessage ("\nПоток установлен!!!\n");
	}
	//checking data during the chatting
	private void whileChatting () throws IOException{
		String message = "Вы подключенны!!";
		sendMessage (message);
		readyToType(true);
		do {
			try {
				message = (String) inputStream.readObject();
				showMessage("\n" + message);
			}
			catch(ClassNotFoundException c) {
				showMessage("\nНе пойму что за хрень отправил пользователь!!!");
				
			}
		}while (!message.equals("КЛИЕНТ - *"));
		
	}
}
