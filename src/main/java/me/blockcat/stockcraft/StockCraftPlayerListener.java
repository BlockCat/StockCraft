/**
 * StockCraft
 *
 * @author Dominik Uphoff
 */

package me.blockcat.stockcraft;
import java.sql.SQLException;
import java.util.Arrays;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class StockCraftPlayerListener implements Listener {
	private final StockCraft plugin;

	public StockCraftPlayerListener(StockCraft instance) {
		plugin = instance;
	}


	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerCommandPreprocess(final PlayerCommandPreprocessEvent event){
		final Player player = event.getPlayer();
		String[] split = event.getMessage().split(" ");
		
		if(split.length > 0) {
			if (split[0].equalsIgnoreCase("/stock") || split[0].equalsIgnoreCase("/stockcraft")) {

				final String[] args = Arrays.copyOfRange(split, 1, split.length);

				StockCraft.threadPool.execute(new Runnable() {
					public void run() {
						try {
							StockCraftCommands.getInstance().infosystem(args, player, event);
						} catch (SQLException e) {
							e.printStackTrace();
						}							
					}
				});

				event.setCancelled(true);
			}
		}
	}


	public StockCraft getPlugin() {
		return plugin;
	}

}
