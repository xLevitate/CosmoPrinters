package me.levitate.cosmoprinters.menu;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.levitate.cosmoprinters.CosmoPrinters;
import me.levitate.cosmoprinters.utilities.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class printerMenu
{
    private final Inventory inventory;

    public String printerValue = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjBmMTBlODU0MThlMzM0ZjgyNjczZWI0OTQwYjIwOGVjYWVlMGM5NWMyODc2ODVlOWVhZjI0NzUxYTMxNWJmYSJ9fX0=";
    public String speedValue = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWRhMDI3NDc3MTk3YzZmZDdhZDMzMDE0NTQ2ZGUzOTJiNGE1MWM2MzRlYTY4YzhiN2JjYzAxMzFjODNlM2YifX19";
    public String withdrawValue = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjA5Mjk5YTExN2JlZTg4ZDMyNjJmNmFiOTgyMTFmYmEzNDRlY2FlMzliNDdlYzg0ODEyOTcwNmRlZGM4MWU0ZiJ9fX0=";

    printerMenu(Player player, CosmoPrinters plugin)
    {
        this.inventory = Bukkit.createInventory(null, 27, plugin.getPrinterManager().getMenuTitle());

        ArrayList<String> printerLore = new ArrayList<>();
        printerLore.add("§7Printers that work passively");
        printerLore.add("§7and generate money.");
        printerLore.add("");
        printerLore.add("§9§lAMOUNT: §f0 / " + plugin.getPrinterManager().getMaxPrinters());
        printerLore.add("§9§lPRICE: §f$10,000");
        printerLore.add("§e§l➥ §eClick to purchase!");

        ArrayList<String> speedLore = new ArrayList<>();
        speedLore.add("§7Upgrade your printers");
        speedLore.add("§7making them faster.");
        speedLore.add("");
        speedLore.add("§9§lAMOUNT: §f0 / " + plugin.getPrinterManager().getMaxSpeed());
        speedLore.add("§9§lPRICE: §f$50,000");
        speedLore.add("§e§l➥ §eClick to purchase!");

        ArrayList<String> withdrawLore = new ArrayList<>();
        withdrawLore.add("§7Withdraw the money");
        withdrawLore.add("§7you have stored.");
        withdrawLore.add("");
        withdrawLore.add("§9§lAMOUNT: §f$999,999");
        withdrawLore.add("§e§l➥ §eClick to withdraw!");

        createGlass();

        // Printer button
        inventory.setItem(10, createPrinter(printerValue, plugin.getPrinterManager().getPrinterTitle(), printerLore));

        // Speed upgrade button
        inventory.setItem(13, createSpeed(speedValue, plugin.getPrinterManager().getSpeedTitle(), speedLore));

        // Withdraw money button
        inventory.setItem(16, createWithdraw(withdrawValue, plugin.getPrinterManager().getWithdrawTitle(), withdrawLore));

        player.openInventory(inventory);
    }

    protected ItemStack createItem(final String... lore) {
        final ItemStack item = XMaterial.BLUE_STAINED_GLASS_PANE.parseItem();
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(" ");

        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    public ItemStack createPrinter(String value, String name, ArrayList<String> lore) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
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

        meta.setDisplayName(name);
        meta.setLore(lore);

        head.setItemMeta(meta);

        return head;
    }

    public ItemStack createSpeed(String value, String name, ArrayList<String> lore) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        profile.getProperties().put("textures", new Property("textures", value));
        Field profileField;
        try {
            profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }

        meta.setDisplayName(name);
        meta.setLore(lore);

        head.setItemMeta(meta);

        return head;
    }

    public ItemStack createWithdraw(String value, String name, ArrayList<String> lore) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        profile.getProperties().put("textures", new Property("textures", value));
        Field profileField;
        try {
            profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }

        meta.setDisplayName(name);
        meta.setLore(lore);

        head.setItemMeta(meta);

        return head;
    }

    void createGlass()
    {
        for (int i = 0; i < 27; i++)
        {
            inventory.setItem(i, createItem());
        }
    }
}
