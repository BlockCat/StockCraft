package StockCraft.Economy.handler;

import me.ashtheking.currency.Currency;
import me.ashtheking.currency.CurrencyList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import StockCraft.Economy.EconomyHandler;

public class MultiCurrencyHandler extends EconomyHandler{

	private boolean enabled;
	private Currency economy = null;

	@Override
	public boolean hasMoney(Player player, double money) {
		return CurrencyList.hasEnough(player.getName(), money);
	}

	@Override
	public void subtract(Player player, double money) {
		CurrencyList.subtract(player.getName(), money);
		
	}

	@Override
	public void add(Player player, double money) {
		CurrencyList.add(player.getName(), money);
	}

	@Override
	public double getBalance(Player player) {
		return CurrencyList.getValue((String) CurrencyList.maxCurrency(player.getName())[0], player.getName());
	}

	@Override
	public String getName() {
		return "MultiCurrency";
	}

	@Override
	public void setEnabled(boolean b) {
		enabled = b;
		if (enabled) {
			if (economy == null) {
	            Plugin multiCurrency = Bukkit.getServer().getPluginManager().getPlugin("MultiCurrency");
	            if (multiCurrency != null && multiCurrency.isEnabled()) {
	                economy = (Currency) multiCurrency;
	                System.out.println("[StockCraft] hooked into: " + getName());
	            }
	        }
		}
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

}
