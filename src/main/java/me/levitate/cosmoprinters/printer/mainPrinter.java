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
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
        meta.spigot().setUnbreakable(true);
        printer.setItemMeta(meta);

        NBTItem nbti = new NBTItem(printer);
        nbti.setBoolean("isPrinter", true);

        return nbti.getItem();
    }

    public void run_printer() {
        // this the rewrite
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            int printer_amount = 0;
            Economy economy = this.plugin.getEconomy();

            for (ItemStack item : player.getInventory().getContents()) {
                if (item != null) {
                    NBTItem nbti = new NBTItem(item);

                    if (nbti.getBoolean("isPrinter")) {
                        printer_amount += 1;
                    }
                }
            }

            int deposit_amount = 1000 * printer_amount;

            if (deposit_amount != 0) {
                economy.depositPlayer(player, 1000 * printer_amount);
                player.sendMessage(Utilities.translateColor("&2• &aYou have received $1,000 from your printer."));
            }
        }
    }
}
