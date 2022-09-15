package me.levitate.cosmoprinters.sqlite;

import me.levitate.cosmoprinters.CosmoPrinters;

import java.util.logging.Level;

public class error {
    public static void execute(CosmoPrinters plugin, Exception e) {
        plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", e);
    }

    public static void close(CosmoPrinters plugin, Exception e) {
        plugin.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", e);
    }
}
