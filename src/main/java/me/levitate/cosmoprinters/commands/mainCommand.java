package me.levitate.cosmoprinters.commands;

import de.tr7zw.nbtapi.NBTItem;
import me.levitate.cosmoprinters.CosmoPrinters;
import me.levitate.cosmoprinters.printer.mainPrinter;
import me.levitate.cosmoprinters.utilities.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class mainCommand implements CommandExecutor {
    private final CosmoPrinters plugin;

    public mainCommand(CosmoPrinters instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("printer")) {
            String currencyName = this.plugin.getConfig().getString("name-of-currency");

            if (args.length == 0) {
                sender.sendMessage(Utilities.translateColor("&f&l[!] &b&lPrinter Commands"));
                sender.sendMessage(Utilities.translateColor("&8• &f/printer &8give <player>"));
                sender.sendMessage(Utilities.translateColor("&8• &f/printer &8upgrade"));
            }
            else if (args.length >= 2 && args[0].equalsIgnoreCase("give")) {
                if (args[1] == null || args[1].equals("")) {
                    sender.sendMessage(Utilities.translateColor("&8• &f/printer &8give <player>"));
                }

                if (sender.hasPermission("cosmoprinter.give")) {
                    Player target = this.plugin.getServer().getPlayer(args[1]);

                    sender.sendMessage(Utilities.translateColor(this.plugin.getConfig().getString("sent-message")));

                    if (target != null) {
                        mainPrinter printer = new mainPrinter(plugin);
                        target.getInventory().addItem(new ItemStack[]{printer.getPrinter()});
                        target.sendMessage(Utilities.translateColor(this.plugin.getConfig().getString("received-message")));
                    }
                    else {
                        sender.sendMessage(Utilities.translateColor(this.plugin.getConfig().getString("error-sending")));
                    }
                }
                else {
                    sender.sendMessage(Utilities.translateColor(this.plugin.getConfig().getString("no-permission")));
                }
            }
            else if (args.length >= 1 && args[0].equalsIgnoreCase("upgrade")) {
                if (sender.hasPermission("cosmoprinter.upgrade")) {
                    Player player = (Player) sender;
                    ItemStack item = player.getItemInHand();

                    if (item != null && item.getType() != Material.AIR) {
                        NBTItem nbti = new NBTItem(item);

                        if (nbti.getBoolean("isPrinter")) {
                            // currently broken, gotta fix next time I work on this
                            nbti.setInteger("earningUpgrades", 2);
                            player.sendMessage("upgraded printer to " + nbti.getInteger("earningUpgrades"));
                        }
                        else {
                            player.sendMessage("you must be holding a printer");
                            return true;
                        }
                    }
                    else {
                        player.sendMessage("you must be holding a printer");
                    }
                }
            }
        }

        return true;
    }
}
