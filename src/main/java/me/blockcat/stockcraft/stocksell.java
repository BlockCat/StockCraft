/**
 * StockCraft
 *
 * @author Dominik Uphoff
 */

package me.blockcat.stockcraft;

import java.sql.ResultSet;
import java.sql.SQLException;

import me.blockcat.stockcraft.connectUtils.DatabaseHandler;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class stocksell
{
	public static void stocksellcommand(Player player,String [] split) throws SQLException
	{
		if(StockCraftPermissions.getInstance().stocksell(player))
		{
			String amount = "1";
			String idname = String.valueOf(split[1]);
			String id = StockCraftCommands.idchange(idname);
			String[] gtext = StockCraftDatabase.getcourse(id);
			String course = gtext[1];
			if(split.length > 2){amount = split[2];}
			
			float fcourse = Float.valueOf(course.equalsIgnoreCase("") ? "0" : course);
			
			int iamount = Integer.valueOf(amount);			
			float sumget = (iamount * fcourse);
			float allprofit = 0;
			float price = (iamount * fcourse);
			
			iamount = Math.abs(iamount);

			//Statement statement = StockCraftDatabase.inter.getStatement();
			if(id!=null)
			{
				ResultSet resultset = null;
				int amountof = 0;
				float sumpaid = 0;
				try{
					String sql = "SELECT amount,sumpaid FROM stocks WHERE name ='"+player.getName()+"' AND stockname='"+idname+"'";
					resultset = DatabaseHandler.executeWithResult(sql);
					while (resultset.next()) {
						sumpaid = Float.valueOf(resultset.getString("sumpaid"));
						amountof = Integer.valueOf(resultset.getString("amount"));							
					}
					
					if(amountof < 0 && price <= bankmoney.checkmoney(player)){
						if(Config.shorten == true) {								
							sql = "UPDATE stocks SET amount ="+(amountof - iamount)+", sumpaid ="+(sumpaid + price)+" WHERE name ='"+player.getName()+"' AND stockname ='"+idname+"'";
							DatabaseHandler.execute(sql);
							player.sendMessage(ChatColor.GREEN+amount+" "+idname+" stocks sold (shorted) -> "+price);
							sumget = -sumget;
							bankmoney.addmoney(player, sumget);
						}
						else{
							player.sendMessage(ChatColor.RED+"Shorten is not allowed!");
						}
					}
					else if(amountof < 0 && price > bankmoney.checkmoney(player)){
						player.sendMessage(ChatColor.RED+"Not enough money! You need "+(iamount * fcourse)+"! You have "+bankmoney.checkmoney(player)+"!");
					}
					else if(amountof == 0){
						if(Config.shorten == true ) {
							if(price <= bankmoney.checkmoney(player))
							{
								sql = "INSERT INTO stocks (name,stockname,sumpaid,amount) VALUES('"+player.getName()+"','"+idname+"',"+price+","+ -iamount+")";
								DatabaseHandler.execute(sql);
								player.sendMessage(ChatColor.GREEN+amount+" "+idname+" stocks sold (shorted) -> "+price);
								sumget = -sumget;
								bankmoney.addmoney(player, sumget);
							}
							else if(amountof == 0 && price > bankmoney.checkmoney(player)){
								player.sendMessage(ChatColor.RED+"Not enough money! You need "+(iamount * fcourse)+"! You have "+bankmoney.checkmoney(player)+"!");
							}
						}
						else{
							player.sendMessage(ChatColor.RED+"You don't have stocks of "+idname);
						}							
					}
					else{
						if(amountof <= iamount)
						{
							if(amountof < iamount)
							{
								player.sendMessage(ChatColor.RED+"You don't have "+iamount+" stocks of "+idname+"! You have sold "+amountof+" stocks!");
								iamount = amountof;
								sumget = (iamount * fcourse);
							}
							sql = "DELETE FROM stocks WHERE name ='"+player.getName()+"' AND stockname ='"+idname+"'";
						}
						else{
							sql = "UPDATE stocks SET amount ="+(amountof - iamount)+", sumpaid ="+(sumpaid - (sumpaid/amountof)*iamount)+" WHERE name ='"+player.getName()+"' AND stockname ='"+idname+"'";
						}	
						DatabaseHandler.execute(sql);
						float profit = sumget -(sumpaid/amountof*iamount);
						player.sendMessage(ChatColor.GOLD+"You got "+profit+" profit!");
						sql = "SELECT profit FROM stockstats WHERE name ='"+player.getName()+"'";
						resultset = DatabaseHandler.executeWithResult(sql);
						while (resultset.next()) {
							allprofit = Float.valueOf(resultset.getString("profit"));							
						}
						sql = "SELECT name FROM stockstats WHERE name ='"+player.getName()+"'";
						resultset = DatabaseHandler.executeWithResult(sql);
						String name = null;
						while (resultset.next()) {
							name = resultset.getString("name");							
						}
						if(name!=null){
							sql = "UPDATE stockstats SET profit="+(allprofit+profit)+" WHERE name ='"+player.getName()+"'";
						}
						else {
							sql = "INSERT INTO stockstats (name,profit) VALUES ('"+player.getName()+"',"+profit+")";
						}			
						DatabaseHandler.execute(sql);
						bankmoney.addmoney(player, sumget);
						player.sendMessage(ChatColor.GREEN + "" + amountof + " "+idname+" stocks sold -> "+sumget);

					}

				}
				catch(Exception e){
					e.printStackTrace();					
				}

			}
			else{
				if(id == null){
					player.sendMessage(ChatColor.RED+"There is no id named "+idname+"!");
				}					
			}
		}
		else{
			player.sendMessage(ChatColor.RED+"You don't have permission!");
		}
	}
}