package StockCraft.Economy.handler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import StockCraft.Economy.EconomyHandler;

import com.greatmancode.craftconomy3.CC3BukkitLoader;
import com.greatmancode.craftconomy3.Common;
import com.greatmancode.craftconomy3.currency.CurrencyManager;

public class CraftConomyHandler extends EconomyHandler{

	private boolean enabled = false;
	protected CC3BukkitLoader economy = null;
	private Common common;

	@Override
	public boolean hasMoney(Player player, double money) {
		Common.getInstance();
		return common.getAccountManager().getAccount(player.getName()).getBalance(player.getWorld().getName(), Common.getInstance().getCurrencyManager().getCurrency(CurrencyManager.defaultCurrencyID).getName()) > money;
	}

	@Override
	public void subtract(Player player, double money) {
		common.getAccountManager().getAccount(player.getName()).withdraw(money, common.getServerCaller().getDefaultWorld(), common.getCurrencyManager().getCurrency(CurrencyManager.defaultCurrencyID).getName());
	}

	@Override
	public void add(Player player, double money) {
		common.getAccountManager().getAccount(player.getName()).deposit(money, common.getServerCaller().getDefaultWorld(), common.getCurrencyManager().getCurrency(CurrencyManager.defaultCurrencyID).getName());
	}
	@Override
	public double getBalance(Player player) {
		return common.getAccountManager().getAccount(player.getName()).getBalance(Common.getInstance().getServerCaller().getDefaultWorld(), Common.getInstance().getCurrencyManager().getCurrency(CurrencyManager.defaultCurrencyID).getName());
	}

	@Override
	public String getName() {
		return "Craftconomy3";
	}

	@Override
	public void setEnabled(boolean b) {
		enabled = b;
		if (enabled) {
			if (economy == null) {
				Plugin ec = Bukkit.getServer().getPluginManager().getPlugin("Craftconomy3");
				if (ec != null && ec.isEnabled() && ec.getClass().getName().equals("com.greatmancode.craftconomy3.CC3BukkitLoader")) {
					economy = (CC3BukkitLoader) ec;
					System.out.println("[StockCraft] hooked into CraftConomy 3.");
					common = Common.getInstance();
				}
			}
		}
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

}
