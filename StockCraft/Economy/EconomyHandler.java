package StockCraft.Economy;

import org.bukkit.entity.Player;

public abstract class EconomyHandler {
	
	public abstract boolean hasMoney(Player player, double money);
	public abstract void subtract (Player player, double  money);
	public abstract void add(Player player, double money);
	public abstract double getBalance(Player player);
	public abstract String getName();
	public abstract void setEnabled(boolean b);
	public abstract boolean isEnabled();
	

}
