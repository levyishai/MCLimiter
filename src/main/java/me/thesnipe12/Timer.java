package me.thesnipe12;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import static me.thesnipe12.listeners.CombatListener.combat;
public class Timer extends BukkitRunnable {
    private final Plugin plugin;

    public Timer(Plugin plugin){
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player p : combat.keySet()) {
            if (combat.get(p) > 0) {
                combat.put(p, combat.get(p) - 1);
            }
        }

        removeBannedEffectsForOnlinePlayers();
    }

    private void removeBannedEffectsForOnlinePlayers() {
        for (Player currentPlayer : Bukkit.getServer().getOnlinePlayers()) {
            combat.putIfAbsent(currentPlayer, 0);

            if(plugin.getConfig().getStringList("bannedEffects").get(0).equals("none")) return;

            for(String s : plugin.getConfig().getStringList("bannedEffects")){
                PotionEffectType effectType = PotionEffectType.getByKey(NamespacedKey.fromString(s.toLowerCase()));

                if(effectType != null && currentPlayer.hasPotionEffect(effectType)){
                    currentPlayer.removePotionEffect(effectType);
                }
            }
        }
    }

}
