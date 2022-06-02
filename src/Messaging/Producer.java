package Messaging;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Producer {

	public static void main(String[] args) {
		boolean moreMessages = true;
		try {
			Socket s = new Socket("localhost", 8888);
			DataOutputStream dout = new DataOutputStream(s.getOutputStream());
			Scanner sc = new Scanner(System.in);
			dout.writeUTF("Producer");
			System.out.println("Enter your message: ");
			
			JFrame frame = new JFrame("Producer");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(300, 300);
			JPanel panel = new JPanel();
			JLabel label = new JLabel("Enter Message");
			JTextField tf = new JTextField(100);
			JButton send = new JButton("Send");
			panel.add(label); // Components Added 
			panel.add(tf);
			panel.add(send);
			frame.getContentPane().add(panel);
			frame.setVisible(true);
			send.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String input = tf.getText();
					try {
						dout.writeUTF(input);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
			while (moreMessages) {
				Scanner scanner = new Scanner(System.in);
				System.out.println("Do you want to enter more messages?");
				String more = scanner.nextLine();
				if (more.equals("yes")) {
					System.out.println("Enter your message: ");
					String message2 = scanner.nextLine();
					dout.writeUTF(message2);
				} else {
					moreMessages = false;
					dout.writeUTF("false");
					dout.flush();
					dout.close();
					s.close();
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
