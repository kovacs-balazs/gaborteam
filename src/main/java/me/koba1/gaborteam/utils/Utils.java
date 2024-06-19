package me.koba1.gaborteam.utils;

import fr.mrmicky.fastboard.FastBoard;
import me.koba1.gaborteam.Main;
import me.koba1.gaborteam.objects.Team;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
public class Utils {

    public static Team getTeam(Player player) {
        for (Team value : Main.getTeams().values()) {
            if(value.getPlayers().contains(player)) {
                return value;
            }
        }
        return null;
    }

    public static Team loadPlayer(Player player) {
        Main.getBoards().put(player, new FastBoard(player));
        Team team = getTeam(Main.getInstance().getConfig().getString(player.getUniqueId().toString(), ""));
        if(team == null) return null;
        team.join(player);
        if(team.isRespawn()) {
            player.setGameMode(GameMode.SURVIVAL);
        }
        return team;
    }

    public static Team getTeam(String teamName) {
        for (Team value : Main.getTeams().values()) {
            if(value.getKey().contains(teamName)) {
                return value;
            }
        }
        return null;
    }

    public static Location getLocation(String text) {
        String[] args = text.split("; ");
        World world = Bukkit.getWorld(args[0]);

        String[] coords = args[1].split(", ");

        return new Location(
                world,
                Double.parseDouble(coords[0]),
                Double.parseDouble(coords[1]),
                Double.parseDouble(coords[2]),
                Float.parseFloat(coords[3]),
                Float.parseFloat(coords[4])
        );
    }
}
