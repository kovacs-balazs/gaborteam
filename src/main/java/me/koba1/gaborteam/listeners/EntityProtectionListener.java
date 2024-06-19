package me.koba1.gaborteam.listeners;

import io.papermc.paper.event.entity.EntityMoveEvent;
import me.koba1.gaborteam.objects.Team;
import me.koba1.gaborteam.utils.Utils;
import org.bukkit.Location;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EntityProtectionListener implements Listener {

    @EventHandler
    public void onMove(EntityMoveEvent e) {
        if(e.getEntity() instanceof Skeleton skeleton) {
            Team team = Utils.getTeam(skeleton);
            if(team == null) return;

            if(e.getFrom().getX() != e.getTo().getX() || e.getFrom().getZ() != e.getTo().getZ()) {
                Location from = e.getFrom().clone();
                from.setYaw(e.getTo().getYaw());
                from.setPitch(e.getTo().getPitch());
                skeleton.teleport(from);
            }
        }
    }
}
