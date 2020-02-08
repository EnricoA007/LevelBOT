package net.magicgames.levelbot;

import java.awt.Color;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventListener extends ListenerAdapter{

	ArrayList<String> inUsers = new ArrayList<String>();
	
	@Override
	public void onMessageReceived(MessageReceivedEvent e) {
		String msg = e.getMessage().getContentDisplay();
		User user = e.getAuthor();
		MessageChannel ch = e.getChannel();
		
		if(msg.equalsIgnoreCase("!xp")) {

			
			
			EmbedBuilder build = new EmbedBuilder();
			build.setTitle("Erfahrungspunkte");
			build.setColor(Color.red);
			build.setDescription(user.getAsMention());
			build.setThumbnail(user.getDefaultAvatarUrl());
			build.addField("Level", XP.get(user.getAsMention()) + "/100", true);
			build.addField("Rang", ""+Rang.get(user.getAsMention()), true);
			
			
			build.setFooter("Sammel Erfahrungen in dem du auf diesen Discord Server schreibst.");
			
			ch.sendMessage(build.build()).queue();
		}else if(msg.equalsIgnoreCase("!help"))  {
			
			EmbedBuilder builder = new EmbedBuilder();
			
			builder.setTitle("Hilfe");
			builder.setColor(Color.green);
			builder.addField("Erfahrung & Rang anzeigen", "!xp", true);
			builder.addField("Top 3 auf dem Server anzeigen", "!topliste", true);
			builder.addField("Spamm Verzögerung anzeigen bzw. setzen", "!spamdelay", true);
			builder.addField("Diese Hilfe anzeigen","!help",true);
			builder.setDescription("Alle Befehle sind hier aufgelistet.");
			
			
			ch.sendMessage(builder.build()).queue();
			
			
		}else if(msg.equalsIgnoreCase("!topliste")) {
			SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
			String date= format.format(new Date());
			
			HashMap<String, Integer> userTopList = new HashMap<String,Integer>();
			ArrayList<String> userTopList_Contain = new ArrayList<String>();
			
			File f = new File("Storage-Rang");
			for(File files : f.listFiles()) {
				try {
					Scanner s = new Scanner(files);
					String line = s.nextLine();
					userTopList.put(files.getName(), Integer.parseInt(line));
					userTopList_Contain.add(files.getName());
					s.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			
			String user1 = "Niemand";
			String user2 = "Niemand";
			String user3 = "Niemand";
			
			
			
			if(!userTopList.isEmpty()) {
				for(String us : userTopList_Contain) {
					int rank = userTopList.get(us); // In der Liste von dem Rang
					if(user1.equals("Niemand")) { // User 1 noch nicht gesetzt
						user1  = us; // Neuen User setzen
					}else {
						int active = userTopList.get(user1); // Welchen Rang der schon gesetzter User 1 hat
						
						if(active > rank) { // Den Rang von User 1 | Größer als | Von dem in der Liste
							if(user2.equals("Niemand")) { // User 2 noch nicht gesetzt
								user2 = us; // Neuen User setzen
							}else {
								int active_2 = userTopList.get(user2); // Welchen Rang der schon gesetzter User 2 hat
								if(active_2 > rank) { // Den Rang von User 2 | Größer als | Von dem in der Liste
									if(user3.equals("Niemand")) { // User 3 noch nicht gesetzt
										user3 = us; // Neuen User setzen
									}else {
										int active_3 = userTopList.get(user3); //Welchen Rang der schon gesetzter User 3 hat
										if(active_3 < rank) { //Den Rang von User 3 | Größer als | Von dem in der Liste
											user3 = us; // Neuen User setzen
										}
									}
								}
							}
						}else {
							user1 = us;
						}
						
					}
				}
			}
 			
			
			
			
			EmbedBuilder build = new EmbedBuilder();
			build.setTitle("Topliste");
			build.setColor(Color.cyan);
			build.setFooter("Liste vom " + date);
			
			if(user3.equals("Niemand")) {
				build.addField("Top 3", "Niemand", true);
			}else {
				build.addField("Top 3", user3 + " | Rang: " + userTopList.get(user3),true);
			}
			if(user2.equals("Niemand"))	{
				build.addField("Top 2", "Niemand", true);
			}else {
				build.addField("Top 2", user2 + " | Rang: " + userTopList.get(user2),true);
			}
			if(user1.equals("Niemand"))	{
				build.addField("Top 1", "Niemand", true);
			}else {
				build.addField("Top 1", user1 + " | Rang: " + userTopList.get(user1),true);
			}

			
			build.setDescription("Top 3 auf dem MyPvP - Discord Server");
			
			ch.sendMessage(build.build()).queue();
		}else if(msg.startsWith("!spamdelay")) {
			try {
				String[] args = msg.split(" ");
				int i = Integer.parseInt(args[1]);
				boolean b = false;
				for(Role r : e.getMember().getRoles()) {
					if(r.getName().equalsIgnoreCase("Admin")) {
						b =true;
					}
				}
				if(b) {
					ch.sendMessage("Das Delay wurde auf " + args[1] + " gesetzt!").queue();
					AntiSpammFilter.delay = i;
				}else {
					ch.sendMessage(user.getAsMention() + " du besitzt keine 'Admin Rolle' !").queue();
				}
			}catch(NumberFormatException f) {
				ch.sendMessage(user.getAsMention() + " du musst eine richtige Zahl eingeben!").queue();
			
			}catch(IndexOutOfBoundsException c) {
				ch.sendMessage(user.getAsMention() + " der Delay ist aktuell auf: " + AntiSpammFilter.delay).queue();
			}
		}else {

			if(!user.isBot()) {
				if(!AntiSpammFilter.antiSpamm.contains(user.getAsMention())) {
					AntiSpammFilter.antiSpamm.add(user.getAsMention());
					new AntiSpammFilter(user.getAsMention()).start();
					Random rdm = new Random();
					XP.add(user.getAsMention(), rdm.nextInt(10));
					
					int xp = XP.get(user.getAsMention());
					
					if(xp >= 100) {
						XP.set(user.getAsMention(), 0);
						Rang.add(user.getAsMention(), 1);
						ch.sendMessage(user.getAsMention() + " Glückwunsch! Du bist nun Rang " + Rang.get(user.getAsMention()) + "!").queue();
					}
					
				}
			}
			

			
			
		}
		
	}
	


}
