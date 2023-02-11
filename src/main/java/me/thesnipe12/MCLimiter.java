package me.thesnipe12;

import me.thesnipe12.listeners.InteractionsListener;
import me.thesnipe12.listeners.CombatListener;
import me.thesnipe12.listeners.PickupListener;
import me.thesnipe12.listeners.ResurrectListener;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;

public final class MCLimiter extends JavaPlugin {
    private final HashMap<Player, Integer> combatTimer = new HashMap<>();

    @Override
    public void onEnable() {
        configSetup();
        classSetup();
        checkForUpdates();
    }

    private void configSetup() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
    }

    private void checkForUpdates() {
        new UpdateChecker(this, 105111).getVersion(version -> {
            if (!version.equalsIgnoreCase(getDescription().getVersion())) {
                getLogger().warning("There is a new version of the plugin available! Go to " +
                        "\"https://www.spigotmc.org/resources/simplecl.101603/\" to download it.");
            } else {
                getLogger().info("You are running the latest version of the plugin!");
            }
        });
    }

    private void classSetup() {
        new Timer(this, combatTimer).runTaskTimer(this, 0L, 20L);
        List<Listener> listeners = List.of(
                new InteractionsListener(this, combatTimer), new PickupListener(this, combatTimer),
                new ResurrectListener(this), new CombatListener(this, combatTimer));
        for (Listener l : listeners) {
            this.getServer().getPluginManager().registerEvents(l, this);
        }
    }

}