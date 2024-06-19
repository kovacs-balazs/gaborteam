package me.koba1.gaborteam.objects;

import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import lombok.Getter;
import me.koba1.gaborteam.Main;
import me.koba1.gaborteam.files.PlayerDataFile;
import me.koba1.gaborteam.utils.Utils;
import me.koba1.gaborteam.utils.formatters.Formatter;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
public class Team {

    public static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacyAmpersand();

    private final String key;
    private String name;
    private String color;
    private final List<Player> players;
    private int maxMembers;
    private TeamEntity entity;

    public Team(String key) {
        this.key = key;
        this.players = new ArrayList<>();
        this.maxMembers = 0;
        this.name = null;
        this.entity = null;

        load();

        Main.getTeams().put(this.key, this);
    }

    public void load() {
        String path = "teams." + this.key + ".";
        this.name = Formatter.applyColor(Main.getInstance().getConfig().getString(path + "name"));
        this.color = Formatter.applyColor(Main.getInstance().getConfig().getString(path + "color")); // LEGACY.deserialize(Main.getInstance().getConfig().getString(path + "color"));
        this.maxMembers = Main.getInstance().getConfig().getInt(path + "maximum_members");

        this.entity = new TeamEntity(
                this,
                Main.getInstance().getConfig().getString(path + "entity.name"),
                Utils.getLocation(Main.getInstance().getConfig().getString(path + "entity.location")),
                Main.getInstance().getConfig().getDouble(path + "entity.health")
        );

        this.entity.getEntity().setHealth(Main.getInstance().getConfig().getDouble(path + "entity.last_health"));

        for (Player player : this.players) {
            setPrefix(player);
        }

        System.out.println("Loadolva team: " + this.key);
    }

    public boolean isRespawn() {
        return !this.entity.getEntity().isDead();
    }
    public void setPrefix(Player player) {
        TabAPI.getInstance().getNameTagManager().setPrefix(TabAPI.getInstance().getPlayer(player.getUniqueId()), this.color);
    }

    public void removePrefix(Player player) {
        if(player == null) return;
        TabPlayer target = TabAPI.getInstance().getPlayer(player.getUniqueId());
        if(target == null) return;
        TabAPI.getInstance().getNameTagManager().setPrefix(target, null);
    }

    public void join(Player player) {
        setPrefix(player);
        this.players.add(player);
        if(isRespawn()) {
            player.setGameMode(GameMode.SURVIVAL);
        }
        PlayerDataFile.get().set(player.getUniqueId().toString(), this.key);
        PlayerDataFile.getConfig().save();
    }

    public void leave(UUID uuid) {
        removePrefix(Bukkit.getPlayer(uuid));
        if(isRespawn()) {
            Player p = Bukkit.getPlayer(uuid);
            if(p == null) return;
            p.setGameMode(GameMode.SURVIVAL);
        }
        PlayerDataFile.get().set(uuid.toString(), null);
        PlayerDataFile.getConfig().save();
    }

    public void save() {
//        for (UUID player : this.players) {
//            PlayerDataFile.get().set(player.toString(), this.teamName);
//        }
//        PlayerDataFile.getConfig().save();
    }
}

