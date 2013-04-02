package me.blockcat.stockcraft.connectUtils;


import java.sql.ResultSet;
import java.sql.SQLException;

import me.blockcat.stockcraft.StockCraft;

import com.mysql.jdbc.Connection;

public class DatabaseHandler {

	public static void checkTables() {
		try {
			execute("SELECT * FROM stocks");
		} catch (SQLException e) {
			try {
				execute("CREATE TABLE stocks(name char(50), stockname char(50), sumpaid float, amount int)");
				System.out.println("[StockCraft] stock table created");
			} catch (SQLException e1) {
			}
		}
		try {
			execute("SELECT * FROM stockstats");
		} catch (SQLException e) {
			try {
				execute("CREATE TABLE stockstats (name char(50),profit float)");
				System.out.println("[StockCraft] StockStats table created");
			} catch (SQLException e1) {
			}
		}
		try {
			execute("SELECT * FROM idtable");
		} catch (SQLException e) {
			try {
				execute("CREATE TABLE idtable (longid char(50), shortid char(50))");
				System.out.println("[StockCraft] ID table created");
			} catch (SQLException e1) {
			}
		}
	}

	public static void execute(String exe) throws SQLException {
		Connection conn = (Connection) StockCraft.connectionPool.get();
		conn.createStatement().execute(exe);
		StockCraft.connectionPool.recycle(conn);
		conn = null;
	}

	public static ResultSet executeWithResult(String exe) throws SQLException {
		Connection conn = (Connection) StockCraft.connectionPool.get();
		java.sql.Statement st = conn.createStatement();
		st.execute(exe);
		ResultSet rs = st.getResultSet();
		StockCraft.connectionPool.recycle(conn);
		conn = null;
		return rs;
	}

}
