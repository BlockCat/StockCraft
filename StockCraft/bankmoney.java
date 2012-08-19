/**
 * StockCraft
 *
 * @author Dominik Uphoff
 */

package StockCraft;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class bankmoney
{
	public static double checkmoney(Player player){
		if(StockCraftProperties.iconomy5 == true){
			if(StockCraft.money != null)
				return StockCraft.money.getBalance(player.getName());
		}

		return 0;
	}

		
	public static void addmoney(Player player,double amount) {
		if (StockCraftProperties.iconomy5 == true) {
			StockCraft.money.depositPlayer(player.getName(), amount);
		}
		
	}
	public static void subtractmoney(Player player,double amount) {
		double tradingfee = 0;
		if(StockCraftProperties.fee > 0 || StockCraftProperties.minimumfee > 0)
		{
			tradingfee = (amount * StockCraftProperties.fee / 100);
			if(tradingfee < StockCraftProperties.minimumfee)
			{
				tradingfee = StockCraftProperties.minimumfee;
			}
			tradingfee = tradingfee *100;
			tradingfee = Math.round(tradingfee);
			tradingfee = tradingfee /100;
			
			player.sendMessage(ChatColor.RED+"You have paid "+tradingfee+" trading fee!");
		}
		if (StockCraftProperties.iconomy5 == true) {
			StockCraft.money.withdrawPlayer(player.getName(), amount + tradingfee);
		}
	}
}