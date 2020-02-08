package net.magicgames.levelbot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class Rang {


	@SuppressWarnings("resource")
	public static int get(String user) {
		File f = new File("Storage-Rang/" + user);
		if(!f.exists()) {
			create(user);
			return 0;
		}
		

		try {
			Scanner s = new Scanner(f);
			while(s.hasNextLine()) {
				return Integer.parseInt(s.nextLine());
			}
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		
		return -1;
	}
	
	public static void set(String user, int i) {
		File f = new File("Storage-Rang/" + user);
		if(!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {}
		}

		try {
			FileOutputStream out = new FileOutputStream(f);
			String s = "" + i;
			byte[] b = s.getBytes();
			out.write(b);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void create(String user) {
		set(user, 0);
	}
	
	public static void add(String user, int i) {
		int amount = get(user) + i;
		set(user,amount);
	}

	
}
