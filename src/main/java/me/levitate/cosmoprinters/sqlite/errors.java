package me.levitate.cosmoprinters.sqlite;

public class errors {
    public static String sql_connection_execute(){
        return "Couldn't execute MySQL statement: ";
    }
    public static String sql_connection_close(){
        return "Failed to close MySQL connection: ";
    }
    public static String no_sql_connection(){
        return "Unable to retrieve MySQL connection: ";
    }
    public static String no_table_found(){
        return "No database table found";
    }
}
