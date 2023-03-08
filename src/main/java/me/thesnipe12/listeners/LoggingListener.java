package me.thesnipe12.listeners;

import me.thesnipe12.PluginConstants;
import me.thesnipe12.utilities.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class LoggingListener implements Listener {
    private final Plugin plugin;

    public LoggingListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void on(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        sendNotUpToDateMessageIfOperator(player);
    }

    private void sendNotUpToDateMessageIfOperator(Player player) {
        if (!Bukkit.getServer().getOperators().contains(player)) return;

        new UpdateChecker(plugin, PluginConstants.RESOURCE_ID).getVersion(version -> {

            if (!plugin.getDescription().getVersion().equalsIgnoreCase(version)) {
                player.spigot().sendMessage(ListenersConstants.PREFIX, ListenersConstants.NOT_UP_TO_DATE_MESSAGE_PART1,
                        ListenersConstants.NOT_UP_TO_DATE_MESSAGE_PART2, ListenersConstants.NOT_UP_TO_DATE_MESSAGE_PART3);
            }

        });
    }

}
