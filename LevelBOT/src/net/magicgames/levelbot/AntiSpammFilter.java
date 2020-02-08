package net.magicgames.levelbot;

import java.util.ArrayList;

public class AntiSpammFilter extends Thread{

	public static int delay = 3;
	public static ArrayList<String> antiSpamm = new ArrayList<String>();
	
	private String user;
	private int playerDelay = 0;
	
	public AntiSpammFilter(String user) {
		this.user = user;
	}
	
	public AntiSpammFilter(String user, int i) {
		this.user = user;
		this.playerDelay = i;
	}
	
	public void run() {
		try {
			int delay_ = delay + playerDelay;
			String waitD = delay_ + "000";
			Thread.sleep(Integer.parseInt(waitD));
			if(antiSpamm.contains(user)) antiSpamm.remove(user);
		}catch(Exception e) {}
	}
}
