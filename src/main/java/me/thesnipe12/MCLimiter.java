package me.thesnipe12;

import me.thesnipe12.listeners.InteractionsListener;
import me.thesnipe12.listeners.CombatListener;
import me.thesnipe12.listeners.PickupListener;
import me.thesnipe12.listeners.ResurrectListener;
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
        List<Listener> listeners = List.of(new InteractionsListener(this), new PickupListener(this),
                new ResurrectListener(this), new CombatListener(this));
        for(Listener l : listeners) {
            this.getServer().getPluginManager().registerEvents(l, this);
        }
    }

}