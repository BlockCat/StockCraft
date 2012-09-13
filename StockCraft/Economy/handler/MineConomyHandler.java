package StockCraft.Economy.handler;

import me.mjolnir.mineconomy.MineConomy;
import me.mjolnir.mineconomy.exceptions.NoAccountException;
import me.mjolnir.mineconomy.internal.MCCom;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import StockCraft.Economy.EconomyHandler;

public class MineConomyHandler extends EconomyHandler {

	private boolean enabled;
	private MineConomy econ = null;

	@Override
	public boolean hasMoney(Player player, double money) {
		  try {
	            return MCCom.canExternalAfford(player.getName(), money);
	        } catch(NoAccountException e) {
	            MCCom.create(player.getName());
	            return MCCom.canExternalAfford(player.getName(), money);
	        }
	}

	@Override
	public void subtract(Player player, double money) {
		MCCom.setExternalBalance(player.getName(), getBalance(player) - money);
	}

	@Override
	public void add(Player player, double money) {
		MCCom.setExternalBalance(player.getName(), getBalance(player) + money);
	}

	@Override
	public double getBalance(Player player) {
		  try
	        {
	            return MCCom.getExternalBalance(player.getName());
	        }
	        catch (NoAccountException e)
	        {
	            MCCom.create(player.getName());
	            return MCCom.getExternalBalance(player.getName());
	        }
	}

	@Override
	public String getName() {
		return "MineConomy";
	}

	@Override
	public void setEnabled(boolean b) {
		enabled = b;
		if (enabled) {
			if (econ == null) {
	            Plugin econ = Bukkit.getServer().getPluginManager().getPlugin("MineConomy");
	            if (econ != null && econ.isEnabled()) {
	                this.econ = (MineConomy) econ;
	                System.out.println("[StockCraft hooked into: " + getName());
	            }
	        }
		}
		
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

}
