package me.thesnipe12;

import me.thesnipe12.listeners.Interact;
import me.thesnipe12.listeners.PVP;
import me.thesnipe12.listeners.Pickup;
import me.thesnipe12.listeners.Resurrect;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class MCLimiter extends JavaPlugin {
    @Override
    public void onEnable() {
        configSetup();

        classSetup();
    }
    private void configSetup(){
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
    }
    private void classSetup() {
        new Timer(this).runTaskTimer(this, 0L, 20L);
        List<Listener> listeners = List.of(new Interact(this), new Pickup(this),
                new Resurrect(this), new PVP(this));
        for(Listener l : listeners) {
            this.getServer().getPluginManager().registerEvents(l, this);
        }
    }
}