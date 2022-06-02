package Messaging;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Consumer {

	public static void main(String[] args) {
		
		try {
			Socket s = new Socket("localhost", 8888);
			Scanner sc = new Scanner(System.in);
			DataOutputStream dout = new DataOutputStream(s.getOutputStream());
			DataInputStream dis = new DataInputStream(s.getInputStream());
			dout.writeUTF("Consumer");
			JFrame frame = new JFrame("Consumer");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(300, 300);
			JPanel panel = new JPanel(); // the panel is not visible in output
			JButton refreshbtn = new JButton("Refresh");
			panel.add(refreshbtn);
			frame.getContentPane().add(panel); // Adds Button to content pane of frame
			frame.setVisible(true);
			refreshbtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						dout.writeUTF("yes");
						boolean read= true;
						while(read) {
							String output= dis.readUTF();
							if(output.equals("NO MORE")) {
								read= false;
								System.out.println("NO MORE");
								JOptionPane.showMessageDialog(null, String.format("NO MORE", 175, 175));
							}
							else {
								System.out.println(output);
								JOptionPane.showMessageDialog(null, String.format(output, 175, 175));
							}
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
			boolean refresh = true;
			while (refresh) {
				System.out.println("Do you want to read new messages?");
				String more = sc.nextLine();
				if (more.equals("yes")) {
					dout.writeUTF(more);
					boolean read= true;
					while(read) {
						String output= dis.readUTF();
						if(output.equals("NO MORE")) {
							read= false;
						}
						else {
							System.out.println(output);
						}
					}
					
				} else {
					refresh = false;
					dout.flush();
					dout.close();
					s.close();
				}
			}
			dout.flush();
			dout.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
