package me.levitate.cosmoprinters.sqlite;

import me.levitate.cosmoprinters.CosmoPrinters;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public class sqlite extends database {
    private final String database_name;

    public sqlite(CosmoPrinters instance){
        super(instance);
        database_name = "database";
    }

    public static String sqlite_create_currency_table = "CREATE TABLE IF NOT EXISTS currency (" +
            "`uuid` varchar(32) NOT NULL," +
            "`amount` varchar(64)," +
            "PRIMARY KEY (`uuid`)" +
            ");";

    public Connection get_sql_connection() {
        File dataFolder = new File(plugin.getDataFolder(), database_name +".db");

        if (!dataFolder.exists()){
            try {
                dataFolder.createNewFile();
            }
            catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "File write error: "+ database_name +".db");
            }
        }

        try {
            if(connection!=null&&!connection.isClosed()){
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;
        }
        catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE,"SQLite exception while initializing.", e);
        }
        catch (ClassNotFoundException ex) {
            plugin.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library, put it in /lib folder.");
        }

        return null;
    }

    public void load() {
        connection = get_sql_connection();

        try {
            Statement s = connection.createStatement();
            s.executeUpdate(sqlite_create_currency_table);
            s.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        init();
    }
}
