package me.thesnipe12.listeners;

import me.thesnipe12.utilities.Utilities;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;

public class InteractionsListener implements Listener {
    private final Plugin plugin;
    private final HashMap<Player, Integer> combatTimer;

    public InteractionsListener(Plugin plugin, HashMap<Player, Integer> combatTimer) {
        this.plugin = plugin;
        this.combatTimer = combatTimer;
    }

    @EventHandler
    public void on(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        if (!(event.getWhoClicked() instanceof Player player)) return;

        final Material itemType = event.getCurrentItem().getType();
        final ItemStack item = event.getCurrentItem();
        final Inventory inventory = event.getInventory();

        removeBannedItemsAndMakeNeededAction(event, player, item);

        if (shouldStopFromMovingToContainer(player, inventory, itemType))
            event.setCancelled(true);
    }

    @EventHandler
    public void on(PlayerInteractEvent event) {
        if (event.getItem() == null) return;

        final Player player = event.getPlayer();
        final ItemStack item = event.getItem();

        removeBannedItemsAndMakeNeededAction(event, player, item);
    }

    private boolean shouldStopFromMovingToContainer(Player player, Inventory currentInventory, Material currentItem) {
        final List<String> maxPVPStack = plugin.getConfig().getStringList("items.maxPVPStack");

        return
                currentInventory.getType().equals(InventoryType.CRAFTING) && maxPVPStack.get(0).equals("none 0") &&
                !Utilities.containsIgnoreCase(maxPVPStack, currentItem.toString(), true) && combatTimer.get(player) > 0;
    }

    private void removeBannedItemsAndMakeNeededAction(Cancellable cancellable, Player player, ItemStack currentItem) {
        final List<String> maxEnchantLevel = plugin.getConfig().getStringList("items.maxEnchantLevel");

        if (isBanned(currentItem.getType())) {
            cancellable.setCancelled(true);
            player.getInventory().remove(currentItem.getType());
        }

        if (currentItem.getItemMeta() == null) return;
        final ItemStack lastItem = Utilities.maximizeEnchants(currentItem, maxEnchantLevel);
        if (lastItem == currentItem) return;

        cancellable.setCancelled(true);
        Utilities.replaceItem(player, currentItem, lastItem);
    }

    private boolean isBanned(Material itemType) {
        final List<String> bannedItems = plugin.getConfig().getStringList("items.bannedItems");

        return Utilities.containsIgnoreCase(bannedItems, itemType.toString(), true);
    }

}
