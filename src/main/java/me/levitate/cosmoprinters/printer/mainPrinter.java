package me.levitate.cosmoprinters.printer;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.tr7zw.nbtapi.NBTItem;
import me.levitate.cosmoprinters.CosmoPrinters;
import me.levitate.cosmoprinters.commands.mainCommand;
import me.levitate.cosmoprinters.utilities.Utilities;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class mainPrinter {
    private final CosmoPrinters plugin;

    public mainPrinter(CosmoPrinters instance) {
        plugin = instance;
    }

    public ItemStack getPrinter() {
        ItemStack printer = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        SkullMeta meta = (SkullMeta) printer.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        profile.getProperties().put("textures", new Property("textures", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjBmMTBlODU0MThlMzM0ZjgyNjczZWI0OTQwYjIwOGVjYWVlMGM5NWMyODc2ODVlOWVhZjI0NzUxYTMxNWJmYSJ9fX0="));
        Field profileField;
        try {
            profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }

        List<String> lore = new ArrayList();
        Iterator itemLore = this.plugin.getConfig().getStringList("item-lore").iterator();

        while (itemLore.hasNext()) {
            String s = (String)itemLore.next();
            lore.add(Utilities.translateColor(s));
        }

        meta.setLore(lore);
        meta.setDisplayName(Utilities.translateColor(this.plugin.getConfig().getString("item-name")));
        meta.spigot().setUnbreakable(true);
        printer.setItemMeta(meta);

        NBTItem nbti = new NBTItem(printer);
        nbti.setBoolean("isPrinter", true);
        nbti.setInteger("earningUpgrades", 2); // set to 2 for testing, change this to 0 later

        return nbti.getItem();
    }

    public List<ItemStack> get_printers(Player plr) {
        List<ItemStack> printers = new ArrayList<ItemStack>();

        for (ItemStack item : plr.getInventory().getContents()) {
            if (item != null) {
                NBTItem nbti = new NBTItem(item);

                if (nbti.getBoolean("isPrinter")) {
                    printers.add(item);
                }
            }
        }

        return printers;
    }

    public void run_printer() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            List<ItemStack> printers = get_printers(player);
            Economy economy = this.plugin.getEconomy();

            int total = 0;

            for (ItemStack printer : printers) {
                NBTItem nbti = new NBTItem(printer);

                float deposit_amount = 0;

                if (nbti.getInteger("earningUpgrades") != 0) {
                    deposit_amount = this.plugin.getConfig().getInt("printer-money") * (nbti.getInteger("earningUpgrades") * (float)this.plugin.getConfig().getDouble("upgrade-earnings-increase"));
                }
                else {
                    deposit_amount = this.plugin.getConfig().getInt("printer-money");
                }

                if (deposit_amount != 0) {
                    total += (int) deposit_amount;
                }
            }

            if (total != 0) {
                economy.depositPlayer(player, total);
                player.sendMessage(Utilities.translateColor("&2• &aYou have received $" + total + " from your printer."));
            }
        }
    }
}
