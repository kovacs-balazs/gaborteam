package me.koba1.gaborteam;

import fr.mrmicky.fastboard.FastBoard;
import lombok.Getter;
import me.koba1.gaborteam.coimmands.TeamCommand;
import me.koba1.gaborteam.files.PlayerDataFile;
import me.koba1.gaborteam.listeners.*;
import me.koba1.gaborteam.objects.Team;
import me.koba1.gaborteam.objects.TeamItem;
import me.koba1.gaborteam.scoreboard.ScoreboardManager;
import me.koba1.gaborteam.utils.Utils;
import me.koba1.gaborteam.utils.formatters.Formatter;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.event.player.PlayerLoadEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class Main extends JavaPlugin {

    @Getter private static Map<String, TeamItem> items;
    @Getter private static Map<Player, FastBoard> boards;
    @Getter private static Map<String, Team> teams;
    @Getter private static Main instance;
    @Getter private static ScoreboardManager scoreboardManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        items = new HashMap<>();
        boards = new HashMap<>();
        teams = new HashMap<>();

        if(!new File(getDataFolder(), "config.yml").exists()) {
            saveResource("config.yml", false);
        }

        new PlayerDataFile("players.yml");

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        getServer().getPluginManager().registerEvents(new EntityTargetListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawnListener(), this);
        getServer().getPluginManager().registerEvents(new EntityProtectionListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageListener(), this);

        TeamCommand teamCommand = new TeamCommand();
        teamCommand.registerMainCommand(this, "team");

        reload();

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            Utils.loadPlayer(onlinePlayer);
        }

        scoreboardTimer();

        TabAPI.getInstance().getEventBus().register(PlayerLoadEvent.class, event -> {
            Player player = Bukkit.getPlayer(event.getPlayer().getUniqueId());
            if(player == null) return;
            Team team = Utils.getTeam(player);
            if(team == null) return;

            team.setPrefix(player);
        });
    }

    public void loadItems() {
        items.clear();
        for (String key : getConfig().getConfigurationSection("items").getKeys(false)) {
            TeamItem item = new TeamItem(getConfig().getConfigurationSection("items." + key));
            items.put(key, item);
        }
    }

    public void reload() {
        for (String key : getConfig().getConfigurationSection("teams").getKeys(false)) {
            if (Main.getTeams().containsKey(key)) {
                teams.get(key).reload();
                continue;
            }

            new Team(key);
        }

        loadItems();

        String title = getConfig().getString("scoreboard.title");
        scoreboardManager = new ScoreboardManager(
                Formatter.applyColor(title),
                getConfig().getStringList("scoreboard.lines").stream().map(Formatter::applyColor).toList(),
                getConfig().getString("scoreboard.health_design")
        );
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (Team value : teams.values()) {
            value.save();
            value.getEntity().save();
        }
    }

    public void scoreboardTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<Player, FastBoard> board : boards.entrySet()) {
                    board.getValue().updateTitle(scoreboardManager.getTitle());
                    board.getValue().updateLines(scoreboardManager.getLines(board.getKey()));
                }
            }
        }.runTaskTimer(this, 1L, 1L);
    }
}
