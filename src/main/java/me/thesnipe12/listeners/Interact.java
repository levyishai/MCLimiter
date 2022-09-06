package me.thesnipe12.listeners;

import me.thesnipe12.MCLimiter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static me.thesnipe12.Utils.containsIgnoreCase;
import static me.thesnipe12.Utils.removeBannedEnchant;
import static me.thesnipe12.listeners.PVP.combat;

public class Interact implements Listener {
    MCLimiter plugin;
    public Interact(MCLimiter plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void on(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        if (!(event.getWhoClicked() instanceof Player player)) return;

        Material itemType = event.getCurrentItem().getType();
        ItemStack item = event.getCurrentItem();
        final List<String> bannedItems = plugin.getConfig().getStringList("items.bannedItems");
        final List<String> maxEnchantLevel = plugin.getConfig().getStringList("items.maxEnchantLevel");
        final List<String> maxPVPStack = plugin.getConfig().getStringList("items.maxPVPStack");

        if (!bannedItems.get(0).equals("none" +
                "") && containsIgnoreCase(bannedItems, itemType.toString(), false)) {
            event.setCancelled(true);
            player.getInventory().remove(itemType);
        }

        if (item.getItemMeta()  == null) return;
        ItemStack lastItem = removeBannedEnchant(item, maxEnchantLevel);

        if(lastItem != null) {
            event.setCancelled(true);
            for(ItemStack i : player.getInventory()) {
                if(i == null) continue;
                if(i.equals(item)) {
                    player.getInventory().remove(item);
                    player.getInventory().addItem(lastItem);
                    return;
                }
            }
        }

        if(event.getInventory().getType().equals(InventoryType.CRAFTING)) return;
        if(maxPVPStack.get(0).equals("none 0")) return;
        if (containsIgnoreCase(maxPVPStack, itemType.toString(), true)
                && combat.get(((Player) event.getWhoClicked()).getPlayer()) > 0) {
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void on(PlayerInteractEvent event) {
        if (event.getItem() == null) return;

        Player player = event.getPlayer();
        Material itemType = event.getItem().getType();
        ItemStack item = event.getItem();
        final List<String> bannedItems = plugin.getConfig().getStringList("items.bannedItems");
        final List<String> maxEnchantLevel = plugin.getConfig().getStringList("items.maxEnchantLevel");

        if (!bannedItems.get(0).equals("none") && containsIgnoreCase(bannedItems, itemType.toString(), false)) {
            event.setCancelled(true);
            player.getInventory().remove(itemType);
        }

        if (item.getItemMeta() == null) return;
        ItemStack lastItem = removeBannedEnchant(item, maxEnchantLevel);

        if(lastItem != null) {
            event.setCancelled(true);
            for(ItemStack i : player.getInventory()){
                if(i == null) continue;
                if(i.equals(item)){
                    player.getInventory().remove(item);
                    player.getInventory().addItem(lastItem);
                    return;
                }
            }
        }
    }
}
