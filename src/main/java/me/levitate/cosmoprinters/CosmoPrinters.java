package me.levitate.cosmoprinters;

import me.levitate.cosmoprinters.commands.mainCommand;
import me.levitate.cosmoprinters.events.blockPlaceEvent;
import me.levitate.cosmoprinters.menu.printerEvent;
import me.levitate.cosmoprinters.printer.mainPrinter;
import me.levitate.cosmoprinters.sqlite.sqlite;
import me.levitate.cosmoprinters.utilities.Utilities;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

// make printers a physical item that players purchase, configurable the max amount of printers they can purchase along with the price, etc.
// implement NBTAPI and use it to handle upgrades, etc.
// don't know how I'm going to make them earn their money, will look through the spigot api and see if there are any events I can use for this.
// doing withdrawals shouldn't be hard to do using NBTAPI and vault of course.

public final class CosmoPrinters extends JavaPlugin {
    private printerEvent printerManager;
    private CosmoPrinters plugin;
    private Economy eco;
    private sqlite sqLite;

    @Override
    public void onEnable() {
        plugin = this;

        // As it suggests it setups the economy, and checks dependencies.
        if (!setupEconomy()) {
            getServer().getConsoleSender().sendMessage("§9[CosmoPrinters] §cYou must have Vault and an Economy plugin installed!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Checks for the NBTAPI plugin.
        if (getServer().getPluginManager().getPlugin("NBTAPI") == null) {
            getServer().getConsoleSender().sendMessage("§9[CosmoPrinters] §cYou must have the NBTAPI plugin installed!");
            getServer().getPluginManager().disablePlugin(this);
        }

        // Default configuration.
        saveDefaultConfig();

        // Events
        getServer().getPluginManager().registerEvents(new blockPlaceEvent(this), this);

        // Command
        getCommand("printer").setExecutor(new mainCommand(this));

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                mainPrinter printer = new mainPrinter(plugin);
                printer.run_printer();
            }
        }, 150, Utilities.secondsToTicks(this.plugin.getConfig().getInt("printer-time")));
    }

    @Override
    public void onDisable() {}

    public <T> T getConfig(String path)
    {
        return getConfig(path, null);
    }

    public <T> T getConfig(String path, Object def)
    {
        return (T) getConfig().get(path, def);
    }

    public printerEvent getPrinterManager()
    {
        return printerManager;
    }

    public sqlite getSQLite() {
        return this.sqLite;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        eco = rsp.getProvider();
        return eco != null;
    }

    public Economy getEconomy() {
        return eco;
    }
}
