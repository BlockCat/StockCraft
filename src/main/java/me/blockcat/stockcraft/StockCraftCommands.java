/**
 * StockCraft
 *
 * @author Dominik Uphoff
 */

package me.blockcat.stockcraft;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.blockcat.stockcraft.connectUtils.DatabaseHandler;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class StockCraftCommands {

	Logger log = Logger.getLogger("Minecraft");	
	private static StockCraft plugin;
	public StockCraftCommands(StockCraft instance) {
		plugin = instance;
	}
	private static volatile StockCraftCommands instance;
	public static StockCraftCommands  getInstance() {
		if (instance == null) {
			instance = new StockCraftCommands(plugin);
		}
		return instance;
	}

	public static String idchange(String longid) throws SQLException{
		String shortid = null;
		//Statement statement = (Statement) StockCraftDatabase.conn.createStatement();
		//if(statement != null)
		{
			ResultSet resultset = null;
			String sql = "SELECT shortid FROM idtable WHERE longid ='"+longid+"'";
			try {
				//resultset = statement.executeQuery(sql);
				resultset = DatabaseHandler.executeWithResult(sql);

				while (resultset.next()) {
					shortid = resultset.getString("shortid");					
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		return shortid;
	}
	public String[] loadidlist(Player player,String loc)
	{
		String[] texti = null;
		try{
			FileReader readFile = new FileReader(loc);
			String text = "";    
			Scanner scanner = new Scanner(readFile);
			/*while((text = rf.readLine()) != null){
				stringBuffer.append(text);            	
			}*/
			while (scanner.hasNextLine()) {
				text += scanner.nextLine() + ":";
			}
			//text = String.valueOf(stringBuffer);
			texti = text.split(":"); 
			scanner.close();
			//rf.close();
		}
		catch(Exception e){
			log.log(Level.SEVERE, "There is no file "+loc+"!", e);        	    
		}
		return texti;
	}

	public void writetxt(String loc,PlayerCommandPreprocessEvent event)
	{
		Player player = event.getPlayer();
		try{
			FileReader readfile = new FileReader(loc);
			Scanner scanner = new Scanner(readfile);

			while(scanner.hasNextLine()) {
				String line = ChatColor.translateAlternateColorCodes('&', scanner.nextLine());
				player.sendMessage(line);
			}
			scanner.close();			
		}
		catch(Exception e){
			log.log(Level.SEVERE, "File not found");
			File f = new File(loc);
			try {
				f.createNewFile();
				FileWriter fw = new FileWriter(f);
				fw.write("&a---STOCKHELP---\n" +
						"&dRemember that there is no course change on weekends and that there are trading times!\n" +
						"&6/stock ids [page] -> to check the available stocks (try /ids s [page])\n" +
						"&e/stock course [id] -> to check the course of a stock\n" +
						"&6/stock list -> to check your stocks\n" +
						"&e/stock buy [id] [amount] -> to buy stocks\n" +
						"&6/stock sell [id] [amount] -> to sell stocks\n" +
						"&b- For Admins -\n" +
						"&e/stock add -> to add stocks\n" +
						"&6/stock removeid -> to remove stocks\n" +
						"&e/stock addlist <list> -> add a list with stocks");
				fw.close();
				this.writetxt(loc, event);
			} catch (IOException e1) {
			}
		}
	}

	public static String idback(String shortid) throws SQLException{
		String longid = null;
		//Statement statement = (Statement) StockCraftDatabase.conn.createStatement();
		//if(statement != null)
		{
			ResultSet resultset = null;
			String sql = "SELECT longid FROM idtable WHERE shortid ='"+shortid+"'";
			try {
				resultset = DatabaseHandler.executeWithResult(sql);
				while (resultset.next()) {
					longid = resultset.getString("longid");		
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		return longid;
	}
	public void infosystem(String[] split, Player player, PlayerCommandPreprocessEvent event) throws SQLException{

		if (split.length <= 0) {
			event.setCancelled(true);
			if(StockCraftPermissions.getInstance().stockhelp(player))
			{
				String rulesloc = "plugins/StockCraft/stockhelp.txt";
				writetxt(rulesloc,event);  
			}
			else{
				player.sendMessage(ChatColor.RED+"You don't have permission!");
			}			
			return;
		} else if(split[0].equalsIgnoreCase("course") && split.length > 1) {
			this.Course(player, split);
		} else if(split[0].equalsIgnoreCase("list") || split[0].equalsIgnoreCase("own")) {
			event.setCancelled(true); 
			if(StockCraftPermissions.getInstance().stocks(player))
			{
				//Statement statement = (Statement) StockCraftDatabase.conn.createStatement();
				//if(statement != null)
				{
					ResultSet resultset = null;
					int amountof = 0;
					float sumpaid = 0;
					String id = null;
					String shortid = null;
					String course = null;

					String sql = "SELECT stockname,amount,sumpaid FROM stocks WHERE name ='"+player.getName()+"'";
					try {
						//resultset = statement.executeQuery(sql);
						resultset = DatabaseHandler.executeWithResult(sql);
						try {
							Boolean bought = false;
							int l = 0;
							while (resultset.next()) {
								l++;
							}

							resultset.beforeFirst();
							String[] idList = new String[l];
							float[] sumPaidList = new float[l];
							int[] amountOfList = new int[l];

							for (int i = 0;resultset.next();i++) {
								id = resultset.getString("stockname");	
								sumPaidList[i] = Float.valueOf(resultset.getString("sumpaid"));
								amountOfList[i] = Integer.valueOf(resultset.getString("amount"));	
								if(id != null){
									bought = true; 
								}
								shortid = idchange(id);
								idList[i] = shortid;
							}

							if(bought== false) {
								player.sendMessage(ChatColor.RED+"You don't have stocks -> buy some first!");
							} else {
								String[] gtext = StockCraftDatabase.getcourse(idList);
								float profit = 0;
								for(int i = 0;i<idList.length;i++)
								{
									course = gtext[i];
									amountof = amountOfList[i];
									sumpaid = sumPaidList[i];
									id = idback(idList[i]);
									float avp = 0;
									if(amountof > 0){								
										profit = Float.valueOf(course) - (sumpaid/amountof);
										avp = (sumpaid/amountof);
									}
									else if(amountof < 0){
										profit = Float.valueOf(course) - (sumpaid/(-amountof));
										profit = -profit;
										avp = (sumpaid/-amountof);
									}

									if(profit > 0){
										player.sendMessage(ChatColor.YELLOW+id+ChatColor.LIGHT_PURPLE+" amount: "+amountof+ChatColor.GREEN+" average paid: "+avp+ChatColor.BLUE+" course: "+course+ChatColor.GREEN+" +"+profit);
									}
									else if (profit == 0)
									{
										player.sendMessage(ChatColor.YELLOW+id+ChatColor.LIGHT_PURPLE+" amount: "+amountof+ChatColor.GREEN+" average paid: "+avp+ChatColor.BLUE+" course: "+course+ChatColor.WHITE+" "+profit);
									}
									else if (profit < 0)
									{
										player.sendMessage(ChatColor.YELLOW+id+ChatColor.LIGHT_PURPLE+" amount: "+amountof+ChatColor.GREEN+" average paid: "+avp+ChatColor.BLUE+" course: "+course+ChatColor.RED+" "+profit);
									}
								}
							}
						}
						catch (SQLException e) {
							e.printStackTrace();
						}
					}
					catch (SQLException e) {
						e.printStackTrace();
					}				
				}
			}
			else{
				player.sendMessage(ChatColor.RED+"You don't have permission!");
			}
		} else if(split[0].equalsIgnoreCase("ids")) {
			event.setCancelled(true); 
			ids.idscommand(player, split);

		} else if(split[0].equalsIgnoreCase("sell")) {
			event.setCancelled(true);
			if (split.length > 1){
				stocksell.stocksellcommand(player, split);	
			} else {
				player.sendMessage(ChatColor.RED + "No stock selected.");
			}

		} else if(split[0].equalsIgnoreCase("buy")) {
			event.setCancelled(true); 
			if (split.length > 1) {
				stockbuy.stockbuycommand(player, split);
			} else {
				player.sendMessage(ChatColor.RED + "No stock selected.");
			}

		} else if(split[0].equalsIgnoreCase("add")) {
			event.setCancelled(true); 
			if(StockCraftPermissions.getInstance().addid(player)){		
				if(split.length > 2){
					StockCraftDatabase.idadd(player, split[1], split[2]);
				}				
				else{
					player.sendMessage(ChatColor.RED+"/addid [name] [id]");
					player.sendMessage(ChatColor.RED+"name = The name you want to use for the stock");
					player.sendMessage(ChatColor.RED+"id = The id from 'finance.yahoo.com' (BMW -> BMW.DE)");
					player.sendMessage(ChatColor.RED+"Example: /addid BMW BMW.DE");
				}
			}

		} else if(split[0].equalsIgnoreCase("addlist")) {
			event.setCancelled(true); 
			if(StockCraftPermissions.getInstance().addid(player)){		
				if(split.length > 1){
					String loc = "plugins/StockCraft/" + split[1] +".txt";
					String[] list = loadidlist(player, loc);

					for(int i = 0;i<list.length;i=i+2) {
						StockCraftDatabase.idadd(player, list[i], list[i+1]);
					}
				}				
				else{
					player.sendMessage(ChatColor.RED+"/addidlist [name]");
					player.sendMessage(ChatColor.RED+"name = The name of the list in your plugins/StockCraft/ directory");
					player.sendMessage(ChatColor.RED+"If the file is named mylist.txt the name is mylist");
					player.sendMessage(ChatColor.RED+"Example: /addidlist mylist");
				}
			}

		} else if(split[0].equalsIgnoreCase("remove")) {
			event.setCancelled(true); 
			if(StockCraftPermissions.getInstance().removeid(player)){		
				if(split.length > 1){
					//Statement statement = (Statement) StockCraftDatabase.conn.createStatement();
					//if(statement != null)
					{	
						try {
							ResultSet resultset = null;
							String sql = "SELECT longid FROM idtable WHERE longid='"+split[1]+"'";
							//resultset = statement.executeQuery(sql);
							resultset = DatabaseHandler.executeWithResult(sql);
							String longid = "";
							while (resultset.next()) {
								longid = resultset.getString("longid");						
							}
							if(longid.toLowerCase().equals(split[1].toLowerCase())){
								sql = "SELECT name FROM stocks WHERE stockname='"+longid+"'";
								//resultset = statement.executeQuery(sql);
								resultset = DatabaseHandler.executeWithResult(sql);
								String name = null;
								while (resultset.next()) {
									name = resultset.getString("name");	
									player.sendMessage(ChatColor.RED+name+" has still stocks from "+longid+"!");
								}
								sql = "DELETE FROM idtable WHERE longid='"+split[1]+"'";
								//statement.execute(sql);
								DatabaseHandler.execute(sql);
								player.sendMessage(ChatColor.GREEN+longid+" successfully deleted!");
							}
							else{
								player.sendMessage(ChatColor.RED+longid+"There is no stock "+split[1]+"!");
							}

						} catch (SQLException e1) {
							e1.printStackTrace();
						}

					}
				}

				else{
					player.sendMessage(ChatColor.RED+"/removeid [name]");
					player.sendMessage(ChatColor.RED+"name = The name of the stock you want to remove");
					player.sendMessage(ChatColor.RED+"Example: /removeid BMW");
				}
			}

		} else if(split[0].equalsIgnoreCase("top")){
			event.setCancelled(true);
			if(StockCraftPermissions.getInstance().stocktop(player))
			{
				//Statement statement = (Statement) StockCraftDatabase.conn.createStatement();
				//if(statement != null)
				{	
					try {
						ResultSet resultset = null;
						String sql = "SELECT name,profit FROM stockstats ORDER BY profit DESC";
						//resultset = statement.executeQuery(sql);
						resultset = DatabaseHandler.executeWithResult(sql);
						String name = "";
						String profit = "";
						player.sendMessage(ChatColor.GREEN+"--- Stocks profit toplist ---");
						for(int i = 1;resultset.next() && i<11;i++) {
							name = resultset.getString("name");		
							profit = resultset.getString("profit");
							player.sendMessage(ChatColor.LIGHT_PURPLE+""+i+". "+name+" "+profit);
						}    
					} catch (SQLException e1) {
						e1.printStackTrace();
					}	

				}
			}
			else{
				player.sendMessage(ChatColor.RED+"You don't have permission!");
			}

		} else if(split[0].equalsIgnoreCase("help") || split[0].equalsIgnoreCase("?") || split[0].equalsIgnoreCase("h")){
			event.setCancelled(true);
			if(StockCraftPermissions.getInstance().stockhelp(player))
			{
				String rulesloc = "plugins/StockCraft/stockhelp.txt";
				writetxt(rulesloc,event);  
			}
			else{
				player.sendMessage(ChatColor.RED+"You don't have permission!");
			}
		} else {
			player.sendMessage(ChatColor.DARK_RED + "Wrong usage.");
		}
	}

	private void Course(Player player, String[] split) throws SQLException {
		if(StockCraftPermissions.getInstance().course(player))
		{
			String idname = String.valueOf(split[1]);
			String id = idchange(idname);
			String[] gtext = StockCraftDatabase.getcourse(id);
			idname = gtext[0];
			String course = gtext[1];
			String lastday = gtext[2];
			String percent = gtext[3];

			player.sendMessage(colorString(String.format("&6Stock: &b%s", idname)));
			player.sendMessage(colorString(String.format("&aCourse: &b%s", course)));
			player.sendMessage(colorString(String.format("&eChange in percent: &b%s", percent)));
			player.sendMessage(colorString(String.format("&aEnd of last day: &b%s", lastday)));

		}
		else {
			player.sendMessage(ChatColor.RED+"You don't have permission!");
		}
	}

	private String colorString(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}
}
