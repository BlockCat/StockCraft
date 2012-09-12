package StockCraft.Economy.handler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import StockCraft.Economy.EconomyHandler;

import com.iCo6.iConomy;
import com.iCo6.system.Holdings;

public class iConomyHandler extends EconomyHandler{

	private boolean enabled = false;

	private static iConomy iconomy = null;

	@Override
	public boolean hasMoney(Player player, double money) {
		return new Holdings(player.getName()).hasEnough(money);
	}

	@Override
	public void subtract(Player player, double money) {
		new Holdings(player.getName()).subtract(money);
	}
	@Override 
	public void add (Player player, double money) {
		new Holdings(player.getName()).add(money);
	}
	@Override
	public double getBalance (Player player) {
		double balance = new Holdings(player.getName()).getBalance();
		return balance;
	}


	@Override
	public String getName() {
		return "iConomy";
	}

	@Override
	public void setEnabled(boolean b) {
		enabled  = b;
		if (enabled) {
			if (iconomy == null) {
				Plugin ec = Bukkit.getServer().getPluginManager().getPlugin("iConomy");
				if (ec != null && ec.isEnabled() && ec.getClass().getName().equals("com.iCo6.iConomy")) {
					iconomy = (iConomy) ec;
					System.out.println("[StockCraft] Hooked into Iconomy!");
				}
			}
		}
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

}
