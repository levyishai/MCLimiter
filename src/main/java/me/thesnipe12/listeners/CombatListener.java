package me.thesnipe12.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

import static me.thesnipe12.utilities.Utilities.isNumeric;

public class CombatListener implements Listener {
    private final Plugin plugin;
    private final HashMap<Player, Integer> combatTimer;

    public CombatListener(Plugin plugin, HashMap<Player, Integer> combatTimer) {
        this.plugin = plugin;
        this.combatTimer = combatTimer;
    }

    @EventHandler
    public void on(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;

        Entity damager = event.getDamager();
        if (damager instanceof Projectile && ((Projectile) damager).getShooter() instanceof Entity) {
            damager = (Entity) ((Projectile) damager).getShooter();
        }
        Entity damaged = event.getEntity();

        if (damager instanceof Player && damaged instanceof Player && damager != damaged) {
            combatTimer.put((Player) damager, 30);
            combatTimer.put((Player) damaged, 30);
            HashMap<Material, Integer> maxPVPStack = new HashMap<>();
            for (String s : plugin.getConfig().getStringList("items.maxPVPStack")) {
                if (s.equals("none 0")) return;
                String[] split = s.split(" ");
                if (isNumeric(split[1]))
                    maxPVPStack.put(Material.getMaterial(split[0].toUpperCase()), Integer.parseInt(split[1]));
            }

            limitItemsInInventoryFromList((Player) damager, maxPVPStack);
            limitItemsInInventoryFromList((Player) damaged, maxPVPStack);
        }
    }

    private void limitItemsInInventoryFromList(Player player, HashMap<Material, Integer> list) {
        HashMap<Material, Integer> itemsAmount = new HashMap<>();
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null) continue;
            if (list.containsKey(item.getType())) {
                itemsAmount.put(item.getType(), itemsAmount.getOrDefault(item.getType(), 0) + item.getAmount());
            }
        }

        if (itemsAmount.isEmpty()) return;
        for (Material material : itemsAmount.keySet()) {
            if (itemsAmount.get(material) > list.get(material)) {
                player.getInventory().remove(material);
                player.getInventory().addItem(new ItemStack(material, list.get(material)));
            }
        }
    }

}
