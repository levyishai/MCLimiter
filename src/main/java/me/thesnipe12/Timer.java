package me.thesnipe12;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import static me.thesnipe12.listeners.PVP.combat;
public class Timer extends BukkitRunnable {
    MCLimiter plugin;
    public Timer(MCLimiter plugin){
        this.plugin = plugin;
    }
    @Override
    public void run() {
        for (Player p : combat.keySet()) {
            if (combat.get(p) > 0) {
                combat.put(p, combat.get(p) - 1);
            }
        }

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            combat.putIfAbsent(p, 0);

            if(plugin.getConfig().getStringList("bannedEffects").get(0).equals("none")) return;

            for(String s : plugin.getConfig().getStringList("bannedEffects")){
                PotionEffectType effectType = PotionEffectType.getByKey(NamespacedKey.fromString(s.toLowerCase()));
                if(effectType != null && p.hasPotionEffect(effectType)){
                    p.removePotionEffect(effectType);
                }
            }
        }
    }
}
