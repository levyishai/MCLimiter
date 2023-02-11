package me.thesnipe12.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.plugin.Plugin;

public class ResurrectListener implements Listener {
    private final Plugin plugin;

    public ResurrectListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void on(EntityResurrectEvent event) {
        if (!plugin.getConfig().getBoolean("totemsRevive"))
            event.setCancelled(true);
    }

}
