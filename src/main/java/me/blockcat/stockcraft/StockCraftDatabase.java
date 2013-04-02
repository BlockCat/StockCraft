/**
 * StockCraft
 *
 * @author Dominik Uphoff, BlockCat
 */

package me.blockcat.stockcraft;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import me.blockcat.stockcraft.connectUtils.DatabaseHandler;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mysql.jdbc.Connection;


public class StockCraftDatabase {

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

	public Connection getConnection() {
		return (Connection) StockCraft.connectionPool.get();
	}

	public static String linksFromWebsite(String url) throws Exception
	{
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new URL(url).openStream());

		doc.normalize();
		String text = "Id,";

		NodeList list = doc.getElementsByTagName("finance");

		Node node = list.item(0);
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			Element element = (Element) node;
			
			Element idTag = (Element) element.getElementsByTagName("company").item(0);
			String id = idTag.getAttribute("data");

			Element courseTag = (Element) element.getElementsByTagName("last").item(0);
			String course = courseTag.getAttribute("data");
			
			Element closeTag = (Element) element.getElementsByTagName("y_close").item(0);
			String prevClose = closeTag.getAttribute("data");
			
			Element percentTag = (Element) element.getElementsByTagName("perc_change").item(0);
			String percent = percentTag.getAttribute("data");
			if (!percent.startsWith("-")) {
				percent = "+" + (percent.equalsIgnoreCase("") ? "0" : percent) + "%";
			}

			text = String.format("%s,,%s,,%s,,%s", id,course, prevClose, percent);
		}

		return text;

	}
	public static String linksFromWebsite2( String url, String[] idlist ) throws Exception
	{
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new URL(url).openStream());

		doc.normalize();
		String text = "";

		NodeList list = doc.getElementsByTagName("finance");
		
		for (String id : idlist) {
			for (int i = 0; i < list.getLength(); i++) {
				String tag = ((Element) ((Element) list.item(i)).getElementsByTagName("symbol").item(0)).getAttribute("data");
				if (tag.equalsIgnoreCase(id)) {
					Node node = list.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						Element element = (Element) node;
						

						Element courseTag = (Element) element.getElementsByTagName("last").item(0);
						text += courseTag.getAttribute("data") + "\n";				
					}
				}
			}
		}
		return text;
	}

	public static String[] getcourse(String[] idlist)
	{
		String text = null;
		int l = idlist.length;
		try {
			//text = "http://de.finance.yahoo.com/d/quotes.xml?s=";
			text = "http://www.google.com/ig/api?stock=";
			text = text +idlist[0];
			for(int i = 1;i<l;i++)
			{
				text = text +"&stock="+idlist[i];
			}				
			//			text = text +"&f=l1&e=.xml";	
			text = linksFromWebsite2(text, idlist);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] gtext = text.split("\n");
		return gtext;
	}
	
	public static String[] getcourse(String id)
	{
		String text = null;
		try {
			//			text = linksFromWebsite("http://de.finance.yahoo.com/d/quotes.xml?s="+id+"&f=sl1pp2m3&e=.xml");
			text = linksFromWebsite("http://www.google.com/ig/api?stock=" + id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] gtext = text.split(",,");
		return gtext;
	}
	public static void idadd(Player player, String idlong, String idshort) throws SQLException
	{
		String[] gtext = StockCraftDatabase.getcourse(idshort);
		if (gtext.length < 2) {
			player.sendMessage(ChatColor.RED+"There is no id named "+idshort+" on 'finance.google.com' available!");
			return;
		}
		String course = gtext[1];
		float courseValue = 0;
		try {
			courseValue = Float.valueOf(course);
		} catch (Exception e) {
			player.sendMessage(ChatColor.RED+"There is no id named "+idshort+" on 'finance.google.com' available!");
			return;
		}
		if(courseValue > 0)
		{
			//Statement statement = (Statement) StockCraftDatabase.conn.createStatement();
			//if(statement != null)
			{	
				ResultSet resultset = null;
				String sql = "SELECT longid,shortid FROM idtable WHERE longid='"+idlong+"' OR shortid='"+idshort+"'";
				String longid = null;
				String shortid = null;
				try {
					//resultset = statement.executeQuery(sql);
					resultset = DatabaseHandler.executeWithResult(sql);
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
						//statement.execute(sql);
						DatabaseHandler.execute(sql);
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
			player.sendMessage(ChatColor.RED+"There is no id named "+idshort+" on 'finance.google.com' available!");
		}
	}

}