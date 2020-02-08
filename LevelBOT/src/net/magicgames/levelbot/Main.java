package net.magicgames.levelbot;

import java.io.File;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;

public class Main {

	public static void main(String[] args) {
		File f =new File("Storage");
		File f2 =new File("Storage-Rang");
		if(!f.exists()) {
			f.mkdir();
		}
		
		if(!f2.exists()) {
			f2.mkdir();
		}
		
		DefaultShardManagerBuilder build = new DefaultShardManagerBuilder();
		build.setToken("ZENSIERT");
		build.addEventListeners(new EventListener());

		

		
		try {
			build.build();
		} catch (LoginException | IllegalArgumentException e) {}
	}

}
