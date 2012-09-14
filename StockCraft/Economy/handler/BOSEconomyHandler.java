package StockCraft.Economy.handler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import StockCraft.Economy.EconomyHandler;
import cosine.boseconomy.BOSEconomy;

public class BOSEconomyHandler extends EconomyHandler{

	private boolean enabled;
	private BOSEconomy economy = null;

	@Override
	public boolean hasMoney(Player player, double money) {
		return getBalance(player) >= money;
	}

	@Override
	public void subtract(Player player, double money) {
		economy.setPlayerMoney(player.getName(), getBalance(player) - money, true);
	}

	@Override
	public void add(Player player, double money) {
		economy.setPlayerMoney(player.getName(), getBalance(player) + money, false);
	}

	@Override
	public double getBalance(Player player) {
		return economy.getPlayerMoneyDouble(player.getName());
	}

	@Override
	public String getName() {
		return "BOSEconomy";
	}

	@Override
	public void setEnabled(boolean b) {
		enabled = b;
		
		if(enabled) {
			 Plugin temp = Bukkit.getServer().getPluginManager().getPlugin("BOSEconomy");
			    
			    if(temp == null) {
			        economy = null;
			    } else {
			        economy = (BOSEconomy)temp;
			        System.out.println("[StockCraft] hooked into: " + getName());
			    }
		}
		
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

}
