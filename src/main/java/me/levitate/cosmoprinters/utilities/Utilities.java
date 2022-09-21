package me.levitate.cosmoprinters.utilities;

import me.levitate.cosmoprinters.CosmoPrinters;
import me.levitate.cosmoprinters.sqlite.sqlite;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.DecimalFormat;
import java.util.UUID;

public class Utilities
{
    private static final CosmoPrinters plugin = JavaPlugin.getPlugin(CosmoPrinters.class);

    public static String translateColor(String input)
    {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null)
            return false;

        try {
            double d = Double.parseDouble(strNum.replaceAll(",", ""));
        }
        catch (NumberFormatException nfe) {
            return false;
        }

        return true;
    }

    public static double getNumber(String strNum) {
        double d;

        try {
            d = Double.parseDouble(strNum.replaceAll(",", ""));
        }
        catch (NumberFormatException nfe) {
            return 0.0d;
        }

        return d;
    }

    public static String formatNumber(String str) {
        if(isNumeric(str)) {
            double amount = Double.parseDouble(str);
            DecimalFormat formatter = new DecimalFormat("#,##0.00");

            return formatter.format(amount);
        } else {
            return "";
        }
    }

    public static String formatNumber(Double num) {
        DecimalFormat formatter = new DecimalFormat("#,##0.00");
        return formatter.format(num);
    }

    public static Double roundToHundredths(Double x) {
        return getNumber(formatNumber(x));
    }

    public static int secondsToTicks(int seconds) {
        return seconds * 20;
    }

    public static void log(String msg)
    {
        plugin.getLogger().info(msg);
    }
}
