package me.thesnipe12;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Utils {
    public static boolean containsIgnoreCase(List<String> list, String soughtFor, boolean split) {
        for (String current : list) {
            if (split) {
                if(current.split(" ")[0].equalsIgnoreCase(soughtFor)) return true;
            } else {
                if(current.equalsIgnoreCase(soughtFor)) return true;
            }
        }
        return false;
    }
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static Enchantment containsEnchantIgnoreCase(Set<Enchantment> lst, String s){
        for (Enchantment e : lst) {
            if(Enchantment.getByKey(NamespacedKey.fromString(s.toLowerCase())) == e) return e;
        }
        return null;
    }

    public static ItemStack removeBannedEnchant(ItemStack item, List<String> maxEnchantLevel) {
        ItemStack lastItem = null;
        if(item.getItemMeta() == null) return null;
        if(item.getItemMeta().hasEnchants()) {
            Map<Enchantment, Integer> enchants = item.getItemMeta().getEnchants();
            for (String s : maxEnchantLevel) {
                if (s.equals("none 0")) return null;
                Enchantment e = containsEnchantIgnoreCase(enchants.keySet(), s.split(" ")[0]);
                if (e != null) {
                    if (isNumeric(s.split(" ")[1])) {
                        int maxLevel = Integer.parseInt(s.split(" ")[1]);
                        if (maxLevel == 0) {
                            if(lastItem == null) lastItem = new ItemStack(item);
                            lastItem.removeEnchantment(e);
                        }else if (enchants.get(e) > maxLevel) {
                            if(lastItem == null) lastItem = new ItemStack(item);
                            lastItem.removeEnchantment(e);
                            lastItem.addUnsafeEnchantment(e, maxLevel);
                        }
                    }
                }
            }
        }
        return lastItem;
    }
}
