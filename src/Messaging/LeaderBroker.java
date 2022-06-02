package Messaging;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class LeaderBroker {

	public static void main(String[] args) {
		File partition1 = new File(
				"C:\\Users\\shahd\\Desktop\\Assignments\\CS454_A1_20186046_20186053\\Partitions\\Partition1.txt");
		File partition2 = new File(
				"C:\\Users\\shahd\\Desktop\\Assignments\\CS454_A1_20186046_20186053\\Partitions\\Partition2.txt");
		int files = 2;
		boolean morePartitions = true;
		try {
			partition1.createNewFile();
			partition2.createNewFile();

			while (morePartitions) {
				System.out.println("There are " + files + " partitions, do you want more?");
				Scanner sc = new Scanner(System.in);
				String message = sc.nextLine();
				if (message.equals("yes")) {
					System.out.println("How many?");
					int partitions = sc.nextInt();
					for (int i = files + 1; i <= files + partitions; i++) {
						File file = new File(
								"C:\\Users\\shahd\\Desktop\\Assignments\\CS454_A1_20186046_20186053\\Partitions\\Partition"
										+ i + ".txt");
						file.createNewFile();
					}
					files = files + partitions;
				} else {
					morePartitions = false;
				}
			}
			ServerSocket ss = new ServerSocket(8888);
			boolean bool= true;
			while (bool) {
				Socket s = ss.accept();// establishes connection
				DataInputStream dis = new DataInputStream(s.getInputStream());
				DataOutputStream dout = new DataOutputStream(s.getOutputStream());
				String type = dis.readUTF();

				Thread thread = new Multithread(type, dis, files, dout);
				thread.start();
			}
			ss.close();

		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
