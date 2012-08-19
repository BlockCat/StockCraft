/**
 * StockCraft
 *
 * @author Dominik Uphoff
 */

package StockCraft;

import java.sql.SQLException;

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
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event){
    	Player player = event.getPlayer();
    	String[] split = event.getMessage().split(" ");
    	//Check if the command is an mcMMO related help command
    	try {
			StockCraftCommands.getInstance().infosystem(split, player, event);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }


	public StockCraft getPlugin() {
		return plugin;
	}
    
}
