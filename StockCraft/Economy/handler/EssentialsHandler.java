package StockCraft.Economy.handler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import StockCraft.Economy.EconomyHandler;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;

public class EssentialsHandler extends EconomyHandler {

	private boolean enabled;
	private Essentials ess = null;

	@Override
	public boolean hasMoney(Player player, double money) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void subtract(Player player, double money) {
		try {
			Economy.subtract(player.getName(), money);
		} catch (UserDoesNotExistException e) {
			Economy.createNPC(player.getName());
			subtract(player, money);
		} catch (NoLoanPermittedException e) {
		}
	}

	@Override
	public void add(Player player, double money) {
		try {
			Economy.add(player.getName(), money);
		} catch (UserDoesNotExistException e) {
			Economy.createNPC(player.getName());
			add(player, money);
		} catch (NoLoanPermittedException e) {
		}
	}

	@Override
	public double getBalance(Player player) {
		try {
			return Economy.getMoney(player.getName());
		} catch (UserDoesNotExistException e) {
			Economy.createNPC(player.getName());
			return 0;
		}
	}

	@Override
	public String getName() {
		return "Essentials";
	}

	@Override
	public void setEnabled(boolean b) {
		enabled = b;
		if (enabled) {
			if (ess == null) {
	            Plugin essentials = Bukkit.getServer().getPluginManager().getPlugin("Essentials");
	            if (essentials != null && essentials.isEnabled()) {
	                ess = (Essentials) essentials;
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
