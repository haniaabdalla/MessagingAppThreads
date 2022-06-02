package Messaging;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Multithread extends Thread{

	String type;
	int files;
	DataInputStream dis;
	DataOutputStream dout;
	
	Multithread(String type, DataInputStream dis, int files, DataOutputStream dout) {
		this.type=type;
		this.dis=dis;
		this.files=files;
		this.dout=dout;
	}
	
	public void run() {
		try {
			if (type.equals("Producer")) {
				boolean hania = true;
				while (hania) {
					for (int i = 1; i <= files; i++) {
						String word= dis.readUTF();
						if (word.equals("false")) {
							hania = false;
						} else {
							FileWriter myWriter = new FileWriter(
									"C:\\Users\\shahd\\Desktop\\Assignments\\CS454_A1_20186046_20186053\\Partitions\\Partition"
											+ i + ".txt",
									true);
							BufferedWriter buff = new BufferedWriter(myWriter);
							buff.write(Count.counter + " " + word + "\n");
							Count.counter++;
							// buff.newLine();
							buff.close();
							// System.out.println("message= " + str);
						}
					}
				}
			} else if (type.equals("Consumer")) {
				ArrayList<String> newArr = new ArrayList<String>();
				ArrayList<String> oldArr = new ArrayList<String>();
				Scanner sc = new Scanner(System.in);
				boolean refresh = true;
				while (refresh) {
					String word= dis.readUTF();
					if (word.equals("yes")) {
						
						for (int i = 1; i <= files; i++) {
							File file = new File(
									"C:\\Users\\shahd\\Desktop\\Assignments\\CS454_A1_20186046_20186053\\Partitions\\Partition"
											+ i + ".txt");
							Scanner scanner = new Scanner(file);
							boolean eof = scanner.hasNextLine();
							while (eof) {
								newArr.add(scanner.nextLine());
								eof = scanner.hasNextLine();
							}
						}
						if (oldArr.size() == 0) {
							for (int i = 0; i < newArr.size(); i++) {
								dout.writeUTF(newArr.get(i));
							}
							dout.writeUTF("NO MORE");
							oldArr.addAll(newArr);
							newArr.clear();
						} else {
							newArr.removeAll(oldArr);
							if (newArr.size() == 0) {
								dout.writeUTF("NO MORE");
							} else {
								for (int i = 0; i < newArr.size(); i++) {
									dout.writeUTF(newArr.get(i));
								}
								dout.writeUTF("NO MORE");
								oldArr.addAll(newArr);
								newArr.clear();
							}
						}
					} else {
						refresh = false;
					}
				}
			} else {
				System.out.println("No such type.");
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
