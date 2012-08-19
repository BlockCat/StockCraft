/**
 * StockCraft
 *
 * @author Dominik Uphoff
 */

package StockCraft;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

import me.BlockCat.bukkitSQL.BSQLinterface;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;


public class StockCraftDatabase {

	public static BSQLinterface inter;
	public static Connection conn;
	private static StockCraft plugin;
	
	public StockCraftDatabase(StockCraft instance) {
		plugin = instance;
	}
	
	private static volatile StockCraftDatabase instance;
	
	public static StockCraftDatabase  getInstance() {
		if (instance == null) {
			instance = new StockCraftDatabase(plugin);
		}
		return instance;
	}

	public Connection connecting() {
		RegisteredServiceProvider<BSQLinterface> bukkitSQLProvider = plugin.getServer()
				.getServicesManager()
				.getRegistration(me.BlockCat.bukkitSQL.BSQLinterface.class);

		if(bukkitSQLProvider != null){
			inter = bukkitSQLProvider.getProvider();
			conn = inter.getConnection();
			return conn;
		}	
		else
		{
			return null;
		}
		
	}

	public static String linksFromWebsite(String url) throws Exception
	{
		// opening URL
		BufferedReader in = new BufferedReader( new InputStreamReader( new URL(url).openStream() ) );

		// Read  entire text
		StringBuffer content = new StringBuffer();
		String line = in.readLine();
		while( line!=null )
		{
			content.append( line );
			line = in.readLine();
		}
		String text = content.toString();


		return text;
	}
	public static String linksFromWebsite2( String url ) throws Exception
	{
		// opening URL
		BufferedReader in = new BufferedReader( new InputStreamReader( new URL(url).openStream() ) );

		// Read entire text
		StringBuffer inhalt = new StringBuffer();
		String line = in.readLine();
		while( line!=null )
		{
			inhalt.append( line );
			inhalt.append( "\n" );
			line = in.readLine();
		}
		String text = inhalt.toString();

		return text;
	}

	public static String[] getcourse(String[] idlist)
	{
		String text = null;
		int l = idlist.length;
		try {
			text = "http://de.finance.yahoo.com/d/quotes.xml?s=";			
			text = text +idlist[0];
			for(int i = 1;i<l;i++)
			{
				text = text +"+"+idlist[i];
			}				
			text = text +"&f=l1&e=.xml";	
			text = linksFromWebsite2(text);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
		String[] gtext = text.split("\n");
		return gtext;
	}
	public static String[] getcourse(String id)
	{
		String text = null;
		try {
			text = linksFromWebsite("http://de.finance.yahoo.com/d/quotes.xml?s="+id+"&f=sl1pp2m3&e=.xml");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
		String[] gtext = text.split(",");
		return gtext;
	}
	public static void idadd(Player player, String idlong, String idshort) throws SQLException
	{
		String[] gtext = StockCraftDatabase.getcourse(idshort);
		String course = gtext[1];
		if(Float.valueOf(course) > 0)
		{
			Statement statement = (Statement) StockCraftDatabase.conn.createStatement();
			if(statement != null)
			{	
				ResultSet resultset = null;
				String sql = "SELECT longid,shortid FROM idtable WHERE longid='"+idlong+"' OR shortid='"+idshort+"'";
				String longid = null;
				String shortid = null;
				try {
					resultset = statement.executeQuery(sql);
					longid = "no";
					shortid = "no";
					while (resultset.next()) {
						longid = resultset.getString("longid");
						shortid = resultset.getString("shortid");							
					}

				} catch (SQLException e1) {
					e1.printStackTrace();
				}		
				if(longid.equals("no") && shortid.equals("no")){
					sql = "INSERT INTO idtable (longid,shortid) VALUES ('"+idlong+"','"+idshort+"')";
					try {
						statement.execute(sql);
						player.sendMessage(ChatColor.GREEN+"Added "+ idshort+" to available stocks!");
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				else{
					if(longid.toLowerCase().equals(idlong.toLowerCase())){
						player.sendMessage(ChatColor.RED+"There is an entry with "+idlong+" already in the database!");
					}
					else if(shortid.toLowerCase().equals(idshort.toLowerCase())){
						player.sendMessage(ChatColor.RED+"There is an entry with "+idshort+" already in the database!");
					}
				}
			}
		}
		else{
			player.sendMessage(ChatColor.RED+"There is no id named "+idshort+" on 'finance.yahoo.com' available!");
		}
	}

}