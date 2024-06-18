package me.koba1.gaborteam;

import lombok.Getter;
import me.koba1.gaborteam.files.PlayerDataFile;
import me.koba1.gaborteam.objects.Team;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class Main extends JavaPlugin {

    @Getter private static Map<String, Team> teams;
    @Getter private static Main instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        teams = new HashMap<>();

        if(!new File(getDataFolder(), "config.yml").exists())
            saveResource("config.yml", false);

        new PlayerDataFile("players.yml");

        for (String key : getConfig().getConfigurationSection("teams").getKeys(false)) {
            new Team(key);
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
