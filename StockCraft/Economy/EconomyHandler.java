package StockCraft.Economy;

import org.bukkit.entity.Player;

public abstract class EconomyHandler {
	
	public abstract boolean hasMoney(Player player, double money);
	public abstract void payMoney (Player player, double  money);

}
