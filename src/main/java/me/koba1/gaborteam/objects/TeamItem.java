package me.koba1.gaborteam.objects;

import lombok.Getter;
import me.koba1.gaborteam.utils.formatters.Formatter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class TeamItem {
    @Getter
    private ItemStack itemStack;
    private final ConfigurationSection section;

    public TeamItem(ConfigurationSection section) {
        this.section = section;
        load();
    }

    public ItemStack getItemStack() {
        return getItemStack(true);
    }

    public ItemStack getItemStack(boolean useRandomAmount) {
        return this.itemStack;
    }

    public void load() {
        ConfigurationSection c = this.section;
        ItemStack is = new ItemStack(Material.STONE, 1);
        ItemMeta im = is.getItemMeta();
        if (c.contains("material")) {
            if(c.getString("material").startsWith("base64-")) {
                String base64 = c.getString("material").split("-")[1];
                is.setItemMeta(im);
                is.setType(Material.PLAYER_HEAD);
                UUID hashAsId = new UUID(base64.hashCode(), base64.hashCode());
                is = Bukkit.getUnsafe().modifyItemStack(is,
                        "{SkullOwner:{Id:\"" + hashAsId + "\",Properties:{textures:[{Value:\"" + base64 + "\"}]}}}"
                );
                im = is.getItemMeta();
            } else if(c.getString("material").startsWith("head-")) {
                String name = c.getString("material").split("-")[1];
                is.setItemMeta(im);

                is.setType(Material.PLAYER_HEAD);
                SkullMeta meta = (SkullMeta) is.getItemMeta();
                meta.setOwner(name);
                im = is.getItemMeta();
            } else {
                Material material = Material.getMaterial(c.getString("material").toUpperCase());
                if (material == null) {
                    Bukkit.getLogger().severe("Material nem található: " + section.getCurrentPath());
                    return;
                }
                is.setType(material);
            }
        }

        if (c.contains("amount")) {
            is.setAmount(c.getInt("amount"));
        }

//        if(c.contains("infinitevoucher")) {
//            is.setItemMeta(im);
//            String voucher = c.getString("infinitevoucher");
//            NBTItem nbtItem = new NBTItem(is);
//            nbtItem.setString("voucher", voucher);
//            is = nbtItem.getItem();
//            im = is.getItemMeta();
//            //System.out.println(new NBTItem(is).getString("voucher"));
//        }

        if (c.contains("display_name")) {
            String dpName = c.getString("display_name");
            im.setDisplayName(Formatter.applyColor(dpName));
        }

        if (c.contains("lore"))
            im.setLore(Formatter.format(c.getStringList("lore")).applyColor().list());

        if (c.contains("custom_model_data"))
            im.setCustomModelData(c.getInt("custom_model_data"));

        if (c.contains("unbreakable"))
            im.setUnbreakable(c.getBoolean("unbreakable"));

        if (c.contains("durability")) {
            is.setItemMeta(im);
            is.setDurability(((short) c.getInt("durability")));
            im = is.getItemMeta();
        }

        if (c.contains("enchantments")) {
            List<String> enchantments = c.getStringList("enchantments");
            if (!enchantments.isEmpty()) {
                Map<Enchantment, Integer> enchants = new HashMap<>();

                for (String e : enchantments) {
                    if (e.contains(";")) {
                        String[] parts = e.split(";");
                        if (parts.length == 2) {
                            Enchantment enc = Enchantment.getByName(parts[0].toUpperCase());
                            int level = 1;

                            if (enc != null) {
                                try {
                                    level = Integer.parseInt(parts[1]);
                                } catch (NumberFormatException ex) {
                                    ex.printStackTrace();
                                }

                                enchants.put(enc, level);
                            }
                        }
                    }
                }

                if(is.getType() == Material.ENCHANTED_BOOK) {
                    is.setItemMeta(im);
                    EnchantmentStorageMeta encIm = (EnchantmentStorageMeta) is.getItemMeta();

                    for (Map.Entry<Enchantment, Integer> hash : enchants.entrySet()) {
                        encIm.addStoredEnchant(hash.getKey(), hash.getValue(), true);
                    }
                    im = encIm;
                } else {
                    for (Map.Entry<Enchantment, Integer> hash : enchants.entrySet()) {
                        im.addEnchant(hash.getKey(), hash.getValue(), true);
                    }
                }
            }
        }

        if (c.contains("potion_effects") && c.isList("potion_effects")) {
            List<PotionEffect> potionEffects = new ArrayList<>();
            for (String e : c.getStringList("potion_effects")) {
                try {
                    if (!e.contains(";")) {
                        continue;
                    }
                    String[] metaParts = e.split(";", 3);
                    if (metaParts.length != 3) {
                        continue;
                    }
                    PotionEffectType type = PotionEffectType.getByName(metaParts[0]);
                    int duration = Integer.parseInt(metaParts[1]);
                    int amplifier = Integer.parseInt(metaParts[2]);

                    if (type == null) {
                        continue;
                    }

                    potionEffects.add(type.createEffect(duration, amplifier));
                } catch (Exception ex) {
                }
            }

            if (!potionEffects.isEmpty()) {
                is.setItemMeta(im);
                PotionMeta meta = (PotionMeta) is.getItemMeta();
                for (PotionEffect potionEffect : potionEffects) {
                    meta.addCustomEffect(potionEffect, false);
                }
                im = meta;
            }
        }

        if (c.contains("item_flags")) {
            if (c.isString("item_flags")) {
                String flagAsString = c.getString("item_flags");
                ItemFlag flag;
                try {
                    flag = ItemFlag.valueOf(flagAsString.toUpperCase());
                    im.addItemFlags(flag);
                } catch (IllegalArgumentException | NullPointerException ignored) {
                }
            } else {
                List<ItemFlag> flags = new ArrayList<>();

                for (String flagAsString : c.getStringList("item_flags")) {
                    try {
                        flags.add(ItemFlag.valueOf(flagAsString.toUpperCase()));
                    } catch (Exception ignored) {
                    }
                }

                if (!flags.isEmpty()) {
                    im.addItemFlags(flags.toArray(new ItemFlag[0]));
                }
            }
        }

        is.setItemMeta(im);
        this.itemStack = is;
    }
}
