/**
 * StockCraft
 *
 * @author Dominik Uphoff
 */

package me.blockcat.stockcraft;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class bankmoney
{
	public static double checkmoney(Player player){
		if(Config.iconomy5 == true){
			if(StockCraft.money != null)
				return StockCraft.money.getBalance(player.getName());
		}

		return 0;
	}

		
	public static void addmoney(Player player,double amount) {
		if (Config.iconomy5 == true) {
			StockCraft.money.depositPlayer(player.getName(), amount);
		}
		
	}
	public static void subtractmoney(Player player,double amount) {
		double tradingfee = 0;
		if(Config.fee > 0 || Config.minimumfee > 0)
		{
			tradingfee = (amount * Config.fee / 100);
			if(tradingfee < Config.minimumfee)
			{
				tradingfee = Config.minimumfee;
			}
			tradingfee = tradingfee *100;
			tradingfee = Math.round(tradingfee);
			tradingfee = tradingfee /100;
			
			player.sendMessage(ChatColor.RED+"You have paid "+tradingfee+" trading fee!");
		}
		if (Config.iconomy5 == true) {
			StockCraft.money.withdrawPlayer(player.getName(), amount + tradingfee);
		}
	}
}