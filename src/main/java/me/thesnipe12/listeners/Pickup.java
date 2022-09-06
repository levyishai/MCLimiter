package me.thesnipe12.listeners;

import me.thesnipe12.MCLimiter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static me.thesnipe12.Utils.containsIgnoreCase;
import static me.thesnipe12.Utils.removeBannedEnchant;


public class Pickup implements Listener {
    private final MCLimiter plugin;

    public Pickup(MCLimiter plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void on(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        ItemStack item = event.getItem().getItemStack();
        Material itemType = item.getType();
        final List<String> bannedItems = plugin.getConfig().getStringList("items.bannedItems");
        final List<String> maxPVPStack = plugin.getConfig().getStringList("items.maxPVPStack");
        final List<String> maxEnchantLevel = plugin.getConfig().getStringList("items.maxEnchantLevel");
        
        if(!bannedItems.get(0).equals("none") &&containsIgnoreCase(bannedItems, itemType.toString(), false)) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> player.getInventory().remove(itemType), 1L);
        }
        
        if(!bannedItems.get(0).equals("none 0")&&containsIgnoreCase(maxPVPStack, itemType.toString(), true)) {
            if (PVP.combat.get(player) > 0) event.setCancelled(true);
        }
        
        if ( item.getItemMeta() == null) return;
        ItemStack lastItem = removeBannedEnchant(item, maxEnchantLevel);

        final ItemStack lastItemF = lastItem;
        if (lastItem != null) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                player.getInventory().remove(item);
                player.getInventory().addItem(lastItemF);
            }, 1L);
        }
    }
}

