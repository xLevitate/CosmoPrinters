package me.levitate.cosmoprinters.events;

import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.plugin.NBTAPI;
import me.levitate.cosmoprinters.CosmoPrinters;
import me.levitate.cosmoprinters.printer.mainPrinter;
import me.levitate.cosmoprinters.utilities.Utilities;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class blockPlaceEvent implements Listener {
    private final CosmoPrinters plugin;

    public blockPlaceEvent(CosmoPrinters instance) {
        plugin = instance;
    }

    @EventHandler
    void onBlockPlace(BlockPlaceEvent event) {
        Block b = event.getBlock();
        Player p = event.getPlayer();
        NBTItem nbti = new NBTItem(p.getItemInHand());
        mainPrinter printer = new mainPrinter(plugin);
        Material block_material = event.getBlockPlaced().getType();

        if (event.getBlockPlaced().getType() == Material.SKULL && nbti.getBoolean("isPrinter")) {
            p.sendMessage(Utilities.translateColor("&c• You can't place a printer."));
            event.setCancelled(true);
        }
    }

}
