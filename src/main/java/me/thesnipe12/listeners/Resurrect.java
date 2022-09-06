package me.thesnipe12.listeners;

import me.thesnipe12.MCLimiter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;

public class Resurrect implements Listener {
    MCLimiter plugin;
    public Resurrect(MCLimiter plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void on(EntityResurrectEvent event) {
        if(!plugin.getConfig().getBoolean("totemsRevive")) event.setCancelled(true);
    }
}
