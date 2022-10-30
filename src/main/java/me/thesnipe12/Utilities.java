package me.thesnipe12;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Utilities {
    /**
     * Checks if a list contains a string, ignoring case.
     *
     * @param list      the list to check.
     * @param soughtFor the string to check for.
     * @param split     whether to split the string by spaces and only check for the first word in the current word each iteration.
     * @return true if the list contains the string, ignoring case, false otherwise.
     */
    public static boolean containsIgnoreCase(List<String> list, String soughtFor, boolean split) {
        for (String current : list) {
            if (split) {
                if (current.split(" ")[0].equalsIgnoreCase(soughtFor)) return true;
            } else {
                if (current.equalsIgnoreCase(soughtFor)) return true;
            }
        }

        return false;
    }

    /**
     * Checks if a string is numeric.
     *
     * @param string the string to check.
     * @return true if the string is numeric, false otherwise.
     */
    public static boolean isNumeric(String string) {
        try {
            Double.parseDouble(string);
        } catch (NumberFormatException nfe) {
            return false;
        }

        return true;
    }

    /**
     * Checks if an enchantment set contains a string name of an enchantment, ignoring case.
     *
     * @param enchantmentSet the enchantment set to check.
     * @param string         the string to check for.
     * @return the enchantment if the set contains the string, ignoring case, null otherwise.
     */
    public static Enchantment containsEnchantIgnoreCase(Set<Enchantment> enchantmentSet, String string) {
        for (Enchantment e : enchantmentSet) {
            if (Enchantment.getByKey(NamespacedKey.fromString(string.toLowerCase())) == e) return e;
        }

        return null;
    }

    /**
     * Maximizes an ItemStack's enchantments from a given string that specifies the enchantment, and it's maximum.
     *
     * @param item the ItemStack to maximize its enchantments.
     * @param maxEnchantLevel the maximum level of the enchantment in a string list (should be formatted as 'enchantment level').
     * @return the ItemStack with its enchantments maximized.
     */
    public static ItemStack removeBannedEnchant(ItemStack item, List<String> maxEnchantLevel) {
        ItemStack lastItem = null;

        if (item.getItemMeta() == null || !item.getItemMeta().hasEnchants()) return item;
        final Map<Enchantment, Integer> enchants = item.getItemMeta().getEnchants();

        for (String currentString : maxEnchantLevel) {
            if (currentString.equals("none 0")) return item;

            final Enchantment enchantment = containsEnchantIgnoreCase(enchants.keySet(), currentString.split(" ")[0]);

            if (enchantment == null || !isNumeric(currentString.split(" ")[1])) continue;

            final int maxLevel = Integer.parseInt(currentString.split(" ")[1]);

            if (maxLevel == 0) {
                if (lastItem == null) lastItem = new ItemStack(item);

                lastItem.removeEnchantment(enchantment);
            } else if (enchants.get(enchantment) > maxLevel) {
                if (lastItem == null) lastItem = new ItemStack(item);

                lastItem.removeEnchantment(enchantment);
                lastItem.addUnsafeEnchantment(enchantment, maxLevel);
            }
        }

        return lastItem;
    }

}
