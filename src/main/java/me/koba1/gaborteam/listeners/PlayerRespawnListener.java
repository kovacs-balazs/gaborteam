package me.koba1.gaborteam.listeners;

import me.koba1.gaborteam.Main;
import me.koba1.gaborteam.objects.Team;
import me.koba1.gaborteam.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerRespawnListener implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Team team = Utils.getTeam(e.getPlayer());
        if(team == null) return;
        if(!team.isRespawn()) {
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> e.getPlayer().setGameMode(GameMode.SPECTATOR), 1L);
        }
    }
}
