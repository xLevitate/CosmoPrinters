package me.levitate.cosmoprinters.menu;

import me.levitate.cosmoprinters.CosmoPrinters;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class printerCommand implements CommandExecutor
{
    private final CosmoPrinters plugin;

    printerCommand(CosmoPrinters plugin)
    {
        this.plugin = plugin;
    }

    private void openMenu(Player player)
    {
        plugin.getPrinterManager().openPrinter(player);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            return true;
        }

        openMenu((Player) sender);
        return true;
    }
}