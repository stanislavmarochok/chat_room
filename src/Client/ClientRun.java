package Client;

import Server.*;
import javax.swing.JFrame;

public class ClientRun {
	public static void main (String[] args)
	{
		Client evilNerd;
		evilNerd = new Client("127.0.0.1");
		evilNerd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		evilNerd.startClient();
	}
}
