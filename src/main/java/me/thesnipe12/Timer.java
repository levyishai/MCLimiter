package me.thesnipe12;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class Timer extends BukkitRunnable {
    private final Plugin plugin;
    private final HashMap<Player, Integer> combatTimer;

    public Timer(Plugin plugin, HashMap<Player, Integer> combatTimer) {
        this.plugin = plugin;
        this.combatTimer = combatTimer;
    }

    @Override
    public void run() {
        for (Player p : combatTimer.keySet()) {
            if (combatTimer.get(p) > 0) {
                combatTimer.put(p, combatTimer.get(p) - 1);
            }
        }

        removeBannedEffectsForOnlinePlayers();
    }

    private void removeBannedEffectsForOnlinePlayers() {
        for (Player currentPlayer : Bukkit.getServer().getOnlinePlayers()) {
            combatTimer.putIfAbsent(currentPlayer, 0);

            if (plugin.getConfig().getStringList("bannedEffects").get(0).equals("none")) return;

            for (String s : plugin.getConfig().getStringList("bannedEffects")) {
                PotionEffectType effectType = PotionEffectType.getByKey(NamespacedKey.fromString(s.toLowerCase()));

                if (effectType != null && currentPlayer.hasPotionEffect(effectType)) {
                    currentPlayer.removePotionEffect(effectType);
                }
            }
        }
    }

}
