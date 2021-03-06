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
		super ("��������� �����.");
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
					setUpStreams();
					whileChatting ();
				}
				catch (EOFException eofException) {
					showMessage ("\n������ ������� ����������!!!\n");
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
		showMessage("\n�������� ����������� ��������...\n");
		connection = serverSocket.accept();
		showMessage("\n�������� � \n" + connection.getInetAddress().getHostName());
	}
	//setting up all the streams for sending and getting data
	private void setUpStreams () throws IOException{
		outputStream = new ObjectOutputStream (connection.getOutputStream());
		outputStream.flush();
		inputStream = new ObjectInputStream (connection.getInputStream());
		showMessage ("\n����� ����������!!!\n");
	}
	//checking data during the chatting
	private void whileChatting () throws IOException{
		String message = "�� �����������!!";
		sendMessage (message);
		readyToType(true);
		do {
			try {
				message = (String) inputStream.readObject();
				showMessage("\n" + message);
			}
			catch(ClassNotFoundException c) {
				showMessage("\n�� ����� ��� �� ����� �������� ������������!!!");
				
			}
		}while (!message.equals("������ - *"));	
	}
	//close all the sockets and stream when user finished chatting
	private void closeConnection () {
		showMessage ("\n�������� ����������");
		readyToType (false);
		try {
			outputStream.close();
			inputStream.close();
			connection.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	//sending a message to client
	private void sendMessage (String message)
	{
		try {
			outputStream.writeObject("������ - " + message);
			outputStream.flush();
			showMessage("\n������ - " + message);
		}
		catch(IOException e){
			chatWindow.append("\n������: �����, � �� ���� ��� ���������!!!");
		}
	}
	//updating thwe chat windows
	private void showMessage (final String text){
		SwingUtilities.invokeLater(
			new Runnable() {
				
				@Override
				public void run(){
					 chatWindow.append(text);
				}				
			}
			);
		}
	//setting up right for input of the data
	private void readyToType(final boolean trueOrFalse ) {
		SwingUtilities.invokeLater(
					new Runnable () {
						
						@Override
						public void run() {
							userInputText.setEditable(trueOrFalse);
						}
					}
				);
	}
}
