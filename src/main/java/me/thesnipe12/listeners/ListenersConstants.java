package me.thesnipe12.listeners;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;

public class ListenersConstants {
    static final TextComponent
            PREFIX = new TextComponent(ChatColor.GREEN + "MCLimiter" + ChatColor.GRAY + " >>" + ChatColor.RESET + " "),
            NOT_UP_TO_DATE_MESSAGE_PART1 = new TextComponent("There is a new version of the plugin available! Go to "),
            NOT_UP_TO_DATE_MESSAGE_PART2 = new TextComponent("\"https://www.spigotmc.org/resources/simplecl.101603/\""),
            NOT_UP_TO_DATE_MESSAGE_PART3 = new TextComponent(" to download it!");

    static {
        NOT_UP_TO_DATE_MESSAGE_PART1.setColor(net.md_5.bungee.api.ChatColor.RED);
        NOT_UP_TO_DATE_MESSAGE_PART2.setColor(net.md_5.bungee.api.ChatColor.GOLD);
        NOT_UP_TO_DATE_MESSAGE_PART2.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,
                "https://www.spigotmc.org/resources/simplecl.101603/"));
        NOT_UP_TO_DATE_MESSAGE_PART2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new Text("Click to open URL")));
        NOT_UP_TO_DATE_MESSAGE_PART3.setColor(net.md_5.bungee.api.ChatColor.RED);
    }
}
