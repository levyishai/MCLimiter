package me.thesnipe12.listeners;

import me.thesnipe12.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;

import static me.thesnipe12.Utilities.containsIgnoreCase;
import static me.thesnipe12.Utilities.maximizeEnchants;


public class PickupListener implements Listener {
    private final Plugin plugin;
    private final HashMap<Player, Integer> combatTimer;

    public PickupListener(Plugin plugin, HashMap<Player, Integer> combatTimer) {
        this.plugin = plugin;
        this.combatTimer = combatTimer;
    }

    @EventHandler
    public void on(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        final ItemStack item = event.getItem().getItemStack();
        final Material itemType = item.getType();
        final List<String> maxEnchantLevel = plugin.getConfig().getStringList("items.maxEnchantLevel");

        removeBannedItemIfNeeded(player, itemType);

        if (shouldStopActionBecauseOfCombat(player, itemType))
            event.setCancelled(true);

        if (item.getItemMeta() == null) return;
        final ItemStack maximizedEnchantsItem = maximizeEnchants(item, maxEnchantLevel);

        if (maximizedEnchantsItem.equals(item)) return;

        Bukkit.getScheduler().runTaskLater(
                plugin,
                () -> Utilities.replaceItem(player, item, maximizedEnchantsItem),
                1L
        );
    }

    private boolean shouldStopActionBecauseOfCombat(Player player, Material itemType) {
        final List<String> maxPVPStack = plugin.getConfig().getStringList("items.maxPVPStack");

        return !maxPVPStack.get(0).equals("none 0") &&
                containsIgnoreCase(maxPVPStack, itemType.toString(), true) &&
                combatTimer.get(player) > 0;
    }

    private void removeBannedItemIfNeeded(Player player, Material material) {
        final List<String> bannedItems = plugin.getConfig().getStringList("items.bannedItems");

        if (!bannedItems.get(0).equals("none") && containsIgnoreCase(bannedItems, material.toString(), false))
            Bukkit.getScheduler().runTaskLater(plugin, () -> player.getInventory().remove(material), 1L);
    }

}

