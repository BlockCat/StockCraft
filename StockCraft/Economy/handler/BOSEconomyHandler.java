package StockCraft.Economy.handler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import StockCraft.Economy.EconomyHandler;

public class BOSEconomyHandler extends EconomyHandler{

	private boolean enabled;
	private BOSEconomyHandler economy = null;

	@Override
	public boolean hasMoney(Player player, double money) {
		return economy.hasMoney(player, money);
	}

	@Override
	public void subtract(Player player, double money) {
		economy.subtract(player, money);
	}

	@Override
	public void add(Player player, double money) {
		economy.add(player, money);
	}

	@Override
	public double getBalance(Player player) {
		return economy.getBalance(player);
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
			        economy = (BOSEconomyHandler)temp;
			        System.out.println("[StockCraft] hooked into: " + getName());
			    }
		}
		
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

}
