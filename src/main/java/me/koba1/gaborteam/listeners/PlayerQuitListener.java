package me.koba1.gaborteam.listeners;

import fr.mrmicky.fastboard.FastBoard;
import me.koba1.gaborteam.Main;
import me.koba1.gaborteam.objects.Team;
import me.koba1.gaborteam.utils.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Main.getBoards().remove(e.getPlayer());
        Team team = Utils.getTeam(e.getPlayer());
        if(team == null) return;

        team.removePrefix(e.getPlayer());
    }
}
