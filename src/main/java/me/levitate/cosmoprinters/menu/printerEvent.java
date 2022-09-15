package me.levitate.cosmoprinters.menu;

import me.levitate.cosmoprinters.CosmoPrinters;
import me.levitate.cosmoprinters.utilities.Utilities;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class printerEvent implements Listener
{
    private final CosmoPrinters plugin;

    private final String menuTitle;
    private final String printerTitle;
    private final String speedTitle;
    private final String withdrawTitle;

    private final String statAlreadyMaxed;
    private final String notEnoughMoney;

    private final int maxPrinters;
    private final int maxSpeed;

    public printerEvent(CosmoPrinters plugin)
    {
        this.plugin = plugin;

        menuTitle = Utilities.translateColor(plugin.getConfig("menu.title"));
        printerTitle = Utilities.translateColor(plugin.getConfig("menu.printer_title"));
        speedTitle = Utilities.translateColor(plugin.getConfig("menu.speed_title"));
        withdrawTitle = Utilities.translateColor(plugin.getConfig("menu.withdraw_title"));

        statAlreadyMaxed = Utilities.translateColor(plugin.getConfig("printers.stat_already_maxed"));
        notEnoughMoney = Utilities.translateColor(plugin.getConfig("printers.not_enough_money"));

        maxPrinters = plugin.getConfig("printers.max_printers");
        maxSpeed = plugin.getConfig("printers.max_speed");

        Bukkit.getPluginManager().registerEvents(this, plugin);

        plugin.getCommand("cosmoprinter").setExecutor(new printerCommand(plugin));
    }

    String getMenuTitle()
    {
        return menuTitle;
    }

    String getPrinterTitle()
    {
        return printerTitle;
    }

    String getSpeedTitle()
    {
        return speedTitle;
    }

    String getWithdrawTitle()
    {
        return withdrawTitle;
    }

    String getStatAlreadyMaxed()
    {
        return statAlreadyMaxed;
    }

    String getNotEnoughMoney()
    {
        return notEnoughMoney;
    }

    Integer getMaxPrinters()
    {
        return maxPrinters;
    }

    Integer getMaxSpeed()
    {
        return maxSpeed;
    }

    // database simulation, using this while I don't have a database setup for testing purposes
    public int printers = 1;
    public int price = 10000;
    public int speed = 1;
    public int money = 100;

    @EventHandler
    public void onClick(InventoryClickEvent event)
    {
        if (event.getClickedInventory() == null) return;

        if (event.getClickedInventory().getTitle().equalsIgnoreCase(getMenuTitle()))
        {
            event.setCancelled(true);

            Economy eco = this.plugin.getEconomy();
            final ItemStack item = event.getCurrentItem();
            final Player player = (Player) event.getWhoClicked();

            String playerMoneyString = eco.format(eco.getBalance(player));
            int playerMoney = Integer.parseInt(playerMoneyString.replace("$", ""));

            if (item == null || item.getType() == Material.AIR || item.getType() == Material.STAINED_GLASS_PANE) return;

            if (item.getItemMeta().getDisplayName().equals(getPrinterTitle()))
            {
                if (printers >= getMaxPrinters())
                {
                    player.sendMessage(getStatAlreadyMaxed());
                    player.playSound(player.getLocation(), Sound.ANVIL_LAND, 0.6F, 2);
                    return;
                }

                if (playerMoney < price)
                {
                    player.sendMessage(getNotEnoughMoney());
                    player.playSound(player.getLocation(), Sound.ANVIL_LAND, 0.6F, 2);
                    return;
                }

                player.playSound(player.getLocation(), Sound.LEVEL_UP, 0.65F, 2);
            }
            else if (item.getItemMeta().getDisplayName().equals(getSpeedTitle()))
            {
                if (speed >= getMaxSpeed())
                {
                    player.sendMessage(getStatAlreadyMaxed());
                    player.playSound(player.getLocation(), Sound.ANVIL_LAND, 0.6F, 2);
                    return;
                }

                if (playerMoney < price)
                {
                    player.sendMessage(getNotEnoughMoney());
                    player.playSound(player.getLocation(), Sound.ANVIL_LAND, 0.6F, 2);
                    return;
                }

                player.playSound(player.getLocation(), Sound.LEVEL_UP, 0.65F, 2);
            }
            else if (item.getItemMeta().getDisplayName().equals(getWithdrawTitle()))
            {
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 0.65F, 2);
            }
        }
    }

    void openPrinter(Player player)
    {
        new printerMenu(player, plugin);
    }
}