package me.levitate.cosmoprinters.commands;

import de.tr7zw.nbtapi.NBTItem;
import me.levitate.cosmoprinters.CosmoPrinters;
import me.levitate.cosmoprinters.printer.mainPrinter;
import me.levitate.cosmoprinters.utilities.Utilities;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class mainCommand implements CommandExecutor {
    private final CosmoPrinters plugin;

    public mainCommand(CosmoPrinters instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("printer")) {
            if (args.length == 0) {
                sender.sendMessage(Utilities.translateColor("&f&l[!] &b&lPrinter Commands"));
                sender.sendMessage(Utilities.translateColor("&8• &f/printer &8give <player>"));
                sender.sendMessage(Utilities.translateColor("&8• &f/printer &8upgrade"));
            }
            else if (args.length >= 2 && args[0].equalsIgnoreCase("give")) {
                if (args[1] == null || args[1].equals("")) {
                    sender.sendMessage(Utilities.translateColor("&8• &f/printer &8give <player>"));
                }

                Player player = (Player) sender;

                if (sender.hasPermission("cosmoprinter.give")) {
                    Player target = this.plugin.getServer().getPlayer(args[1]);

                    sender.sendMessage(Utilities.translateColor(this.plugin.getConfig().getString("sent-message")));
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 0.65F, 2);

                    if (target != null) {
                        mainPrinter printer = new mainPrinter(plugin);
                        target.getInventory().addItem(new ItemStack[]{printer.getPrinter()});
                        target.sendMessage(Utilities.translateColor(this.plugin.getConfig().getString("received-message")));
                        target.playSound(target.getLocation(), Sound.LEVEL_UP, 0.65F, 2);
                    }
                    else {
                        player.playSound(player.getLocation(), Sound.ANVIL_LAND, 0.6F, 2);
                        sender.sendMessage(Utilities.translateColor(this.plugin.getConfig().getString("error-sending")));
                    }
                }
                else {
                    player.playSound(player.getLocation(), Sound.ANVIL_LAND, 0.6F, 2);
                    sender.sendMessage(Utilities.translateColor(this.plugin.getConfig().getString("no-permission")));
                }
            }
            else if (args.length >= 1 && args[0].equalsIgnoreCase("upgrade")) {
                if (sender.hasPermission("cosmoprinter.upgrade")) {
                    Player player = (Player) sender;
                    ItemStack item = player.getItemInHand();
                    Economy economy = this.plugin.getEconomy();

                    if (item != null && item.getType() != Material.AIR) {
                        NBTItem nbti = new NBTItem(item);

                        double upgrade_price;
                        if (nbti.getInteger("earningUpgrades") == 0) {
                            upgrade_price = this.plugin.getConfig().getInt("upgrade-price");
                        }
                        else {
                            upgrade_price = this.plugin.getConfig().getInt("upgrade-price") * nbti.getInteger("earningUpgrades") * this.plugin.getConfig().getInt("upgrade-price-multiplier");
                        }

                        if (nbti.getBoolean("isPrinter")) {
                            if (economy.getBalance(player) >= upgrade_price) {
                                if (nbti.getInteger("earningUpgrades") < this.plugin.getConfig().getInt("max-upgrades")) {
                                    economy.withdrawPlayer(player, upgrade_price);
                                    nbti.setInteger("earningUpgrades", nbti.getInteger("earningUpgrades") + 1);
                                    player.setItemInHand(nbti.getItem());
                                    player.sendMessage(Utilities.translateColor(this.plugin.getConfig().getString("already-max-upgrades")) + nbti.getInteger("earningUpgrades"));
                                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 0.65F, 2);

                                    return true;
                                }
                                else {
                                    player.playSound(player.getLocation(), Sound.ANVIL_LAND, 0.6F, 2);
                                    player.sendMessage(Utilities.translateColor(this.plugin.getConfig().getString("already-max-upgrades")));
                                }
                            }
                            else {
                                player.playSound(player.getLocation(), Sound.ANVIL_LAND, 0.6F, 2);
                                player.sendMessage(Utilities.translateColor(this.plugin.getConfig().getString("not-enough-money")));
                            }
                        }
                        else {
                            player.playSound(player.getLocation(), Sound.ANVIL_LAND, 0.6F, 2);
                            player.sendMessage(Utilities.translateColor(this.plugin.getConfig().getString("not-holding-printer")));
                            return true;
                        }
                    }
                    else {
                        player.playSound(player.getLocation(), Sound.ANVIL_LAND, 0.6F, 2);
                        player.sendMessage(Utilities.translateColor(this.plugin.getConfig().getString("not-holding-printer")));
                    }
                }
            }
        }

        return true;
    }
}
