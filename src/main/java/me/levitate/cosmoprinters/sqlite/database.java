package me.levitate.cosmoprinters.sqlite;

import me.levitate.cosmoprinters.CosmoPrinters;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public abstract class database {
    public final CosmoPrinters plugin;
    public Connection connection;

    public database(CosmoPrinters instance) {
        plugin = instance;
    }

    public abstract Connection get_sql_connection();
    public abstract void load();

    public void init() {
        connection = get_sql_connection();

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM currency WHERE uuid = ?");
            ResultSet rs = ps.executeQuery();
            close(ps,rs);
        }
        catch(SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Unable to retrieve connection", e);
        }
    }

    public Map<String, String> get_all_balances() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = get_sql_connection();
            ps = conn.prepareStatement("SELECT * FROM currency;");
            rs = ps.executeQuery();

            Map<String, String> output = new HashMap<>();

            while(rs.next()){
                output.put(rs.getString("uuid"), rs.getString("amount"));
            }

            return output;
        }
        catch (SQLException e) {
            error.execute(plugin, e);
        }
        finally {
            close(ps, conn);
        }

        return new HashMap<>();
    }

    public String get_balance(UUID plr_uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = get_sql_connection();
            ps = conn.prepareStatement("SELECT * FROM currency WHERE uuid = ?;");
            ps.setString(1, plr_uuid.toString());
            rs = ps.executeQuery();

            while (rs.next()) {
                if(rs.getString("uuid").equalsIgnoreCase(plr_uuid.toString().toLowerCase())){
                    return rs.getString("amount");
                }
            }
        }
        catch(SQLException e) {
            plugin.getLogger().log(Level.SEVERE, errors.sql_connection_execute(), e);
        }
        finally {
            close(ps, conn);
        }

        return "0.000";
    }

    public int set_balance(UUID plr_uuid, Double amount) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = get_sql_connection();
            ps = conn.prepareStatement("UPDATE currency SET amount = ? WHERE uuid = ?");
            ps.setString(1, String.valueOf(amount)); // sets the amount to the parameter
            ps.setString(2, plr_uuid.toString()); // sets the player uuid to the parameter
            ps.executeUpdate();

            return 0;
        }
        catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, errors.sql_connection_execute(), e);
        }
        finally {
            close(ps, conn);
        }

        return 1;
    }

    public int add_row_to_currency(UUID uuid, Double amount) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = get_sql_connection();
            ps = conn.prepareStatement("INSERT INTO currency (uuid,amount) VALUES(?,?)");

            ps.setString(1,uuid.toString());
            ps.setString(2, amount.toString());

            ps.executeUpdate();

            return 0;
        }
        catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, errors.sql_connection_execute(), e);
        }
        finally {
            close(ps, conn);
        }

        return 1;
    }

    public void recreate_currency_database() {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = get_sql_connection();
            ps = conn.prepareStatement(sqlite.sqlite_create_currency_table);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, errors.sql_connection_execute(), e);
        }
        finally {
            close(ps, conn);
        }
    }

    public void delete_row(String database_name, UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = get_sql_connection();
            ps = conn.prepareStatement("DELETE FROM " + database_name + " WHERE uuid=(?)");
            ps.setString(1, uuid.toString());
            ps.executeUpdate();
        }
        catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, errors.sql_connection_execute(), e);
        }
        finally {
            close(ps, conn);
        }
    }

    public void close(PreparedStatement ps, ResultSet rs){
        try {
            if (ps != null) // if ps isn't null then close it
                ps.close();

            if (rs != null) // if rs isn't null then close it
                rs.close();
        }
        catch (SQLException e) {
            error.close(plugin, e);
        }
    }

    public void close(PreparedStatement ps, Connection conn) {
        try {
            if (ps != null) // if ps isn't null then close it
                ps.close();

            if (conn != null) // if conn isn't null then close it
                conn.close();
        }
        catch (SQLException e) {
            error.close(plugin, e);
        }
    }
}
